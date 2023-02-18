package com.cotato.nightview.comment;

import java.util.List;

public interface CommentService {
    Comment createComment(String email, String title, CommentRequestDto commentRequestDto);
    void deleteComment(Long id);

    List<Comment> findAllComment();
}
