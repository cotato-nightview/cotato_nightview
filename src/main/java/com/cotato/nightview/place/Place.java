package com.cotato.nightview.place;

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
    private double longitude;
    private double latitude;

    @Builder
    public Place(String title, String address, String roadAddress, double longitude, double latitude) {
        this.title = title;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}