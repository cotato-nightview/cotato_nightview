package com.cotato.nightview.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor      //기본 생성자 생성(파라미터 X)
@AllArgsConstructor     //모든 필드 값을 파라미터로 받는 생성자 만듦
@Builder                //@Builder와 @NoArgsConstructor를 함께 쓰면 오류가 발생?   더 알아보자
@Entity //DB테이블과 일대일로 매칭되는 객체 단위, Entity객체의 인스턴스 하나가 테이블에서 하나의 레코드 값을 의미, JPA가 관리

public class Member {

    @Id //pk 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue : 기본키를 자동 생성해주는 어노테이션
    //IDENTITY 전략은 기본 키 생성을 데이터베이스에 위임하는 전략
    private Long member_id;

    @Column
    private String membername;

    @Column
    private String email;

    @Column
    private String password;
}
