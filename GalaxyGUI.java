import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author Emma Dennis
 * CIS142 - Final Project
 * 5/29/2025
 * 
 */
/*
 * The visual display - the GUI file
 * This class will draw the grid and objects
 * will use a timer to animate the simulation
 * relies on the GalaxySimulationGrid for data
 */

  public class GalaxyGUI extends JPanel {
   private GalaxySimulationGrid model;
   private Timer timer;
   private int cellSize = 15; //can adjust later
    
    //constructor
    public GalaxyGUI(GalaxySimulationGrid model) {
      this.model = model;

      //set up timer
      timer = new Timer(100, e -> {
         model.update();
         repaint();
      });
      timer.start();

      //set the size based on model
      int width = model.getGridWidth() * cellSize;
      int height = model.getGridHeight() * cellSize;
      setPreferredSize(new Dimension(width, height));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      for (int x = 0; x < model.getGridWidth(); x++) {
         for (int y = 0; y < model.getGridHeight(); y++) {
            int px = x * cellSize;
            int py = y * cellSize;
            
            Celestial obj = model.getObjectAt(x, y);
            if (obj != null) {
               obj.draw(g, cellSize);
            }
         }
      }      
    
}