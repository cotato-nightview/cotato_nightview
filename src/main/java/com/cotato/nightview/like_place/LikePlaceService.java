package com.cotato.nightview.like_place;


import java.util.Map;

public interface LikePlaceService {
    Map<String,Boolean> addLike (LikePlaceRequestDto likePlaceRequestDto);
}
