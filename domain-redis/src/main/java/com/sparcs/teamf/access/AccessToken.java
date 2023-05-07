package com.sparcs.teamf.access;

import java.io.Serializable;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "accessToken", timeToLive = 86400)
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private final String accessToken;

    private final Long memberId;

    public AccessToken(Long memberId, String accessToken) {
        this.memberId = memberId;
        this.accessToken = accessToken;
    }
}
