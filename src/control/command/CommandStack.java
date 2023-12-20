package control.command;

import java.util.Stack;


public class CommandStack {
	
	private CommandStackListener listener;
	
	private Stack<Command> undoStack;
	private Stack<Command> redoStack;
	
	public CommandStack() {
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();
	}
	
	public void addCommand(Command c) {
		//La commande va être exécutée avant d'être ajoutée dans l'undoStack
		c.execute();
		this.undoStack.add(c);
		this.redoStack.removeAll(this.redoStack);
		this.updateListener();
	}
	
	public void undo() {
		if(this.isUndoEmpty()) return;
		
		Command c = this.undoStack.pop();
		c.undo();
		this.redoStack.add(c);
		this.updateListener();
	}
	
	public void redo() {
		if(this.isRedoEmpty()) return;
		
		Command c = this.redoStack.pop();
		c.execute();
		this.undoStack.add(c);
		this.updateListener();
	}
	
	public boolean isUndoEmpty() {
		return this.undoStack.isEmpty();
	}
	
	public boolean isRedoEmpty() {
		return this.redoStack.isEmpty();
	}
	
	public void setListener(CommandStackListener listener) {
		this.listener = listener;
		this.updateListener();
	}
	
	private void updateListener() {
		if(this.listener != null) {
			this.listener.stackChanged();
		}
	}
}
