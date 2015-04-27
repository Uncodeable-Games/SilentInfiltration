package Widgetclasses;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Test extends Frame {
       
   public Test(){
      super("Java AWT Examples");
      prepareGUI();
   }

   public static void main(String[] args){
      Test  awtGraphicsDemo = new Test();  
      awtGraphicsDemo.setVisible(true);
   }

   private void prepareGUI(){
      setSize(400,400);
      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      }); 
   }    

   @Override
   public void paint(Graphics g) {
      Ellipse2D shape = new Ellipse2D.Float();
      shape.setFrame(100, 150, 200,100);
      Graphics2D g2 = (Graphics2D) g; 
      g2.draw (shape);


   }
}