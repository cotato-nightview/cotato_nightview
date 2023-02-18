package com.cotato.nightview.comment;

import com.cotato.nightview.member.Member;
import com.cotato.nightview.member.MemberRepository;
import com.cotato.nightview.place.Place;
import com.cotato.nightview.place.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService{


    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public Comment createComment(String email, String title, CommentRequestDto commentRequestDto){
        Member member = memberRepository.findByEmail(email);
        Place place = placeRepository.findByTitle(title);

        commentRequestDto.setMember(member);
        commentRequestDto.setPlace(place);

        Comment comment = commentRequestDto.toEntity();

        return commentRepository.save(comment); //이거 맞는지 잘 모르겟
    }

    @Transactional
    @Override
    public void deleteComment(Long id){
        Comment comment = commentRepository.findById(id).get(); //.get 추가
        commentRepository.delete(comment);
    }


    @Transactional
    @Override
    public List<Comment> findAllComment(){
        return commentRepository.findAll();
    }

}
