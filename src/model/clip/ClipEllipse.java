package model.clip;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipEllipse extends AbstractClip {

	public ClipEllipse(double left, double top, double right, double bottom, Color color) {
		super(left, top, right, bottom, color);
	}

	@Override
	public void draw(GraphicsContext ctx) {
		ctx.setFill(this.getColor());
		ctx.fillOval(this.getLeft(), this.getTop(), this.getRight() - this.getLeft(), this.getBottom() - this.getTop());
	}

	@Override
	public Clip copy() {
		return new ClipEllipse(this.getLeft(), this.getTop(), this.getRight(), this.getBottom(), this.getColor());
	}
	
	@Override
	public boolean isSelected(double x, double y) {
		double cx = (this.getLeft() + this.getRight()) / 2;
		double cy = (this.getTop() + this.getBottom()) / 2;
		double rx = (this.getRight() - this.getLeft()) / 2;
		double ry = (this.getBottom() - this.getTop()) / 2;
		
		double a = Math.pow((x - cx) / rx, 2);
		double b = Math.pow((y - cy) / ry, 2);

		return a + b <= 1.;
		
	}
	
}
