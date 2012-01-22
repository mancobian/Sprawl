package tsdc.sprawl;

import java.io.*;
import java.util.*;
import java.lang.*;

public class Node
{
   public File file;
   public List<Node> children;
   protected List<File> pending;
   protected int totalDirectories;
   protected int scannedDirectories;
   
   public Node(String path)
   {
      this.init(new File(path));
   }
   
   public Node(File file)
   {
      this.init(file);
   }
   
   public int numTotalDirectories() { return this.totalDirectories; }
   public int numScannedDirectories() { return this.scannedDirectories; }
   
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
         this.scanPath(file);
         ++this.scannedDirectories;
      }
      return true;
   }
   
   protected void init(File file)
   {
      this.file = file;
      this.pending = new ArrayList<File>();
      this.children = new ArrayList<Node>();
      this.addDirectory(this.file);
   }
   
   protected boolean addDirectory(File file)
   {
      if (!file.isDirectory()) { return false; }
      this.pending.add(file);
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
            if (!absolute.equals(canonical)) { continue; }
         }
         catch (IOException exception) 
         {
            System.out.println ("EXCEPTION: " + exception.getMessage());
         }
         
         Node child = new Node(childFile.getPath());
         this.children.add(child);
         // System.out.println ("INFO: Added child node = {" + child.file.getPath() + "}");
         
         if (childFile.isDirectory()) 
         { 
            this.addDirectory(childFile); 
            // System.out.println ("INFO: Added pending path = {" + childFile.getPath() + "}");
         }
      }
      return true;
   }
   
   public void print()
   {
      this.print(0);
   }
   
   public void print(int depth)
   {
      /// Print one space per current level of depth
      for (int i = 0; i < depth; ++i)
      {
         System.out.print(" ");
      }
      System.out.println (file.getName());
      
      /// Print children
      if (this.children == null) { return; }
      for (Node node : this.children)
      {
         node.print(depth + 1);
      }
   }
}
