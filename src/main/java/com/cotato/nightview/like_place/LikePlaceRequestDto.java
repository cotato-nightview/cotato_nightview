package com.cotato.nightview.like_place;

import com.cotato.nightview.comment.Comment;
import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikePlaceRequestDto {

    private Long id;

    private String username;
    private Long placeId;

    public LikePlace toEntity(Member member, Place place){
        LikePlace likePlace = LikePlace.builder()
                .member(member)
                .place(place)
                .build();
        return likePlace;
    }
}
