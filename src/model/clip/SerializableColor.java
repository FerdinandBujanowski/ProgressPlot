package model.clip;

import java.io.Serializable;

import javafx.scene.paint.Color;

public class SerializableColor implements Serializable {
	
	private double r, g, b;
	
	public SerializableColor(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public SerializableColor(Color c) {
		this(c.getRed(), c.getGreen(), c.getBlue());
	}
	
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	
	public double getG() {
		return this.g;
	}
	public void setG(double g) {
		this.g = g;
	}
	
	public double getB() {
		return this.b;
	}
	public void setB(double b) {
		this.b = b;
	}
	
	public Color asColor() {
		return new Color(r, g, b, 1.);
	}
	

}
