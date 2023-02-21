package com.sparcs.teamf.api.auth.dto;

import lombok.Builder;

@Builder
public record TokenResponse(long memberId,
                            String accessToken,
                            String refreshToken
) {

}
