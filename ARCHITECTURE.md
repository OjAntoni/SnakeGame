# Refactored Snake Game: Architecture and Logic Explanation

### 1. High-Level Architecture: Model-View-Controller (MVC)

The project is now structured following a pattern similar to **Model-View-Controller (MVC)**. This architectural choice is fundamental to achieving SOLID principles, especially the **Single Responsibility Principle (SRP)**.

*   **Model:** Represents the data and the core, framework-independent business logic of the application. (e.g., `Point`, `Food`, `GameController`).
*   **View:** Responsible for displaying the data from the Model to the user. It should not contain any game logic. (e.g., `GameView`).
*   **Controller/Orchestrator:** Acts as an intermediary, taking user input, telling the Model to update its state, and telling the View to refresh its display. In our case, the `SnakeGame` class acts as this orchestrator, handling input and the main game loop.

This separation ensures that changes to the UI (`View`) don't break the game's rules (`Model`), and the core logic can be tested independently of the user interface.

### 2. Directory Structure

The new structure clearly separates classes by their responsibility:

```
src/main/java/com/snakegame/
├── SnakeGame.java         // Main application class (Controller/Orchestrator)
├── config/
│   └── GameConfiguration.java // Constants and configuration
├── controller/
│   └── GameController.java    // Core game logic (Part of the Model)
├── enums/
│   ├── Direction.java
│   └── FoodType.java
├── model/
│   ├── Point.java
│   ├── Food.java
│   ├── FoodEffect.java      // Strategy Pattern Interface
│   ├── AppleEffect.java     // Strategy Pattern Concrete Class
│   ├── GoldenAppleEffect.java
│   └── GreenFoodEffect.java
└── view/
    └── GameView.java        // Rendering logic
```

---

### 3. Component-by-Component Breakdown

#### **`config/GameConfiguration.java`**

*   **Purpose:** Centralizes all static constants (like screen dimensions, tile size, and game speed).
*   **SOLID Principle:** This helps with maintainability. If you need to change the window size, you only need to do it in one place, rather than hunting for "magic numbers" throughout the code.

```java
// C:\Users\zeliy\Desktop\snakeJavaUI\src\main\java\com\snakegame\config\GameConfiguration.java
package com.snakegame.config;

public class GameConfiguration {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int TILE_SIZE = 20;
    public static final int GRID_WIDTH = WIDTH / TILE_SIZE;
    public static final int GRID_HEIGHT = HEIGHT / TILE_SIZE;
    public static final long NORMAL_SPEED = 100_000_000; // 10 FPS
    public static final long FAST_SPEED = 50_000_000;   // 20 FPS
}
```

#### **`model/` - The Data Classes**

*   **Purpose:** These are simple Plain Old Java Objects (POJOs) that hold data. `Point` holds coordinates, and `Food` holds a `Point` and a `FoodType`. They have no logic.
*   **SOLID Principle:** **Single Responsibility Principle (SRP)**. These classes are only responsible for holding data.

```java
// C:\Users\zeliy\Desktop\snakeJavaUI\src\main\java\com\snakegame\model\Point.java
package com.snakegame.model;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
```

#### **`view/GameView.java`**

*   **Purpose:** This class is solely responsible for drawing the game state onto the screen. It takes the `GameController` as a data source and renders the snake, food, grid, and game-over screen. It contains no game logic.
*   **SOLID Principle:** **SRP**. Its single responsibility is rendering. If you wanted to switch from JavaFX Canvas to a different rendering engine, this is the only class you would need to change significantly.

```java
// C:\Users\zeliy\Desktop\snakeJavaUI\src\main\java\com\snakegame\view\GameView.java
package com.snakegame.view;

// ... imports
public class GameView {

    public void draw(GraphicsContext gc, GameController controller) {
        // Clear screen
        gc.setFill(Color.web("#2c3e50"));
        gc.fillRect(0, 0, GameConfiguration.WIDTH, GameConfiguration.HEIGHT);

        // Draw game elements
        drawGrid(gc);
        drawBorder(gc);
        drawSnake(gc, controller);
        drawFood(gc, controller);
    }

    private void drawSnake(GraphicsContext gc, GameController controller) {
        for (int i = 0; i < controller.getSnake().size(); i++) {
            Point p = controller.getSnake().get(i);
            // ... drawing logic ...
        }
    }
    // ... other draw methods ...
}
```

#### **`controller/GameController.java`** - The Brain

*   **Purpose:** This is the heart of the game's logic. It manages the snake's position, direction, score, and speed. It handles collision detection, food spawning, and what happens when food is eaten. It knows nothing about how the game is displayed or how input is received.
*   **SOLID Principle:** **SRP**. Its single responsibility is managing the state of the game. It could be reused with a different UI or even in a text-based version of Snake.

```java
// C:\Users\zeliy\Desktop\snakeJavaUI\src\main\java\com\snakegame\controller\GameController.java
package com.snakegame.controller;

// ... imports
public class GameController {

    private List<Point> snake = new ArrayList<>();
    private Food food;
    private Direction direction = Direction.RIGHT;
    // ... other game state variables

    public void update() {
        if (gameOver) return;

        Point head = new Point(snake.get(0).x, snake.get(0).y);

        // Move head based on direction
        switch (direction) { /* ... */ }

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

    private void handleFoodConsumption() {
        score += food.getType().getScore();
        // DELEGATES the effect to the food type itself
        food.getType().getEffect().applyEffect(this);
        spawnFood();
    }
    // ... other methods like isCollision, spawnFood, getters/setters ...
}
```

