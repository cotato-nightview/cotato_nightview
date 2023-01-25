package com.cotato.nightview.dong;

import com.cotato.nightview.gu.Gu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "dong")
@Getter
@Setter
@NoArgsConstructor
public class Dong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dong_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gu_id")
    private Gu gu;


    @Builder
    public Dong(String name, Gu gu) {
        this.name = name;
        this.gu = gu;
    }
}
