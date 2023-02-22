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

@Transactional
@RequiredArgsConstructor
@Service
public class LikePlaceServiceImpl implements LikePlaceService {

    private final PlaceRepository placeRepository;
    private final LikePlaceRepository likePlaceRepository;
    private final MemberRepository memberRepository;

    private final MemberServiceImpl memberService;
    private final PlaceService placeService;



    @Transactional
    @Override
    public boolean addLike(LikePlaceRequestDto likePlaceRequestDto){
        Member member = memberService.findByUsername(likePlaceRequestDto.getUsername());
        Place place = placeService.findById(likePlaceRequestDto.getId());

        if(isNotAlreadyLike(member, place)) {
            likePlaceRepository.save(likePlaceRequestDto.toEntity(member, place));
            return true;
        }

        return false;
    }

    private boolean isNotAlreadyLike(Member member, Place place) {
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
