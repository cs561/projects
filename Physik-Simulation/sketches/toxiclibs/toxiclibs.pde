import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import toxi.geom.*;

VerletPhysics2D world;
Particle particle1, particle2;

void setup(){
     size(800, 600);
  
     world = new VerletPhysics2D();
     // strongness and direction
     world.addBehavior(new GravityBehavior(new Vec2D(0,0.5)));
     
     world.setWorldBounds(new Rect(0, 0, width, height));
     
     particle1 = new Particle(new Vec2D(width/2,250));
     particle2 = new Particle(new Vec2D(100,180));
     
     particle1.lock();
     
     // now connect the two
     float len = 50;
     float strength = 0.001;
     
     VerletSpring2D spring = new VerletSpring2D(particle1, particle2, len, strength);
     
     world.addParticle(particle1);
     world.addParticle(particle2);
     world.addSpring(spring);
}

void draw(){
    world.update(); 
    
    background(255);
    
    stroke(0);
    strokeWeight(2);
    System.out.println(particle1.x);
    System.out.println(particle1.y);
    System.out.println(particle2.x);
    System.out.println(particle2.y);
    line(particle1.x, particle1.y, particle2.x, particle2.y);
    
    particle1.display();
    particle2.display();
    
    if(mousePressed){
         particle2.lock();
         particle2.x = mouseX;
         particle2.y = mouseY;
         particle2.unlock();
    }
}

