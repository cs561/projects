package game;


import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import processing.core.PVector;


class Bullet{
	
	
	private Body          	body;
	private final float   	RADIUS = 10;
    private Level         	level;
    private Game			game;
    private int				angularVelocity;
  
    
    Bullet(Level level, int dx, int dy, int force){ // TODO force not used
        this.level = level;
        game = level.getGame();
      
        BodyDef bd = new BodyDef();
        bd.position = level.getLevel().coordPixelsToWorld(80, 600);
        bd.type = BodyType.DYNAMIC;
        bd.bullet = true;
        bd.linearDamping = 0.01f;
        body = level.getLevel().world.createBody(bd);
            
        CircleShape cs = new CircleShape();
        cs.m_radius = level.getLevel().scalarPixelsToWorld(RADIUS);
            
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 2.0f;
        fd.friction = 0.1f;
        fd.restitution = 0.3f;
            
        body.createFixture(fd);

        PVector v = new PVector(dx, dy);
        v.normalize();
        v.mult(game.actualForce*1.3f);
        
        body.applyLinearImpulse(new Vec2(v.x, v.y), body.getPosition());
       // body.setAngularVelocity(10000);
    }
    
    
    void display(){
        Vec2 pos = level.getLevel().getBodyPixelCoord(body);
        float a = body.getAngle();
        game.pushMatrix();
        game.translate(pos.x, pos.y);
        game.rotate(-a);
        game.fill(50);
        game.stroke(0);
        game.strokeWeight(1);
        game.ellipse(0,0,RADIUS*2, RADIUS*2);
        game.line(0,0,RADIUS,0);
        game.popMatrix();
    }
    
}