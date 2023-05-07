package com.sparcs.teamf.dto;

public record TokenResponse(long memberId,
                            String accessToken,
                            String refreshToken) {

}
