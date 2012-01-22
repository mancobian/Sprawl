package tsdc.sprawl.treemap;

import java.io.*;
import java.lang.*;
import processing.core.*;
import treemap.*;
import tsdc.sprawl.*;

public class FileItem extends SimpleMapItem
{
   public static class Ui
   {
      public static void draw(PApplet applet, FileItem item)
      {
         /// Update item bounds
         item.calculateBounds();
         
         /// Draw item
         applet.fill(255);
         applet.rect(item.left, item.top, item.right, item.bottom);
         // System.out.println("INFO: Printed rect = {" + item.left + ", " + item.top + ", " + item.right + ", " + item.bottom + "}");
         
         if (Ui.isTextFit(applet, item)) { Ui.drawTitle(applet, item); }
      }
      
      public static boolean isTextFit(PApplet applet, FileItem item)
      {
         float width = applet.textWidth(item.name()) + (item.textPadding * 2);
         float height = applet.textAscent() + applet.textDescent() + (item.textPadding * 2);
         return ((item.right - item.left) > width) && ((item.bottom - item.top) > height);
      }
      
      protected static void drawTitle(PApplet applet, FileItem item)
      {
         /// Draw item title
         applet.fill(0);
         applet.textAlign(applet.LEFT);
         applet.text(item.name(), item.left + item.textPadding, item.bottom - item.textPadding);
      }
   } /// class Ui
   
   public FolderItem parent;
   public File file;
   public int level;
   public float textPadding = 8, left, top, right, bottom;
   protected PApplet applet;
   
   public String name() { return this.file.getName(); }
   
   public FileItem(PApplet applet, FolderItem parent, File file, int level, int order)
   {
      this.applet = applet;
      this.parent = parent;
      this.file = file;
      this.level = level;
      this.order = order;
      this.size = this.file.length();
   }
   
   public void calculateBounds()
   {
      this.left = this.x;
      this.top = this.y;
      this.right = this.x + this.w;
      this.bottom = this.y + this.h;
   }
   
   @Override
   public void draw()
   {
      Ui.draw(this.applet, this);
   }
   
   public void update() {}
} /// class FileTreemapItem
