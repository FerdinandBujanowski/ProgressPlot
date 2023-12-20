package control.clipboard;

import model.clip.Clip;

import java.util.ArrayList;
import java.util.List;

public class Clipboard {
	
	private List<Clip> clips;
	private List<ClipboardListener> listeners;
	
	private static Clipboard INSTANCE;
	
	private Clipboard() {
		this.clips = new ArrayList<>();
		this.listeners = new ArrayList<>();
	}
	
	public static Clipboard getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Clipboard();
		}
		return INSTANCE;
	}
	
	public void copyToClipboard(List<Clip> clips) {
		for(Clip c : clips) {
			this.clips.add(c.copy());
		}
		this.updateListeners();
	}
	
	public List<Clip> copyFromClipboard() {
		List<Clip> copy = new ArrayList<>();
		for(Clip c : this.clips) {
			copy.add(c.copy());
		}
		return copy;
	}
	
	public void clear() {
		this.clips.clear();
		this.updateListeners();
	}
	
	public boolean isEmpty() {
		return this.clips.isEmpty();
	}
	
	public void addListener(ClipboardListener listener) {
		this.listeners.add(listener);
		this.updateListeners();
	}
	
	public void removeListener(ClipboardListener listener) {
		this.listeners.remove(listener);
	}
	
	private void updateListeners() {
		for(ClipboardListener l : this.listeners) {
			l.clipboardChanged();
		}
	}

}
