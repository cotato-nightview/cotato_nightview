package com.cotato.nightview.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentRestController {
    private final CommentService commentService;

    @GetMapping("/{place_id}")
    public Page<CommentResponseDto> getComments(@PathVariable(name = "place_id") Long placeId, Pageable pageable) {
        return  commentService.findAllByPlaceId(placeId, pageable);
    }

    @PostMapping("")
    public ResponseEntity createComment(@Valid @RequestBody CommentRequestDto commentRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : errors.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity(errorMap, HttpStatus.BAD_REQUEST);
        }
        System.out.println(commentRequestDto.toString());
        commentService.saveComment(commentRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity deleteComment(@RequestBody CommentRequestDto commentRequestDto) {
        System.out.println(commentRequestDto.toString());
        commentService.deleteComment(commentRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
