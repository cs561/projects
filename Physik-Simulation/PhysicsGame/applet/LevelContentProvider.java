package game;


import java.util.ArrayList;

import org.jbox2d.common.Vec2;


class LevelContentProvider{
	  
	private boolean god_mode;
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private int tempY2;
  
    public LevelContentProvider(boolean god_mode){
    	this.god_mode = god_mode;
    }
    
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
                surface.add(new Vec2(200, HEIGHT/2+250));
            	break;
        }
       
        return surface; 
    }
    
    private double getSine(int a){
    	return Math.sin(a);
    }
    
    private double getCosine(int a){
    	return Math.cos(a);
    }
    
    public ArrayList<TowerElement> getTowerElements(int levelNo, Level level){
        ArrayList<TowerElement> towers = new ArrayList<TowerElement>();
       
        switch(levelNo){
            case 1:
                towers.add(new WoodElement(level, 1000, HEIGHT/2, 20, 100, true));
                towers.add(new WoodElement(level, 1050, HEIGHT/2, 20, 100, true));
                towers.add(new WoodElement(level, 1025, 299, 100, 20, false));
                break;
            case 2:
                towers.add(new StoneElement(level, 1000, HEIGHT/2, 20, 100, true));
                towers.add(new StoneElement(level, 1050, HEIGHT/2, 20, 100, true));
                towers.add(new StoneElement(level, 1025, 299, 100, 20, false));
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
            	towers.add(new FallingTowerElement(level, 500, -0, 20, 100, false));
            	towers.add(new FallingTowerElement(level, 300, -0, 20, 100, false));
            	towers.add(new WoodElement(level, 400, -0, 20, 100, false));
            	towers.add(new MetalElement(level, 900, -0, 20, 100, false));
            	towers.add(new FallingTowerElement(level, 700, -0, 20, 100, false));
            	towers.add(new FallingTowerElement(level, 800, -0, 20, 100, false));
            	break;
        }
       
        return towers; 
    }
    
    
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
    	}
    	
    	return elems;
    }
    
    
    public String getLevelBackgroundImage(int levelNo){
    	String img = "background";
    	
    	switch(levelNo){
    		case 1: case 2:
    			img += "01";
    			break;
    		case 3: case 4:
    			img += "03";
    			break;
    		case 5:
    			img += "05";
    			break;
    		case 6:
    			img += "06";
    			break;
    		case 7:
    			img += "07";
    			break;
    		case 8:
    			img += "08";
    			break;
    		default:
    			return "blank.jpg";
    	}
    	
    	
    	img += ".jpg";
    	
    	
    	return img;
    }
    
    
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
	            case 7:
	            	return 3;
	            case 8:
	            	return 3;
	            case 9:
	            	return 9999;
	            default:
	                return 1; 
	        }	
        }
    }
    
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
		
		tempY2 = y2;
		
		vertices.add(new Vec2(900, y2));
		vertices.add(new Vec2(900, y2+50));
		vertices.add(new Vec2(x-200, y2+50));
		vertices.add(new Vec2(x-200,0));
		vertices.add(new Vec2(WIDTH, 0));
		vertices.add(new Vec2(WIDTH, HEIGHT/2+250));
    	
    	return vertices;
    }
  
}