package com.cotato.nightview.dong;

import com.cotato.nightview.gu.Gu;
import lombok.*;

import java.lang.reflect.Member;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DongDto {
    Long id;
    private String name;

    @Builder
    public DongDto(String name) {
        this.name = name;
    }

    public Dong toEntity(Gu gu){
        return Dong.builder()
                .name(name)
                .gu(gu)
                .build();
    }
}
