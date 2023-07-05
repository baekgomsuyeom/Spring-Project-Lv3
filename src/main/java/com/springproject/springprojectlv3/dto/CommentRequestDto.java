package com.springproject.springprojectlv3.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private String username;
    private String comment;
}
