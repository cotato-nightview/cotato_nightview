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
    @Column(name = "gu_id")
    private Long id;
    private String name;
    private double latitude;
    private double longitude;

    @Builder
    public Gu(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
