package client;

import java.awt.Font;

import com.jmex.font2d.Font2D;
import com.jmex.font2d.Text2D;
import common.world.Ship;

import client.world.NodeThatCanRemoveAllChildren;

public class TextBox extends NodeThatCanRemoveAllChildren {
	private static final long serialVersionUID = 1L;
	
	private static final Font2D f2d = new Font2D();
	
	private final float xbase;
	private final float ybase;
	private float growDir;
	
	public TextBox(String name, float xbase, float ybase) {
		super(name);
		this.xbase = xbase;
		this.ybase = ybase;
		setGrowUpwards(false);
	}
	
	public void updateText(String[] lines) {
		removeAllChildren();
		
		float pos = 0f;
		for (String line : lines) {
			final Text2D text = f2d.createText(line, 1f, Font.PLAIN);
			text.setLocalTranslation(xbase, ybase+pos, 0f);
			attachChild(text);
			
			pos += growDir;
		}
	}
	
	public void setGrowUpwards(boolean b) {
		growDir = b ? 20f : -20f; 
	}
}
