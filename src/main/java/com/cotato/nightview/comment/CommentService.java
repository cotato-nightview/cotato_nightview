package com.cotato.nightview.comment;

import java.util.List;

public interface CommentService {
    void saveComment(CommentRequestDto commentRequestDto);
    void deleteComment(Long id);
    List<CommentResponseDto> findAllByPlaceId(Long id);
}
