class Pendulum {
  
  float r;
  float angle;
  float aVelocity;
  float aAcceleration;
  float damping;
  float bobRadius;
  
  PVector origin;
  PVector location;
  
  Pendulum(float startAngle, float rowLength, PVector ori, float bobR) {
    angle = startAngle;
    r = rowLength;
    origin = ori;
    location = new PVector(0, 0);
    damping = 0.995;
    bobRadius = bobR;
  }
  
  void update() {
    //arbitrarily
    float gravity = 0.4;
    
    //F_p = (F_g*sin(angle))/r
    aAcceleration = (-1 * gravity * sin(angle)) / r;
    
    aVelocity += aAcceleration;
    angle += aVelocity;
    
    //simulate friction
    aVelocity *= damping;
  }
  
  void display() {
    location = new PVector(r*sin(angle), r*cos(angle));
    location.add(origin);
    
    stroke(0);
    line(origin.x, origin.y, location.x, location.y);
    
    fill(0, 0, 230);
    ellipse(location.x, location.y, bobRadius*2, bobRadius*2);
  }
  
  void go() {
    update();
    display();
  }
  
  boolean touchingBob(int x, int y) {
    float newX = location.x - x;
    float newY = location.y - y;
    
    if((newX*newX + newY*newY) < (bobRadius*bobRadius)) return true;
    else return false;
  }
  
}
