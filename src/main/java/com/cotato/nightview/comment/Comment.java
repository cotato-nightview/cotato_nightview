package com.cotato.nightview.comment;

import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Table(name = "comment")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column
    private String content;
    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    private String username;


    //사용자 1명에 댓글 여러개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //장소 1곳에 댓글 여러개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;



    @Builder
    public Comment(String content, LocalDateTime createdAt, Member member, Place place) {
        this.content = content;
        this.createdAt = createdAt;
        this.member = member;
        this.place = place;
    }

    public CommentResponseDto toResponseDto(){
        CommentResponseDto responseDto = CommentResponseDto.builder()
                .id(id)
                .username(member.getUsername())
                .placeId(place.getId())
                .content(content)
                .createdAt(createdAt)
                .build();
        return responseDto;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", username='" + username + '\'' +
                ", member=" + member +
                ", place=" + place +
                '}';
    }
}
