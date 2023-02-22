package com.cotato.nightview.like_place;

import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikePlaceRepository extends JpaRepository<LikePlace, Long> {

    Optional<LikePlace> findById(Long id);
    Optional<LikePlace> findByMemberAndPlace(Member member, Place place);   //맴버와 장소를 인자로 받아 좋아요 한 적있는지 체크하는 용도로 사용
}
