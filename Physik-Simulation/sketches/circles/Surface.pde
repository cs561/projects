class Surface{
    ArrayList<Vec2> surface;
    
    Surface(){
        surface = new ArrayList<Vec2>();        
        ChainShape chain = new ChainShape();
        
        float theta = 0;

        for (float x = width+10; x > -10; x -= 5){
            float y = map(cos(theta),-1,1,75,height-10);
            theta += 0.075;
            surface.add(new Vec2(x,y));
        }
        
        Vec2[] vertices = new Vec2[surface.size()];
        
        for(int i=0; i<vertices.length; i++){
            vertices[i] = world.coordPixelsToWorld(surface.get(i)); 
        }
        
        chain.createChain(vertices, vertices.length);
        
        BodyDef bd = new BodyDef();
        Body body = world.world.createBody(bd);
        body.createFixture(chain, 1);    
    }
    
    void display(){
        strokeWeight(1);
        stroke(0);
        noFill();
        beginShape();
        for(Vec2 v: surface){
            vertex(v.x, v.y);
        } 
        endShape();
    }
}
