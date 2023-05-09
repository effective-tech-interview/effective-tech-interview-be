package com.sparcs.teamf.email;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "emailAuthentication", timeToLive = 3600)
public class EmailAuthentication implements Serializable {

    @Serial
    private static final long serialVersionUID = -5785239819451023400L;

    /**
     * 이메일과 이벤트를 저장하는 필드
     * ex) example@gmail.com:signup, example@gmail.com:password-reset
     */
    @Id
    private String emailKey;

    private Integer verificationCode;

    private Boolean isAuthenticated;

    public EmailAuthentication(String emailKey, Integer verificationCode) {
        this.emailKey = emailKey;
        this.verificationCode = verificationCode;
        this.isAuthenticated = false;
    }

    public void authenticate() {
        this.isAuthenticated = true;
    }
}
