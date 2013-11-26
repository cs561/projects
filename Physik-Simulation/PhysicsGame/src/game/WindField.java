package game;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import processing.core.PImage;

public class WindField{

	Vec2	origin, wind;
	int		width, height, color;
	Level 	level;
	float 	angle;
	PImage	arrow;
	
	public WindField(Level level, Vec2 origin, int width, int height, Vec2 wind, int color){
		this.origin = origin;
		this.level = level;
		this.width = width;
		this.height = height;
		this.wind = wind;
		this.color = color;
		
		arrow = level.getGame().loadImage("wind.png");
		angle = calculateAngle(wind);
	}
	
	public void checkIfInFieldAndApplyForce(Body body){
		Vec2 pos = level.getLevel().getBodyPixelCoord(body);
		if(pos.x >= origin.x && pos.x <= origin.x+width && pos.y >= origin.y && pos.y <= origin.y+height){
			body.applyForceToCenter(wind);
		}
	}
	
	public void display(){
		level.getGame().noStroke();
		level.getGame().fill(color, 50);
		level.getGame().rect(origin.x, origin.y, width, height);
		level.getGame().pushMatrix();
		level.getGame().translate(origin.x + width/2, origin.y + height/2);
		level.getGame().rotate(angle);
		level.getGame().image(arrow,  - 50, - 17);
		level.getGame().popMatrix();
	}
	
	private float calculateAngle(Vec2 wind){
		if(wind.x == 0) wind.x = 0.001f;
		if(wind.y == 0) wind.y = 0.001f;
		float angle;
		if(wind.x < 0 && wind.y >= 0) {
			angle = new Float(Math.PI) + level.getGame().atan(wind.y/wind.x);
		} else if(wind.x < 0 && wind.y < 0){
			angle = new Float(-1*Math.PI) + level.getGame().atan(wind.y/wind.x);
		} else {
			angle = level.getGame().atan(wind.y/wind.x);
		}
		
		return angle;
	}
}
