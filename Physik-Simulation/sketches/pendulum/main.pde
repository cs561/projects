Pendulum p;
boolean pendulumReleased;
boolean touchingPendulum;

void setup() {
  size(1000, 300);
  p = new Pendulum(0, 200, new PVector(width/2, 0), 15);
  pendulumReleased = false;
  touchingPendulum = false;
  p.display();
}


void draw() {
  if(pendulumReleased){
    background(255);
    p.go();
  }
}

void mouseDragged(){
  if(touchingPendulum) {
    PVector newLocation = new PVector(mouseX-p.origin.x, mouseY-p.origin.y);
    p.location = newLocation.get();
    
    newLocation.normalize();
    p.angle = asin(newLocation.x);
    
    background(255);
    p.display();
  }
}

void mousePressed() {
  if(p.touchingBob(mouseX, mouseY)) touchingPendulum = true;
  else touchingPendulum = false;
}

void mouseReleased() {
  if(touchingPendulum) pendulumReleased = true;
  touchingPendulum = false;
}
