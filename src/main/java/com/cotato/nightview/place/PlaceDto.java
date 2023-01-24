package com.cotato.nightview.place;

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
    private double longitude;
    private double latitude;

    @Builder
    public PlaceDto(String title, String address, String roadAddress, double mapx, double mapy) {
        this.title = title;
        this.address = address;
        this.roadAddress = roadAddress;
        this.longitude = mapx;
        this.latitude = mapy;
    }

    public Place toEntity() {
        return Place.builder()
                .title(title)
                .address(address)
                .roadAddress(roadAddress)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }
}
