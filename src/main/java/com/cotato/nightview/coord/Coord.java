package com.cotato.nightview.coord;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Coord {
    private double x;
    private double y;

    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
        alignDecimal();
    }

    private void alignDecimal() {
        this.x = Math.round(this.x*1000000)/1000000.0;
        this.y = Math.round(this.y*1000000)/1000000.0;
    }
}
