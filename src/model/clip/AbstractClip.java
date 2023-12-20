package model.clip;

import java.io.Serializable;
import javafx.scene.paint.Color;

public abstract class AbstractClip implements Clip, Serializable {

	private double left, right, top, bottom;
	private SerializableColor color;

	public AbstractClip(double left, double top, double right, double bottom, Color color) {
		this.setGeometry(left, top, right, bottom);
		this.color = new SerializableColor(color);
	}
	
	@Override
	public double getTop() {
		return this.top;
	}

	@Override
	public double getLeft() {
		return this.left;
	}

	@Override
	public double getBottom() {
		return this.bottom;
	}

	@Override
	public double getRight() {
		return this.right;
	}

	@Override
	public void setGeometry(double left, double top, double right, double bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	@Override
	public void move(double x, double y) {
		this.setGeometry(this.left + x, this.top + y, this.right + x, this.bottom + y);
		
	}

	@Override
	public boolean isSelected(double x, double y) {
		return x > this.left && x <= this.right && y > this.top && y <= this.bottom;
	}

	@Override
	public void setColor(Color c) {
		this.color = new SerializableColor(c);
	}

	@Override
	public Color getColor() {
		return this.color.asColor();
	}

}
