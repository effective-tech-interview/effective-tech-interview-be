package com.sparcs.teamf.api.emailauth.service;

public interface EmailService {
    void send(String email, int verificationCode);
}
