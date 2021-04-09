import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lightbees extends PApplet {

private Hive hive;

public void setup() {
  
  background(0);
  
  hive = new Hive( this, 800 );
}

public void draw() {
  hive.Draw();
}
  
  


//SinOsc[] oscillators = new SinOsc[201];

class Hive {
   private Lightbee[] lightbees;
   private PApplet applet;
   private int numberbees;
   private float radius = 125;
   private int gencount = 0;
   private int maxcoherence = 0;
   private int maxmodus = 0;
   
   public Hive( PApplet _applet, int _numberbees ) {
      applet = _applet;
      numberbees = _numberbees;
      lightbees = new Lightbee[numberbees];
      for( int i=0; i<numberbees; i++ ) {
          lightbees[i] = new Lightbee(applet);
      }
      
//      for( int i = 0; i<=200; i++ ) {
//        oscillators[i] = new SinOsc(applet);
//        oscillators[i].freq(((200-i)*20)+20);
////        oscillators[i].amp(0);
//        oscillators[i].stop();
//      }           
   }
   
   public void Draw() {
     background(0);
     int minFreq = lightbees[0].getFreqMaximum();
     int maxFreq = 0;
     int totFreq = 0;
     int lbfreq = 0;
     float avgFreq = 0.0f;
     int flashers = 0;    
     int[] histogram = new int[lightbees[0].getFreqMaximum()+1];
   
     for( int i=0; i<=lightbees[0].getFreqMaximum(); i++ ) {
       histogram[i] = 0;
     }
     
     for(int i = 0; i<numberbees; i++ ) {
       lightbees[i].Draw();

       lbfreq = lightbees[i].getFrequency();
       totFreq += lbfreq;
       
       if (lbfreq < minFreq) minFreq = lbfreq;
       if (lbfreq > maxFreq) maxFreq = lbfreq;
              
       if (lightbees[i].didFlash()) {
          flashers++;
          
          histogram[lbfreq]++;
          
          PVector p = lightbees[i].position();
          
          for( int j=0; j<numberbees; j++ ) {
             if (j==i) continue;
             
             PVector q = lightbees[j].position();
             
             if (( q.x > p.x-radius) && ( q.x < p.x+radius) &&
                 ( q.y > p.y-radius) && ( q.x < p.y+radius)) {
                lightbees[j].adjust();
             }
          }
       }
     }
     
     avgFreq = totFreq / numberbees;
     
     if (flashers > maxcoherence) maxcoherence = flashers;
     
     gencount++;
     
     int freqmax = 0;
     int modus = 0;
     
     for( int i=0; i<=lightbees[0].getFreqMaximum(); i++ ) {
       if (histogram[i] > freqmax) {
         freqmax = histogram[i];
         modus = i;         
       }
     //  if (histogram[i] > 0) {
     //    float amplitude = histogram[i] / flashers;
     //    if (amplitude > 0.05) {
     //      oscillators[i].amp(amplitude);
     //      oscillators[i].play();
     //    }
     //  } else {
     //    //oscillators[i].amp(0);
     //    oscillators[i].stop();
     //  }
     }
     
     if (freqmax > maxmodus) maxmodus = freqmax;
            
     if (maxcoherence > 0) {       
       float percentage = (maxmodus*100.0f) / maxcoherence;
       println( "Generation: ", gencount, "  min: ",minFreq,"  avg: ", avgFreq, "  max: ",maxFreq," num flashers: ", flashers, "  max flashers: ", maxcoherence, " modus: ", modus," with ", freqmax, " flashers  maximum of modus: ", maxmodus, " = ", percentage, "% " );
     } else {
       println( "Generation: ", gencount, "  min: ",minFreq,"  avg: ", avgFreq, "  max: ",maxFreq," num flashers: ", flashers, "  max flashers: ", maxcoherence, " modus: ", modus," with ", freqmax, " flashers  maximum of modus: ", maxmodus );
     }
   }
}
             
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
    
    dx = (random(8) - 4.0f );
    dy = (random(8) - 4.0f );
    
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
    
    float red = 0.0f;
    float green = 0.0f;
    float blue = 0.0f;
        
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
  public void settings() {  size(800,800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "lightbees" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
