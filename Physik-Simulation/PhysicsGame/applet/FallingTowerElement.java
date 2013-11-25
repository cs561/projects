package game;

public class FallingTowerElement extends TowerElement{
	
	public FallingTowerElement(Level level, int tlX, int tlY, int w, int h, boolean onGround){
		super(level, tlX, tlY, w, h, 10000.0f, 0.7f, 0.1f, 1.0f, 0, onGround);
	}

}
