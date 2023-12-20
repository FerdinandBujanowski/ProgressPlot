package model.clip;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipGroup extends AbstractClip implements Composite {
	
	private List<Clip> clips;

	public ClipGroup() {
		super(0, 0, 0, 0, Color.BLACK);
		this.clips = new ArrayList<>();
	}
	public ClipGroup(List<Clip> elements) {
		this();
		for(Clip c : elements) {
			this.addClip(c);
		}
	}

	@Override
	public void draw(GraphicsContext ctx) {
		for(Clip c : this.clips) {
			c.draw(ctx);
		}
		
	}

	@Override
	public Clip copy() {
		ClipGroup copy = new ClipGroup();
		for(Clip c : this.clips) {
			copy.addClip(c.copy());
		}
		return copy;
	}

	@Override
	public List<Clip> getClips() {
		return this.clips;
	}

	@Override
	public void addClip(Clip toAdd) {
		this.clips.add(toAdd);
		this.updateGeometry(toAdd);
	}

	@Override
	public void removeClip(Clip toRemove) {
		this.clips.remove(toRemove);
		
		//on va calculer de nouveau le rectangle englobant
		this.setGeometry(0, 0, 0, 0);
		for(Clip c : this.clips) {
			this.updateGeometry(c);
		}
	}
	
	@Override
	public void move(double x, double y) {
		super.move(x, y);
		for(Clip c : this.clips) {
			c.move(x, y);
		}
	}
	
	private void updateGeometry(Clip newClip) {
		//cas spécial : newClip est le premier clip
		if(this.clips.size() == 1) {
			this.setGeometry(
					newClip.getLeft(), 
					newClip.getTop(), 
					newClip.getRight(), 
					newClip.getBottom()
			);
		}
		else this.setGeometry(
				Math.min(getLeft(), newClip.getLeft()),
				Math.min(getTop(), newClip.getTop()),
				Math.max(getRight(), newClip.getRight()),
				Math.max(getBottom(), newClip.getBottom())
		);
	}

}
