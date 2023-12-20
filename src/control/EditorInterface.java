package control;

import control.command.CommandStack;
import control.selection.Selection;
import model.clip.Board;

public interface EditorInterface {
	public Board getBoard();
	public Selection getSelection();
	public CommandStack getUndoStack();
}
