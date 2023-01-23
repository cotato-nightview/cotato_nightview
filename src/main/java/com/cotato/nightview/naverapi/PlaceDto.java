package com.cotato.nightview.naverapi;

import jdk.jfr.Category;
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
    private double mapx;
    private double mapy;

    @Builder
    public PlaceDto(String title, String address, String roadAddress, double mapx, double mapy) {
        this.title = title;
        this.address = address;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
    }

    public Place toEntity() {
        return Place.builder()
                .title(title)
                .address(address)
                .roadAddress(roadAddress)
                .mapx(mapx)
                .mapy(mapy)
                .build();
    }
}
