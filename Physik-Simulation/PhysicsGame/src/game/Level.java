package game;


import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.w3c.dom.Attr;


import pbox2d.PBox2D;


class Level{
 
	/**
	 * The Level class. It contains all Box2D-Objects, that are related
	 * to a specific level. For each level number, a new object of this
	 * class is created.
	 */
	
	private PBox2D    level;
    private int       levelNo, points, bulletsLeft, pointsToReach; 

    private LevelContentProvider contentProvider;
    private Game game;
    private Surface   surface;
    private ArrayList<TowerElement> tower = new ArrayList<TowerElement>();
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<FixedElement> fixedElements;
    private ArrayList<Attractor> attractors;
    private ArrayList<WindField> windFields;
    private ArrayList<Satellite> satellites;
    private float gravity;
     
    /**
     * Constructor
     */
    Level(int levelNo, Game game, LevelContentProvider contentProvider){
        this.levelNo = levelNo;
        this.game = game;
        this.contentProvider = contentProvider;
        level = new PBox2D(game);
        level.createWorld();
        gravity = contentProvider.getGravity(this, levelNo);
        level.setGravity(0, gravity);
        points = 0;
        createLevel();
    }
    
    /**
     * Creates the surface and all ArrayList containing elements.
     */
    void createLevel(){
        surface = new Surface(this, contentProvider); 
        tower = contentProvider.getTowerElements(levelNo, this);
        bulletsLeft = contentProvider.getNumberOfBullets(levelNo);
        attractors = contentProvider.getAllAttractors(this, levelNo);
        windFields = contentProvider.getAllWindFields(this, levelNo);
        satellites = contentProvider.getSatellites(this, levelNo);
        pointsToReach = tower.size();
        fixedElements = contentProvider.getFixedElement(this, contentProvider, levelNo);
    }
    
    /**
     * Calls the display()-method of every Box2D-Element of the level.
     */
    void displayLevel(){
        surface.display();
        
        for(TowerElement e : tower){
            e.display(); 
        }
        
        for(Bullet b : bullets){
            b.display(); 
        }
        
        for(FixedElement e : fixedElements){
        	e.display();
        }
        
        for(Attractor a : attractors) {
        	a.display();
        }
        
        for(WindField w : windFields){
        	if (levelNo != 11) {
				w.display();
			}
        }
        
        for(Satellite s : satellites){
        	s.display();
        }
    }
    
    /**
     * Calculates the attraction between the bullets and the
     * attractors.
     */
    void calculateAttraction(){
    	for(Attractor a : attractors){
		   for(Bullet b : bullets) {
			  Vec2 force = a.attract(b.getBody());
			  b.getBody().applyForce(force, b.getBody().getPosition());
		   } 
		}
    	
    	for(Attractor a : attractors){
    		for(Satellite s : satellites){
    			Vec2 force = a.attract(s.getBody());
    			s.getBody().applyForce(force, s.getBody().getPosition());
    		}
    	}
    }
    
    void calculateWindImpact() {
    	for(WindField w : windFields){
    		for(Bullet b: bullets){
    			w.checkIfInFieldAndApplyForce(b.getBody());
    		}
    	}
    }
    
    /**
     * Adds a flying bullet to the level.
     */
    void addBullet(int dx, int dy, float actualAngle, int spin){
        bullets.add(new Bullet(this, dx, dy, spin)); 
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
    
    public boolean hasGravity(){
    	if(Math.abs(gravity) > 0) return true;
    	return false;
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
    
    ArrayList<Attractor> getAttractors(){
    	return attractors;
    }
    
    ArrayList<Bullet> getBullets(){
    	return bullets;
    }
    
    void stepWorld(){
        level.step(); 
    }
    
}