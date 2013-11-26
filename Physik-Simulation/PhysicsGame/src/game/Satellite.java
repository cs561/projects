package game;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import processing.core.PVector;

public class Satellite {
	
	private Body          	body;
	private Vec2			pos;
	private int				radius;
    private Level         	level;
    private Game			game;
    private int				color;
  
    
    Satellite(Level level, Vec2 pos, int r, Vec2 initImpulse, int color){ // TODO force not used
        this.level = level;
        this.pos = pos;
        this.radius = r;
        this.color = color;
        game = level.getGame();
      
        // body definition
        BodyDef bd = new BodyDef();
        bd.position = level.getLevel().coordPixelsToWorld(pos.x, pos.y);
        bd.type = BodyType.DYNAMIC;
        
        // body
        body = level.getLevel().world.createBody(bd);
            
        // shape (circle)
        CircleShape cs = new CircleShape();
        cs.m_radius = level.getLevel().scalarPixelsToWorld(radius);
            
        // ficture definition
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 2;
            
        // fixture 
        body.createFixture(fd);
        
        body.applyLinearImpulse(initImpulse, body.getPosition());

    }
    
    Body getBody() {
    	return this.body;
    }

    
    void display(){
        Vec2 pos = level.getLevel().getBodyPixelCoord(body);
        float a = body.getAngle();
        game.pushMatrix();
        game.translate(pos.x, pos.y);
        game.rotate(-a);
        game.fill(color);
        game.noStroke();
        game.ellipse(0,0,radius*2, radius*2);
        game.popMatrix();
    }
    
}