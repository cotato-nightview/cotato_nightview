package com.cotato.nightview.place;

import com.cotato.nightview.dong.Dong;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto {
    private Long id;
    private String title;
    private String category;
    private String address;
    private String roadAddress;
    @JsonAlias("mapx")
    private double longitude;
    @JsonAlias("mapy")

    private double latitude;

    @Builder
    public PlaceDto(String title, String address, String roadAddress, double longitude, double latitude) {
        this.title = title;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Place toEntity(Dong dong) {
        return Place.builder()
                .title(title)
                .address(address)
                .roadAddress(roadAddress)
                .longitude(longitude)
                .latitude(latitude)
                .dong(dong)
                .build();
    }

    @Override
    public String toString() {
        return "PlaceDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", address='" + address + '\'' +
                ", roadAddress='" + roadAddress + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
