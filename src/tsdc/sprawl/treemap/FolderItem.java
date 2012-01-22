package tsdc.sprawl.treemap;

import java.io.*;
import java.util.*;
import processing.core.*;
import treemap.*;
import tsdc.sprawl.*;

public class FolderItem extends FileItem implements MapModel
{
   public static class Ui
   {
      public static void draw(PApplet applet, FolderItem item)
      {
         item.update();
       
         if (item.isContentsVisible())
         {
            for (Mappable mappable : item.items())
            {  
               mappable.draw();
            }
         }
         else
         {
            FileItem.Ui.draw(item.applet, item);
         }
      }
   } /// class Ui
   
   protected MapLayout algorithm;
   protected List<Mappable> items;
   protected List<File> pending;
   protected boolean isContentsVisible, isLayoutValid;
   protected int totalDirectories, scannedDirectories;
   
   public FolderItem(PApplet applet, FolderItem parent, File directory, int level, int order)
   {
      super(applet, parent, directory, level, order);
      this.init(directory);
   }
   
   public boolean isContentsVisible() { return this.isContentsVisible; }
   public void isContentsVisible(boolean value) { this.isContentsVisible = value; }
   public boolean isLayoutValid() { return this.isLayoutValid; }
   public int numTotalDirectories() { return this.totalDirectories; }
   public int numScannedDirectories() { return this.scannedDirectories; }
   public List<Mappable> items() { return this.items; }
   
   public void validateLayout()
   {
      if (!this.isLayoutValid && !this.items.isEmpty())
      {
         this.algorithm.layout(this, this.bounds);
         if (this.pending.isEmpty()) { this.isLayoutValid = true; }
      }
   }
   
   public boolean scan() { return this.scan(0); }
   public boolean scan(int steps)
   {
      if (this.pending.isEmpty()) { return false; }
      if (steps <= 0) { steps = this.pending.size(); }
      steps = Math.min(steps, this.pending.size());
      
      Node[] nodes = new Node[steps];
      for (int i = 0; i < steps; ++i)
      {
         File file = this.pending.remove(0);
         System.out.println("Scanning pending path " + file.getPath() + " for " + this.name());
         this.scanPath(file);
         ++this.scannedDirectories;
      }
      return true;
   }
   
   @Override
   public Mappable[] getItems() { return (Mappable[]) this.items.toArray(new Mappable[this.items.size()]); }
   
   @Override
   public void draw()
   {
      Ui.draw(this.applet, this);
   }
   
   @Override
   public void update()
   {
      if (!this.pending.isEmpty()) { this.scan(1); }
      
      this.validateLayout();
      this.calculateBounds();
      
      for (Mappable mappable : this.items())
      {  
         ((FileItem)mappable).update();
      }
   }
   
   @Override
   public String toString()
   {
      return this.name();
   }
   
   protected void init(File directory)
   {
      this.items = new ArrayList<Mappable>();
      this.pending = new ArrayList<File>();
      this.algorithm = new PivotBySplitSize();
      this.isContentsVisible = false;
      this.isLayoutValid = false;
      this.addDirectory(directory);
   }
   
   protected boolean addDirectory(File file)
   {
      if (!file.isDirectory()) { return false; }
      this.pending.add(file);
      System.out.println("Add pending path " + file.getPath() + " to " + this.name());
      ++this.totalDirectories;
      return true;
   }
   
   protected boolean scanPath(File file)
   {
      List<String> contents = Arrays.asList(file.list());
      if (contents == null) 
      {
         System.out.println ("WARNING: Contents null for file = {" + file.getPath() + "}");
         return false; 
      }
      
      Collections.sort(contents);
      
      for (String node : contents)
      {
         if (node.equals(".") || node.equals("..")) { continue; }
         File childFile = new File(file, node);
         try
         {
            String absolute = childFile.getAbsolutePath();
            String canonical = childFile.getCanonicalPath();
            if (!absolute.equals(canonical)) 
            {
               System.out.println ("WARNING: Invalid path => " + absolute + " != " + canonical);
               continue; 
            }
         }
         catch (IOException exception) 
         {
            System.out.println ("EXCEPTION: " + exception.getMessage());
         }
         
         FileItem item = null;
         if (childFile.isDirectory()) 
         { 
            item = new FolderItem(this.applet, this, childFile, level+1, this.items.size());
            System.out.println("INFO: Added directory = {" + childFile.getPath() + "} to " + this.name());
         }
         else 
         { 
            item = new FileItem(this.applet, this, childFile, level+1, this.items.size());
            System.out.println("INFO: Added file = {" + childFile.getPath() + "} to " + this.name());
         }
         this.items.add(item);
      }
      return true;
   }
} /// class FolderItem
