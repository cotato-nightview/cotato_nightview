package com.cotato.nightview.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    void saveComment(CommentRequestDto commentRequestDto);
    void deleteComment(CommentRequestDto commentRequestDto);

    void updateComment(CommentRequestDto commentRequestDto);
    Page<CommentResponseDto> findAllByPlaceId(Long id, Pageable pageable);
}
