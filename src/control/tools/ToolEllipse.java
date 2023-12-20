package control.tools;

import control.command.CommandAdd;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import model.clip.ClipEllipse;
import control.EditorInterface;

public class ToolEllipse extends ToolRect {
	
	@Override
	public void release(EditorInterface i, MouseEvent e) {
		this.drag = false;
		ClipEllipse newEllipse = new ClipEllipse(this.left, this.top, this.right, this.bottom, this.getColor());
		i.getUndoStack().addCommand(new CommandAdd(i, newEllipse));;
	}

	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		if(this.drag) {
			gc.setStroke(this.getColor());
			gc.strokeOval(this.left, this.top, this.right - this.left, this.bottom - this.top);
		}	
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Create Ellipse";
	}

}
