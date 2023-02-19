package com.cotato.nightview.comment;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 조회인데 이거는 우리가 원하는 format 아님
//    @GetMapping("/comment/{place_id}")
//    public String viewComments(Model model, @PathVariable(name = "place_id") Long placeId) {
//        System.out.println(placeId);
//        List<Comment> commentList = commentService.findAllComment();
//        model.addAttribute("commentList", commentList);
////        return "map/map :: #place" + placeId + "-comment";  //어딘가의 경로.. 물어봐야함
//        return "map/map :: place22-comment";  //어딘가의 경로.. 물어봐야함
//    }
//

    //댓글 등록하기
//    @PostMapping("/comment")
//    public String createComment(CommentRequestDto commentRequestDto, Model model) {
//        commentService.saveComment(commentRequestDto);
//        List<Comment> commentList = commentService.findAllComment();
//        model.addAttribute("commentList", commentList);
//
//        return "map/map :: place" + commentRequestDto.getPlaceId() + "-comment";  //어딘가의 경로.. 물어봐야함
//    }

}
