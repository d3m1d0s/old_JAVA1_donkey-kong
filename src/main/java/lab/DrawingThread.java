package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DrawingThread extends AnimationTimer {

	private final Canvas canvas;
	private final GraphicsContext gc;

	private long lastTime;

	private final Game game;

	public DrawingThread(Canvas canvas) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();

		this.game = new Game(canvas.getWidth(), canvas.getHeight());
	}

	//private double currentX = 0;
	@Override
	public void handle(long now) {
		if (lastTime > 0) {
			double deltaT = (now - lastTime) / 1e9;
			game.simulate(deltaT);
		}
		game.draw(gc);

		lastTime = now;

	}



}
