package com.snakegame.controller;

import com.snakegame.config.GameConfiguration;
import com.snakegame.model.Food;
import com.snakegame.model.Point;
import com.snakegame.enums.Direction;
import com.snakegame.enums.FoodType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    private List<Point> snake = new ArrayList<>();
    private Food food;
    private Direction direction = Direction.RIGHT;
    private boolean gameOver = true;
    private int score = 0;
    private long currentSpeed = GameConfiguration.NORMAL_SPEED;
    private long speedBoostEndTime = 0;

    public GameController() {
        resetGame();
    }

    public void resetGame() {
        snake.clear();
        snake.add(new Point(GameConfiguration.GRID_WIDTH / 2, GameConfiguration.GRID_HEIGHT / 2));
        snake.add(new Point(GameConfiguration.GRID_WIDTH / 2 - 1, GameConfiguration.GRID_HEIGHT / 2));
        snake.add(new Point(GameConfiguration.GRID_WIDTH / 2 - 2, GameConfiguration.GRID_HEIGHT / 2));
        direction = Direction.RIGHT;
        spawnFood();
        gameOver = false;
        score = 0;
        currentSpeed = GameConfiguration.NORMAL_SPEED;
        speedBoostEndTime = 0;
    }

    public void update() {
        if (gameOver) return;

        Point head = new Point(snake.get(0).x, snake.get(0).y);

        switch (direction) {
            case UP: head.y--; break;
            case DOWN: head.y++; break;
            case LEFT: head.x--; break;
            case RIGHT: head.x++; break;
        }

        if (isCollision(head)) {
            gameOver = true;
            return;
        }

        snake.add(0, head);

        if (head.x == food.getPoint().x && head.y == food.getPoint().y) {
            handleFoodConsumption();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private boolean isCollision(Point head) {
        if (head.x < 0 || head.x >= GameConfiguration.GRID_WIDTH || head.y < 0 || head.y >= GameConfiguration.GRID_HEIGHT) {
            return true;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.x == snake.get(i).x && head.y == snake.get(i).y) {
                return true;
            }
        }
        return false;
    }

    private void handleFoodConsumption() {
        score += food.getType().getScore();
        food.getType().getEffect().applyEffect(this);
        spawnFood();
    }

    public void spawnFood() {
        Random rand = new Random();
        int foodX, foodY;
        do {
            foodX = rand.nextInt(GameConfiguration.GRID_WIDTH);
            foodY = rand.nextInt(GameConfiguration.GRID_HEIGHT);
        } while (isFoodOnSnake(foodX, foodY));

        FoodType[] foodTypes = FoodType.values();
        FoodType randomType = foodTypes[rand.nextInt(foodTypes.length)];
        food = new Food(new Point(foodX, foodY), randomType);
    }

    private boolean isFoodOnSnake(int x, int y) {
        for (Point p : snake) {
            if (p.x == x && p.y == y) {
                return true;
            }
        }
        return false;
    }

    public void increaseSnakeLength(int length) {
        for (int i = 0; i < length; i++) {
            snake.add(new Point(-1, -1));
        }
    }

    public void setSpeed(long newSpeed, long durationNanos) {
        this.currentSpeed = newSpeed;
        this.speedBoostEndTime = System.nanoTime() + durationNanos;
    }

    public List<Point> getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore() {
        return score;
    }

    public long getCurrentSpeed() {
        if (System.nanoTime() > speedBoostEndTime) {
            currentSpeed = GameConfiguration.NORMAL_SPEED;
        }
        return currentSpeed;
    }
}
