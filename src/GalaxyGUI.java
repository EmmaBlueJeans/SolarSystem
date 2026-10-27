import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Emma Dennis
 * CIS142 - Final Project
 * 5/29/2025
 * 
 */
/*
 * The visual display - the GUI file
 * The GUI acts like a projector asking the model for the list of objects.
 * it loops through the list and asks each object to draw itself
 * 
 * It will use a timer to advance the simulation
 * ask the model for the list of celestial objects
 * call draw() on each object to render it
 * provide basic controls - start, pause, etc...)
 * movement is now based on pixel space, not a 2D Grid....
 */

  public class GalaxyGUI extends JPanel {
   private GalaxySimulation model;
   private Timer timer;
   
    
    //constructor
    public GalaxyGUI(GalaxySimulation model) {
      this.model = model;
      setPreferredSize(new Dimension(800, 600));  //can change the size;
      setBackground(Color.BLACK);

      
      //set up timer
      timer = new Timer(100, e -> {
         model.update();   //update all objects positions
         repaint();        //and redraw everything
      });
      timer.start();
   }
          
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      Graphics2D g2d = (Graphics2D) g; //allows for rotations/scaling  
      
      //draw objects from the model
      for (Celestial obj : model.getObjects()) {
         if (!obj.isDestroyed()) {   //add safety check in case of collisions
         obj.draw(g2d);  //ask each object to draw itself
         }
      }
   }

   //may not be needed -we can add buttons if we want this as an option for the users...
   public void pause() {
      timer.stop();
   }

   public void resume() {
      timer.start();
   }

   }