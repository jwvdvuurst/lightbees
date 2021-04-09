private Hive hive;

void setup() {
  size(800,800); //<>//
  background(0);
  
  hive = new Hive( this, 800 );
}

void draw() {
  hive.Draw();
}
  
  
