package control.tools;

import control.EditorInterface;
import control.command.CommandMove;
import control.selection.Selection;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ToolSelection implements Tool {

	private double pressX, pressY;
	private double deltaX, deltaY;
	
	@Override
	public void press(EditorInterface i, MouseEvent e) {
		this.pressX = e.getX();
		this.pressY = e.getY();
		
		//Selection éventuelle
		if(e.isShiftDown()) {
			i.getSelection().toggleSelect(i.getBoard(), pressX, pressY);
		} else {
			i.getSelection().select(i.getBoard(), pressX, pressY);
		}
	}

	@Override
	public void drag(EditorInterface i, MouseEvent e) {
		//mouvement "théorique"
		this.deltaX = e.getX() - this.pressX;
		this.deltaY = e.getY() - this.pressY;
	}

	@Override
	public void release(EditorInterface i, MouseEvent e) {
		//mouvement va être réalisé
		i.getUndoStack().addCommand(new CommandMove(i, i.getSelection().getContents(), this.deltaX, this.deltaY));
		this.deltaX = 0;
		this.deltaY = 0;
	}

	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		i.getSelection().drawFeedBack(gc, this.deltaX, this.deltaY);
		
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Selection Tool";
	}

	@Override
	public Color getColor() {
		return Selection.selectionColor;
	}

	@Override
	public void setColor(Color c) {}

}
