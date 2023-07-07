package com.springproject.springprojectlv3.controller;

import com.springproject.springprojectlv3.dto.BoardRequestDto;
import com.springproject.springprojectlv3.dto.BoardResponseDto;
import com.springproject.springprojectlv3.dto.MsgResponseDto;
import com.springproject.springprojectlv3.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {
    private final BoardService boardService;

    // 게시글 작성
    @PostMapping("/board")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return ResponseEntity.ok(boardService.createBoard(requestDto, request));
    }

    // 게시글 전체 조회
    @GetMapping("/board")
    public ResponseEntity<List<BoardResponseDto>> getBoardList() {
        return ResponseEntity.ok(boardService.getBoardList());
    }

    // 게시글 선택 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    // 게시글 수정
    @PutMapping("/board/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequestDto requestDto, HttpServletRequest request) {
        return ResponseEntity.ok(boardService.updateBoard(boardId, requestDto, request));
    }

    // 게시글 삭제
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<MsgResponseDto> deleteBoard(@PathVariable Long boardId, HttpServletRequest request) {
        return ResponseEntity.ok(boardService.deleteBoard(boardId, request));
    }
}
