package com.cotato.nightview.like_place;

import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "like_place")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LikePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeplace_id")
    private Long id;


    //사용자 한명이 좋아요 정보 여러개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //장소 하나에 좋아요 여러개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;


    @Builder
    public LikePlace( Member member, Place place) {
        this.member = member;
        this.place = place;
    }
}
