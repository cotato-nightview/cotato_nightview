package com.cotato.nightview.comment;


import com.cotato.nightview.member.MemberDto;
import com.cotato.nightview.place.PlaceDto;
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
    @GetMapping( "/post/{place_id}")
    public String viewComments(Model model){
        List<Comment> commentList = commentService.findAllComment();
        model.addAttribute("??", commentList);

        return "/place/map";
    }



    //댓글 등록하기
    @PostMapping("/post")
    public String registComment(@RequestBody CommentRequestDto commentRequestDto, @RequestBody MemberDto memberDto,
                                @RequestBody PlaceDto placeDto){
        commentService.createComment(memberDto.getEmail(), placeDto.getTitle(), commentRequestDto);
        return "map/map";  //어딘가의 경로.. 물어봐야함
    }

}
