package com.cotato.nightview.comment;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<CommentResponseDto>> viewComments(@PathVariable(name = "place_id") Long placeId) {
        List<CommentResponseDto> commentList = commentService.findAllByPlaceId(placeId);
        for (CommentResponseDto commentResponseDto : commentList) {
            System.out.println("commentResponseDto = " + commentResponseDto.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
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
        commentService.saveComment(commentRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
