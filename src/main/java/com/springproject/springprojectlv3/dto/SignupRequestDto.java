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
}
