package com.cotato.nightview.like_place;

import com.cotato.nightview.comment.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likedplace")
public class LikePlaceController {

    private final LikePlaceService likePlaceService;

    @PostMapping("/like")
    public ResponseEntity<?> addLike(@RequestBody LikePlaceRequestDto likePlaceRequestDto){
        boolean result = false;
        result = likePlaceService.addLike(likePlaceRequestDto);

        return result ?
                new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


//    @PostMapping
//    public ResponseEntity<?> insertLike(@RequestBody LikePlaceRequestDto likePlaceRequestDto){
//        likePlaceService.insertLike(likePlaceRequestDto);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<?> deleteLike(@RequestBody LikePlaceRequestDto likePlaceRequestDto){
//        likePlaceService.deleteLike(likePlaceRequestDto);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
