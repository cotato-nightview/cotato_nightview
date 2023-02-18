package com.cotato.nightview.comment;


import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {   //요청으로 부터 정보를 받아 DB에 저장할 때


    private String content;

    private LocalTime created_at;

    private Member member;
    private Place place;

    public Comment toEntity(){
        Comment comment = Comment.builder()
                .content(content)
                .created_at(created_at)
                .member(member)
                .place(place)
                .build();
        return comment;
    }

}
