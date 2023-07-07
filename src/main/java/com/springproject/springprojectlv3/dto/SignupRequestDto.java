package com.springproject.springprojectlv3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    @NotBlank
    @Size(min = 4,max = 10, message ="작성자명은 4자 이상 10자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "작성자명은 알파벳 소문자, 숫자만 사용 가능합니다.")
    private String username;

    @NotBlank
    @Size(min = 8,max = 15, message ="비밀번호는 8자 이상 15자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z_0-9`~!@#$%^&*()+|={};:,.<>/?]*$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자만 사용 가능합니다.")
    private String password;

    private boolean admin = false;      // 디폴트 값은 false. 관리자 권한일 경우 true 로 변한다

    private String adminToken = "";
}
