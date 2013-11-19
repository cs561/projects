import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.processing.*;
 
Vec3D BOX_SIZE = new Vec3D(5,5,20);
float SCALE=130;
 
TriangleMesh[] boxes=new TriangleMesh[300];
ToxiclibsSupport gfx;
 
void setup() {
  size(680,382,P3D);
  gfx=new ToxiclibsSupport(this);
  for(int i=0; i<boxes.length; i++) {
    // create a new direction vector for each box
    Vec3D dir=new Vec3D(cos(i*TWO_PI/75),sin(i*TWO_PI/50),sin(i*TWO_PI/25)).normalize();
    // create a position on a sphere, using the direction vector
    Vec3D pos=dir.scale(SCALE);
    // create a box mesh at the origin
    TriangleMesh b= (TriangleMesh)new AABB(new Vec3D(), BOX_SIZE).toMesh();
    // align the Z axis of the box with the direction vector
    b.pointTowards(dir);
    // move the box to the correct position
    b.transform(new Matrix4x4().translateSelf(pos.x,pos.y,pos.z));
    boxes[i]=b;
  }
}
 
void draw() {
  background(51);
  lights();
  translate(width / 2, height / 2, 0);
  rotateX(mouseY * 0.01f);
  rotateY(mouseX * 0.01f);
  noStroke();
  for(int i=0; i<boxes.length; i++) {
    gfx.mesh(boxes[i]);
  }
}

