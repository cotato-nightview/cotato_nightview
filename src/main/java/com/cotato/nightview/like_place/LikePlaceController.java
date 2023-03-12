package com.cotato.nightview.like_place;

import com.cotato.nightview.comment.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likedplace")
public class LikePlaceController {
    private final LikePlaceService likePlaceService;

    @PostMapping("/like")
    public ResponseEntity<Map<String,Boolean>> addLike(@RequestBody LikePlaceRequestDto likePlaceRequestDto){
        System.out.println(likePlaceRequestDto);
        return ResponseEntity.ok().body(likePlaceService.addLike(likePlaceRequestDto));
    }
}
