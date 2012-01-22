package tsdc.sprawl;

import java.io.*;
import processing.core.*;
import treemap.*;
import tsdc.sprawl.treemap.*;

public class Main extends PApplet
{
   private static final long serialVersionUID = 1L;
   protected FolderItem root;
   protected PFont font;
   
   public static void main(String args[])
   {
      PApplet.main(new String[] { "--present", tsdc.sprawl.Main.class.getName() });
   }

   public void setup()
   {
      size(1024, 768);
      rectMode(CORNERS);
      
      this.font = createFont("SansSerif", 13);
      
      this.setRoot("/Applications/Processing.app");
   }
   
   public void setRoot(String path)
   {
      this.root = new FolderItem(this, null, new File(path), 0, 0);
      this.root.setBounds(0, 0, width-1, height-1);
      this.root.isContentsVisible(true);
   }

   public void draw()
   {
      background(255);
      textFont(font);
      
      if (this.root != null) { this.root.draw(); }
   }
   
   private void drawScanProgress()
   {
      float barX = 30;
      float barY = 60;
      float barWidth = width - (barX * 2.0f);
      float barHeight = 20;
      int numScanned = this.root.numScannedDirectories();
      int numNodes = this.root.numTotalDirectories();
      float percentComplete = map(numScanned + 1, 0, numNodes, 0, barWidth);
      
      fill(0);
      
      String message = null;
      if (numScanned != numNodes)
      {
         message = new String("Scanning " 
            + nfc(numScanned + 1) 
            + " out of "
            + nfc(numNodes)
            + " directories...");
      }
      else
      {
         message = new String("Completed scanning "
            + nfc(numNodes)
            + " directories.");
      }
      text(message, barX, barY - 10);
      
      fill(128);
      rect(barX, barY, barWidth, barHeight);
      
      fill(255);
      rect(barX, barY, percentComplete, barHeight);
   }
}
