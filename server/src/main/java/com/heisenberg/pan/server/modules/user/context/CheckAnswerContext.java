package com.heisenberg.pan.server.modules.user.context;


import lombok.Data;

import java.io.Serializable;


@Data
public class CheckAnswerContext implements Serializable {


    private static final long serialVersionUID = -8639912363789520497L;
    private String answer;
    private String username;
    private String question;
}
