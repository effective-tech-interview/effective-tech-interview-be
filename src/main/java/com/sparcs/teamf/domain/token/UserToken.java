package com.sparcs.teamf.domain.token;

import javax.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 604800)
public class UserToken {

    @Id
    private final Long memberId;
    private final String refreshToken;

    public UserToken(Long memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }
}
