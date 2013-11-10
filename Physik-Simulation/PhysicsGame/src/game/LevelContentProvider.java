package game;


import java.util.ArrayList;

import org.jbox2d.common.Vec2;


class LevelContentProvider{
	  
	
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
  
    
    public ArrayList<Vec2> getLevelSurfaceVertices(int levelNo){
        ArrayList<Vec2> surface = new ArrayList<Vec2>();
       
        switch(levelNo){
            case 1: case 2:
                surface.add(new Vec2(0, HEIGHT/2+250));
                surface.add(new Vec2(200, HEIGHT/2+250));
                surface.add(new Vec2(WIDTH/2+300, HEIGHT/2+50));
                surface.add(new Vec2(WIDTH, HEIGHT/2+50)); 
                break;
            case 3: case 4: 
                surface.add(new Vec2(0, HEIGHT/2+250));
                surface.add(new Vec2(600, HEIGHT/2+250));
                surface.add(new Vec2(640, HEIGHT/2));
                surface.add(new Vec2(660, HEIGHT/2));
                surface.add(new Vec2(720, HEIGHT/2));
                surface.add(new Vec2(740, HEIGHT/2+350));
                surface.add(new Vec2(WIDTH/2+300, HEIGHT/2+50));
                surface.add(new Vec2(WIDTH, HEIGHT/2+50));
                break;
        }
       
        return surface; 
    }
    
    
    public ArrayList<TowerElement> getTowerElements(int levelNo, Level level){
        ArrayList<TowerElement> towers = new ArrayList<TowerElement>();
       
        switch(levelNo){
            case 1:
                towers.add(new WoodElement(level, 1000, HEIGHT/2, 20, 100));
                towers.add(new WoodElement(level, 1050, HEIGHT/2, 20, 100));
                towers.add(new WoodElement(level, 1025, 299, 100, 20));
                break;
            case 2:
                towers.add(new StoneElement(level, 1000, HEIGHT/2, 20, 100));
                towers.add(new StoneElement(level, 1050, HEIGHT/2, 20, 100));
                towers.add(new StoneElement(level, 1025, 299, 100, 20));
                break;
            case 3:
                towers.add(new WoodElement(level, 1000, HEIGHT/2, 20, 100));
                towers.add(new WoodElement(level, 1050, HEIGHT/2, 20, 100));
                towers.add(new StoneElement(level, 1025, 299, 100, 20));
                break;
            case 4:
            	towers.add(new WoodElement(level, 1000, HEIGHT/2, 20, 100));
                towers.add(new MetalElement(level, 1025, HEIGHT/2, 20, 100));
                towers.add(new WoodElement(level, 1050, HEIGHT/2, 20, 100));
                towers.add(new StoneElement(level, 1025, 299, 100, 20));
            	break;
        }
       
        return towers; 
    }
    
    
    public int getNumberOfBullets(int levelNo){
        switch(levelNo){
            case 1:
                return 3;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
            	return 4;
            default:
                return 1; 
        }
    }
  
}