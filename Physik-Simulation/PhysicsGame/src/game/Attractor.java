package game;


import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

public class Attractor{

	  // We need to keep track of a Body and a radius
	  Body body;
	  float r;
	  Level level;
	  float G;
	  int attracts, maxConstrain;
	  float x, y;
	  
	  
	  Attractor(float r, float x, float y, Level level, int attracts, int maxConstrain, float G) {
	    this.r = r;
	    this.G = G;
	    this.level = level;
	    this.attracts = attracts;
	    this.maxConstrain = maxConstrain;
	    this.x = x;
	    this.y = y;
	    // Define a body
	    BodyDef bd = new BodyDef();
	    bd.type = BodyType.STATIC;
	    // Set its position
	    bd.position = level.getLevel().coordPixelsToWorld(x,y);
	    body = level.getLevel().world.createBody(bd);

	  }

	  Vec2 attract(Body body) {
	    Vec2 pos = this.body.getWorldCenter();
	    Vec2 moverPos = body.getWorldCenter();
	    Vec2 force = pos.sub(moverPos);
	    
	    float distance = force.length();
	    distance = level.getGame().constrain(distance,1,maxConstrain);
	    force.normalize();
	    
	    float strength = (G * 1 * body.m_mass) / (distance * distance);  
	    force.mulLocal(strength*attracts);        
	    return force;
	  }
	  
	  Body getBody(){
		  return body;
	  }

	  void display() {
	    // We look at each body and get its screen position
	    Vec2 pos = level.getLevel().getBodyPixelCoord(body);
	    // Get its angle of rotation
	    float a = body.getAngle();
	    level.getGame().pushMatrix();
	    level.getGame().translate(pos.x,pos.y);
	    level.getGame().rotate(a);
	    level.getGame().fill(255, 215, 0, 100);
	    level.getGame().stroke(255, 215, 0);
	    level.getGame().strokeWeight(2);
	    level.getGame().ellipse(0,0,r*2,r*2);
	    level.getGame().popMatrix();
	  }
}