

import java.awt.*;

// A Planet class extends the Orbit class, which is a subclass of Celestial
public class Planet extends Orbit{
    
    // A Planet constructor which calls the super for the star's x and y coordinates, the orbit's radius and speed, and mass 850 and size 20
    // Grabs star's coordinates in order to know the center point and orbit around it
    public Planet(Star star, double radius, double speed) {
        super(star.getX(), star.getY(), radius, speed, 850, 20);
    }
    
    // An overriden draw method which draws out a circular red planet 
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval((int)(getX() - getSize() / 2), (int)(getY() - getSize() / 2), getSize(), getSize());
    }
    
    // An overriden getType method which returns the String of the type "Planet"
    @Override
    public String getType() {
        return "Planet";
    }
}
