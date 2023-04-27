package com.sparcs.teamf.service;

public interface EmailService {

    void send(String email, int verificationCode);
}
