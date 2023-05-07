package com.sparcs.teamf.api.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record SendEmailRequest(
        @Email(message = "올바른 이메일을 입력해주세요.")
        @NotBlank(message = "이메일 입력은 필수입니다.")
        String email
) {

}
