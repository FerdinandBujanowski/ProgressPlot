package control.tools;

import control.command.CommandAdd;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.clip.ClipRect;
import control.EditorInterface;

/**
 * Outil de création d'un rectangle
 */
public class ToolRect implements Tool {
	
	/**
	 * Coordonnées des évenements souris
	 */
	private int pressX = 0, dragX = 0, pressY = 0, dragY = 0;
	protected boolean drag = false;
	/**
	 * Coordonnées courantes du rectangle qui va être créé
	 */
	protected int top, left, bottom, right;
	
	private Color color = Color.WHITE;

	@Override
	/**
	 * Mise à jour des coordonnées 'press' et 'drag'
	 */
	public void press(EditorInterface i, MouseEvent e) {
		this.top = 0; this.left = 0; this.bottom = 0; this.right = 0;
		
		this.pressX = (int)e.getX();
		this.pressY = (int)e.getY();
		this.dragX = this.pressX;
		this.dragY = this.pressY;
		this.drag = true;
		
	}

	@Override
	/**
	 * Mise à jour des coordonnées 'drag' et coordonnées courantes du rectangle
	 */
	public void drag(EditorInterface i, MouseEvent e) {
		this.dragX = (int)e.getX();
		this.dragY = (int)e.getY();
		
		if(this.dragX < this.pressX) {
			this.left = this.dragX;
			this.right = this.pressX;
		} else {
			this.left = this.pressX;
			this.right = this.dragX;
		}
		if(this.dragY < this.pressY) {
			this.top = this.dragY;
			this.bottom = this.pressY;
		} else {
			this.top = this.pressY;
			this.bottom = this.dragY;
		}
	}

	@Override
	/**
	 *Création du rectangle
	 */
	public void release(EditorInterface i, MouseEvent e) {
		this.drag = false;
		
		ClipRect newRect = new ClipRect(this.left, this.top, this.right, this.bottom, this.getColor());
		i.getUndoStack().addCommand(new CommandAdd(i, newRect));
	}

	@Override
	/**
	 * Dessin du contour du rectangle pour indiquer ses coordonnées courantes
	 */
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		if(this.drag) {
			gc.setStroke(this.color);
			gc.strokeRect(this.left, this.top, this.right - this.left, this.bottom - this.top);
		}	
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Create Rectangle";
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void setColor(Color c) {
		this.color = c;
	}

}
