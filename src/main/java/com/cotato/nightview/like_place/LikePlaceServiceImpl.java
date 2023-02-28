package com.cotato.nightview.like_place;


import com.cotato.nightview.member.Member;
import com.cotato.nightview.member.MemberRepository;
import com.cotato.nightview.member.MemberServiceImpl;
import com.cotato.nightview.place.Place;
import com.cotato.nightview.place.PlaceRepository;
import com.cotato.nightview.place.PlaceService;
import com.cotato.nightview.validation.ValidateService;
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
    private final ValidateService validateService;

    @Override
    public Map<String, Boolean> addLike(LikePlaceRequestDto likePlaceRequestDto) {
        Member member = validateService.findMemberByUsername(likePlaceRequestDto.getUsername());
        Place place = validateService.findPlaceById(likePlaceRequestDto.getPlaceId());
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

    }


}
