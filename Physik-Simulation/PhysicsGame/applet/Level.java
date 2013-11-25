package game;


import java.util.ArrayList;

import pbox2d.PBox2D;


class Level{
 
	
    private PBox2D    level;
    private int       levelNo, points, bulletsLeft, pointsToReach; 
    private Surface   surface;
    private ArrayList<TowerElement> tower = new ArrayList<TowerElement>();
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private LevelContentProvider contentProvider;
    private Game game;
    private ArrayList<FixedElement> elems;
     
  
    Level(int levelNo, Game game, LevelContentProvider contentProvider){
        this.levelNo = levelNo;
        this.game = game;
        this.contentProvider = contentProvider;
        level = new PBox2D(game);
        level.createWorld();
        level.setGravity(0, -27.81f);
        points = 0;
        createLevel();
    }
    
    
    void createLevel(){
        surface = new Surface(this, contentProvider); 
        tower = contentProvider.getTowerElements(levelNo, this);
        bulletsLeft = contentProvider.getNumberOfBullets(levelNo);
        pointsToReach = tower.size();
        
        switch(levelNo){
		    case 6: case 7: case 8:
				elems = contentProvider.getFixedElement(this, contentProvider, levelNo);
				break;
        }
        
        
    }
    
    
    void displayLevel(){
        surface.display();
        
        for(TowerElement e : tower){
            e.display(); 
        }
        
        for(Bullet b : bullets){
            b.display(); 
        }
        if(elems != null){
        	for(FixedElement elem : elems){
        		elem.display();
        	}
        }
    }
    
    
    void addBullet(int dx, int dy, int actualForce, float actualAngle, int spin){
        bullets.add(new Bullet(this, dx, dy, actualForce, spin)); 
        decreaseBulletsLeft();
    }
    
    
    void increasePoints(){
        points++; 
        if(points == pointsToReach){
            game.countPoints = true; 
        }
    }

    
    void decreaseBulletsLeft(){
        if(bulletsLeft > 0){
            bulletsLeft--;
        } 
    }
    
    
    int[] getTowerBarTypes(){
        ArrayList<Integer> towerBarTypes = new ArrayList<Integer>();
        for(TowerElement e : tower){
            if(e instanceof WoodElement){
                towerBarTypes.add(1);
            }
            if(e instanceof StoneElement){
                towerBarTypes.add(2); 
            }
            if(e instanceof MetalElement){
                towerBarTypes.add(3); 
            }
        }
        
        int[] types = new int[3];
        
        for(Integer e : towerBarTypes){
            types[(e-1)]++; 
        }
        
        return types;
    }
    
    
    public Game getGame(){
    	return game;
    }
    
    
    PBox2D getLevel(){
        return level; 
    }
    
    
    int getLevelNo(){
        return levelNo; 
    }
    
    
    int getBulletsLeft(){
        return bulletsLeft; 
    }
    
    
    int getPoints(){
        return points; 
    }
    
    
    int getPointsToReach(){
        return pointsToReach; 
    }
    
    
    void stepWorld(){
        level.step(); 
    }
    
}