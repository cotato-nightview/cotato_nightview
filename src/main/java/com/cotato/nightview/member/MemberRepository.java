package com.cotato.nightview.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long id);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
