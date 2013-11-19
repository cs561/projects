class Mover {
  
  PVector location;
  PVector velocity;
  PVector acceleration;
  float topspeed;
  float colorCoeff;
  
  Mover(PVector velocity, float colorCoeff) {
    location = new PVector(random(width), random(height));
    this.velocity = velocity;
    this.colorCoeff = colorCoeff;
    topspeed = 4;
  }
  
  void checkEdges() {
    
    if(location.x < 0) {
      location.x = width;
    } else if (location.x > width) {
      location.x = 0;
    }
    
    if(location.y < 0) {
      location.y = height;
    } else if (location.y > height) {
      location.y = 0;
    }
  }
  
  void update() {
    PVector mouse = new PVector(mouseX, mouseY);
    PVector dir = PVector.sub(mouse, location);
    
    dir.normalize();
    dir.mult(0.1);
    acceleration = dir;
    
    velocity.add(acceleration);
    velocity.limit(topspeed);
    location.add(velocity);
  }
  
  void display() {
    stroke(0);
    fill(255*(1-colorCoeff), 50, 255*colorCoeff);
    ellipse(location.x, location.y, 30, 30);
  }
}

Mover[] movers;
int numberOfMovers;

void setup() {
  size(640, 360);
  numberOfMovers = 1;
  movers = new Mover[100];
  
  for(int i = 0; i < numberOfMovers; i++){
    movers[i] = new Mover(new PVector(0,0), random(0, 1));
  }
  
}


void draw() {
  
  background(255);
  for(int i = 0; i < numberOfMovers; i++) {
     Mover mover = movers[i];
     mover.update();
     mover.checkEdges();
     mover.display();
  }
  
}

void mousePressed() {
  if(numberOfMovers <= 98){
    movers[numberOfMovers++] = new Mover(new PVector(0,0), random(0, 1));
  }
  
}
