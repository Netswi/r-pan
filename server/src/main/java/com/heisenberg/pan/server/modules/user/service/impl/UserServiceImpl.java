package com.heisenberg.pan.server.modules.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heisenberg.pan.cache.core.constants.CacheConstants;
import com.heisenberg.pan.core.exception.RPanException;
import com.heisenberg.pan.core.response.R;
import com.heisenberg.pan.core.response.ResponseCode;
import com.heisenberg.pan.core.utils.IdUtil;
import com.heisenberg.pan.core.utils.JwtUtil;
import com.heisenberg.pan.core.utils.PasswordUtil;
import com.heisenberg.pan.server.modules.file.constants.FileConstants;
import com.heisenberg.pan.server.modules.file.context.CreateFolderContext;
import com.heisenberg.pan.server.modules.file.entity.RPanUserFile;
import com.heisenberg.pan.server.modules.file.service.IUserFileService;
import com.heisenberg.pan.server.modules.user.constants.UserConstants;
import com.heisenberg.pan.server.modules.user.context.*;
import com.heisenberg.pan.server.modules.user.converter.UserConverter;
import com.heisenberg.pan.server.modules.user.entity.RPanUser;
import com.heisenberg.pan.server.modules.user.service.IUserService;
import com.heisenberg.pan.server.modules.user.mapper.RPanUserMapper;
import com.heisenberg.pan.server.modules.user.vo.UserInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.heisenberg.pan.server.modules.user.constants.UserConstants.ONE_DAY_LONG;

