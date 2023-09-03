package com.heisenberg.pan.server.modules.user.converter;

import com.heisenberg.pan.server.modules.file.entity.RPanUserFile;
import com.heisenberg.pan.server.modules.user.context.ChangePasswordContext;
import com.heisenberg.pan.server.modules.user.context.CheckAnswerContext;
import com.heisenberg.pan.server.modules.user.context.CheckUsernameContext;
import com.heisenberg.pan.server.modules.user.context.ResetPasswordContext;
import com.heisenberg.pan.server.modules.user.context.UserLoginContext;
import com.heisenberg.pan.server.modules.user.context.UserRegiseterContext;
import com.heisenberg.pan.server.modules.user.entity.RPanUser;
import com.heisenberg.pan.server.modules.user.po.ChangePasswordPO;
import com.heisenberg.pan.server.modules.user.po.CheckAnswerPO;
import com.heisenberg.pan.server.modules.user.po.CheckUsernamePO;
import com.heisenberg.pan.server.modules.user.po.ResetPasswordPO;
import com.heisenberg.pan.server.modules.user.po.UserLoginPO;
import com.heisenberg.pan.server.modules.user.po.UserRegisterPO;
import com.heisenberg.pan.server.modules.user.vo.UserInfoVO;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-01T15:53:07+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.2 (Oracle Corporation)"
)
@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserRegiseterContext userRegiseterContext2UserRegisterPO(UserRegisterPO userRegisterPO) {
        if ( userRegisterPO == null ) {
            return null;
        }

        UserRegiseterContext userRegiseterContext = new UserRegiseterContext();

        userRegiseterContext.setUsername( userRegisterPO.getUsername() );
        userRegiseterContext.setPassword( userRegisterPO.getPassword() );
        userRegiseterContext.setQuestion( userRegisterPO.getQuestion() );
        userRegiseterContext.setAnswer( userRegisterPO.getAnswer() );

        return userRegiseterContext;
    }

    @Override
    public RPanUser userRegiseterContext2RPanUser(UserRegiseterContext userRegiseterContext) {
        if ( userRegiseterContext == null ) {
            return null;
        }

        RPanUser rPanUser = new RPanUser();

        rPanUser.setUsername( userRegiseterContext.getUsername() );
        rPanUser.setQuestion( userRegiseterContext.getQuestion() );
        rPanUser.setAnswer( userRegiseterContext.getAnswer() );

        return rPanUser;
    }

    @Override
    public UserLoginContext userLoginPO2UserLoginContext(UserLoginPO userLoginPO) {
        if ( userLoginPO == null ) {
            return null;
        }

        UserLoginContext userLoginContext = new UserLoginContext();

        userLoginContext.setUsername( userLoginPO.getUsername() );
        userLoginContext.setPassword( userLoginPO.getPassword() );

        return userLoginContext;
    }

    @Override
    public CheckUsernameContext checkUsernamePO2CheckUsernameContext(CheckUsernamePO checkUsernamePO) {
        if ( checkUsernamePO == null ) {
            return null;
        }

        CheckUsernameContext checkUsernameContext = new CheckUsernameContext();

        checkUsernameContext.setUsername( checkUsernamePO.getUsername() );

        return checkUsernameContext;
    }

    @Override
    public CheckAnswerContext checkAnswerPO2CheckAnswerContext(CheckAnswerPO checkAnswerPO) {
        if ( checkAnswerPO == null ) {
            return null;
        }

        CheckAnswerContext checkAnswerContext = new CheckAnswerContext();

        checkAnswerContext.setAnswer( checkAnswerPO.getAnswer() );
        checkAnswerContext.setUsername( checkAnswerPO.getUsername() );
        checkAnswerContext.setQuestion( checkAnswerPO.getQuestion() );

        return checkAnswerContext;
    }

    @Override
    public ResetPasswordContext resetPasswordPO2ResetPasswordContext(ResetPasswordPO resetPasswordPO) {
        if ( resetPasswordPO == null ) {
            return null;
        }

        ResetPasswordContext resetPasswordContext = new ResetPasswordContext();

        resetPasswordContext.setUsername( resetPasswordPO.getUsername() );
        resetPasswordContext.setPassword( resetPasswordPO.getPassword() );
        resetPasswordContext.setToken( resetPasswordPO.getToken() );

        return resetPasswordContext;
    }

    @Override
    public ChangePasswordContext changePasswordPO2ChangePasswordContext(ChangePasswordPO changePasswordPO) {
        if ( changePasswordPO == null ) {
            return null;
        }

        ChangePasswordContext changePasswordContext = new ChangePasswordContext();

        changePasswordContext.setNewPassword( changePasswordPO.getNewPassword() );

        return changePasswordContext;
    }

    @Override
    public UserInfoVO assembleUserInfoVO(RPanUser rPanUser, RPanUserFile rPanUserFile) {
        if ( rPanUser == null && rPanUserFile == null ) {
            return null;
        }

        UserInfoVO userInfoVO = new UserInfoVO();

        if ( rPanUser != null ) {
            userInfoVO.setUsername( rPanUser.getUsername() );
        }
        if ( rPanUserFile != null ) {
            userInfoVO.setRootFileId( rPanUserFile.getFileId() );
            userInfoVO.setRootFileName( rPanUserFile.getFilename() );
        }

        return userInfoVO;
    }
}
