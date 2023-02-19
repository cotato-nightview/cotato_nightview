package com.cotato.nightview.comment;

import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalTime;


@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column
    private String content;
    @CreatedDate
    @Column
    private LocalTime created_at;

    private String email;


    //사용자 1명에 댓글 여러개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //장소 1곳에 댓글 여러개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;



    @Builder
    public Comment(String content, LocalTime created_at, Member member, Place place) {
        this.content = content;
        this.created_at = created_at;
        this.member = member;
        this.place = place;
    }

}
