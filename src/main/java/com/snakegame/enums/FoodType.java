package com.snakegame.enums;

import com.snakegame.model.AppleEffect;
import com.snakegame.model.FoodEffect;
import com.snakegame.model.GoldenAppleEffect;
import com.snakegame.model.GreenFoodEffect;
import javafx.scene.paint.Color;

public enum FoodType {
    APPLE(Color.web("#e74c3c"), 1, new AppleEffect()),
    GOLDEN_APPLE(Color.web("#f1c40f"), 5, new GoldenAppleEffect()),
    GREEN_FOOD(Color.web("#2ecc71"), 10, new GreenFoodEffect());

    private final Color color;
    private final int score;
    private final FoodEffect effect;

    FoodType(Color color, int score, FoodEffect effect) {
        this.color = color;
        this.score = score;
        this.effect = effect;
    }

    public Color getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }

    public FoodEffect getEffect() {
        return effect;
    }
}
