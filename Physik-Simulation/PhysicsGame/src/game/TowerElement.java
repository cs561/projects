package game;



import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import processing.core.PImage;


class TowerElement{
	  
	/**
	 * This class represents a single tower in a tower construction of a level. It has specified 
	 * physical attributes like density, friction etc., which are hard coded in the respective 
	 * subclass (WoodElement, StoneElement etc.). Additionally, it provides a display()-function
	 * to draw the element and a detection-method to detect if the tower is considered as "fallen".
	 * 
	 */
	
    private int       	topLeftX, topLeftY, w, h;
    private float     	density, friction, restitution;
    private Body      	body;
    private Level     	level;
    private Game		game;
    private boolean   	pointed, horizontal, onGround, isFalling;
    private PImage    	tex;
    
    private String[]  textureNames = {"wood1.jpg", "wood2.jpg", "wood3.jpg", "stone1.jpg", "stone2.jpg", "stone3.jpg", "metal1.jpg", "metal2.jpg", "metal3.jpg"};
   
    /**
     * Constructor
     */
    TowerElement(Level level, int tlX, int tlY, int w, int h, float d, float f, float r, float ld, int type, boolean onGround, boolean isFalling){
        this.isFalling = isFalling;
    	this.level = level;
        this.topLeftX = tlX;
        this.topLeftY = tlY;    
        this.w = w;
        this.h = h;
        this.density = d;
        this.friction = f;
        this.restitution = r;
        this.onGround = onGround;
        game = level.getGame();
        
        /* define if the tower is in horizontal position */
        if(w > h){
        	horizontal = true;
        }else{
        	horizontal = false;
        }
        
        /* load the appropriate texture */
        switch(type){
            case 0:
                tex = game.loadImage(textureNames[(int)game.random(1,3)]);
                break;
            case 1:
                tex = game.loadImage(textureNames[(int)game.random(4,6)]);
                break;
            case 2:
                tex = game.loadImage(textureNames[(int)game.random(7,9)]);
                break;
        }
        
        // body definition
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(level.getLevel().coordPixelsToWorld(topLeftX, topLeftY));
        
        // body creation
        body = level.getLevel().createBody(bd);
        bd.linearDamping = ld;
        
        // shape (polygon)
        PolygonShape ps = new PolygonShape();
        float bW = level.getLevel().scalarPixelsToWorld(w/2);  // ACHTUNGGGGGGGGGgg 
        float bH = level.getLevel().scalarPixelsToWorld(h/2);
        ps.setAsBox(bW, bH);
        
        // fixture definiton
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = density;
        fd.friction = friction;
        fd.restitution = restitution;
        
        // fixture creation
        body.createFixture(fd);
    } 
    
    
    void display(){
        Vec2 pos = level.getLevel().getBodyPixelCoord(body);
        float a = body.getAngle(); 

        game.pushMatrix();
        game.translate(pos.x, pos.y);
        game.rotate(-a); 
        game.noFill();
        game.image(tex, -w/2, -h/2, w, h);
        game.popMatrix();
        
        // if the tower is considered as fallen, a hook is drawn on it
        if(pointed){
        	game.noStroke();
        	game.fill(255,100);
        	game.ellipse(pos.x-2, pos.y, 20, 20);
            game.image(game.loadImage("check.png"), pos.x-8, pos.y-8, 15, 15);
        }
        
        
        int absX = (int)Math.abs(pos.x - topLeftX);
        int absY = (int)Math.abs(pos.y - topLeftY);
        
        if(detectIfFallen(absX, absY, a) && !pointed){
        	pointed = true;
        	level.increasePoints();
        }
    }
    
   /**
    * detect, if the tower is fallen. It is necessary to switch between the three cases:
    * - a tower element is standing on the ground (no horizontal towers allowed)!!!!!
    * - a tower element is standing vertically on other towers
    * - a tower element is lying horizontally on other towers
    * @return
    */
	boolean detectIfFallen(int absX, int absY, float angle){
		if (level.hasGravity()) {
			if (onGround) {
				if (Math.abs(angle) > game.PI / 4) {
					return true;
				}
			} else {
				if (horizontal) {
					if (absY > 20) {
					return true;
					}
				} else {
					if (Math.abs(angle) > game.PI / 4 && absY > 40) {
						return true;
					}
				}
			}
		}else{
			Vec2 currentPosition = level.getLevel().getBodyPixelCoord(body);
			if(currentPosition.x < -50 || currentPosition.x > 1330 || currentPosition.y < 0 || currentPosition.y > 770) return true;
			else return false;
		}
		return false;
	}
    
   /**
    * Returns the coordinates of the body as Processing coords.
    * @return
    */
    int[] getCoords(){
        Vec2 pos = level.getLevel().getBodyPixelCoord(body);
        int[] a = {(int)pos.x, (int)pos.y};
        return a;
    }
}
