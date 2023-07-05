package com.springproject.springprojectlv3.service;

import com.springproject.springprojectlv3.dto.CommentRequestDto;
import com.springproject.springprojectlv3.dto.CommentResponseDto;
import com.springproject.springprojectlv3.dto.MsgResponseDto;
import com.springproject.springprojectlv3.entity.Board;
import com.springproject.springprojectlv3.entity.Comment;
import com.springproject.springprojectlv3.entity.User;
import com.springproject.springprojectlv3.entity.UserRoleEnum;
import com.springproject.springprojectlv3.exception.CustomException;
import com.springproject.springprojectlv3.jwt.JwtUtil;
import com.springproject.springprojectlv3.repository.BoardRepository;
import com.springproject.springprojectlv3.repository.CommentRepository;
import com.springproject.springprojectlv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.springproject.springprojectlv3.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;


    // 댓글 작성
    public CommentResponseDto createComment(Long boardId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new CustomException(NOT_FOUND_COMMENT)
        );

        User user = getUserFromToken(request);

        Comment comment = new Comment(commentRequestDto, board, user);
        Comment saveComment = commentRepository.save(comment);

        return new CommentResponseDto(saveComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long cmtId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        User user = getUserFromToken(request);

        // 게시글이 있는지
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        Comment comment;

        // 사용자의 권한 확인
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment = commentRepository.findById(cmtId).orElseThrow (
                    () -> new CustomException(NOT_FOUND_COMMENT)
            );
        } else {
            comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow (
                    () -> new CustomException(NOT_FOUND_COMMENT)
            );
        }

        // username 일치 여부 확인
        if (commentRequestDto.getUsername().equals(user.getUsername())) {
            comment.update(commentRequestDto);
        } else {
            throw new CustomException(AUTHORIZATION);
        }

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    public MsgResponseDto deleteComment(Long boardId, Long cmtId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        User user = getUserFromToken(request);

        // 게시글이 있는지
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        Comment comment;

        // 사용자의 권한 확인
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment = commentRepository.findById(cmtId).orElseThrow (
                    () -> new CustomException(NOT_FOUND_COMMENT)
            );
        } else {
            comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow (
                    () -> new CustomException(NOT_FOUND_COMMENT)
            );
        }

        // username 일치 여부 확인
        if (commentRequestDto.getUsername().equals(user.getUsername())) {
            commentRepository.deleteById(cmtId);
        } else {
            throw new CustomException(AUTHORIZATION);
        }

        return new MsgResponseDto("댓글을 삭제했습니다.", HttpStatus.OK.value());
    }

    private User getUserFromToken(HttpServletRequest request) {
        String token = jwtUtil.getJwtFromHeader(request);
        Claims claims;          // JWT 안에 있는 정보를 담는 Claims 객체

        if (StringUtils.hasText(token)) {        // JWT 토큰 있는지 확인
            if (jwtUtil.validateToken(token)) {     // JWT 토큰 검증
                claims = jwtUtil.getUserInfoFromToken(token);       // true 일 경우, JWT 토큰에서 사용자 정보 가져오기
            } else {
                throw new CustomException(INVALID_TOKEN);
            }

            // 검증된 JWT 토큰에서 사용자 정보 조회 및 가져오기
            return userRepository.findByUsername(claims.getSubject()).orElseThrow (
                    () -> new CustomException(AUTHORIZATION)
            );
        }

        throw new CustomException(INVALID_TOKEN);
    }
}
