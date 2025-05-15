package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;

public class GameController {
    private Game game;
    private AnimationTimer animationTimer;
    private Canvas canvas;

    public GameController(Canvas canvas) {
        this.canvas = canvas;
        this.game = new Game(canvas.getWidth(), canvas.getHeight());
        // Дополнительная инициализация, если требуется
    }

    public void startGame() {
        /*
        // Инициализация игры
        game.initializeGameObjects(); // Предполагается, что это метод для инициализации объектов игры
         */
        game.startGame();

        // Настройка анимационного таймера
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.simulate(1.0 / 60); // Предположим, что метод simulate принимает deltaTime
                game.draw(canvas.getGraphicsContext2D());
            }
        };

        animationTimer.start();
    }

    public void stopGame() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }

    public void initializeControlHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                game.getMario().moveRight();
            } else if (event.getCode() == KeyCode.LEFT) {
                game.getMario().moveLeft();
            } else if (event.getCode() == KeyCode.SPACE) {
                game.getMario().jump();
            }
            else if (event.getCode() == KeyCode.UP) {
                if (game.getMario().isOnLadder() && game.getMario().isNearLadder()) {
                    game.getMario().startClimbing();
                    game.getMario().climbLadder(-4); // Примерное значение для подъема вверх
                }
            } else if (event.getCode() == KeyCode.DOWN) {
                if (game.getMario().isOnLadder()) {
                    game.getMario().startClimbing();
                    game.getMario().climbLadder(4); // Примерное значение для спуска вниз
                }
            }
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT) {
                game.getMario().stopMoving();
            } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
                if (game.getMario().isOnLadder() && game.getMario().isNearLadder()) {
                    game.getMario().stopClimbing();
                } //else game.getMario().stopClimbing();
            }
        });
    }
}
