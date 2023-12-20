package control.command;

import java.util.ArrayList;
import java.util.List;

import control.EditorInterface;
import model.clip.Clip;

public class CommandMove implements Command {

	//jsp pourquoi il faut l'inclure
	private EditorInterface editor;
	
	private List<Clip> clips;
	private double deltaX, deltaY;
	
	private CommandMove(EditorInterface editor, double dX, double dY) {
		this.clips = new ArrayList<>();
		this.editor = editor;
		this.deltaX = dX;
		this.deltaY = dY;
	}
	public CommandMove(EditorInterface editor, Clip c, double dX, double dY) {
		this(editor, dX, dY);
		this.clips.add(c);
	}
	public CommandMove(EditorInterface editor, List<Clip> c, double dX, double dY) {
		this(editor, dX, dY);
		this.clips.addAll(c);
	}

	@Override
	public void execute() {
		for(Clip clip : this.clips) {
			clip.move(this.deltaX, this.deltaY);
		}
	}

	@Override
	public void undo() {
		for(Clip clip : this.clips) {
			clip.move(-this.deltaX, -this.deltaY);
		}
	}
	
	
}
