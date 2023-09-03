package com.heisenberg.pan.server.modules.user.controller;

import com.heisenberg.pan.core.response.R;
import com.heisenberg.pan.core.utils.IdUtil;
import com.heisenberg.pan.server.common.annotation.LoginIgnore;
import com.heisenberg.pan.server.common.utils.UserIdUtil;
import com.heisenberg.pan.server.modules.user.context.*;
import com.heisenberg.pan.server.modules.user.converter.UserConverter;
import com.heisenberg.pan.server.modules.user.po.*;
import com.heisenberg.pan.server.modules.user.service.IUserService;
import com.heisenberg.pan.server.modules.user.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserConverter userConverter;

    @LoginIgnore
    @ApiOperation(value = "用户注册",notes = "接口提供用户注册功能",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("register")
    public R register(@Validated @RequestBody UserRegisterPO userRegisterPO){
        UserRegiseterContext userRegiseterContext = userConverter.userRegiseterContext2UserRegisterPO(userRegisterPO);
        Long userID =  userService.register(userRegiseterContext);
        return R.data(IdUtil.encrypt(userID));//加密
    }

    @LoginIgnore
    @ApiOperation(value = "用户登录",notes = "接口提供用户登录功能",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("login")
    public R login(@Validated @RequestBody UserLoginPO userLoginPO){
        UserLoginContext userLoginContext = userConverter.userLoginPO2UserLoginContext(userLoginPO);
        String accessToken = userService.login(userLoginContext);
        return R.data(accessToken);//加密
    }

    @ApiOperation(value = "用户登出",notes = "接口提供用户登出功能",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("exit")
    public R exit(){
        Long userId = UserIdUtil.get();
        userService.exit(userId);
        return R.success("登出成功");
    }

    @LoginIgnore
    @ApiOperation(value = "用户忘记密码",notes = "接口提供用户忘记密码功能",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("username/check")
    public R checkUserName(@Validated @RequestBody CheckUsernamePO checkUsernamePO){
        CheckUsernameContext checkUsernameContext = userConverter.checkUsernamePO2CheckUsernameContext(checkUsernamePO);
        String question = userService.checkUsername(checkUsernameContext);//传给前端密保问题
        return R.data(question);
    }

    @LoginIgnore
    @ApiOperation(value = "校验密保答案",notes = "接口提供用户校验密保答案功能",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("answer/check")
    public R checkAnswer(@Validated @RequestBody CheckAnswerPO checkAnswerPO){
        CheckAnswerContext checkAnswerContext = userConverter.checkAnswerPO2CheckAnswerContext(checkAnswerPO);
        String token = userService.checkAnswer(checkAnswerContext);
        return R.data(token);
    }
    @LoginIgnore
    @ApiOperation(value = "用户重置密码",notes = "接口提供用户重置密码功能",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("password/reset")
    public R resetPassword(@Validated @RequestBody ResetPasswordPO resetPasswordPO){
        ResetPasswordContext resetPasswordContext = userConverter.resetPasswordPO2ResetPasswordContext(resetPasswordPO);
        userService.resetPassword(resetPasswordContext);
        return R.success();
    }

    @ApiOperation(value = "用户在线修改密码",notes = "接口提供用户在线修改密码功能",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @PostMapping("password/change")
    public R change(@Validated @RequestBody ChangePasswordPO changePasswordPO){
        ChangePasswordContext changePasswordContext = userConverter.changePasswordPO2ChangePasswordContext(changePasswordPO);
        changePasswordContext.setUserId(IdUtil.get());
        userService.changePassword(changePasswordContext);
        return R.success();
    }

    @ApiOperation(value = "用户查询",notes = "接口提供用户查询功能",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @GetMapping("/")
    public R<UserInfoVO> info(@Validated @RequestBody ChangePasswordPO changePasswordPO){
        UserInfoVO userInfoVO = userService.info(UserIdUtil.get());
        return R.data(userInfoVO);
    }
}
