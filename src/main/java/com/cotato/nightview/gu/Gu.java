package com.cotato.nightview.gu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "gu")
@Getter
@Setter
@NoArgsConstructor
public class Gu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private String name;

    @Builder
    public Gu(String name) {
        this.name = name;
    }
}
