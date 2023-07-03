package com.springproject.springprojectlv3.service;

import com.springproject.springprojectlv3.dto.BoardRequestDto;
import com.springproject.springprojectlv3.dto.BoardResponseDto;
import com.springproject.springprojectlv3.dto.MsgResponseDto;
import com.springproject.springprojectlv3.entity.Board;
import com.springproject.springprojectlv3.entity.User;
import com.springproject.springprojectlv3.jwt.JwtUtil;
import com.springproject.springprojectlv3.repository.BoardRepository;
import com.springproject.springprojectlv3.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 게시글 작성
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        User user = getUserFromToken(request);			// getUserFromToken 메서드를 호출

        // 여기 3줄은 입문 에서의 '게시글 작성'과 동일
        Board board = new Board(requestDto, user);
        Board saveBoard = boardRepository.save(board);

        return new BoardResponseDto(saveBoard);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoardList() {
        return boardRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(BoardResponseDto::new)
                .toList();
    }

    // 게시글 선택 조회
    public BoardResponseDto getBoard(Long id) {
        // 해당 id 가 없을 경우
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        // 해당 id 가 있을 경우
        return new BoardResponseDto(board);
    }

    // 게시글 수정
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, HttpServletRequest request) {
        User user = getUserFromToken(request);			// getUserFromToken 메서드를 호출

        Board board = boardRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자의 게시글을 찾을 수 없습니다.")
        );

        board.update(requestDto);

        return new BoardResponseDto(board);
    }

    // 게시글 삭제
    public MsgResponseDto deleteBoard(Long id, HttpServletRequest request) {
        User user = getUserFromToken(request);			// getUserFromToken 메서드를 호출

        Board board = boardRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자의 게시글을 찾을 수 없습니다.")
        );

        boardRepository.delete(board);

        return new MsgResponseDto("게시글을 삭제했습니다.", HttpStatus.OK.value());
    }

    private User getUserFromToken(HttpServletRequest request) {
        String token = jwtUtil.getJwtFromHeader(request);       // request 에서 Token 가져오기
        Claims claims;          // JWT 안에 있는 정보를 담는 Claims 객체

        if (StringUtils.hasText(token)) {        // JWT 토큰 있는지 확인
            if (jwtUtil.validateToken(token)) {     // JWT 토큰 검증
                claims = jwtUtil.getUserInfoFromToken(token);       // ture 일 경우, JWT 토큰에서 사용자 정보 가져오기
            } else {
                throw new IllegalArgumentException("올바른 token 이 아닙니다.");
            }

            // 검증된 JWT 토큰에서 사용자 정보 조회 및 가져오기
            return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.")
            );
        }

        throw new IllegalArgumentException("token 이 없습니다.");
    }
}
