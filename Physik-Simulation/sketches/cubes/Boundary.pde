class Boundary{
  
  float x,y;
  float w,h;
  Body b;
  
  Boundary(float x, float y, float w, float h){
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
   
    BodyDef bd = new BodyDef();
    bd.position.set(world.coordPixelsToWorld(x,y));
    bd.type = BodyType.STATIC; 
    b = world.createBody(bd);
    
    float boxW = world.scalarPixelsToWorld(w/2);
    float boxH = world.scalarPixelsToWorld(h/2);
    PolygonShape ps = new PolygonShape();
    ps.setAsBox(boxW, boxH);

    b.createFixture(ps,1);
  }
  
  void display(){
    fill(0);
    stroke(0);
    rectMode(CENTER);
    rect(x,y,w,h); 
  }
}
