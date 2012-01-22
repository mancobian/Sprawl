package tsdc.sprawl;

import java.io.*;
import processing.core.PApplet;

public class Sprawl extends PApplet
{
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   
   public static void main(String args[])
   {
     PApplet.main(new String[] { "--present", tsdc.sprawl.Sprawl.class.getName() });
   }

   public void setup()
   {
      size(200, 200);
      Node root = new Node(new File("/Applications/Processing.app"));
      root.print();
   }

   public void draw()
   {
   }
}
