package game;


import java.util.ArrayList;

import org.jbox2d.collision.shapes.ChainShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;


class Surface{
	
	
	private ArrayList<Vec2>     surface;
    private Game				game;
    
    
    Surface(Level level, LevelContentProvider contentProvider){
        game = level.getGame();
        
        surface = contentProvider.getLevelSurfaceVertices(level.getLevelNo());
        ChainShape chain = new ChainShape();
        Vec2[] vertices = new Vec2[surface.size()];
        
        for(int i=0; i<vertices.length; i++){
            vertices[i] = level.getLevel().coordPixelsToWorld(surface.get(i)); 
        }

        chain.createChain(vertices, vertices.length);
        
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
        for(Vec2 v: surface){
            game.vertex(v.x, v.y);
        } 
        game.endShape();
    }
}
