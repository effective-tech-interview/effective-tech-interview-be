package com.sparcs.teamf.api.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public record AuthenticateEmailRequest(
        @Email(message = "올바른 이메일을 입력해주세요.")
        @NotBlank(message = "이메일 입력은 필수입니다.")
        String email,

        @Range(min = 100000, max = 999999, message = "6자리 숫자를 입력해주세요.")
        Integer verificationCode
) {

}
