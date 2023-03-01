package com.cotato.nightview.validation;

import com.cotato.nightview.dong.Dong;
import com.cotato.nightview.dong.DongRepository;
import com.cotato.nightview.enums.ExceptionMessageEnum;
import com.cotato.nightview.gu.Gu;
import com.cotato.nightview.gu.GuRepository;
import com.cotato.nightview.member.Member;
import com.cotato.nightview.member.MemberRepository;
import com.cotato.nightview.place.Place;
import com.cotato.nightview.place.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ValidateService {
    private final MemberRepository memberRepository;
    private final GuRepository guRepository;
    private final DongRepository dongRepository;
    private final PlaceRepository placeRepository;

    public Gu findGuByName(String guName) {
        return guRepository.findByName(guName)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessageEnum.NON_EXISTENT_AREA_NAME.getMessage()));
    }

    public Dong findDongByAddress(String address) {
        String[] addressSplit = address.split(" ");
        return findDongByName(addressSplit[2]);
    }

    public Dong findDongByName(String dongName) {
        return dongRepository.findByName(dongName)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessageEnum.NON_EXISTENT_AREA_NAME.getMessage()));
    }

    public List<Dong> findAllDong() {
        return dongRepository.findAll();
    }

    public Place findPlaceById(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageEnum.NON_EXISTENT_PLACE_NAME.getMessage()));
    }

    public Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageEnum.NON_EXISTENT_USER_NAME.getMessage()));
    }

    public Optional<String> getAuthUsername() {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        } else {
            return Optional.of(principal.getName());
        }
    }
}
