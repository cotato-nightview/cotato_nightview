package com.cotato.nightview.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentRestController {
    private final CommentService commentService;

    @GetMapping("/{place_id}")
    public List<CommentResponseDto> viewComments(@PathVariable(name = "place_id") Long placeId) {
        return commentService.findAllByPlaceId(placeId);
    }

//    @PostMapping("")
//    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto) {
//        System.out.println(commentRequestDto.toString());
//        commentService.saveComment(commentRequestDto);
//        return new ResponseEntity<>(HttpStatus.OK);
////        return commentRequestDto.toString();
//    }
    @PostMapping("")
    public String createComment(@RequestBody CommentRequestDto commentRequestDto) {
        System.out.println(commentRequestDto.toString());
        commentService.saveComment(commentRequestDto);
        return commentRequestDto.getEmail();
//        return commentRequestDto.toString();
    }
}
