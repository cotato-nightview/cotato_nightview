package com.cotato.nightview.gu;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GuDto {
    private Long id;
    private String gu;

    @Builder
    public GuDto(String gu) {
        this.gu = gu;
    }

    public Gu toEntity() {
        return Gu.builder()
                .name(gu)
                .build();
    }
}
