class ParticleExample{
    VerletParticle2D particle;
   
    ParticleExample(Vec2D pos){
        particle = new VerletParticle2D(pos); 
    } 
    
    void display(){
        fill(0,150);
        stroke(0);
        ellipse(particle.x, particle.y, 16, 16); // notice the non existing transformation
    }
}
