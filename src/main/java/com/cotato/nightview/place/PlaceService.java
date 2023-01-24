package com.cotato.nightview.place;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    public void savePlace(PlaceDto dto){
       placeRepository.save(dto.toEntity());
    }
    public boolean vaildPlace(PlaceDto dto) {
        if (!(dto.getCategory().contains("명소") || dto.getCategory().contains("지명"))) {
            return false;
        }
        return placeRepository.findByTitle(dto.getTitle()) == null;
    }
}
