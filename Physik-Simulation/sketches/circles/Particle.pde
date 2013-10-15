class Particle{
    Body body;
    float r;
 
    Particle(float x, float y, float r){
        this.r = r;
        makeBody(x,y,r);
    }   
    
    void makeBody(float x, float y, float r){
        BodyDef bd = new BodyDef();
        bd.position = world.coordPixelsToWorld(x,y);
        bd.type = BodyType.DYNAMIC;
        body = world.world.createBody(bd);
        
        CircleShape cs = new CircleShape();
        cs.m_radius = world.scalarPixelsToWorld(r);
        
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 1;
        fd.friction = 0.01;
        fd.restitution = 0.8;
        
        body.createFixture(fd);
        
        body.setLinearVelocity(new Vec2(random(-15f,15f), random(5f, 10f)));
        body.setAngularVelocity(random(-10,10));
    }
    
    void display(){
        Vec2 pos = world.getBodyPixelCoord(body);
        float a = body.getAngle();
        pushMatrix();
        translate(pos.x, pos.y);
        rotate(-a);
        fill(175, 100, 0);
        stroke(0);
        strokeWeight(1);
        ellipse(0,0,r*2, r*2);
        line(0,0,r,0);
        popMatrix();
    }
    
    boolean done(){
        Vec2 pos = world.getBodyPixelCoord(body);
        if(pos.y > height+r*2){
            killBody();
            return true;
        } 
        return false;
    }
    
    void killBody(){
        world.destroyBody(body);  
    }
}
