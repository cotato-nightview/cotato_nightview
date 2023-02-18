package com.cotato.nightview.comment;

import com.cotato.nightview.dong.Dong;

import com.cotato.nightview.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findById(String content);
}