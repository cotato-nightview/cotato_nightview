package com.cotato.nightview.gu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuRepository extends JpaRepository<Gu, Long> {
    public Gu findByName(String name);
    public boolean existsByName(String name);
}
