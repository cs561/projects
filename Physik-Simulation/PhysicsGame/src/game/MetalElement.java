package game;

class MetalElement extends TowerElement{

    // using physical features of steel c30
    MetalElement(Level level, int tlX, int tlY, int w, int h){
        super(level, tlX, tlY, w, h, 7.85f, 0.7f, 0.1f, 2);
    } 
    
}
