package com.springproject.springprojectlv3.controller;

import com.springproject.springprojectlv3.dto.CommentRequestDto;
import com.springproject.springprojectlv3.dto.CommentResponseDto;
import com.springproject.springprojectlv3.dto.MsgResponseDto;
import com.springproject.springprojectlv3.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{boardId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.createComment(boardId, commentRequestDto, request));
    }

    // 댓글 수정
    @PutMapping("/{boardId}/{cmtId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long boardId, @PathVariable Long cmtId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.updateComment(boardId, cmtId, commentRequestDto, request));
    }

    // 댓글 삭제
    @DeleteMapping("/{boardId}/{cmtId}")
    public ResponseEntity<MsgResponseDto> deleteComment(@PathVariable Long boardId, @PathVariable Long cmtId, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.deleteComment(boardId, cmtId, commentRequestDto, request));
    }
}
