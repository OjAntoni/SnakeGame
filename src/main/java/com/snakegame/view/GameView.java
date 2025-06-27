package com.snakegame.view;

import com.snakegame.config.GameConfiguration;
import com.snakegame.controller.GameController;
import com.snakegame.model.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameView {

    public void draw(GraphicsContext gc, GameController controller) {
        gc.setFill(Color.web("#2c3e50"));
        gc.fillRect(0, 0, GameConfiguration.WIDTH, GameConfiguration.HEIGHT);

        drawGrid(gc);
        drawBorder(gc);
        drawSnake(gc, controller);
        drawFood(gc, controller);
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.web("#34495e"));
        gc.setLineWidth(1);
        for (int i = 0; i < GameConfiguration.GRID_WIDTH; i++) {
            gc.strokeLine(i * GameConfiguration.TILE_SIZE, 0, i * GameConfiguration.TILE_SIZE, GameConfiguration.HEIGHT);
        }
        for (int i = 0; i < GameConfiguration.GRID_HEIGHT; i++) {
            gc.strokeLine(0, i * GameConfiguration.TILE_SIZE, GameConfiguration.WIDTH, i * GameConfiguration.TILE_SIZE);
        }
    }

    private void drawBorder(GraphicsContext gc) {
        gc.setStroke(Color.web("#ecf0f1"));
        gc.setLineWidth(4);
        gc.strokeRect(0, 0, GameConfiguration.WIDTH, GameConfiguration.HEIGHT);
    }

    private void drawSnake(GraphicsContext gc, GameController controller) {
        for (int i = 0; i < controller.getSnake().size(); i++) {
            Point p = controller.getSnake().get(i);
            if (i == 0) {
                gc.setFill(Color.web("#1abc9c"));
            } else {
                gc.setFill(Color.web("#16a085"));
            }
            gc.fillRoundRect(p.x * GameConfiguration.TILE_SIZE, p.y * GameConfiguration.TILE_SIZE, GameConfiguration.TILE_SIZE, GameConfiguration.TILE_SIZE, 10, 10);
        }
    }

    private void drawFood(GraphicsContext gc, GameController controller) {
        gc.setFill(controller.getFood().getType().getColor());
        gc.fillOval(controller.getFood().getPoint().x * GameConfiguration.TILE_SIZE, controller.getFood().getPoint().y * GameConfiguration.TILE_SIZE, GameConfiguration.TILE_SIZE, GameConfiguration.TILE_SIZE);
    }

    public void drawGameOver(GraphicsContext gc, int score) {
        gc.setFill(Color.web("#2c3e50", 0.8));
        gc.fillRect(0, 0, GameConfiguration.WIDTH, GameConfiguration.HEIGHT);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 50));
        gc.fillText("Game Over", GameConfiguration.WIDTH / 2 - 150, GameConfiguration.HEIGHT / 2 - 50);
        gc.setFont(new Font("Arial", 24));
        gc.fillText("Your Score: " + score, GameConfiguration.WIDTH / 2 - 80, GameConfiguration.HEIGHT / 2);
        gc.setFont(new Font("Arial", 18));
        gc.fillText("Press ENTER to restart", GameConfiguration.WIDTH / 2 - 100, GameConfiguration.HEIGHT / 2 + 50);
    }
}
