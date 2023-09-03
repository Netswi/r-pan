package com.heisenberg.pan.server.modules.user.converter;

import com.heisenberg.pan.server.modules.file.entity.RPanUserFile;
import com.heisenberg.pan.server.modules.user.context.*;
import com.heisenberg.pan.server.modules.user.entity.RPanUser;
import com.heisenberg.pan.server.modules.user.po.*;
import com.heisenberg.pan.server.modules.user.vo.UserInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 将传过来的po转换成context 使用mapstruct 用户模块实体转化工具类
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    /**
     * @mapping注解定制化传入与传出值 目前传的都一样
     * @param userRegisterPO
     * @return
     */
    UserRegiseterContext userRegiseterContext2UserRegisterPO(UserRegisterPO userRegisterPO);

    @Mapping(target = "password",ignore = true)
    RPanUser userRegiseterContext2RPanUser(UserRegiseterContext userRegiseterContext);

    UserLoginContext userLoginPO2UserLoginContext(UserLoginPO userLoginPO);

    CheckUsernameContext checkUsernamePO2CheckUsernameContext(CheckUsernamePO checkUsernamePO);

    CheckAnswerContext checkAnswerPO2CheckAnswerContext (CheckAnswerPO checkAnswerPO);

    ResetPasswordContext resetPasswordPO2ResetPasswordContext (ResetPasswordPO resetPasswordPO);

    ChangePasswordContext changePasswordPO2ChangePasswordContext (ChangePasswordPO changePasswordPO);

    /**
     * 拼装用户基本信息返回实体
     * @param rPanUser
     * @param rPanUserFile
     * @return
     */
    @Mapping(source = "rPanUser.username",target = "username")
    @Mapping(source = "rPanUserFile.fileId",target = "rootFileId")
    @Mapping(source = "rPanUserFile.filename",target = "rootFileName")
    UserInfoVO assembleUserInfoVO(RPanUser rPanUser, RPanUserFile rPanUserFile);
}
