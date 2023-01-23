package com.cotato.nightview.naverapi;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "place")
@Getter @Setter
@NoArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    private String title;
    private String address;
    private String roadAddress;
    private double mapx;
    private double mapy;

    @Builder
    public Place(String title, String address, String roadAddress, double mapx, double mapy) {
        this.title = title;
        this.address = address;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
    }
}