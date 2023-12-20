package control.command;

import java.util.List;

import control.EditorInterface;
import model.clip.Clip;

public class CommandDelete extends CommandAdd {

	public CommandDelete(EditorInterface editor, Clip toRemove) {
		super(editor, toRemove);
	}
	
	public CommandDelete(EditorInterface editor, List<Clip> toRemove) {
		super(editor, toRemove);
	}
	
	@Override
	public void execute() {
		this.remove();
	}
	
	@Override
	public void undo() {
		this.add();
	}

}
