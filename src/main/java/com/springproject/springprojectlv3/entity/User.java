package com.springproject.springprojectlv3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 4,max = 10, message ="작성자명은 4자 이상 10자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "작성자명은 알파벳 소문자, 숫자만 사용 가능합니다.")
    private String username;

    @Column(nullable = false)
    @Size(min = 8,max = 15, message ="비밀번호는 8자 이상 15자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z_0-9`~!@#$%^&*()+|={};:,.<>/?]*$", message = "비밀번호는 알파벳 대소문자, 숫자, 특수문자만 사용 가능합니다.")
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}