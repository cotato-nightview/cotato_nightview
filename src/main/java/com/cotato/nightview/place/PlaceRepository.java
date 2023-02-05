package com.cotato.nightview.place;

import com.cotato.nightview.dong.Dong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    public Place findByTitle(String title);
    public List<Place> findAllByDongIn(List<Dong> dongList);
    public List<Place> findAll();
}
