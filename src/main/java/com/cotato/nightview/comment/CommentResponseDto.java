package com.cotato.nightview.comment;


import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalTime created_at;
    private String email;
    private Long placeId;

//    @Builder
//    public CommentResponseDto(Comment comment) {
//        this.content = comment.getContent();
//        this.created_at = comment.getCreated_at();
//        this.member = comment.getMember();
//        this.place = comment.getPlace();
//    }

}
