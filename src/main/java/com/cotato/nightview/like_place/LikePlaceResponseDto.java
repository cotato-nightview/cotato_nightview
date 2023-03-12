package com.cotato.nightview.like_place;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class LikePlaceResponseDto {
    private Long id;

    private Long memberId;
    private Long placeId;

    @Builder
    public LikePlaceResponseDto(Long id,Long memberId, Long placeId) {
        this.id= id;
        this.memberId = memberId;
        this.placeId = placeId;
    }

}
