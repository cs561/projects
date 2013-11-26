package game;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import processing.core.PVector;

class Bullet{
	
	/**
	 * The bullet, which gets shot by the player. It defines the neccessary
	 * Box2D objects and provides a display()-method to draw the bullet.
	 */
	
	private Body          	body;
	private final float   	RADIUS = 10;
    private Level         	level;
    private Game			game;
  
    
    Bullet(Level level, int dx, int dy, int spin){
        this.level = level;
        game = level.getGame();
      
        // body definition
        BodyDef bd = new BodyDef();
        bd.position = level.getLevel().coordPixelsToWorld(80, 600);
        bd.type = BodyType.DYNAMIC;
        bd.bullet = true;
        bd.linearDamping = 0.01f;
        
        // body
        body = level.getLevel().world.createBody(bd);
            
        // shape (circle)
        CircleShape cs = new CircleShape();
        cs.m_radius = level.getLevel().scalarPixelsToWorld(RADIUS);
            
        // ficture definition
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 2.0f;
        fd.friction = 0.1f;
        fd.restitution = 0.3f;
            
        // fixture 
        body.createFixture(fd);

        // applying the actual force
        PVector v = new PVector(dx, dy);
        v.normalize();
        float ballVelocity = game.actualForce;
  
        v.mult(((level.hasGravity())? ballVelocity : (ballVelocity/2)) *1.3f);
        
        body.applyLinearImpulse(new Vec2(v.x, v.y), body.getPosition());
        body.setAngularVelocity(spin*25);
        
       
        
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
        if(level.getLevelNo() == 3 || level.getLevelNo() == 4 || level.getLevelNo() == 12){
        	game.fill(225);
        }else{
            game.fill(50);	
        }
        game.stroke(0);
        game.strokeWeight(1);
        game.ellipse(0,0,RADIUS*2, RADIUS*2);
        game.popMatrix();
    }
    
}