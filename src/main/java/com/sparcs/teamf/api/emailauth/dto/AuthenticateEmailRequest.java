package com.sparcs.teamf.api.emailauth.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record AuthenticateEmailRequest(
        @Email(message = "올바른 이메일을 입력해주세요.")
        @NotBlank(message = "이메일 입력은 필수입니다.")
        String email,

        @Size(min = 6, max = 6, message = "6자리 숫자를 입력해주세요.")
        @Digits(integer = 6, fraction = 0, message = "6자리 숫자를 입력해주세요.")
        String verificationCode
) {

}
