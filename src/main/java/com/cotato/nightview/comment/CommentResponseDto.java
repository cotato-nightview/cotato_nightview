package com.cotato.nightview.comment;


import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class CommentResponseDto {

    private final String content;

    private final LocalTime created_at;
    private Member member;
    private Place place;

    @Builder
    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.created_at = comment.getCreated_at();
        this.member = comment.getMember();
        this.place = comment.getPlace();
    }

}
