package com.sparcs.teamf.api.auth.dto;

public record TokenResponse(long memberId,
                            String accessToken,
                            String refreshToken
) {

}
