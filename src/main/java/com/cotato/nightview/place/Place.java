package com.cotato.nightview.place;

import com.cotato.nightview.dong.Dong;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "dong_id")
    private Dong dong;


    @Builder
    public Place(String title, String address, String roadAddress, double longitude, double latitude, Dong dong) {
        this.title = title;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dong = dong;
    }

}