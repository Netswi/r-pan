package com.heisenberg.pan.server.modules.user.service;

import com.heisenberg.pan.server.modules.user.context.*;
import com.heisenberg.pan.server.modules.user.entity.RPanUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heisenberg.pan.server.modules.user.vo.UserInfoVO;

/**
* @author 2Bug
* @description 针对表【r_pan_user(用户信息表)】的数据库操作Service
* @createDate 2023-08-17 15:36:18
*/
public interface IUserService extends IService<RPanUser> {
    /**
     *用户注册
     * @param userRegiseterContext
     * @return
     */
    Long register(UserRegiseterContext userRegiseterContext);

    String login(UserLoginContext userLoginContext);

    /**
     * logout
     * @param userId
     */
    void exit(Long userId);

    String checkUsername(CheckUsernameContext checkUsernameContext);

    String checkAnswer(CheckAnswerContext checkAnswerContext);

    void resetPassword(ResetPasswordContext resetPasswordContext);

    void changePassword(ChangePasswordContext changePasswordContext);

    UserInfoVO info(Long userId);
}
