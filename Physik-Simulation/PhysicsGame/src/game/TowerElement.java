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
    private boolean   	pointed, horizontal;
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
        
        if(w > h){
        	horizontal = true;
        }else{
        	horizontal = false;
        }
        
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
        
      /*  game.fill(255);
        game.rect(pos.x, pos.y, 10, 10);
        game.fill(200);
        game.rect(pos.x-w/2, pos.y+h/2, 10, 10);*/
        
        if(pointed){
        	game.fill(255,100);
        	game.ellipse(pos.x-2, pos.y-2, 16, 16);
            game.image(game.loadImage("check.png"), pos.x-8, pos.y-8, 15, 15);
        }
        
        // TODO better detection of upset
        
        boolean fallen = (Math.abs(a) > game.PI/4)? true : false;
        
        int absX = (int)Math.abs(pos.x - topLeftX);
        int absY = (int)Math.abs(pos.y - topLeftY);
        
        if((absY > 13) && !pointed){
        	if(horizontal){
        		if(absX > 13){
                    pointed = true;
                    level.increasePoints();
        		}
        	}else{
                pointed = true;
                level.increasePoints();
        	}
        } 
    }
    
    
    int[] getCoords(){
        Vec2 pos = level.getLevel().getBodyPixelCoord(body);
        int[] a = {(int)pos.x, (int)pos.y};
        return a;
    }
}
