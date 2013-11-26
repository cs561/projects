package game;


class WoodElement extends TowerElement{
	
    // using physical features of american red oak
    WoodElement(Level level, int tlX, int tlY, int w, int h, boolean onGround){
        super(level, tlX, tlY, w, h, 0.74f, 0.7f, 0.1f, 0.5f, 0, onGround, false);
    }
    
}
