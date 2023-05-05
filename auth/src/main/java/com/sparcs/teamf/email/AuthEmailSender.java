package com.sparcs.teamf.email;

public interface AuthEmailSender {

    void sendSignupEmail(String email, int verificationCode);

    void sendRegisterEmail(String email, int verificationCode);
}
