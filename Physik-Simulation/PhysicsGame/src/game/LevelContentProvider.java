package game;


import java.util.ArrayList;

import org.jbox2d.common.Vec2;

import processing.core.PApplet;



class LevelContentProvider{
	
	/**
	 * The heart of the game setup. This class provides all necessary values, vertexes,
	 * settings and other according to the level number. 
	 * 
	 * NOTE: If you want to create your own levels, this is the only class that needs
	 * to be modified. You will find a detailed tutorial on how you achieve this.
	 * 
	 * CAUTION: Due to some Processing structures, it is not possible to define this
	 * class with static methods and functions. 
	 */
	  
	private boolean god_mode;
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
  
    public LevelContentProvider(boolean god_mode){
    	this.god_mode = god_mode;
    }
    
    /**
     * Returns the vertexes for a Level's surface. 
     * CAUTION: The first part of the surface, on which the cannon is located, must
     * be from point (0, HEIGHT/2+250) to at least point (200, HEIGHT/2+250)!
     * @param levelNo	the actual level number
     * @return			an ArrayList with the vertices
     */
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
                surface.add(new Vec2(200, HEIGHT/2+250));
                surface.add(new Vec2(640, HEIGHT/2));
                surface.add(new Vec2(660, HEIGHT/2));
                surface.add(new Vec2(720, HEIGHT/2));
                surface.add(new Vec2(740, HEIGHT/2+350));
                surface.add(new Vec2(WIDTH/2+300, HEIGHT/2+50));
                surface.add(new Vec2(WIDTH, HEIGHT/2+50));
                break;
            case 5:
                surface.add(new Vec2(0, HEIGHT/2+250));
                surface.add(new Vec2(200, HEIGHT/2+250));
                surface.add(new Vec2(300, HEIGHT/2+250));
            	for(int i=301; i<930; i+=15){
            		surface.add(new Vec2(i, (int)(getSine(i)/2*100+500)));
            	}
                surface.add(new Vec2(WIDTH/2+300, HEIGHT/2+50));
                surface.add(new Vec2(WIDTH, HEIGHT/2+50));
            	break;
            case 6:
                surface.add(new Vec2(0, HEIGHT/2+250));
                surface.add(new Vec2(200, HEIGHT/2+250));
                surface.add(new Vec2(300, HEIGHT/2+250));
                surface.add(new Vec2(WIDTH/2+300, HEIGHT/2+50));
                surface.add(new Vec2(WIDTH, HEIGHT/2+50));
            	break;
            case 7: case 8: 
            	surface.add(new Vec2(0, HEIGHT/2+250));
            	surface.add(new Vec2(WIDTH, HEIGHT/2+250));
            	break;
            case 9:
                surface.add(new Vec2(0, HEIGHT/2+250));
                surface.add(new Vec2(WIDTH, HEIGHT/2+250));
            	break;
            case 10:
            	surface.add(new Vec2(0, HEIGHT/2+250));
                surface.add(new Vec2(300, HEIGHT/2+250));
                surface.add(new Vec2(300, HEIGHT));
            case 11:
            	surface.add(new Vec2(0, HEIGHT/2+250));
                surface.add(new Vec2(WIDTH, HEIGHT/2+250));
            	break;
            case 12:
            	surface.add(new Vec2(0, HEIGHT/2+250));
                surface.add(new Vec2(1, HEIGHT/2+250));
                break;
        }
       
        return surface; 
    }
    
    /**
     * A helper method to create a specific surface look. Feel free to implement 
     * further methods!
     */
    private double getSine(int a){
    	return Math.sin(a);
    }
    
    /**
     * Returns the TowerElements of a specific Level. This elements are the towers
     * to tear down in a Level.
     * 
     * CAUTION: Don't define horizontal towers lying on the ground! 
     * 
     * @param levelNo	the actual level number
     * @param level		the Level Object itself
     * @return			an ArrayList with the TowerElements.
     */
    public ArrayList<TowerElement> getTowerElements(int levelNo, Level level){
        ArrayList<TowerElement> towers = new ArrayList<TowerElement>();
       
        switch(levelNo){
            case 1:
                towers.add(new WoodElement(level, 1000, HEIGHT/2, 20, 100, true));
                towers.add(new WoodElement(level, 1050, HEIGHT/2, 20, 100, true));
                towers.add(new WoodElement(level, 1025, 299, 100, 20, false));
                break;
            case 2:
                towers.add(new MetalElement(level, 1000, HEIGHT/2, 20, 100, true));
                towers.add(new MetalElement(level, 1050, HEIGHT/2, 20, 100, true));
                towers.add(new MetalElement(level, 1025, 299, 100, 20, false));
                towers.add(new StoneElement(level, 1000, HEIGHT/2-120, 20, 100, false));
                towers.add(new StoneElement(level, 1050, HEIGHT/2-120, 20, 100, false));
                towers.add(new WoodElement(level, 1025, 200, 100, 20, false));
                break;
            case 3:
                towers.add(new WoodElement(level, 1000, HEIGHT/2, 20, 100, true));
                towers.add(new WoodElement(level, 1050, HEIGHT/2, 20, 100, true));
                towers.add(new StoneElement(level, 1025, 299, 100, 20, false));
                break;
            case 4:
            	towers.add(new WoodElement(level, 1000, HEIGHT/2, 20, 100, true));
                towers.add(new StoneElement(level, 1025, HEIGHT/2, 20, 100, true));
                towers.add(new WoodElement(level, 1050, HEIGHT/2, 20, 100, true));
                towers.add(new StoneElement(level, 1025, 299, 100, 20, false));
            	break;
            case 5: case 6:
            	towers.add(new WoodElement(level, 1000, HEIGHT/2, 20, 100, true));
                towers.add(new StoneElement(level, 1000, HEIGHT/2-60, 100, 20, false));
                towers.add(new StoneElement(level, 1150, HEIGHT/2, 20, 100, true));
                towers.add(new WoodElement(level, 1150, HEIGHT/2-60, 100, 20, false));
            	break;
            case 7:
            	towers.add(new WoodElement(level, 800, HEIGHT/2, 20, 100, true));
            	towers.add(new StoneElement(level, 800, HEIGHT/2-60, 100, 20, false));
            	break;
            case 8:
            	towers.add(new WoodElement(level, 1130, HEIGHT/2+200, 20, 100, true));
        		towers.add(new StoneElement(level, 1130, HEIGHT/2+140, 100, 20, false));
            	towers.add(new StoneElement(level, 1180, HEIGHT/2-210, 20, 100, true));
        		towers.add(new StoneElement(level, 1180, HEIGHT/2-270, 100, 20, false));
            	break;
            case 9:
            	towers.add(new WoodElement(level, 900, 560, 20, 100, true));
            	towers.add(new StoneElement(level, 900, 500, 100, 20, false));
            	break;
            case 10:
            	towers.add(new WoodElement(level, 80, 310, 20, 100, true));
                towers.add(new WoodElement(level, 160, 310, 20, 100, true));
                towers.add(new StoneElement(level, 120, 250, 100, 20, false));
            	break;
            case 11:
            	 towers.add(new WoodElement(level, 1000, 560, 20, 100, true));
                 towers.add(new WoodElement(level, 1050, 560, 20, 100, true));  // TODO instabiler!!!!!!!!
                 towers.add(new WoodElement(level, 1025, 500, 100, 20, false));
            	break;
            case 12:
            	towers.add(new MetalElement(level, 1100, 560, 20, 100, false));
            	towers.add(new WoodElement(level, 1120, 560, 20, 100, false));
            	towers.add(new StoneElement(level, 1140, 560, 20, 100, false));
            	towers.add(new MetalElement(level, 1160, 560, 20, 100, false));
            	break;
        }
       
        return towers; 
    }
    
    /**
     * Returns a fixed Element. This is an element which is fixed in the scene, but not part
     * of the surface.
     * @param level				the Level Object itself
     * @param contentProvider	an instance of the LevelContentPrvoider
     * @param levelNo			the actual level number
     * @return					an ArrayList with the FixedElements
     */
    public ArrayList<FixedElement> getFixedElement(Level level, LevelContentProvider contentProvider, int levelNo){
    	
    	ArrayList<FixedElement> elems = new ArrayList<FixedElement>();
    	ArrayList<Vec2> vertices = new ArrayList<Vec2>();
		
    	switch(levelNo){
    		case 6:
    			vertices.add(new Vec2(300, HEIGHT/2+200));
    			vertices.add(new Vec2(WIDTH/2+300, HEIGHT/2));
    			vertices.add(new Vec2(800, 0));
    			vertices.add(new Vec2(100, 0));
    			
    			elems.add(new FixedElement(level, contentProvider, vertices));
    			break;
    		case 7:
    			vertices.add(new Vec2(900, HEIGHT/2+250));
    			ArrayList<Vec2> level7 = buildLevel7();
    			for(Vec2 v : level7){
    				vertices.add(v);
    			}
    			elems.add(new FixedElement(level, contentProvider, (ArrayList<Vec2>)vertices.clone()));
    			
    			vertices.clear();
    			vertices.add(new Vec2(500, HEIGHT/2+50));
    			vertices.add(new Vec2(550, HEIGHT/2+50));
    			vertices.add(new Vec2(525, HEIGHT/2-50));
    			elems.add(new FixedElement(level, contentProvider, (ArrayList<Vec2>)vertices.clone()));
    			break;
    		case 8:
    			vertices.add(new Vec2(500, HEIGHT/2+250));
    			vertices.add(new Vec2(1010, HEIGHT/2+250));
    			vertices.add(new Vec2(1010, HEIGHT/2+120));
    			vertices.add(new Vec2(550, HEIGHT/2-50));
    			elems.add(new FixedElement(level, contentProvider, (ArrayList<Vec2>)vertices.clone()));
    			
    			vertices.clear();
    			vertices.add(new Vec2(570, HEIGHT/2-80));
    			vertices.add(new Vec2(1000, HEIGHT/2+90));
    			vertices.add(new Vec2(1000, HEIGHT/2-160));
    			vertices.add(new Vec2(800,200));
    			elems.add(new FixedElement(level, contentProvider, (ArrayList<Vec2>)vertices.clone()));
    			
    			vertices.clear();
    			vertices.add(new Vec2(550, HEIGHT/2-110));
    			vertices.add(new Vec2(500, 0));
    			vertices.add(new Vec2(800, 170));
    			elems.add(new FixedElement(level, contentProvider, (ArrayList<Vec2>)vertices.clone()));
    			
    			vertices.clear();
    			vertices.add(new Vec2(1000, HEIGHT/2-160));
    			vertices.add(new Vec2(1000, HEIGHT/2-110));
    			vertices.add(new Vec2(WIDTH, HEIGHT/2-110));
    			vertices.add(new Vec2(WIDTH, HEIGHT/2-160));
    			elems.add(new FixedElement(level, contentProvider, (ArrayList<Vec2>)vertices.clone()));
   
    			break;
    			
    		case 9:
    			vertices.add(new Vec2(700, 610));
    			vertices.add(new Vec2(700, 300));
    			vertices.add(new Vec2(720, 300));
    			vertices.add(new Vec2(720, 610));
    			elems.add(new FixedElement(level, contentProvider, vertices));
    			break;
    		
    		case 10:
    			vertices.add(new Vec2(0, 360));
    			vertices.add(new Vec2(0, 365));
    			vertices.add(new Vec2(200, 365));
    			vertices.add(new Vec2(200, 360));
    			elems.add(new FixedElement(level, contentProvider, vertices));
    			break;
    	}
    	
    	return elems;
    }
    
    public ArrayList<Attractor> getAllAttractors(Level level, int levelNo){
    	
    	ArrayList<Attractor> attractors = new ArrayList<Attractor>();
    	switch(levelNo) {
    		case 9:
    			attractors.add(new Attractor(30, 710, 200, level, -1, 10000));
    			break;
    		case 12:
    			attractors.add(new Attractor(75, WIDTH/2, HEIGHT/2, level, 1, 5));
    			break;
    	}
    	return attractors;
    }
    
    public ArrayList<WindField> getAllWindFields(Level level, int levelNo){
    	
    	ArrayList<WindField> windFields = new ArrayList<WindField>();
    	
    	switch(levelNo){
    		case 10:
    			windFields.add(new WindField(level, new Vec2(200, 0), 100, 360, new Vec2(100, 0), level.getGame().color(0, 0, 255)));
    			windFields.add(new WindField(level, new Vec2(300, 0), 300, 360, new Vec2(-300, 0), level.getGame().color(145, 234, 5)));
    			break;
    		case 11:
    			PApplet applet = level.getGame();
    			windFields.add(new WindField(level, new Vec2(1100, 0), 480, 360, new Vec2(-10000, -20000),
						level.getGame().color(255)));
    			windFields.add(new WindField(level, new Vec2(600, 360), 400, 250, new Vec2(-1000, 0),
						level.getGame().color(255)));
    			break;
    	}
    	
    	return windFields;
    }
    
    public ArrayList<Satellite> getSatellites(Level level, int levelNo){
    	
    	ArrayList<Satellite> satellites = new ArrayList<Satellite>();
    	
    	switch(levelNo){
    		case 12: 
    			satellites.add(new Satellite(level, new Vec2(WIDTH/2+325, HEIGHT/2-200), 10, new Vec2(5, -3), level.getGame().color(205, 133, 63)));
    			satellites.add(new Satellite(level, new Vec2(WIDTH/2-125, HEIGHT/2+155), 25, new Vec2(-260, 560), level.getGame().color(0, 255, 255)));
    			satellites.add(new Satellite(level, new Vec2(WIDTH/2+725, HEIGHT/2-755), 45, new Vec2(-1200, 0), level.getGame().color(181, 94, 181)));
    			break;
    	}
    	
    	return satellites;
    }
    
    /**
     * Returns the name of the background image according to the level number.
     * @param levelNo	the actual level number
     * @return			the name of the background image
     */
    public String getLevelBackgroundImage(int levelNo){
    	String img = "background";
    	
    	switch(levelNo){
    		case 2: case 4:
    			img += "0"+(levelNo-1);
    			break;
    		case 1: case 3: case 5: case 6: case 7:
    		case 8: case 9: 
    			img += "0" + levelNo;
    			break;
    		case 10: case 11: case 12:
    			img += ""+levelNo;
    			break;
    		default:
    			return "blank.jpg";
    	}
    	
    	
    	img += ".jpg";
    	
    	
    	return img;
    }
    
    /**
     * Returns the number of allowed bullets in a level.
     * @param levelNo	the actual level number
     * @return			the number of allowed bullets
     */
    public int getNumberOfBullets(int levelNo){
        if(god_mode){
        	return 999;
        }else{
	    	switch(levelNo){
	            case 1: case 2: 
	            	return 2;
	            case 3:
	                return 4;
	            case 4:
	            	return 4;
	            case 5:
	            	return 4;
	            case 6:
	            	return 2;
	            case 7: case 8: case 9:
	            	return 3;
	            case 10: case 11: 
	            	return 4;
	            case 12: 
	            	return 10;
	            default:
	                return 3; 
	        }	
        }
    }
    
    /**
     * A helper method to build level 7
     */
    private ArrayList<Vec2> buildLevel7(){
    	ArrayList<Vec2> vertices = new ArrayList<Vec2>();
    	
    	int y = HEIGHT/2;
		int x = 1000;
		int r = 250;
		int y2 = 0;

		for(int i=x; i<=x+r; i+=10){
			y2 = (int)(Math.sqrt((r*r)-((i-x)*(i-x))))+y;
			vertices.add(new Vec2(i, y2));
		}
		
		for(int i=x+r; i>=x; i-=10){
			y2 = (int)(Math.sqrt((r*r)-((i-x)*(i-x))))*-1+y;
			vertices.add(new Vec2(i, y2));
		}
		
		vertices.add(new Vec2(400, y2));
		
		x = 350;
		r = 150;
		y -= 100;
		
		for(int i=x; i>=x-r; i-=10){
			y2 = (int)(Math.sqrt((r*r)-((i-x)*(i-x))))*-1+y;
			vertices.add(new Vec2(i, y2));
		}
		
		for(int i=x-r; i<=x; i+=10){
			y2 = (int)(Math.sqrt((r*r)-((i-x)*(i-x))))+y;
			vertices.add(new Vec2(i, y2));
		}
		
		vertices.add(new Vec2(900, y2));
		vertices.add(new Vec2(900, y2+50));
		vertices.add(new Vec2(x-200, y2+50));
		vertices.add(new Vec2(x-200,0));
		vertices.add(new Vec2(WIDTH, 0));
		vertices.add(new Vec2(WIDTH, HEIGHT/2+250));
    	
    	return vertices;
    }
    
    /**
     * Returns the gravity according to the actual level number.
     */
    public float getGravity(Level level, int levelNo) {
    	
    	switch(levelNo) {
    		case 12:
    			return 0f;
			default:
				return -27.81f;
    	}
    	
    }
  
}