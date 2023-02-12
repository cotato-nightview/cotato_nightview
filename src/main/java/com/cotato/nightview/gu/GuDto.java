package com.cotato.nightview.gu;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GuDto {
    private Long id;
    private String name;

    @Builder
    public GuDto(String name) {
        this.name = name;
    }

    public Gu toEntity() {
        return Gu.builder()
                .name(name)
                .build();
    }
}
