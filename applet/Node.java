package tsdc.sprawl;

import java.io.*;
import java.util.*;

public class Node
{
   File file;
   List<Node> children;
   
   Node (File file)
   {
      this.file = file;
      if (!file.isDirectory()) { return; }
      
      List<String> contents = Arrays.asList(file.list());
      if (contents != null)
      {
         Collections.sort(contents);
         
         this.children = new ArrayList<Node>(contents.size());
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
            catch (IOException exception) {}
            
            Node childNode = new Node(childFile);
            this.children.add(childNode);
         }
      }
   }
   
   void print()
   {
      this.print(0);
   }
   
   void print(int depth)
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
