package com.springproject.springprojectlv3.repository;

import com.springproject.springprojectlv3.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndUserId(Long cmtId, Long userId);
}
