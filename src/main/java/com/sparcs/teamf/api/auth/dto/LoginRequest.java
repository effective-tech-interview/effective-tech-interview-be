package com.sparcs.teamf.api.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(
        @Email(message = "올바른 이메일을 입력해주세요.")
        @NotBlank(message = "이메일 입력은 필수입니다.")
        String email,

        @NotBlank(message = "올바른 비밀번호를 입력해주세요.")
        @Length(min = 8, max = 20, message = "비밀번호는 영문, 숫자 조합 8자 이상 20자 이하입니다.")
        String password
) {

}
