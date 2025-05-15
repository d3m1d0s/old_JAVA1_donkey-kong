package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class UniqueBarrel extends Barrel {
    public UniqueBarrel(Game game, Point2D position, double width, double height, Point2D velocity) {
        super(game, position, width, height, velocity);
        // Замените изображение на ваше собственное изображение для UniqueBarrel

        this.imageR = new Image(getClass().getResourceAsStream("unique_barrel.gif"), width, height,
                true, true);
        this.imageL = new Image(getClass().getResourceAsStream("unique_barrel.gif"), width, height,
                true, true);
    }

    @Override
    public void simulate(double timeDelta) {
        // Ваша собственная логика симуляции для UniqueBarrel
        // Можете переопределить этот метод по своему усмотрению.
        // Не забудьте вызвать супер-метод simulate, если нужно сохранить логику из Barrel.
        super.simulate(timeDelta);

        // Дополнительная логика для UniqueBarrel
        // ...
    }
}
