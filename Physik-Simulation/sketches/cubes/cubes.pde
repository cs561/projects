import pbox2d.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;

ArrayList<Box> boxes;
PBox2D world;
Boundary boundary1, boundary2;

void setup(){
  world = new PBox2D(this);
  world.createWorld();
  world.setGravity(0, -9.81);
  
  size(800,600);
  boxes = new ArrayList<Box>();
  
  boundary1 = new Boundary(100, 450, 500, 15);
  boundary2 = new Boundary(700, 250, 500, 15);
}

void draw(){
  
  world.step(); // very important, tells box2d to move forward ""
  
  background(255);
 
  for(Box b : boxes){
    b.display();
  }

  boundary1.display();
  boundary2.display();
}

void mouseReleased(){
  boxes.add(new Box());
}

void keyPressed(){
  if(key == 'c' || key == 'C'){
    boxes.clear();
  }
}

