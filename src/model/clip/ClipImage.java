package model.clip;

import java.io.*;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ClipImage extends ClipRect {

	private File fileName;

	private transient Image image;

	public ClipImage(double left, double top, double right, double bottom, File fileName) {
		super(left, top, right, bottom, Color.BLACK);
		this.fileName = fileName;
	}

	@Override
	public void draw(GraphicsContext ctx) {
		if(this.image == null) {
			try {
				this.image = new Image(new FileInputStream(fileName.getAbsolutePath()), getRight()-getLeft(), getBottom()-getTop(), false, true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		ctx.drawImage(this.image, this.getLeft(), this.getTop());
	}

	@Override
	public Clip copy() {
		return new ClipImage(this.getLeft(), this.getTop(), this.getRight(), this.getBottom(), this.fileName);
	}

}
