package com.springproject.springprojectlv3.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    private String username;
    private String title;
    private String contents;
}