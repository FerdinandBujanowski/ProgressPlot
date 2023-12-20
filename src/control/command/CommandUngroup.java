package control.command;

import control.EditorInterface;
import model.clip.ClipGroup;

public class CommandUngroup extends CommandGroup {
	
	public CommandUngroup(EditorInterface editor, ClipGroup group) {
		super();
		this.editor = editor;
		this.group = group;
		this.ungrouped = this.group.getClips();
	}
	
	public CommandUngroup(EditorInterface editor) {
		this(editor, (ClipGroup) editor.getSelection().getContents().get(0));
	}
	
	@Override
	public void execute() {
		this.ungroup();
	}
	
	@Override
	public void undo() {
		this.group();
	}

}
