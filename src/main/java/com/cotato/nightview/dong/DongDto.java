package com.cotato.nightview.dong;

import com.cotato.nightview.gu.Gu;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DongDto {
    Long id;
    private String name;
    private double latitude;
    private double longitude;

    @Builder
    public DongDto(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Dong toEntity(Gu gu) {
        return Dong.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .gu(gu)
                .build();
    }
}
