package com.cotato.nightview.like_place;


import java.util.Map;

public interface LikePlaceService {
//    void insertLike(LikePlaceRequestDto likePlaceRequestDto);
//    void deleteLike(LikePlaceRequestDto likePlaceRequestDto);
    Map<String,Boolean> addLike (LikePlaceRequestDto likePlaceRequestDto);
}
