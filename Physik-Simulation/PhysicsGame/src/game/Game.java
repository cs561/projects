package game;


import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;


public class Game extends PApplet{
	
	static final long serialVersionUID = 1;

	
	public PImage actualBackground;
	public Level actualLevel;
	public boolean intersecting, allowShoot, hoverNext, hoverEnd, win, lost, countPoints, hoverRestart, hoverEnd2;
	public int actualDx, actualDy, actualForce, countdown, actualLevelNo, tutorialStage, totalPoints, lostCountdown, lostCountdown2;
	public float actualAngle;
	public PFont baveuse30, baveuse45, baveuse60;
	public LevelContentProvider coordProvider;

	
	public void setup(){
	    size(1280, 720);
	    smooth();
	    baveuse30 = createFont("Baveuse", 30);	// TODO reduce
	    baveuse45 = createFont("Baveuse", 45);
	    baveuse60 = createFont("Baveuse", 60);
	   
	    // default
	    actualLevelNo = 1;
	    tutorialStage = 1;
	    
	    // debug
	    actualLevelNo = 3;
	    tutorialStage = 0;
	    
	    coordProvider = new LevelContentProvider();
	    startNextLevel(actualLevelNo);
	}

	
	public void draw(){ 
	   background(actualBackground);
	  
	   if(actualLevel.getPoints() == actualLevel.getPointsToReach()){
	       win = true; 
	       lost = false;
	   }

	   actualLevel.displayLevel();
	   drawInformationBar();

	   if(!win){
	       actualLevel.stepWorld(); 
	       if(mouseX > 80 && mouseY < 600 && actualLevel.getBulletsLeft() > 0){
	           if(tutorialStage == 0){
	               allowShoot = true;
	           }else{
	               allowShoot = false;
	           }  
	           drawArrow(); 
	       }else{
	           allowShoot = false; 
	       }    
	   }else{
	       allowShoot = false;
	       drawEnd(); 
	   }
	   
	   if(tutorialStage > 0){
	       showTutorial(); 
	   }
	   
	   drawCannon();
	   
	   if(allowShoot){
		   drawSpinBar();
	   }

	   if(lost){
	       if(lostCountdown > 0){
	           lostCountdown -= 2;
	           fill(0);
	           textSize(20);
	           text("checking...   " + (lostCountdown/100), 20, 100);  
	       }else{
	           if(lostCountdown2 > 0){
	               textSize(150);
	               fill(255, 25, 25);
	               text("YOU LOST", 215, 400);
	               lostCountdown2 -= 7;
	           }else{
	               int cX1 = (width/2)-250;
	               int cY1 = (height/2) - 125;
	               int cY2 = (height/2) + 25;
	               if(mouseX >= cX1 && mouseX <= (cX1+400)){
	                    if(mouseY >= cY1 && mouseY <= cY1 + 100){
	                        hoverRestart = true;
	                        hoverEnd2 = false;
	                    }else{
	                        if (mouseY >= cY2 && mouseY <= cY2 + 100){
	                            hoverRestart = false;
	                            hoverEnd2 = true;  
	                        }else{
	                            hoverRestart = false;
	                            hoverEnd2 = false;
	                        }
	                    }
	                }else{
	                    hoverRestart = false;
	                    hoverEnd2 = false; 
	                } 
	               showOptionMenuEnd();
	           } 
	       }  
	   }
	}

	
	// draws the arrow pointing to the mouse coordinates
	void drawArrow(){
	    PVector g = new PVector(mouseX-80, mouseY-600);  
	    int[] numbers = getCoordsAndForce(g);
	    actualAngle = atan(g.y/g.x);
	    actualForce = numbers[2];
	    
	    stroke(actualForce/2, 255-(actualForce/2), 0);
	    strokeWeight(pow(actualForce,2)/9000);
	    line(80, 600, numbers[0], numbers[1]);
	    drawArrowHead();
	    actualDx = numbers[0]-80;
	    actualDy = 600-numbers[1];
	}

	
	// draws the arrow head
	void drawArrowHead(){
	    float ratio = actualForce/500.0f;
	    float factor = ratio*40.0f - ratio*10;
	    pushMatrix();
	    translate(80, 600);
	    rotate(actualAngle);
	    line(actualForce, 0, actualForce - factor, -factor);
	    line(actualForce, 0, actualForce - factor, factor);
	    popMatrix();
	}

	
	void drawCannon(){
	    PImage img = loadImage("cannon.png");
	    pushMatrix();
	    translate(80, 600);
	    rotate(actualAngle);
	    translate(-80, -600);
	    image(img, 0, 590, 119, 59);
	    popMatrix();
	}
	
	
	void drawSpinBar(){
		
	}

	
	void drawInformationBar(){
	    fill(0,100,255,75);
	    stroke(255);
	    strokeWeight(0);
	    rectMode(CORNER);
	    rect(0,0,1280,60); 
	    int textColor = (win)? color(150, 150, 150) : color(27, 28, 64);
	    fill(textColor);
	    textFont(baveuse30);
	    text("LEVEL " + actualLevelNo, 25, 40);
	    text("upset bars: " + actualLevel.getPoints() + "/" + actualLevel.getPointsToReach(), 230, 40); 
	    text("bullets left: " + actualLevel.getBulletsLeft(), 600, 40);
	    fill(0, 255, 0);
	    text("total points: " + totalPoints, 950, 40);
	}

	
	void drawEnd(){
	   if(countdown > 0){
	       actualLevel.stepWorld(); 
	       fill(27, 28, 64);
	       textFont(baveuse30);
	       textSize(150);
	       text("YOU WIN", 250, 400); 
	       countdown -= 7;
	   }else{
	       if(mouseX >= (width/2)+50 && mouseX <= ((width/2)+450)){
	            if(mouseY >= (height/2) - 125 && mouseY <= (height/2) - 25){
	                hoverNext = true;
	                hoverEnd = false;
	            }else{
	                if (mouseY >= (height/2) + 25 && mouseY <= (height/2) + 125){
	                    hoverNext = false;
	                    hoverEnd = true;  
	                }else{
	                    hoverNext = false;
	                    hoverEnd = false;
	                }
	            }
	          }else{
	              hoverNext = false;
	              hoverEnd = false; 
	          }
	       showPopUp();
	   } 
	}

	
	void showPopUp(){
	    fill(255, 100);
	    rectMode(CORNER);
	    rect(0,60,1280,720);
	    rectMode(CORNER);
	    strokeWeight(5);
	    showGameResult();
	    showOptionMenu();
	}

	
	void showGameResult(){
	    int x1 = 100;
	    int y1 = (height/2) - 175;
	    
	    fill(255, 75);
	    stroke(27, 28, 64, 75);
	    rect(x1, y1, 400, 350);
	    
	    int[] types = actualLevel.getTowerBarTypes();
	    
	    textFont(baveuse45);
	    textSize(17);
	    fill(27, 28, 64);
	    
	    text("wooden bars: ", x1+20, y1+40);
	    text(""+types[0], x1+220, y1+40);
	    text("X", x1+260, y1+40);
	    text("1 point", x1+300, y1+40);
	    
	    text("stone bars: ", x1+20, y1+70);
	    text(""+types[1], x1+220, y1+70);
	    text("X", x1+260, y1+70);
	    text("2 points", x1+300, y1+70);
	    
	    text("metal bars: ", x1+20, y1+100);
	    text(""+types[2], x1+220, y1+100);
	    text("X", x1+260, y1+100);
	    text("3 points", x1+300, y1+100);
	    
	    int subtotal = (types[0] + types[1]*2 + types[2]*3);
	    int bulletsLeft = actualLevel.getBulletsLeft();
	    
	    text("subtotal: " + subtotal, x1+20, y1+200);
	    
	    text("bullets remaining: " + bulletsLeft, x1+20, y1+230);
	    
	    textSize(24);
	    text("TOTAL POINTS: " + (bulletsLeft+1) + " X " + subtotal + " = " + ((bulletsLeft+1)*subtotal), x1+20, y1+300);
	    
	    if(countPoints){
	        totalPoints += (bulletsLeft+1)*subtotal;
	        countPoints = false;
	    }
	}

	
	void showOptionMenu(){
	    int x1 = (width/2);
	    int y1 = (height/2) - 175; 
	    
	    fill(255);
	    stroke(27, 28, 64);
	    rect(x1, y1, 500, 350);
	    
	    fill(27, 28, 64);
	    noStroke();
	    rect(x1+50, y1+50, 400, 100);
	    rect(x1+50, y1+200, 400, 100);
	    
	    textFont(baveuse45);
	    fill(hoverNext? 100 : 255);
	    text("NEXT LEVEL", x1+95, y1+118);
	    fill(hoverEnd? 100 : 255);
	    text("END GAME", x1+107, y1+268);
	}

	
	void showOptionMenuEnd(){
	    int x1 = (width/2-200);
	    int y1 = (height/2) - 175; 
	    
	    fill(255);
	    stroke(27, 28, 64);
	    strokeWeight(5);
	    rect(x1, y1, 500, 350);
	    
	    fill(27, 28, 64);
	    noStroke();
	    rect(x1+50, y1+50, 400, 100);
	    rect(x1+50, y1+200, 400, 100);
	    
	    textFont(baveuse45);
	    fill(hoverRestart? 100 : 255);
	    text("RESTART", x1+125, y1+118);
	    fill(hoverEnd2? 100 : 255);
	    text("END GAME", x1+107, y1+268); 
	}
	

