package control.tools;

import java.io.File;

import control.command.CommandAdd;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import model.clip.ClipImage;
import control.EditorInterface;

public class ToolImage extends ToolRect {
	
	private File file;

	@Override
	public void release(EditorInterface i, MouseEvent e) {
		ClipImage newImage = new ClipImage(this.left, this.top, this.right, this.bottom, this.file);
		i.getUndoStack().addCommand(new CommandAdd(i, new ClipImage(this.left, this.top, this.right, this.bottom, this.file)));
		this.top = 0; this.left = 0; this.bottom = 0; this.right = 0;
	}

	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		super.drawFeedback(i, gc);
		
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Draw Image";
	}
	
	public void setImage(File file) {
		this.file = file;
	}

}
