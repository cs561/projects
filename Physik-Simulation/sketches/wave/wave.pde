float angle = 0;
float step = 0.03;
float angleVel = 0.4;
float amplitude = 100;

void setup() {
  size(1000, 200);
}

void draw() {
  background(255);
  angle += step;
  float temp = angle;
  
  for (int x = 0; x <= width; x += 12) {
    float y = amplitude * sin(temp);
    stroke(0);
    fill((x + 30) % 255, (x + 90)  % 255, (x + 150) % 255, 170);
    ellipse(x, y + height/2, 48, 48);
    temp += angleVel;
  }
}
