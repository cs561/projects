package game;

import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;


public class Game extends PApplet{
	
	/**
	 * The main class of the application. The processing methods setup() and draw() (and others)
	 * are located in this class. It handles the game itself, creating different Level-objects 
	 * according to the players achievements. 
	 */
	
	static final long 			serialVersionUID = 1;

	
	public PImage 				actualBackground, spin1, spin2, cannon, tutCannon, tutRange, 
								tutTowers, tutSpin, tutOTowers, tutAttract, tutWind, tutGravity,
								introImg;
	public HashMap<Integer, PImage>	tutorialImages = new HashMap<Integer, PImage>();
	public Level 				actualLevel;
	public boolean 				intersecting, allowShoot, hoverNext, hoverEnd, win, intro, 
								lost, countPoints, hoverRestart, hoverEnd2,	spinAct, tutorial;
	public int 					actualDx, actualDy, actualForce, winCountdown, actualLevelNo, 
								tutorialStage, totalPoints, checkCountdown, lostCountdown,
								spin, levelHover, introCountdown, winColor, checkColor;
	public float 				actualAngle;
	public PFont 				baveuse;
	public LevelContentProvider coordProvider;
	private boolean[]			levelDone = {true, true, true, true, true, true, true, true, true, true, true, true};
	private boolean[]			firstTut = {true, true, true, true, true, true};
	
	private final boolean		GOD_MODE = false;

	/**
	 * Sets up the GUI to a certain size, loads ressources and defines default values. Also, an
	 * instance of LevelContentProvider is generated here. 
	 */
	public void setup(){
	    size(1280, 720);
	    smooth();
	    baveuse = loadFont("Baveuse.vlw");
	    //levelDone = new boolean[12];
	    
	    spin1 = loadImage("spin1.png");
	    spin2 = loadImage("spin2.png");
	    cannon = loadImage("cannon.png");
	    tutCannon = loadImage("tut1a.png");
	    tutRange = loadImage("tut1b.png");
	    tutTowers = loadImage("tut1c.png");
	    tutSpin = loadImage("tut1d.png");
	    tutOTowers = loadImage("tut2.png");
	    tutAttract = loadImage("tutAttractor.png");
	    tutWind = loadImage("tutWind.png");
	    tutGravity = loadImage("tutGravity.png");
	    introImg = loadImage("tutIntro.png");

	    tutorialImages.put(1, tutCannon);
	    tutorialImages.put(2, tutRange);
	    tutorialImages.put(3, tutTowers);
	    tutorialImages.put(4, tutSpin);
	    tutorialImages.put(6, tutOTowers);
	    tutorialImages.put(8, tutAttract);
	    tutorialImages.put(10, tutWind);
	    tutorialImages.put(12, tutGravity);

	    intro = true;  // TODO
	    introCountdown = 400;
	    
	    // default
	    actualLevelNo = 1;
	    tutorialStage = 1;
	    tutorial = true;
	    
	    // debug
	    /*actualLevelNo = 11;
	    tutorialStage = 11;*/
	    
	    coordProvider = new LevelContentProvider(GOD_MODE);
	    startNextLevel(actualLevelNo);
	}

	/**
	 * The main loop of the whole Processing applet.
	 */
	public void draw(){ 
	   if(intro){
		   drawIntro();
	   }else{
		   drawGame();
	   }  
	}
	
	/**
	 * Draws the intro and sets the boolean value 'intro' to false.
	 */
	void drawIntro(){
		if(introCountdown > 0){
			image(introImg, 0, 0);
			introCountdown -= 2;
		}else{
			intro = false;
		}
	}
	