	void showTutorial(){
	    switch(tutorialStage){
	        case 0: 
	            break;
	        case 1:
	            showTutorialLevel1a();
	            break;
	        case 2:
	            showTutorialLevel1b();
	            break;
	        case 3:
	            showTutorialLevel1c();
	            break;
	        case 5:
	            showTutorialLevel2();
	            break;
	    } 
	}
	

	void showTutorialLevel1a(){
	    fill(255,100);
	    noStroke();
	    rect(0, 60, width, height-60);
	    image(loadImage("tut1a.png"), 0, height/2);
	    textSize(20);
	    fill(27, 28, 64);
	    text("click to skip", width-200, height-20);
	}

	
	void showTutorialLevel1b(){
	    fill(255, 100);
	    noStroke();
	    rect(0, 60, width, height-60);
	    fill(255, 215, 0, 100);
	    arc(70, 610, 1060, 1060, -PI/2, 0);
	    image(loadImage("tut1b.png"), 404, 200);
	    textSize(20);
	    fill(27, 28, 64);
	    text("click to skip", width-200, height-20);
	}
	

	void showTutorialLevel1c(){
	    fill(255,100);
	    noStroke();
	    rect(0, 60, width, height-60);
	    image(loadImage("tut1c.png"), 470, 170);
	    textSize(20);
	    fill(27, 28, 64);
	    text("click to skip", width-200, height-20);
	}
	

