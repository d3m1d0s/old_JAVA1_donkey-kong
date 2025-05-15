package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class MovingEntity extends GameEntity implements Collisionable {
    protected static final double GRAVITY = 200;
    protected Point2D velocity;
    protected double width;
    protected double height;
    protected boolean onPlatform = false;
    protected boolean onLadder = false;
    protected boolean nearLadder = false;
    protected Image imageR;
    protected Image imageL;
    int imgR = 1;

    public MovingEntity(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position);
        this.velocity = velocity;
        this.width = width;
        this.height = height;
    }

    @Override
    public void simulate(double timeDelta) {
        if (onLadder) {
            position = position.add(0, velocity.getY() * timeDelta);

            if (!isOnLadder()) {
                stopClimbing();
            }
        } else {
            standardMovementLogic(timeDelta);
        }
    }

    protected void standardMovementLogic(double timeDelta) {
        if (onPlatform) {
            land(timeDelta);
        }
        if (!onPlatform) {
            fall(timeDelta);
        } else {
            // Определение платформы непосредственно под бочкой
            Platform platformUnderEntity = null;
            for (Platform platform : game.getPlatforms()) {
                if (this.getBoundingBox().intersects(platform.getBoundingBox())) {
                    platformUnderEntity = platform;
                    break;
                }
            }

            if (platformUnderEntity != null) {
                // Проверяем, можем ли мы поднять бочку на платформу
                double platformTopY = platformUnderEntity.getPosition().getY();
                double entityBottomY = position.getY() + height;

                if (Math.abs(entityBottomY - platformTopY) <= 5) {
                    // Поднимаем бочку на уровень платформы
                    position = new Point2D(position.getX(), platformTopY - height);
                } else {
                    // Бочка достигла края платформы и должна упасть
                    onPlatform = false;
                }
            } else {
                onPlatform = false;
            }
        }

        // Обновляем положение бочки с учетом ее скорости
        position = position.add(velocity.multiply(timeDelta));
        // В этом месте могут быть добавлены столкновения и другая логика симуляции.
    }

    public void fall(double timeDelta) {
        if (onPlatform) {
            velocity = new Point2D(velocity.getX(), velocity.getY());
            onPlatform = false;
        } else {
            velocity = velocity.add(0, GRAVITY * timeDelta);
        }
        position = position.add(0, velocity.getY() * timeDelta);
    }

    public void land(double timeDelta) {
        if (velocity.getY() > 0) { // Проверяем, что бочка падала вниз (её скорость по Y была положительной)
            velocity = new Point2D(velocity.getX(), 0); // Останавливаем вертикальное движение

            onPlatform = true; // Устанавливаем флаг, что бочка находится на платформе
            // Корректируем положение бочки, чтобы она "стояла" на платформе
            // Для этого нам нужно знать высоту платформы, но поскольку мы её не знаем,
            // мы можем аппроксимировать, установив Y позицию так, чтобы она была чуть выше Y позиции платформы
            // Это значение "platformTopY" должно быть вычислено или передано в метод при вызове
            double platformTopY = position.getY(); // Примерное вычисление
            double platformX = position.getX();
            position = new Point2D(platformX, platformTopY);
        } else if (velocity.getY() < 0) {
            velocity = new Point2D(velocity.getX(), 20);

            fall(timeDelta);
            double platformBottomY = position.getY(); // Примерное вычисление
            double platformX = position.getX();
            if (velocity.getX() > 0) {
                platformX = position.getX() - 20;
            } else if (velocity.getX() < 0) {
                platformX = position.getX() + 20;
            }
            position = new Point2D(platformX, platformBottomY);
        }
    }

    public void climbLadder(double deltaY) {
        if (onLadder) {
            position = position.add(0, deltaY);
        }
    }

    public void startClimbing() {
        onLadder = true;
        velocity = new Point2D(0, 0); // Останавливаем движение, когда начинаем взбираться
    }

    public void stopClimbing() {
        onLadder = false;
    }

    public boolean isNearLadder() {
        return nearLadder;
    }

    public boolean isOnLadder() {
        for (Ladder ladder : game.getLadders()) {
            if (this.getBoundingBox().intersects(ladder.getBoundingBox())) {
                onLadder = true;
                return true;
            }
        }
        onLadder = false;
        return false;
    }

    @Override
    protected void drawInternal(GraphicsContext gc) {
        if (velocity.getX() > 0) {
            imgR = 1;
            gc.drawImage(imageR, position.getX(), position.getY(), width, height);
        } else if (velocity.getX() < 0) {
            imgR = 0;
            gc.drawImage(imageL, position.getX(), position.getY(), width, height);
        } else if (imgR == 1) {
            gc.drawImage(imageR, position.getX(), position.getY(), width, height);
        } else if (imgR == 0) {
            gc.drawImage(imageL, position.getX(), position.getY(), width, height);
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), width, height);
    }

    @Override
    public boolean intersects(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Platform) {
            onPlatform = true;
            if (isOnLadder()) {
                stopClimbing();
            }
        }
        if (another instanceof Ladder) {
            nearLadder = true;
        } else {
            nearLadder = false;
        }
    }
}


