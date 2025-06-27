package com.snakegame.model;

import com.snakegame.controller.GameController;

public class AppleEffect implements FoodEffect {
    @Override
    public void applyEffect(GameController gameController) {
        gameController.increaseSnakeLength(1);
    }
}
