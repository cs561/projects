package game;

class StoneElement extends TowerElement{

    // using physical features of soapstone
    StoneElement(Level level, int tlX, int tlY, int w, int h){
        super(level, tlX, tlY, w, h, 2.75f, 0.7f, 0.1f, 1);
    } 
    
}
