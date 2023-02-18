package com.cotato.nightview.gu;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GuDto {
    private Long id;
    private String name;
    private double latitude;
    private double longitude;

    @Builder
    public GuDto(String name,double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Gu toEntity() {
        return Gu.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