#### **The Strategy Pattern for Food - `FoodEffect` and `FoodType`**

*   **Purpose:** To allow for adding new types of food with unique effects without ever modifying the `GameController`.
*   **SOLID Principle:** **Open/Closed Principle (OCP)**. The `GameController` is "closed" for modification but "open" for extension. You can add new food types (like a "Shrink" food or "Slow-Mo" food) by simply creating a new `FoodEffect` implementation and adding it to the `FoodType` enum. The controller's `handleFoodConsumption` method never needs to change.

1.  **The Interface (`FoodEffect.java`):** Defines a contract for all food effects.

    ```java
    // C:\Users\zeliy\Desktop\snakeJavaUI\src\main\java\com\snakegame\model\FoodEffect.java
    package com.snakegame.model;
    import com.snakegame.controller.GameController;

    public interface FoodEffect {
        void applyEffect(GameController gameController);
    }
    ```

2.  **A Concrete Strategy (`GoldenAppleEffect.java`):** Implements the effect for a specific food.

    ```java
    // C:\Users\zeliy\Desktop\snakeJavaUI\src\main\java\com\snakegame\model\GoldenAppleEffect.java
    package com.snakegame.model;
    // ... imports

    public class GoldenAppleEffect implements FoodEffect {
        @Override
        public void applyEffect(GameController gameController) {
            gameController.increaseSnakeLength(1);
            gameController.setSpeed(GameConfiguration.FAST_SPEED, 6_000_000_000L);
        }
    }
    ```

3.  **The Enum (`FoodType.java`):** Associates each food type with its color, score, and its specific effect strategy.

    ```java
    // C:\Users\zeliy\Desktop\snakeJavaUI\src\main\java\com\snakegame\enums\FoodType.java
    package com.snakegame.enums;
    // ... imports

    public enum FoodType {
        APPLE(Color.web("#e74c3c"), 1, new AppleEffect()),
        GOLDEN_APPLE(Color.web("#f1c40f"), 5, new GoldenAppleEffect()),
        GREEN_FOOD(Color.web("#2ecc71"), 10, new GreenFoodEffect());

        // ... fields and constructor
        private final FoodEffect effect;

        public FoodEffect getEffect() {
            return effect;
        }
    }
    ```

#### **`SnakeGame.java` - The Orchestrator**

*   **Purpose:** This is the main entry point of the JavaFX application. It's responsible for:
    1.  Setting up the window, canvas, and labels.
    2.  Creating instances of the `GameController` and `GameView`.
    3.  Handling all user input (key presses) and delegating actions to the `GameController`.
    4.  Running the main game loop (`AnimationTimer`) which orchestrates calls to `gameController.update()` and `gameView.draw()`.
*   **SOLID Principle:** **Dependency Inversion Principle (DIP)** (in a way). This high-level module depends on abstractions (the public methods of `GameController` and `GameView`), not on the low-level details of their implementation.

```java
// C:\Users\zeliy\Desktop\snakeJavaUI\src\main\java\com\snakegame\SnakeGame.java
package com.snakegame;
// ... imports

public class SnakeGame extends Application {

    private GameController gameController;
    private GameView gameView;
    // ...

    @Override
    public void start(Stage primaryStage) {
        // ... setup UI (VBox, Canvas, Label) ...

        gameController = new GameController();
        gameView = new GameView();

        Scene scene = new Scene(root);

        // Handle Input
        scene.setOnKeyPressed(e -> {
            // ... logic to check keys ...
            // Tell the controller what to do
            gameController.setDirection(Direction.UP);
        });

        // ... set scene and show stage ...

        // The Game Loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= gameController.getCurrentSpeed()) {
                    if (gameController.isGameOver()) {
                        gameView.drawGameOver(gc, gameController.getScore());
                    } else {
                        // 1. Update game state
                        gameController.update();
                        // 2. Draw the new state
                        gameView.draw(gc, gameController);
                        scoreLabel.setText("Score: " + gameController.getScore());
                    }
                    lastUpdate = now;
                }
            }
        }.start();
    }
}
```

### Summary of Game Logic Flow

1.  **Initialization:** `SnakeGame.start()` runs. It creates the UI, a `GameController`, and a `GameView`. The `GameController`'s constructor immediately sets up the initial snake and food.
2.  **Game Loop:** The `AnimationTimer` starts, calling its `handle` method repeatedly.
3.  **Input:** You press the `UP` arrow key. The `setOnKeyPressed` lambda in `SnakeGame` captures this. It calls `gameController.setDirection(Direction.UP)`. The `GameController` updates its internal `direction` variable.
4.  **Update:** The `AnimationTimer` decides it's time for the next frame. It calls `gameController.update()`.
    *   The controller calculates the new head position based on the current `direction`.
    *   It checks if this new position is a collision (wall or self). If so, it sets `gameOver = true`.
    *   If no collision, it adds the new head to the `snake` list.
    *   It checks if the head is on a food tile.
        *   If **no**, it removes the last segment of the snake's tail (making it move).
        *   If **yes**, it calls `handleFoodConsumption()`. This method gets the `FoodEffect` from the `FoodType` and calls its `applyEffect` method, which might increase the snake's length or change its speed. A new food is then spawned.
5.  **Render:** The `AnimationTimer` then calls `gameView.draw(gc, gameController)`.
    *   The `GameView` gets the latest snake list and food position from the `gameController`.
    *   It clears the screen and redraws everything in its new position.
6.  **Repeat:** The loop continues from step 3/4.
