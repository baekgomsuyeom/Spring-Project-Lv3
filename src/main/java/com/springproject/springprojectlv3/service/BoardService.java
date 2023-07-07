package com.springproject.springprojectlv3.service;

import com.springproject.springprojectlv3.dto.BoardRequestDto;
import com.springproject.springprojectlv3.dto.BoardResponseDto;
import com.springproject.springprojectlv3.dto.CommentResponseDto;
import com.springproject.springprojectlv3.dto.MsgResponseDto;
import com.springproject.springprojectlv3.entity.Board;
import com.springproject.springprojectlv3.entity.Comment;
import com.springproject.springprojectlv3.entity.User;
import com.springproject.springprojectlv3.exception.CustomException;
import com.springproject.springprojectlv3.repository.BoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.springproject.springprojectlv3.exception.ErrorCode.AUTHORIZATION;
import static com.springproject.springprojectlv3.exception.ErrorCode.NOT_FOUND_BOARD;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserService userService;

    // 게시글 작성
    public BoardResponseDto createBoard(BoardRequestDto requestDto, HttpServletRequest request) {
        User user = userService.getUserFromToken(request);

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
        User user = userService.getUserFromToken(request);

        // 게시글이 있는지 & 사용자의 권한 확인
        Board board = userService.findByBoardIdAndUser(boardId, user);

        board.update(requestDto);

        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : board.getCommentList()) {
            commentList.add(new CommentResponseDto(comment));
        }

        return new BoardResponseDto(board, commentList);
    }

    // 게시글 삭제
    public MsgResponseDto deleteBoard(Long boardId, HttpServletRequest request) {
        User user = userService.getUserFromToken(request);

        // 게시글이 있는지 & 사용자의 권한 확인
        Board board = userService.findByBoardIdAndUser(boardId, user);

        boardRepository.delete(board);

        return new MsgResponseDto("게시글을 삭제했습니다.", HttpStatus.OK.value());
    }
}
