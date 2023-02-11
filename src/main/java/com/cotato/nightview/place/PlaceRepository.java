package com.cotato.nightview.place;

import com.cotato.nightview.dong.Dong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    public Place findByTitle(String title);

    public List<Place> findAllByDongIn(List<Dong> dongList);

    public List<Place> findAll();

    public boolean existsByTitle(String title);

    String HAVERSINE_FORMULA = "(6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) *" +
            " cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude))))";

    @Query("SELECT s FROM Place s WHERE " + HAVERSINE_FORMULA + " < :distance ORDER BY " + HAVERSINE_FORMULA + " DESC")
    List<Place> findAllWtihInDistance(@Param("longitude") double longitude,
                                      @Param("latitude") double latitude,
                                      @Param("distance")
                                      double distanceWithInKM);
}
