package com.cotato.nightview.comment;

import com.cotato.nightview.member.Member;
import com.cotato.nightview.place.Place;
import com.cotato.nightview.validation.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{
    private final ValidateService validateService;
    private final CommentRepository commentRepository;

    @Override
    public void saveComment(CommentRequestDto commentRequestDto){
        Member member = validateService.findMemberByUsername(commentRequestDto.getUsername());
        Place place = validateService.findPlaceById(commentRequestDto.getPlaceId());
        commentRepository.save(commentRequestDto.toEntity(member,place));
    }

    @Override
    public void deleteComment(CommentRequestDto commentRequestDto){
        Comment comment = commentRepository.findById(commentRequestDto.getId()).get(); //.get 추가
        commentRepository.delete(comment);
    }


    @Override
    public void updateComment(CommentRequestDto commentRequestDto){
        Comment comment = commentRepository.findById(commentRequestDto.getId()).get(); //.get 추가
        comment.setContent(commentRequestDto.getContent());
    }

    @Override
    public Page<CommentResponseDto> findAllByPlaceId(Long placeId, Pageable pageable){
        Page<Comment> commentEntityList = commentRepository.findAllByPlaceId(placeId,pageable);
        return entitiesToDtos(commentEntityList);
    }

    public Page<CommentResponseDto> entitiesToDtos(Page<Comment> commentEntityList) {
        return commentEntityList.map(Comment::toResponseDto);
    }

}
