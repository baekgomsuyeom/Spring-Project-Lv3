package com.springproject.springprojectlv3.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private boolean admin = false;      // 디폴트 값은 false. 관리자 권한일 경우 true 로 변한다
    private String adminToken = "";
}
