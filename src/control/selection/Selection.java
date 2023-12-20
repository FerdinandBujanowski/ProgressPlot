package control.selection;

import java.util.*;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import model.clip.*;

public class Selection {
	
	public static Color selectionColor = Color.BLUE;
	private List<Clip> contents;
	private List<SelectionListener> selectionListeners;
	
	public Selection() {
		this.contents = new ArrayList<>();
		this.selectionListeners = new ArrayList<>();
	}
	
	public void select(Board board, double x, double y) {
		this.clear();

		for(int i = board.getContents().size() - 1; i >= 0; i--) {
			Clip c = board.getContents().get(i);
			if(c.isSelected(x, y)) {
				this.add(c);
				return;
			}
		}
	}
	
	public void toggleSelect(Board board, double x, double y) {
		
		for(int i = board.getContents().size() - 1; i >= 0; i--) {
			Clip c = board.getContents().get(i);
			if(c.isSelected(x, y)) {
				if(this.contents.contains(c)) {
					this.contents.remove(c);
				} else {
					this.contents.add(c);
				}
				this.updateListeners();
				return;
			}
		}
	}
	
	public void addAll(Collection<Clip> clips) {
		this.contents.addAll(clips);
		this.updateListeners();
	}
	public void add(Clip clip) {
		this.contents.add(clip);
		this.updateListeners();
	}
	
	public void clear() {
		this.contents.clear();
		this.updateListeners();
	}
	
	public List<Clip> getContents() {
		return this.contents;
	}
	
	public void drawFeedBack(GraphicsContext gc, double deltaX, double deltaY) {
		gc.setStroke(selectionColor);
		for(Clip c : this.contents) {
			gc.strokeRect(c.getLeft() + deltaX, c.getTop() + deltaY, c.getRight() - c.getLeft(), c.getBottom() - c.getTop());
		}
	}
	
	public void addListener(SelectionListener listener) {
		this.selectionListeners.add(listener);
		this.updateListeners();
	}
	
	public void removeListener(SelectionListener listener) {
		this.selectionListeners.remove(listener);
	}
	
	public void updateListeners() {
		for(SelectionListener listener : this.selectionListeners) {
			listener.selectionChanged();
		}
	}

	public boolean hasSelectedGroup() {
		if(this.contents.isEmpty()) return false;
		return this.contents.get(0) instanceof ClipGroup;
	}
}
