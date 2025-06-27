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
