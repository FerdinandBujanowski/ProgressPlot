package control.command;

import java.util.ArrayList;
import java.util.List;

import control.EditorInterface;
import model.clip.Clip;
import model.clip.ClipGroup;

public class CommandGroup implements Command {
	
	protected EditorInterface editor;
	protected ClipGroup group;
	protected List<Clip> ungrouped;
	
	public CommandGroup() {
		this.ungrouped = new ArrayList<>();
	}
	public CommandGroup(EditorInterface editor, List<Clip> elements) {
		this();
		this.editor = editor;
		this.ungrouped.addAll(elements);
		this.group = new ClipGroup(elements);
	}
	public CommandGroup(EditorInterface editor) {
		this(editor, editor.getSelection().getContents());
	}
	
	protected void group() {
		this.editor.getBoard().removeClip(this.ungrouped);
		this.editor.getBoard().addClip(this.group);
		
		this.editor.getSelection().clear();
		this.editor.getSelection().add(this.group);
	}
	
	protected void ungroup() {
		this.editor.getBoard().removeClip(this.group);
		this.editor.getBoard().addClip(this.ungrouped);
		
		this.editor.getSelection().clear();
		this.editor.getSelection().addAll(this.ungrouped);
	}

	@Override
	public void execute() {
		this.group();
		
	}

	@Override
	public void undo() {
		this.ungroup();
	}

}
