Mover[] movers;
int numberOfMovers;
boolean gravity;
boolean fric;
PVector gForce;
PVector friction;

float c;
float normal;
float frictionMag;

void setup() {
  size(640, 360);
  numberOfMovers = 1;
  movers = new Mover[100];
  gravity = false;
  fric = false;
  
  //Simplification -> neglection of mass, as for all objects
  //the mass = 1
  gForce = new PVector(0, 0.1);
  
  c = 0.01;
  //Simplicfication, normal should be calculated according
  //to structure of surface
  normal = 1;
  frictionMag = c*normal;
  
  for(int i = 0; i < numberOfMovers; i++){
    movers[i] = new Mover(new PVector(0,0), random(0, 1));
  }
  
}


void draw() {
  
  background(255);
  for(int i = 0; i < numberOfMovers; i++) {
     Mover mover = movers[i];
     if(gravity) {
       if(fric) {
         
         //friction = -c*N*v = -frictionMag*v
         PVector friction = mover.velocity.get();
         friction.mult(-1);
         friction.normalize();
         friction.mult(frictionMag);
              
         mover.applyForce(friction);
       }
       
       mover.applyForce(gForce);

     }
     
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

void keyPressed() {
  if(key == 'g' || key == 'G') gravity = !gravity;
  if(key == 'f' || key == 'F') fric = !fric;
}
