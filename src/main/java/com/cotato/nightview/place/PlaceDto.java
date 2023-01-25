package com.cotato.nightview.place;

import com.cotato.nightview.dong.Dong;
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

    public Place toEntity(Dong dong) {
        return Place.builder()
                .title(title)
                .address(address)
                .roadAddress(roadAddress)
                .longitude(mapx)
                .latitude(mapy)
                .dong(dong)
                .build();
    }

    @Override
    public String toString() {
        return "PlaceDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", roadAddress='" + roadAddress + '\'' +
                '}';
    }
}
