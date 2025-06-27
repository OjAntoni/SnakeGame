package com.snakegame.model;

import com.snakegame.enums.FoodType;

public class Food {
    private final Point point;
    private final FoodType type;

    public Food(Point point, FoodType type) {
        this.point = point;
        this.type = type;
    }

    public Point getPoint() {
        return point;
    }

    public FoodType getType() {
        return type;
    }
}
