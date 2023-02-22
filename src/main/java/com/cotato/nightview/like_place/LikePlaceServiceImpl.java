package com.cotato.nightview.like_place;


import com.cotato.nightview.member.Member;
import com.cotato.nightview.member.MemberRepository;
import com.cotato.nightview.member.MemberServiceImpl;
import com.cotato.nightview.place.Place;
import com.cotato.nightview.place.PlaceRepository;
import com.cotato.nightview.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikePlaceServiceImpl implements LikePlaceService {
    private final LikePlaceRepository likePlaceRepository;
    private final MemberServiceImpl memberService;
    private final PlaceService placeService;

    @Transactional
    @Override
    public Map<String, Boolean> addLike(LikePlaceRequestDto likePlaceRequestDto) {
        Member member = memberService.findByUsername(likePlaceRequestDto.getUsername());
        Place place = placeService.findById(likePlaceRequestDto.getPlaceId());
        Map<String, Boolean> result = new HashMap<>();

        Optional<LikePlace> likePlace = likePlaceRepository.findByMemberAndPlace(member, place);
        if (likePlace.isPresent()) {
            likePlaceRepository.delete(likePlace.get());
            result.put("isLiked", false);
            return result;
        } else {
            likePlaceRepository.save(likePlaceRequestDto.toEntity(member, place));
            result.put("isLiked", true);
            return result;
        }
//        if(isNotAlreadyLike(member, place)) {
//            System.out.println("추가");
//            likePlaceRepository.save(likePlaceRequestDto.toEntity(member, place));
//            return true;
//        } else {
//            likePlaceRepository.findByMemberAndPlace(member, place)
//            System.out.println("삭제");
//            likePlaceRepository.delete(likePlaceRequestDto.toEntity(member, place));
//            return false;
//        }

    }

    public boolean isNotAlreadyLike(String username, Place place) {
        Member member = memberService.findByUsername(username);
        return likePlaceRepository.findByMemberAndPlace(member,place).isEmpty();
    }

    //아래 두 코드 합쳐야함
//    @Transactional
//    @Override
//    public void insertLike(LikePlaceRequestDto likePlaceRequestDto){
//        Member member = memberService.findByUsername(likePlaceRequestDto.getUsername());
//        Place place = placeService.findById(likePlaceRequestDto.getId());
//        likePlaceRepository.save(likePlaceRequestDto.toEntity(member, place));
//    }
//
//    @Transactional
//    @Override
//    public void deleteLike(LikePlaceRequestDto likePlaceRequestDto){
//
//    }

}
