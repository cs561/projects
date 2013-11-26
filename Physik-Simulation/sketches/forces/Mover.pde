class Mover {
  
  PVector location;
  PVector velocity;
  PVector acceleration;
  
  float topspeed;
  float colorCoeff;
  
  float mass;
  
  Mover(PVector velocity, float colorCoeff) {
    location = new PVector(random(width), random(height));
    this.velocity = velocity;
    this.colorCoeff = colorCoeff;
    topspeed = 4;
    mass = 1;
  }
  
  void applyForce(PVector force) {
    PVector f = PVector.div(force, mass);
    acceleration.add(f);
    
  }
  
  void checkEdges() {
    
    if(location.x < 0) {
      location.x = width;
    } else if (location.x > width) {
      location.x = 0;
    }
    
    if(location.y < 0) {
      location.y = 0;
      velocity.y *= -1;
    } else if (location.y > height) {
      location.y = height;
      velocity.y *= -1;
    }
  }
  
  void update() {
    if(!gravity) {
      PVector mouse = new PVector(mouseX, mouseY);
      PVector dir = PVector.sub(mouse, location);
      
      dir.normalize();
      dir.mult(0.1);
      acceleration = dir;
      
      velocity.add(acceleration);
      velocity.limit(topspeed);
      location.add(velocity);
      
    } else {
      
      velocity.add(acceleration);
      location.add(velocity);
      acceleration.mult(0);
      
    }
  }
  
  void display() {
    stroke(0);
    fill(255*(1-colorCoeff), 50, 255*colorCoeff);
    ellipse(location.x, location.y, 30, 30);
  }
}
