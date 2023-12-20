package model.clip;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipRect extends AbstractClip {

	public ClipRect(double left, double top, double right, double bottom, Color color) {
		super(left, top, right, bottom, color);
	}

	@Override
	public void draw(GraphicsContext ctx) {
		ctx.setFill(this.getColor());
		ctx.fillRect(this.getLeft(), this.getTop(), this.getRight() - this.getLeft(), this.getBottom() - this.getTop());
	}

	@Override
	public Clip copy() {
		return new ClipRect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom(), this.getColor());				
	}

}
