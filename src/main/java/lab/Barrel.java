package lab;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.util.Random;

public class Barrel extends WalkingEnemy {
    private Random random = new Random();
    private Ladder currentLadder = null;
    private boolean shouldBeRemoved = false;


    public Barrel(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height, velocity);
        this.imageR = new Image(getClass().getResourceAsStream("common_barrel_right.gif"), width, height, true, true);
        this.imageL = new Image(getClass().getResourceAsStream("common_barrel_left.gif"), width, height, true, true);
    }

    @Override
    public void simulate(double timeDelta) {
        // Обработка движения
        if (onLadder) {
            // Движение по лестнице
            position = position.add(0, velocity.getY() * timeDelta);
            if (currentLadder != null && position.getY() > currentLadder.getPosition().getY() + currentLadder.getHeight()) {
                stopClimbing();
            }
        } else if (onPlatform) {
            // Движение по платформе
            position = position.add(velocity.getX() * timeDelta, 0);
            // Отражение от стенок
            if (position.getX() < 50 || position.getX() > game.getWidth() - width - 50) {
                velocity = new Point2D(-velocity.getX(), velocity.getY());
            }
            if (position.getY() > 291.5 - height && position.getY() < 335.5) {
                position = position.add(-4 * velocity.getX() * timeDelta, 0);
            }
            if (position.getY() > 488.5 - height && position.getY() < 532.5) {
                position = position.add(-4 * velocity.getX() * timeDelta, 0);
            }
            // Определение нахождения бочки на лестнице

            for (Ladder ladder : game.getLadders()) {
                if (this.intersects(ladder.getBoundingBox())) {
                    // Проверка, является ли это лестницей, соприкасающейся с платформой
                    if (ladder.isAtBottom(position, height)) {
                        // Бочка должна "провалиться" сквозь сегмент лестницы
                        continue;
                    }
                    nearLadder = true;
                    // Решение о спуске по лестнице принимается только если прошло достаточно времени

                    if (random.nextBoolean()) {
                        startClimbing();
                        currentLadder = ladder;
                    }

                }
            }
            standardMovementLogic(timeDelta);
        } else {
            // Падение
            fall(timeDelta);
        }
    }

    // Оставшиеся методы класса Barrel...
    // ...

    @Override
    public void startClimbing() {
        onLadder = true;
        velocity = new Point2D(0, 50); // Скорость спуска по лестнице
    }

    @Override
    public void stopClimbing() {
        onLadder = false;
        velocity = new Point2D(velocity.getX(), 0); // Восстанавливаем горизонтальное движение
        currentLadder = null;
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Platform) {
            onPlatform = true;
        } else if (another instanceof Ladder) {
            Ladder ladder = (Ladder) another;
            if (!ladder.isAtBottom(position, height)) {
                nearLadder = true;
                onPlatform = false;
            }
        } else if (another instanceof FireBarrel) {
            shouldBeRemoved = true;  // Установка флага для удаления объекта
        }
    }

    public boolean shouldBeRemoved() {
        return shouldBeRemoved;
    }

    // Методы и поля класса MovingEntity и другие методы, если они есть
    // ...
}
