package game;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

public class FixedElement {

	
	private ArrayList<Vec2>		vertices;
	private Game				game;
	
	
	public FixedElement(Level level, LevelContentProvider contentProvider, ArrayList<Vec2> vertices) {
		game = level.getGame();
		this.vertices = vertices;
		vertices.add(vertices.get(0));
		
		ChainShape chain = new ChainShape();
		
		Vec2[] vertices2 = new Vec2[vertices.size()];
        
        for(int i=0; i<vertices2.length; i++){
            vertices2[i] = level.getLevel().coordPixelsToWorld(vertices.get(i)); 
        }

        chain.createChain(vertices2, vertices2.length);
        
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        Body body = level.getLevel().world.createBody(bd);
        body.createFixture(chain, 1);  
	}
	
	void display(){
    	
        game.strokeWeight(1);
        game.stroke(185, 122, 87);
        
        game.strokeWeight(5);
        game.stroke(0);
        
        game.noFill();
        game.beginShape();
        for(Vec2 v: vertices){
            game.vertex(v.x, v.y);
        } 
        game.endShape(game.CLOSE);
    }
}
