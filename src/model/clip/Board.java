package model.clip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board implements Serializable {

	private List<Clip> elements;

	public Board() {
		this.elements = new ArrayList<>();
	}

	public List<Clip> getContents() {
		return this.elements;
	}

	public void addClip(Clip clip) {
		this.elements.add(clip);
	}

	public void addClip(List<Clip> clip) {
		this.elements.addAll(clip);
	}

	public void removeClip(Clip clip) {
		this.elements.remove(clip);
	}

	public void removeClip(List<Clip> clip) {
		this.elements.removeAll(clip);
	}

	public void draw(GraphicsContext gc) {
		// arrière-plan
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		
		// dessin de chaque élément graphique
		for(Clip c : this.elements) {
			c.draw(gc);
		}
	}
}
