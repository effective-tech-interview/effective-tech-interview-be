package com.sparcs.teamf.api.auth.dto;

import javax.validation.constraints.NotBlank;

public record Oauth2CodeRequest(@NotBlank String code, @NotBlank String redirectUri) {

}
