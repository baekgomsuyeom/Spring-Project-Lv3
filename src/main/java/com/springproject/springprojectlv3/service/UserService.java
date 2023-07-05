package com.springproject.springprojectlv3.service;

import com.springproject.springprojectlv3.dto.LoginRequestDto;
import com.springproject.springprojectlv3.dto.SignupRequestDto;
import com.springproject.springprojectlv3.entity.User;
import com.springproject.springprojectlv3.entity.UserRoleEnum;
import com.springproject.springprojectlv3.exception.CustomException;
import com.springproject.springprojectlv3.jwt.JwtUtil;
import com.springproject.springprojectlv3.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

import static com.springproject.springprojectlv3.exception.ErrorCode.*;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";     // ADMIN_TOKEN

    // 회원 가입
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new CustomException(DUPLICATED_USERNAME);
        }

        // 사용자 ROLE 확인 (admin = true 일 경우 아래 코드 수행)
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new CustomException(NOT_MATCH_ADMIN_TOKEN);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록 (admin = false 일 경우 아래 코드 수행)
        User user = new User(username, password, role);
        userRepository.save(user);
    }

    // 로그인
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NOT_MATCH_INFORMATION)
        );

        // 비밀번호 일치 여부 확인
        if (!user.getPassword().equals(password)) {
            throw new CustomException(NOT_MATCH_INFORMATION);
        }

        // Header 에 key 값과 Token 담기
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.createToken(user.getUsername(), user.getRole()));
    }
}

