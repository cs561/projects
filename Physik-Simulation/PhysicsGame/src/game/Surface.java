package game;


import java.util.ArrayList;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;


class Surface{

	/**
	 * The surface, which represent the ground. It defines the neccessary
	 * Box2D objects and provides a display()-method to draw the surface.
	 */
	
	private ArrayList<Vec2>     surface;
    private Game				game;
    
    /**
     * Constructor
     * @param level				The Level Object, its methods and functions are used.
     * @param contentProvider	The content provider object.
     */
    Surface(Level level, LevelContentProvider contentProvider){
        game = level.getGame();
        
        // body definition
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        
        // body creation
        Body body = level.getLevel().world.createBody(bd);
        
        surface = contentProvider.getLevelSurfaceVertices(level.getLevelNo());
        
        // shape (chain)
        ChainShape chain = new ChainShape();
        Vec2[] vertices = new Vec2[surface.size()];
        
        for(int i=0; i<vertices.length; i++){
            vertices[i] = level.getLevel().coordPixelsToWorld(surface.get(i)); 
        }

        chain.createChain(vertices, vertices.length);
        
        // fixture creation
        body.createFixture(chain, 1);    
    }
    
    
    void display(){
    	
        game.strokeWeight(1);
        game.stroke(185, 122, 87);
        
        game.strokeWeight(5);
        game.stroke(0);
        
        game.noFill();
        game.beginShape();
        for(Vec2 v: surface){
            game.vertex(v.x, v.y);
        } 
        game.endShape();
    }
}