	/**
	 * Draws the main game after the intro.
	 */
	void drawGame(){
		 setCursor();
		   background(actualBackground);
		  
		   /* if the players actual points are equal to the points to reach, the player
		    * has won the game. */
		   
		   if(actualLevel.getPoints() == actualLevel.getPointsToReach()){
		       win = true; 
		       lost = false;
		   }

		   actualLevel.displayLevel();
		   drawInformationBar();

		   if(!win){
			   
			   actualLevel.stepWorld();
			   actualLevel.calculateAttraction();
			   actualLevel.calculateWindImpact();
		        
		       if(mouseX > 80 && mouseY < 600 && actualLevel.getBulletsLeft() > 0){
		           if(!tutorial){
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
		   
		   /* if the variable tutorialStage has a value implying to show a
		    * tutorial (see showTutorial for the values), a certain tutorial
		    * overlay must be displayed. Otherwise, the level bar must be drawed. */
		   
		   drawCannon();
		   
		   if((tutorialStage == 5) || (tutorialStage == 7) || (tutorialStage == 9)   
				   || (tutorialStage == 11) || (tutorialStage == 13)){
		       drawLevelBar(); 
		   }else{
			   if((firstTut[0] && actualLevelNo == 1) || (firstTut[1] && actualLevelNo == 2) 
					   || (firstTut[2] && actualLevelNo == 9) || (firstTut[3] && actualLevelNo == 10) || (firstTut[4] && actualLevelNo == 12)){
				   showTutorial();
			   }else{
				   drawLevelBar();
			   }
		   }
		   
		   drawSpinBar();

		   /* CAUTION!!!! As soon as the player shoots his last bullet, the variable lost
		    * is set to true. Henceforth, a countdown is started. This is the time the game
		    * waits to check if the last bullet is going to hit any towers. Only after this
		    * countdown and without reaching the necessary points, the game is definitely lost. */
		    
		   if(lost){
		       if(checkCountdown > 0){
		           checkCountdown -= 2;
		           fill(checkColor);
		           textSize(20);
		           text("checking...   " + (checkCountdown/100), 20, 100);  
		       }else{
		    	   // lostCountdown2 ist the short period, in which "YOU LOST" is displayed
		           if(lostCountdown > 0){
		               textSize(150);
		               fill(255);
		               text("YOU LOST", 218, 403);
		               fill(255, 25, 25);
		               text("YOU LOST", 215, 400);
		               lostCountdown -= 7;
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

	
	/**
	 *  Draws the arrow pointing to the mouse coordinates.
	 */
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

	
	/**
	 *  Draws the arrow head in the right angle.
	 */
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

	/**
	 * Draws the cannon pointing to the mouse coordinates.
	 */
	void drawCannon(){
	    pushMatrix();
	    translate(80, 600);
	    rotate(actualAngle);
	    translate(-80, -600);
	    image(cannon, 0, 590, 119, 59);
	    popMatrix();
	}
	
	/**
	 * Draws the spin bar on the left side of the screen.
	 */
	void drawSpinBar(){
		int topLeftX = 5;
		
		fill(200);
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

	/**
	 * Draws the information bar at the top of the screen.
	 */
	void drawInformationBar(){
	    fill(0,100,255);
	    stroke(255);
	    strokeWeight(0);
	    rectMode(CORNER);
	    rect(0,0,1280,60); 
	 
	    int textColor = (win)? color(106, 111, 247) : color(27, 28, 64);
	    fill(textColor);
	    textFont(baveuse);
	    textSize(30);
	    text("LEVEL " + actualLevelNo, 25, 40);
	    text("upset bars: " + actualLevel.getPoints() + "/" + actualLevel.getPointsToReach(), 230, 40); 
	    text("bullets left: " + actualLevel.getBulletsLeft(), 600, 40);
	    
	    fill(61, 214, 0);
	    text("total points: " + totalPoints, 950, 40);
	}

	/**
	 * First, a short time the words "YOU WIN" are displayed. After that, the
	 * game results and options for the future game are shown as popup. 
	 */
	void drawEnd(){
	   if(winCountdown > 0){
	       actualLevel.stepWorld();
	       textFont(baveuse);
	       textSize(150); 
	       fill(255); 
	       text("YOU WIN", 253, 403);
	       fill(winColor);
	       text("YOU WIN", 250, 400); 
	       winCountdown -= 7;
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

	/**
	 * Calls the two methods to draw the two popups.
	 */
	void showPopUp(){
	    fill(255, 100);
	    rectMode(CORNER);
	    rect(0,60,1280,720);
	    rectMode(CORNER);
	    strokeWeight(5);
	    showGameResult();
	    showOptionMenu();
	}

	/**
	 * Draws the popup, in which the game results are displayed.
	 */
	void showGameResult(){
	    int x1 = 100;
	    int y1 = (height/2) - 175;
	    
	    fill(255, 75);
	    stroke(27, 28, 64, 75);
	    rect(x1, y1, 400, 350);
	    
	    int[] types = actualLevel.getTowerBarTypes();
	    
	    textFont(baveuse);
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
	    
	    text("TOTAL POINTS: " + (bulletsLeft+1) + " X " + subtotal + " = " + ((bulletsLeft+1)*subtotal), x1+20, y1+300);
	    
	    if(countPoints){
	        totalPoints += (bulletsLeft+1)*subtotal;
	        countPoints = false;
	    }
	}

	/**
	 * Draws the popup with the two buttons "NEXT LEVEL" and "END GAME".
	 */
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
	    
	    textFont(baveuse);
	    fill(hoverNext? 100 : 255);
	    text((actualLevelNo != levelDone.length) ? "NEXT LEVEL" : "NEW GAME", x1+95, y1+118);
	    fill(hoverEnd? 100 : 255);
	    text("END GAME", x1+107, y1+268);
	}

	/**
	 * When a game is lost, the popup with the two buttons "RESTART" and
	 * "END GAME" are drawn.
	 */
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
	    
	    textFont(baveuse); 
	    fill(hoverRestart? 100 : 255);
	    text("RESTART", x1+125, y1+118);
	    fill(hoverEnd2? 100 : 255);
	    text("END GAME", x1+107, y1+268); 
	}
	
	/**
	 * TEMPORARY
	 */
	void drawLevelBar(){
		fill(200);
		stroke(0);
		strokeWeight(2);
		rect(0, 650, 1279, 78);
		
		textFont(baveuse);
		fill(100);
		textSize(25);
		text("LEVEL", 10, 695);
		
		int[] levels = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
		
		for(int i=0; i<levels.length; i++){
			if(levelDone[i]){
				fill(0);
			}else{
				fill(125);
			}
			textSize(35);
			text("" + levels[i], 120 + i*100, 700);
		}
	}
	
	/**
	 * Calls the appropriate method to display the tutorial overlay according
	 * to the variable tutorialStage. The values of tutorialStage are:
	 * 1 - show explanation cannon	(level 1)
	 * 2 - show explanation range
	 * 3 - show explanation towers
	 * 4 - show explanation spin
	 * 6 - show explanation other towers (level 2)
	 * 8 - explanation attractor (level 9)
	 * 10 - explanation wind (level 10)
	 * 12 - explanation 0 gravity (level 12)
	 * 5, 7, 9, 11, 13 - no explanation
	 */
	void showTutorial(){
		fill(255,100);
	    noStroke();
	    rect(0, 60, width, height-60);
	    image(tutorialImages.get(tutorialStage), 0, 0);
	    textSize(20);
	    fill(27, 28, 64);
	    text("click to skip", width-200, height-20);
	    if(tutorialStage == 1){
	    	drawCannon();
	    }
	    if(tutorialStage == 2){
	    	fill(255, 215, 0, 100);
	        arc(70, 610, 1060, 1060, -PI/2, 0);
	        drawCannon();
	    }
	}
		
	/**
	 * Returns the actual dx/dy (cannon-mouse) and the actual force.
	 * @param v	The vector describing the cannon-mouse link.
	 */
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
	
	/**
	 * Sets all relevant variables to default values and starts a new 
	 * level according to the actual level stage.
	 * @param levelNo 	the actual level stage
	 */
	void startNextLevel(int levelNo){
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
	    
	    if(levelNo == 1 || levelNo == 2 || levelNo == 9 || levelNo == 10 || levelNo == 12){
	    	tutorial = true;
	    }else{
	    	tutorial = false;
	    }
	    
	    switch(levelNo){
	    	case 1: 
	    		tutorialStage = 1;
	    		if(firstTut[0]){
	    			tutorial = true;
	    		}else{
		    		tutorial = false;
	    		}
	    		break;
	    	case 2: 
	    		tutorialStage = 6;
	    		if(firstTut[1]){
	    			tutorial = true;
	    		}else{
		    		tutorial = false;
	    		}
	    		break;
	    	case 3: case 4: case 5: case 6: case 7: case 8:
	    		tutorialStage = 7;
	    		tutorial = false;
	    		break;
	    	case 9: 
	    		tutorialStage = 8;
	    		if(firstTut[2]){
	    			tutorial = true;
	    		}else{
		    		tutorial = false;
	    		}
	    		break;
	    	case 10: 
	    		tutorialStage = 10;
	    		if(firstTut[3]){
	    			tutorial = true;
	    		}else{
		    		tutorial = false;
	    		}
	    		break;
	    	case 11: 
	    		tutorialStage = 11;
	    		tutorial = false;
	    		break;
	    	case 12:
	    		tutorialStage = 12;
	    		if(firstTut[4]){
	    			tutorial = true;
	    		}else{
		    		tutorial = false;
	    		}
	    		break;
	    }
	    
	    actualLevelNo = levelNo;
	    actualLevel = new Level(levelNo, this, coordProvider);
	    actualBackground = loadImage(coordProvider.getLevelBackgroundImage(levelNo)); 
	    actualForce = 0;
	    actualDx = 0;
	    actualDy = 0;
	    spin = 0;
	    winCountdown = 1000;
	    checkCountdown = 1300;
	    lostCountdown = 1000;
	    levelHover = 0;
	    
	    if(levelNo == 11){
	    	winColor = color(255);
	    }else{
	    	winColor = color(50,50,255);
	    }
	    
	    if(levelNo == 3 || levelNo == 4 || levelNo == 12){
	    	checkColor = color(255);
	    }else{
	    	checkColor = color(0);
	    }
	}

	
	/**
	 *  If the game is not won yet and the player is allowed to shoot bullets,
	 *  a new bullet is added to the level (aka a bullet gets shot). Optionally, if 
	 *  the player shoots his last bullet, the variable lost is set to true.
	 *  
	 *  If the game is 'finished', the method checks of any hover boolean variable, which 
	 *  represent a hover state of the mouse, is true, calling the appropriate method
	 *  afterwards. 
	 *  Also, it promotes the tutorial by incrementing or zeroing the variable tutorialStage.
	 */
	public void mouseClicked(){	
		//System.out.println("click");
		
		for(int i=-2; i<=2; i++){
			if((Math.abs(Math.pow(mouseX-20, 2)))+(Math.abs(Math.pow(mouseY-(292 + (i * 55)), 2))) < 20*20){
				spin = i;
				return;
			}
		}
		
		switch(levelHover) {
			case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:
			case 9: case 10: case 11: case 12:
				if(levelDone[levelHover-1]){
					totalPoints = 0;
					startNextLevel(levelHover);
				}
				return;
		}
		
		if(!win && allowShoot){
	        actualLevel.addBullet(actualDx, actualDy, actualForce, actualAngle, spin); 
	        if(actualLevel.getBulletsLeft() == 0){
	            lost = true;
	        }
	    }else{
	        if(hoverNext){
	        	levelDone[actualLevelNo-1] = true;
	            if(actualLevelNo != levelDone.length){
	            	visitTutLevel(actualLevelNo);
	            	startNextLevel(++actualLevelNo);
	            }else{
	            	actualLevelNo = 1;
	            	totalPoints = 0;
	            	visitTutLevel(actualLevelNo);
	            	startNextLevel(actualLevelNo);
	            }   
	        }else{
	        	switch(tutorialStage){
		        	case 1: case 2: case 3:
		        		tutorialStage++;
		        		tutorial = true;
		        		break;
		        	case 4: case 6: case 8: case 10: case 12:
		        		tutorial = false;
		        		tutorialStage++;
		        		break;
		        	case 5:
		        		if(actualLevelNo == 2){
		        			tutorial = true;
		        			tutorialStage++;
		        		}
		        		break;
		        	case 7:
		        		if(actualLevelNo == 9){
		        			tutorial = true;
		        			tutorialStage++;
		        		}
		        		break;
		        	case 9:
		        		if(actualLevelNo == 10){
		        			tutorial = true;
		        			tutorialStage++;
		        		}
		        		break;
		        	case 11:
		        		if(actualLevelNo == 12){
		        			tutorial = true;
		        			tutorialStage++;
		        		}
		        		break;        		
	        	}
	        }
	        
	        if(hoverRestart){
	        	visitTutLevel(actualLevelNo);
	            startNextLevel(actualLevelNo);
	        }
	       
	        if(hoverEnd || hoverEnd2){
	            System.exit(0);
	        } 
	        
	        
	        
	    }
	    
	    // TODO check spin
	    
	}
	
	private void visitTutLevel(int lvlNo){
		switch(lvlNo){
			case 1:
				firstTut[0] = false;
				break;
			case 2:
				firstTut[1] = false;
				break;
			case 9:
				firstTut[2] = false;
				break;
			case 10:
				firstTut[3] = false;
				break;
			case 12:
				firstTut[4] = false;
				break;
		}
	}
	
	/**
	 * This method checks, if the mouse is on any spin value, and if so, the button
	 * gets set to this point.
	 * 
	 * levelHover is TEMPORARY
	 */
	/*public void (){
		
		
		System.out.println(tutorialStage);
	}
*/
	/**
	 * Defines according to the mouse coordinates, which cursor should be displayed.
	 */
	private void setCursor(){
		
		for(int i=-2; i<=2; i++){
			if((Math.abs(Math.pow(mouseX-20, 2)))+(Math.abs(Math.pow(mouseY-(292 + (i * 55)), 2))) < 20*20){
				cursor(HAND);
				levelHover = 0;
				return;
			}
		}
		
		if(mouseY < 710 && mouseY > 670){
			int x;
			for(int i=0; i<12; i++){
				x = 135 + i*100;
				if(mouseX > x-20 && mouseX < x+20){
					cursor(HAND);
					levelHover = i+1;
					return;
				}
			}
		}
		levelHover = 0;
		cursor(ARROW);
		
	}
}
