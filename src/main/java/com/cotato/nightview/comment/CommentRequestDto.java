package com.cotato.nightview.comment;


import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {   //요청으로 부터 정보를 받아 DB에 저장할 때
    //앞의 엔터티에서 시간 자동으로 채워주므로 없음
    private Long id;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    private String username;
    private Long placeId;
    public Comment toEntity(Member member, Place place){
        Comment comment = Comment.builder()
                .content(content)
                .username(username)
                .member(member)
                .place(place)
                .build();
        return comment;
    }

    @Override
    public String toString() {
        return "CommentRequestDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", username='" + username + '\'' +
                ", placeId=" + placeId +
                '}';
    }
}
