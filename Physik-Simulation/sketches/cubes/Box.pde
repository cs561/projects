class Box  {

  Body body;
  float w,h;
 
  Box() {
    w = random(5, 46);
    h = random(5, 46);
    
    // STEP 1
    BodyDef bd = new BodyDef();
    
    // STEP 2
    bd.type = BodyType.DYNAMIC;
    bd.position.set(world.coordPixelsToWorld(mouseX,mouseY));
    
    // STEP 3
    body = world.createBody(bd);

    // STEP 4
    PolygonShape ps = new PolygonShape();
    float box2dW = world.scalarPixelsToWorld(w/2);
    float box2dH = world.scalarPixelsToWorld(h/2);
    ps.setAsBox(box2dW, box2dH);

    // STEP 5
    FixtureDef fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 1;
    fd.friction = 0.3;
    fd.restitution = 0.2;

    // STEP 6
    body.createFixture(fd);
  }
 
  void display() {
    
    Vec2 pos = world.getBodyPixelCoord(body);
    float a = body.getAngle(); // needed for rotation
    
    pushMatrix();
    
    translate(pos.x, pos.y);
    rotate(-a); // processing vs. world
    
    fill(175);
    stroke(0);
    rectMode(CENTER);
    rect(0,0,w,h);
    popMatrix();
  }
  
  void killBody(){
    world.destroyBody(body);
  }
}
