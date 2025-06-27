package com.snakegame.model;

import com.snakegame.config.GameConfiguration;
import com.snakegame.controller.GameController;

public class GoldenAppleEffect implements FoodEffect {
    @Override
    public void applyEffect(GameController gameController) {
        gameController.increaseSnakeLength(1);
        gameController.setSpeed(GameConfiguration.FAST_SPEED, 6_000_000_000L);
    }
}
