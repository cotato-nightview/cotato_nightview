package com.cotato.nightview.naverapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaverApiRepository extends JpaRepository<Place,Long> {
}
