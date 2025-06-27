package com.snakegame;

import com.snakegame.controller.GameController;
import com.snakegame.enums.Direction;
import com.snakegame.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static com.snakegame.config.GameConfiguration.HEIGHT;
import static com.snakegame.config.GameConfiguration.WIDTH;

public class SnakeGame extends Application {

    private GameController gameController;
    private GameView gameView;
    private long lastUpdate = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Snake Game");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2c3e50;");

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        Label scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(new Font("Arial", 24));
        scoreLabel.setTextFill(Color.WHITE);
        root.getChildren().add(scoreLabel);

        gameController = new GameController();
        gameView = new GameView();

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();

            if (gameController.isGameOver()) {
                if (code == KeyCode.ENTER) {
                    gameController.resetGame();
                }
                return;
            }

            if (code == KeyCode.UP && gameController.getDirection() != Direction.DOWN) {
                gameController.setDirection(Direction.UP);
            } else if (code == KeyCode.DOWN && gameController.getDirection() != Direction.UP) {
                gameController.setDirection(Direction.DOWN);
            } else if (code == KeyCode.LEFT && gameController.getDirection() != Direction.RIGHT) {
                gameController.setDirection(Direction.LEFT);
            } else if (code == KeyCode.RIGHT && gameController.getDirection() != Direction.LEFT) {
                gameController.setDirection(Direction.RIGHT);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        canvas.requestFocus();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= gameController.getCurrentSpeed()) {
                    if (gameController.isGameOver()) {
                        gameView.drawGameOver(gc, gameController.getScore());
                    } else {
                        gameController.update();
                        gameView.draw(gc, gameController);
                        scoreLabel.setText("Score: " + gameController.getScore());
                    }
                    lastUpdate = now;
                }
            }
        }.start();
    }
}
