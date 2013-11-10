package game;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import processing.core.PImage;


class TowerElement{
	  
	
    private int       	topLeftX, topLeftY, w, h;
    private float     	density, friction, restitution;
    private Body      	body;
    private Level     	level;
    private Game		game;
    private boolean   	pointed;
    private PImage    	tex;
    
    private String[]  textureNames = {"wood1.jpg", "wood2.jpg", "wood3.jpg", "stone1.jpg", "stone2.jpg", "stone3.jpg", "metal1.jpg", "metal2.jpg", "metal3.jpg"};
   
    
    TowerElement(Level level, int tlX, int tlY, int w, int h, float d, float f, float r, int type){
        this.level = level;
        this.topLeftX = tlX;
        this.topLeftY = tlY;    
        this.w = w;
        this.h = h;
        this.density = d;
        this.friction = f;
        this.restitution = r;
        game = level.getGame();
        
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
        
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(level.getLevel().coordPixelsToWorld(topLeftX, topLeftY));
        body = level.getLevel().createBody(bd);
        
        PolygonShape ps = new PolygonShape();
        float bW = level.getLevel().scalarPixelsToWorld(w/2);  // ACHTUNGGGGGGGGGgg 
        float bH = level.getLevel().scalarPixelsToWorld(h/2);
        ps.setAsBox(bW, bH);
        
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = density;
        fd.friction = friction;
        fd.restitution = restitution;
        
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
        
        // TODO better detection of upset
        
        int absX = (int)Math.abs(pos.x - topLeftX);
        int absY = (int)Math.abs(pos.y - topLeftY);
        
        if((absX > 20 || absY > 20) && !pointed){
            pointed = true;
            level.increasePoints();
        } 
    }
    
    
    int[] getCoords(){
        Vec2 pos = level.getLevel().getBodyPixelCoord(body);
        int[] a = {(int)pos.x, (int)pos.y};
        return a;
    }
}
