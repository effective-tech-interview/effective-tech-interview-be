package com.sparcs.teamf.api.signup.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record SignupRequest(
        @Email(message = "올바른 이메일을 입력해주세요.")
        @NotBlank(message = "이메일 입력은 필수입니다.")
        String email,

        @NotBlank(message = "올바른 비밀번호를 입력해주세요.")
        @Length(min = 8, max = 20, message = "비밀번호는 영문, 숫자 조합 8자 이상 20자 이하입니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "비밀번호는 영문, 숫자 조합 8자 이상 20자 이하입니다.")
        String password,

        @NotBlank(message = "비밀번호 확인이 필요합니다.")
        String confirmPassword
) {

}
