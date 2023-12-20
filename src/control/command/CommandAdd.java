package control.command;

import java.util.ArrayList;
import java.util.List;

import control.EditorInterface;
import model.clip.Clip;

public class CommandAdd implements Command {
	
	private EditorInterface editor;
	private List<Clip> toAdd;
	
	public CommandAdd(EditorInterface editor) {
		this.toAdd = new ArrayList<>();
		this.editor = editor;
	}
	public CommandAdd(EditorInterface editor, Clip toAdd) {
		this(editor);
		this.toAdd.add(toAdd);
	}
	public CommandAdd(EditorInterface editor, List<Clip> toAdd) {
		this(editor);
		this.toAdd.addAll(toAdd);
	}
	
	protected void add() {
		this.editor.getBoard().addClip(toAdd);
	}
	
	protected void remove() {
		this.editor.getBoard().removeClip(toAdd);
	}

	@Override
	public void execute() {
		this.add();
	}

	@Override
	public void undo() {
		this.remove();
	}

}
