package com.springproject.springprojectlv3.controller;

import com.springproject.springprojectlv3.dto.BoardRequestDto;
import com.springproject.springprojectlv3.dto.BoardResponseDto;
import com.springproject.springprojectlv3.dto.MsgResponseDto;
import com.springproject.springprojectlv3.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
    private final BoardService boardService;

    // 게시글 작성
    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.createBoard(requestDto, request);
    }

    // 게시글 전체 조회
    @GetMapping("/board")
    public List<BoardResponseDto> getBoardList() {
        return boardService.getBoardList();
    }

    // 게시글 선택 조회
    @GetMapping("/board/{id}")
    public BoardResponseDto getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 게시글 수정
    @PutMapping("/board/{id}")
    public BoardResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return boardService.updateBoard(id, requestDto, request);
    }

    // 게시글 삭제
    @DeleteMapping("/board/{id}")
    public MsgResponseDto deleteBoard(@PathVariable Long id, HttpServletRequest request) {
        return boardService.deleteBoard(id, request);
    }
}
