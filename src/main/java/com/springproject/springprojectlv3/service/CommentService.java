package com.springproject.springprojectlv3.service;

import com.springproject.springprojectlv3.dto.CommentRequestDto;
import com.springproject.springprojectlv3.dto.CommentResponseDto;
import com.springproject.springprojectlv3.dto.MsgResponseDto;
import com.springproject.springprojectlv3.entity.Board;
import com.springproject.springprojectlv3.entity.Comment;
import com.springproject.springprojectlv3.entity.User;
import com.springproject.springprojectlv3.exception.CustomException;
import com.springproject.springprojectlv3.repository.BoardRepository;
import com.springproject.springprojectlv3.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.springproject.springprojectlv3.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    // 댓글 작성
    public CommentResponseDto createComment(Long boardId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        User user = userService.getUserFromToken(request);

        Comment comment = new Comment(commentRequestDto, board, user);
        Comment saveComment = commentRepository.save(comment);

        return new CommentResponseDto(saveComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long cmtId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        User user = userService.getUserFromToken(request);

        // 게시글이 있는지
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 댓글이 있는지 & 사용자의 권한 확인
        Comment comment = userService.findByCmtIdAndUser(cmtId, user);

        comment.update(commentRequestDto);

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    public MsgResponseDto deleteComment(Long boardId, Long cmtId, HttpServletRequest request) {
        User user = userService.getUserFromToken(request);

        // 게시글이 있는지
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 댓글이 있는지 & 사용자의 권한 확인
        Comment comment = userService.findByCmtIdAndUser(cmtId, user);

        commentRepository.delete(comment);

        return new MsgResponseDto("댓글을 삭제했습니다.", HttpStatus.OK.value());
    }
}