/**
* @author 2Bug
* @description 针对表【r_pan_user(用户信息表)】的数据库操作Service实现
* @createDate 2023-08-17 15:36:18
*/
@Service(value = "userService")
public class UserServiceImpl extends ServiceImpl<RPanUserMapper, RPanUser>
    implements IUserService {

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private IUserFileService iUserFileService;

    @Autowired
    //保存token
    private CacheManager cacheManager;

    /**
     * 注册用户业务实现
     * @param userRegiseterContext
     * @return
     */
    @Override
    public Long register(UserRegiseterContext userRegiseterContext) {
        assenbleUserEntity(userRegiseterContext);
        doRegister(userRegiseterContext);
        createUserRootFolder(userRegiseterContext);

        return userRegiseterContext.getEntity().getUserId();
    }

    /**
     * 用户登录实现
     * 1.用户登录信息校验
     * 2.生成具有时效性的accessToken
     * 3.将accessToken缓存起来 实现单机登录(多个会被踢下线)
     * @param userLoginContext
     * @return
     */
    @Override
    public String login(UserLoginContext userLoginContext) {
        checkLoginInfo(userLoginContext);
        generateAndSaveAccessToken(userLoginContext);
        return userLoginContext.getAccessToken();
    }

    /**
     * 退出登录
     *
     * 清楚用户登录accessToken缓存
     * @param userId
     */
    @Override
    public void exit(Long userId) {
        try {
            Cache cache = cacheManager.getCache(CacheConstants.R_PAN_CACHE_NAME);
            cache.evict(UserConstants.USER_LOGIN_PREFIX + userId);
        }catch (Exception e){
            e.printStackTrace();
            throw new RPanException("用户登出失败");
        }
    }

    /**
     * 校验用户名
     *
     * @param checkUsernameContext
     * @return
     */
    @Override
    public String checkUsername(CheckUsernameContext checkUsernameContext) {
        String question = baseMapper.selectQuestionByUsername(checkUsernameContext.getUsername());
        if (StringUtils.isBlank(question)){
            throw new RPanException("没有此用户");
        }

        return question;
    }

    /**
     * 用户忘记密码 校验密保答案
     * @param checkAnswerContext
     * @return
     */
    @Override
    public String checkAnswer(CheckAnswerContext checkAnswerContext) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",checkAnswerContext.getUsername());
        queryWrapper.eq("question",checkAnswerContext.getQuestion());
        queryWrapper.eq("answer",checkAnswerContext.getAnswer());

        int count = count(queryWrapper);
        if (count == 0){
            throw new RPanException("密保答案错误");
        }
        return generateCheckAnswerToken(checkAnswerContext);
    }

    /**
     * 重置用户密码
     * 1.校验token是否有效
     * 2.重制
     * @param resetPasswordContext
     */
    @Override
    public void resetPassword(ResetPasswordContext resetPasswordContext) {
        checkForgetPasswordToken(resetPasswordContext);
        checkAndResetPassword(resetPasswordContext);
    }

    /**
     * 在线修改密码
     * 1.校验旧密码
     * 2.重置新密码
     * 3.退出当前登录状态
     * @param changePasswordContext
     */
    @Override
    public void changePassword(ChangePasswordContext changePasswordContext) {
        checkOldPassword(changePasswordContext);
        doChangePassword(changePasswordContext);
        exitLoginStatus(changePasswordContext);

    }

    /**
     * 查询在线用户信息
     * 1.查询用户基本信息实体
     * 2.查询用户根文件夹实体
     * 3.拼装vo对象返回
     * @param userId
     * @return
     */
    @Override
    public UserInfoVO info(Long userId) {
        RPanUser entity = getById(userId);
        if (Objects.isNull(entity)){
            throw new RPanException("用户信息查询失败");
        }
        RPanUserFile rPanUserFile = getUserRootFileInfo(userId);
        if (Objects.isNull(rPanUserFile)){
            throw new RPanException("查询用户根文件夹信息失败");
        }
        return userConverter.assembleUserInfoVO(entity,rPanUserFile);
    }

    /**
     * 获取用户根文件夹实体
     * @param userId
     * @return
     */
    private RPanUserFile getUserRootFileInfo(Long userId) {

        return iUserFileService.getUserRootFile(userId);
    }

    /**
     * 退出当前登录状态
     * @param changePasswordContext
     */
    private void exitLoginStatus(ChangePasswordContext changePasswordContext) {
        exit(changePasswordContext.getUserId());
    }

    /**
     * 修改密码
     * @param changePasswordContext
     */
    private void doChangePassword(ChangePasswordContext changePasswordContext) {
        String newPassword = changePasswordContext.getNewPassword();
        RPanUser entity = changePasswordContext.getEntity();
        String salt = entity.getSalt();
        String encryptPassword = PasswordUtil.encryptPassword(salt, newPassword);
        entity.setPassword(encryptPassword);

        if (!updateById(entity)){
            throw new RPanException("密码修改失败");
        }
    }

    /**
     * 校验旧密码
     * 查询并封装用户的实体信息到上下文中
     * @param changePasswordContext
     */
    private void checkOldPassword(ChangePasswordContext changePasswordContext) {
        Long userId = changePasswordContext.getUserId();
        String oldPassword = changePasswordContext.getOldPassword();
        RPanUser entity = getById(userId);
        if (Objects.isNull(entity)){
            throw new RPanException("用户信息不存在");
        }
        changePasswordContext.setEntity(entity);
        String encOldPassword = PasswordUtil.encryptPassword(entity.getSalt(), oldPassword);
        String oldDBPassword = entity.getPassword();
        if (!StringUtils.equals(encOldPassword,oldDBPassword)){
            throw new RPanException("旧密码不正确");
        }



    }

    /**
     * 校验及重置用户密码
     * @param resetPasswordContext
     */
    private void checkAndResetPassword(ResetPasswordContext resetPasswordContext) {
        String username = resetPasswordContext.getUsername();
        String password = resetPasswordContext.getPassword();
        RPanUser entity = getUserByName(username);
        if (Objects.isNull(entity)){
            throw new RPanException("用户信息不存在");
        }

        String newDBPassword = PasswordUtil.encryptPassword(entity.getSalt(), password);
        entity.setPassword(newDBPassword);
        entity.setUpdateTime(new Date());

        if (!updateById(entity)){
            throw new RPanException("重置密码失败");
        }

    }

    /**
     * 验证忘记密码的token是否有效
     * @param resetPasswordContext
     */
    private void checkForgetPasswordToken(ResetPasswordContext resetPasswordContext) {
        String token = resetPasswordContext.getToken();
        Object value = JwtUtil.analyzeToken(token, UserConstants.FORGET_USERNAME);
        if (Objects.isNull(value)) {

            throw new RPanException(ResponseCode.TOKEN_EXPIRE);
        }
        String tokenUsername = String.valueOf(value);
        if (Objects.equals(tokenUsername,resetPasswordContext.getUsername())){
            throw new RPanException("token错误");
        }
    }

    /**
     * 生成用户忘记密码校验密保通过的临时token
     * ttl为5分钟
     * @param checkAnswerContext
     * @return
     */
    private String generateCheckAnswerToken(CheckAnswerContext checkAnswerContext) {
        String token = JwtUtil.generateToken(checkAnswerContext.getUsername(),UserConstants.FORGET_USERNAME,checkAnswerContext.getUsername(),UserConstants.FIVE_MIN_LONG);
        return token;
    }

    /**
     * 生成并保存登录之后的凭证
     * @param userLoginContext
     */
    private void generateAndSaveAccessToken(UserLoginContext userLoginContext) {
        RPanUser entity = userLoginContext.getEntity();

        String accessToken = JwtUtil.generateToken(entity.getUsername(), UserConstants.LOGIN_USER_ID,entity.getUserId(),ONE_DAY_LONG);

        Cache cache = cacheManager.getCache(CacheConstants.R_PAN_CACHE_NAME);
        cache.put(UserConstants.USER_LOGIN_PREFIX + entity.getUserId(),accessToken);
        userLoginContext.setAccessToken(accessToken);
    }

    /**
     * 校验用户
     * @param userLoginContext
     */
    private void checkLoginInfo(UserLoginContext userLoginContext) {
        String username = userLoginContext.getUsername();
        String password = userLoginContext.getPassword();
        RPanUser entity = getUserByName(username);
        if (Objects.isNull(entity)){
            throw new RPanException("用户不存在");
        }
        String salt = entity.getSalt();
        String encPassword = PasswordUtil.encryptPassword(salt, password);
        String dbPassword = entity.getPassword();

        if (!Objects.equals(encPassword,dbPassword)){
            throw new RPanException("密码错误");
        }

        userLoginContext.setEntity(entity);
    }

    private RPanUser getUserByName(String username) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return getOne(queryWrapper);
    }

    /**
     * 创建用户的根目录信息
     * @param userRegiseterContext
     */
    private void createUserRootFolder(UserRegiseterContext userRegiseterContext) {
        CreateFolderContext createFolderContext = new CreateFolderContext();
        createFolderContext.setFolderName(FileConstants.ALL_FILE_CN_STR);
        createFolderContext.setUserId(userRegiseterContext.getEntity().getUserId());
        createFolderContext.setPartenId(FileConstants.TOP_PARENT_ID);
        iUserFileService.createFolder(createFolderContext);
    }

    /**
     * 实现注册用户的业务
     * 需要捕获数据库唯一索引冲突异常，来实现全局用户名唯一
     * @param userRegiseterContext
     */
    private void doRegister(UserRegiseterContext userRegiseterContext) {
        RPanUser entity = userRegiseterContext.getEntity();
        if (Objects.nonNull(entity)){
            try {
                if (!save(entity)){
                    throw new RPanException("用户注册失败");
                }
            }catch (DuplicateKeyException duplicateKeyException){
                throw new RPanException("用户名已存在");
            }
            return;
        }
        throw new RPanException(ResponseCode.ERROR);
    }
    /**
     * 实体转化
     * 上下文信息转化为实体，封装进上下文
     * @param userRegiseterContext
     */
    private void assenbleUserEntity(UserRegiseterContext userRegiseterContext) {
       RPanUser entity =  userConverter.userRegiseterContext2RPanUser(userRegiseterContext);
       String salt = PasswordUtil.getSalt();
       String dbPassWord = PasswordUtil.encryptPassword(salt,userRegiseterContext.getPassword());
       entity.setUserId(IdUtil.get());
       entity.setSalt(salt);
       entity.setPassword(dbPassWord);
       entity.setCreateTime(new Date());
       entity.setUpdateTime(new Date());
       userRegiseterContext.setEntity(entity);
    }
}




