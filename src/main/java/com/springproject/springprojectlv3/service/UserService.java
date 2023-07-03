package com.springproject.springprojectlv3.service;

import com.springproject.springprojectlv3.dto.LoginRequestDto;
import com.springproject.springprojectlv3.dto.SignupRequestDto;
import com.springproject.springprojectlv3.entity.User;
import com.springproject.springprojectlv3.jwt.JwtUtil;
import com.springproject.springprojectlv3.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 가입
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 등록 (admin = false 일 경우 아래 코드 수행)
        User user = new User(username, password);
        userRepository.save(user);
    }

    // 로그인
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.")
        );

        // 비밀번호 일치 여부 확인
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // Header 에 key 값과 Token 담기
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.createToken(user.getUsername()));
    }
}

