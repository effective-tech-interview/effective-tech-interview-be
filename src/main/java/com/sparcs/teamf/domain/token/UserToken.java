package com.sparcs.teamf.domain.token;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 604800)
public class UserToken {

    @Id
    private Long memberId;
    private String refreshToken;

    public UserToken(Long memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }
}
