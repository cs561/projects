import pbox2d.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.*;

PBox2D world;
Surface surface;
ArrayList<Particle> particles;

void setup(){
    size(800, 600);
    world = new PBox2D(this);
    world.createWorld();
   
    surface = new Surface(); 
    
    particles = new ArrayList<Particle>();
}

void draw(){
    if(random(1) < 0.5){
        float sz = random(2,6);
        particles.add(new Particle(width/2, 100, sz)); 
    }

    world.step();
    background(255);
    surface.display(); 
    
    for(Particle p: particles){
        p.display(); 
    }
    
    // Particles that leave the screen, we delete them
    // (note they have to be deleted from both the box2d world and our list
    for (int i = particles.size()-1; i >= 0; i--) {
        Particle p = particles.get(i);
        if (p.done()) {
          particles.remove(i);
        }
    }
}
