package com.cotato.nightview.comment;


import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String username;
    private Long placeId;

    @Override
    public String toString() {
        return "CommentResponseDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", username='" + username + '\'' +
                ", placeId=" + placeId +
                '}';
    }
}
