class Lightbee {
  private PVector pos;
  private int frequency;
  private int counter;
  private PApplet applet;
  private boolean flash;
  
  private final int freq_min = 4;
  private final int freq_max = 200;
  
  public Lightbee( PApplet _applet ) {
    float x;
    float y;
    
    applet = _applet;
        
    x = random(applet.width);
    y = random(applet.height);
    
    pos = new PVector(x,y);
    
    frequency = ceil( random(ceil((freq_min + freq_max)/4)) )+floor(freq_max/2);
    counter = 0;
  }
  
  public int getFreqMaximum() {
    return freq_max;
  }
  
  public PVector position() {
    return pos;
  }
  
  public int getFrequency() {
    return frequency;
  }
  
  public boolean didFlash() {
    return flash;
  }
  
  public void adjust() {
    if ((counter == 0) || (counter == frequency-1)) return;
    if ( counter < (frequency-counter) ) {
      if (frequency < freq_max) {
        frequency++;
      }
    } else {
      if (frequency > freq_min) {
        frequency--;
      }
    }
  }
  
  public void Draw() {
    float dx;
    float dy;
    
    dx = (random(8) - 4.0 );
    dy = (random(8) - 4.0 );
    
    if ( pos.x + dx < 0 ) pos.x += applet.width;
    if ( pos.x + dx > applet.width) pos.x -= applet.width;
    if ( pos.y + dy < 0 ) pos.y += applet.height;
    if ( pos.y + dy > applet.height) pos.y -= applet.height;
    
    pos.x += dx;
    pos.y += dy;
    
    counter++;
    
    if (counter == frequency) {
      flash = true;
      counter = 0;
      fill(0,255,255);
    } else {
      flash = false;
      fill(0,0,0);
    }
    
    float red = 0.0;
    float green = 0.0;
    float blue = 0.0;
        
    if (frequency < floor(freq_max / 2)) {
      green = 64;
    } else {
      red = 64;
    }
    
    if (frequency < floor(freq_max / 4)) {
      green += (44 - (frequency - 6))*(128/44);
    }
    
    if (frequency > floor(freq_max * 3 / 4)) {
      red += (frequency - 150) * (128 / 50);
    }
    
    if (flash) {
      red += 16;
      green += 16;
      blue += 16;
    }
    
    stroke( red, green, blue );
    
    circle( pos.x, pos.y, 5 );
  }
}
