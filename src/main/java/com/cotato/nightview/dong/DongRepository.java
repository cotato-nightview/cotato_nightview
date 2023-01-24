package com.cotato.nightview.dong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DongRepository extends JpaRepository<Dong, Long> {
    public Dong findByName(String name);
}
