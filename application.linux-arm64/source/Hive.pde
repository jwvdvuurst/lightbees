import processing.sound.*;

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
     float avgFreq = 0.0;
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
       float percentage = (maxmodus*100.0) / maxcoherence;
       println( "Generation: ", gencount, "  min: ",minFreq,"  avg: ", avgFreq, "  max: ",maxFreq," num flashers: ", flashers, "  max flashers: ", maxcoherence, " modus: ", modus," with ", freqmax, " flashers  maximum of modus: ", maxmodus, " = ", percentage, "% " );
     } else {
       println( "Generation: ", gencount, "  min: ",minFreq,"  avg: ", avgFreq, "  max: ",maxFreq," num flashers: ", flashers, "  max flashers: ", maxcoherence, " modus: ", modus," with ", freqmax, " flashers  maximum of modus: ", maxmodus );
     }
   }
}
             
