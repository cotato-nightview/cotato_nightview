package com.cotato.nightview.comment;

import com.cotato.nightview.member.Member;
import com.cotato.nightview.member.MemberRepository;
import com.cotato.nightview.member.MemberServiceImpl;
import com.cotato.nightview.place.Place;
import com.cotato.nightview.place.PlaceDto;
import com.cotato.nightview.place.PlaceRepository;
import com.cotato.nightview.place.PlaceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{
    private final MemberServiceImpl memberService;
    private final PlaceService placeService;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void saveComment(CommentRequestDto commentRequestDto){
        Member member = memberService.findByUsername(commentRequestDto.getUsername());
        Place place = placeService.findById(commentRequestDto.getPlaceId());
        commentRepository.save(commentRequestDto.toEntity(member,place));
         //이거 맞는지 잘 모르겟
    }

    @Transactional
    @Override
    public void deleteComment(Long id){
        Comment comment = commentRepository.findById(id).get(); //.get 추가
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponseDto> findAllByPlaceId(Long placeId){
        List<Comment> commentList = commentRepository.findAllByPlaceId(placeId);
        return entitiesToDtos(commentList);
    }

    public List<CommentResponseDto> entitiesToDtos(List<Comment> commentEntityList) {
        return commentEntityList.stream().map(comment -> modelMapper.map(comment, CommentResponseDto.class))
                .collect(Collectors.toList());
    }

}
