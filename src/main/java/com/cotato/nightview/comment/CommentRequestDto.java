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
    private String email;
    private Long placeId;

    public Comment toEntity(Member member, Place place){
        Comment comment = Comment.builder()
                .content(content)
                .created_at(created_at)
                .email(email)
                .member(member)
                .place(place)
                .build();
        return comment;
    }

    @Override
    public String toString() {
        return "CommentRequestDto{" +
                "content='" + content + '\'' +
                ", created_at=" + created_at +
                ", email='" + email + '\'' +
                ", placeId=" + placeId +
                '}';
    }
}
