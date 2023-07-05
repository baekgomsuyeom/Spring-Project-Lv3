package com.springproject.springprojectlv3.service;

import com.springproject.springprojectlv3.dto.BoardRequestDto;
import com.springproject.springprojectlv3.dto.BoardResponseDto;
import com.springproject.springprojectlv3.dto.CommentResponseDto;
import com.springproject.springprojectlv3.dto.MsgResponseDto;
import com.springproject.springprojectlv3.entity.Board;
import com.springproject.springprojectlv3.entity.Comment;
import com.springproject.springprojectlv3.entity.User;
import com.springproject.springprojectlv3.entity.UserRoleEnum;
import com.springproject.springprojectlv3.exception.CustomException;
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

import java.util.ArrayList;
import java.util.List;

import static com.springproject.springprojectlv3.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 게시글 작성
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        User user = getUserFromToken(request);

        Board board = new Board(requestDto, user);
        Board saveBoard = boardRepository.save(board);

        return new BoardResponseDto(saveBoard);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoardList() {
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();

        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            List<CommentResponseDto> commentList = new ArrayList<>();
            for (Comment comment : board.getCommentList()) {        
                commentList.add(new CommentResponseDto(comment));
            }

            boardResponseDtoList.add(new BoardResponseDto(board, commentList));
        }

        return boardResponseDtoList;
    }

    // 게시글 선택 조회
    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : board.getCommentList()) {
            commentList.add(new CommentResponseDto(comment));
        }

        return new BoardResponseDto(board, commentList);
    }

    // 게시글 수정
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, HttpServletRequest request) {
        User user = getUserFromToken(request);

        Board board;

        // 사용자의 권한 확인
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            board = boardRepository.findById(boardId).orElseThrow (
                    () -> new CustomException(NOT_FOUND_BOARD)
            );
        } else {
            board = boardRepository.findByIdAndUserId(boardId, user.getId()).orElseThrow (
                    () -> new CustomException(NOT_FOUND_BOARD)

            );
        }

        // username 일치 여부 확인
        if (requestDto.getUsername().equals(user.getUsername())) {
            board.update(requestDto);
        } else {
            throw new CustomException(AUTHORIZATION);
        }

        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : board.getCommentList()) {
            commentList.add(new CommentResponseDto(comment));
        }

        return new BoardResponseDto(board, commentList);
    }

    // 게시글 삭제
    public MsgResponseDto deleteBoard(Long boardId, BoardRequestDto requestDto, HttpServletRequest request) {
        User user = getUserFromToken(request);

        Board board;

        // 사용자의 권한 확인
        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
            board = boardRepository.findById(boardId).orElseThrow(
                    () -> new CustomException(NOT_FOUND_BOARD)
            );
        } else {
            board = boardRepository.findByIdAndUserId(boardId, user.getId()).orElseThrow (
                    () -> new CustomException(NOT_FOUND_BOARD)
            );
        }

        // username 일치 여부 확인
        if (requestDto.getUsername().equals(user.getUsername())) {
            boardRepository.delete(board);
        } else {
            throw new CustomException(AUTHORIZATION);
        }

        return new MsgResponseDto("게시글을 삭제했습니다.", HttpStatus.OK.value());
    }

    private User getUserFromToken(HttpServletRequest request) {
        String token = jwtUtil.getJwtFromHeader(request);
        Claims claims;          // JWT 안에 있는 정보를 담는 Claims 객체

        if (StringUtils.hasText(token)) {        // JWT 토큰 있는지 확인
            if (jwtUtil.validateToken(token)) {     // JWT 토큰 검증
                claims = jwtUtil.getUserInfoFromToken(token);       // ture 일 경우, JWT 토큰에서 사용자 정보 가져오기
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