	void showTutorialLevel2(){
	    fill(255,100);
	    noStroke();
	    rect(0, 60, width, height-60);
	    image(loadImage("tut2.png"), 470, 170);
	}

	
	// returns the dx/dy and force
	int[] getCoordsAndForce(PVector v){
	  int[] data = new int[3];  
	  
	  if((int)((v.x*v.x)+(v.y*v.y)) > (500*500)){
	        v.normalize();
	        v.mult(500);
	        data[0] = (int) (v.x+80);
	        data[1] = (int) (600+v.y);
	        data[2] = 500;
	    }else{
	        if((int)((v.x*v.x)+(v.y*v.y)) < (100*100)){
	            v.normalize();
	            v.mult(100);
	            data[0] = (int) (v.x+80);
	            data[1] = (int) (600+v.y);
	            data[2] = 100;
	        }else{
	            data[0] = mouseX;
	            data[1] = mouseY;
	            data[2] = (int) sqrt((v.x*v.x)+(v.y*v.y));
	        }
	    }
	    
	    return data;
	}
	

	void startNextLevel(int level){
	    intersecting = false;
	    allowShoot = false; 
	    hoverNext = false;
	    hoverEnd = false;
	    hoverEnd2 = false;
	    hoverRestart = false;
	    win = false;
	    lost = false;
	    countPoints = false;
	    
	    if(level == 2){
	        tutorialStage = 4; 
	    }
	    
	    actualLevel = new Level(level, this, coordProvider);
	    actualBackground = loadImage("background" + actualLevelNo + ".jpg"); // TODO from ContentProvider
	    actualForce = 0;
	    actualDx = 0;
	    actualDy = 0;
	    countdown = 1000;
	    lostCountdown = 1000;
	    lostCountdown2 = 1000;
	}

	
	// shoots a bullet in direction of mouse coordinates
	public void mouseReleased(){
	    if(!win && allowShoot){
	        actualLevel.addBullet(actualDx, actualDy, actualForce, actualAngle); 
	        if(actualLevel.getBulletsLeft() == 0){
	            lost = true;
	        }
	    }else{
	        if(hoverNext){
	            startNextLevel(++actualLevelNo);
	        }
	        
	        if(hoverRestart){
	        	actualLevelNo = 1;
	            startNextLevel(actualLevelNo);
	            totalPoints = 0;
	        }
	       
	        if(hoverEnd || hoverEnd2){
	            System.exit(0);
	        } 
	        
	        switch(tutorialStage){
	            case 0: break;
	            case 1:  
	                tutorialStage++;
	                break;
	            case 2:
	                tutorialStage++;
	                break;  
	            case 3:
	                tutorialStage = 0;
	                break;
	            case 4:
	                tutorialStage = 5;
	                break;
	            case 5:
	                tutorialStage = 0;
	                break;
	        }
	    }
	}
	
}
