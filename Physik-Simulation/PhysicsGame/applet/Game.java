package game;


import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;


public class Game extends PApplet{
	
	static final long 			serialVersionUID = 1;

	
	public PImage 				actualBackground, spin1, spin2, cannon, tut1a, tut1b, tut1c, tut1d, tut2;
	public Level 				actualLevel;
	public boolean 				intersecting, allowShoot, hoverNext, hoverEnd, win, 
								lost, countPoints, hoverRestart, hoverEnd2,	spinAct;
	public int 					actualDx, actualDy, actualForce, countdown, actualLevelNo, 
								tutorialStage, totalPoints, lostCountdown, lostCountdown2,
								spin, levelHover;
	public float 				actualAngle;
	public PFont 				baveuse30, baveuse45, baveuse60;
	public LevelContentProvider coordProvider;
	private boolean 			first;
	private boolean[]			levelDone = {true, true, true, true, true, true, true, true};
	
	private final boolean		GOD_MODE = false;

	
	public void setup(){
	    size(1280, 720);
	    smooth();
	    baveuse30 = createFont("Baveuse", 30);	// TODO reduce
	    baveuse45 = createFont("Baveuse", 45);
	    baveuse60 = createFont("Baveuse", 60);
	    
	    spin1 = loadImage("spin1.png");
	    spin2 = loadImage("spin2.png");
	    cannon = loadImage("cannon.png");
	    tut1a = loadImage("tut1a.png");
	    tut1b = loadImage("tut1b.png");
	    tut1c = loadImage("tut1c.png");
	    tut1d = loadImage("tut1d.png");
	    tut2 = loadImage("tut2.png");
	   
	    
	    first = true;
	    
	    // default
	    actualLevelNo = 1;
	    tutorialStage = 1;
	    
	    // debug
	   /* actualLevelNo = 9;
	    tutorialStage = 0;*/
	    
	    coordProvider = new LevelContentProvider(GOD_MODE);
	    startNextLevel(actualLevelNo);
	}

	
	public void draw(){ 
	   setCursor();
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
	   drawSpinBar();
	   if(tutorialStage==0){
		   drawLevelBar();   
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
	    pushMatrix();
	    translate(80, 600);
	    rotate(actualAngle);
	    translate(-80, -600);
	    image(cannon, 0, 590, 119, 59);
	    popMatrix();
	}
	
	
	void drawSpinBar(){
		int topLeftX = 5;
		
		fill(255);
		stroke(0);
		strokeWeight(2);
		rect(topLeftX, 120, 50, 342);
		
		noStroke();
		fill(255,0,0, 150);
		rect(topLeftX+20, 165, 10, 255);
		
		image(spin1, topLeftX+5, 120, 40, 40);
		image(spin2, topLeftX+5, 420, 40, 40);
		
		fill(0,0,255, 150);
		ellipse(31, 292 + (spin * 55), 40, 40);
		
		fill(50);
		textSize(70);
		text("++", 5, 208);
		textSize(60);
		text("+", 20, 259);
		textSize(30);
		text("0", 19, 305);
		textSize(60);
		text("+", 20, 369);
		textSize(70);
		text("++", 5, 425);
		
	}

	
	void drawInformationBar(){
	    fill(0,100,255);
	    stroke(255);
	    strokeWeight(0);
	    rectMode(CORNER);
	    rect(0,0,1280,60); 
	 
	    int textColor = (win)? color(106, 111, 247) : color(27, 28, 64);
	    fill(textColor);
	    textFont(baveuse30);
	    text("LEVEL " + actualLevelNo, 25, 40);
	    text("upset bars: " + actualLevel.getPoints() + "/" + actualLevel.getPointsToReach(), 230, 40); 
	    text("bullets left: " + actualLevel.getBulletsLeft(), 600, 40);
	    
	    fill(61, 214, 0);
	    text("total points: " + totalPoints, 950, 40);
	}

	
	void drawEnd(){
	   if(countdown > 0){
	       actualLevel.stepWorld(); 
	       fill(50,50,255);
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
	
	void drawLevelBar(){
		textFont(baveuse30);
		fill(0);
		textSize(40);
		text("LEVEL", 150, 670);
		
		int[] levels = {1, 2, 3, 4, 5, 6, 7, 8};
		
		for(int i=0; i<levels.length; i++){
			if(levelDone[i]){
				fill(0);
			}else{
				fill(125);
			}
			//textSize(30);
			text("" + levels[i], 355 + i*100, 670);
		}
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
	        case 4:
	        	showTutorialLevel1d();
	        	break;
	        case 6:
		        showTutorialLevel2();	
	        	break;
	    } 
	}
	

	void showTutorialLevel1a(){
	    fill(255,100);
	    noStroke();
	    rect(0, 60, width, height-60);
	    image(tut1a, 0, 0);
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
	    image(tut1b, 0, 0);
	    textSize(20);
	    fill(27, 28, 64);
	    text("click to skip", width-200, height-20);
	}
	

	void showTutorialLevel1c(){
	    fill(255,100);
	    noStroke();
	    rect(0, 60, width, height-60);
	    image(tut1c, 0, 0);
	    textSize(20);
	    fill(27, 28, 64);
	    text("click to skip", width-200, height-20);
	}
	
	void showTutorialLevel1d(){
		fill(255,100);
		noStroke();
		rect(0,60,width, height-60);
		image(tut1d, 0, 0); textSize(20);
	    fill(27, 28, 64);
	    text("click to skip", width-200, height-20);
	}
	

	void showTutorialLevel2(){
	    fill(255,100);
	    noStroke();
	    rect(0, 60, width, height-60);
	    image(tut2, 470, 170);
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
	    spinAct = false;
	    
	    if(level == 2){
	        tutorialStage = 4; 
	    }
	    
	    actualLevelNo = level;
	    actualLevel = new Level(level, this, coordProvider);
	    actualBackground = loadImage(coordProvider.getLevelBackgroundImage(level)); 
	    actualForce = 0;
	    actualDx = 0;
	    actualDy = 0;
	    spin = 0;
	    countdown = 1000;
	    lostCountdown = 1000;
	    lostCountdown2 = 1000;
	    levelHover = 0;
	}

	
	// shoots a bullet in direction of mouse coordinates
	public void mouseReleased(){
	    if(!win && allowShoot){
	        actualLevel.addBullet(actualDx, actualDy, actualForce, actualAngle, spin); 
	        if(actualLevel.getBulletsLeft() == 0){
	            lost = true;
	        }
	    }else{
	        if(hoverNext){
	        	levelDone[actualLevelNo-1] = true;
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
	            case 1: case 2: case 3:
	                tutorialStage++;
	                break;
	            case 4:
	            	tutorialStage = 0;
	            	break;
	            case 5:
	                tutorialStage = 6;
	                break;
	            case 6:
	                tutorialStage = 0;
	                first = false;
	                break;
	        }
	    }
	    
	    // TODO check spin
	    
	}
	
	public void mouseClicked(){
		for(int i=-2; i<=2; i++){
			if((Math.abs(Math.pow(mouseX-20, 2)))+(Math.abs(Math.pow(mouseY-(292 + (i * 55)), 2))) < 20*20){
				spin = i;
				return;
			}
		}
		
		switch(levelHover) {
			case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:
				if(levelDone[levelHover-1]){
					totalPoints = 0;
					startNextLevel(levelHover);
				}
				break;
		}
	}

	
	private void setCursor(){
		
		for(int i=-2; i<=2; i++){
			if((Math.abs(Math.pow(mouseX-20, 2)))+(Math.abs(Math.pow(mouseY-(292 + (i * 55)), 2))) < 20*20){
				cursor(HAND);
				levelHover = 0;
				return;
			}
		}
		
		if(mouseY < 670 && mouseY > 630){
			if(mouseX > 355 && mouseX < 375){
				cursor(HAND);
				levelHover = 1;
				return;
			}
			
			if(mouseX > 455 && mouseX < 485){
				cursor(HAND);
				levelHover = 2;
				return;
			}
			
			if(mouseX > 555 && mouseX < 585){
				cursor(HAND);
				levelHover = 3;
				return;
			}
			
			if(mouseX > 655 && mouseX < 685){
				cursor(HAND);
				levelHover = 4;
				return;
			}
			
			if(mouseX > 755 && mouseX < 785){
				cursor(HAND);
				levelHover = 5;
				return;
			}
			
			if(mouseX > 855 && mouseX < 885){
				cursor(HAND);
				levelHover = 6;
				return;
			}
			
			if(mouseX > 955 && mouseX < 985){
				cursor(HAND);
				levelHover = 7;
				return;
			}
			
			if(mouseX > 1055 && mouseX < 1085){
				cursor(HAND);
				levelHover = 8;
				return;
			}
		}
		levelHover = 0;
		cursor(ARROW);
		
	}
	
	/*textFont(baveuse30);
		fill(0);
		textSize(40);
		text("LEVEL", 150, 670);
		
		int[] levels = {1, 2, 3, 4, 5, 6, 7, 8};
		
		for(int i=0; i<levels.length; i++){
			if(levelDone[i]){
				fill(0);
			}else{
				fill(125);
			}
			//textSize(30);
			text("" + levels[i], 355 + i*100, 670);
		}*/
	
}
