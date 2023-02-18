package com.cotato.nightview.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    //Optional<Member> findByEmail(String email);
    Member findByEmail(String email);
}
