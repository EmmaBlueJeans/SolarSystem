

import java.awt.*;

// Uses a Moon class which orbits a planet
// A Moon class extends the Orbit class, which is a subclass of Celestial
public class Moon extends Orbit {
    private Planet planet;
    
    // A Moon constructor which calls the super for the planet's x and y coordinates, the orbit's radius and speed, and mass 160 and size 10
    // Grabs planet's coordinates in order to know the center point of the planet and orbit around it
    public Moon(Planet planet, double radius, double speed) {
        super(planet.getX(), planet.getY(), radius, speed, 160, 10);
        
        this.planet = planet;
    }
    
    // An overriden update method which sets center point of the planet so that the moon orbits around it
    // calls the super to update the moon's position
    @Override
    public void update() {
        setCenter(planet.getX(), planet.getY());
        super.update();
    }
    
    // An overriden draw method which draws out a circular white moon
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval((int)(getX() - getSize() / 2), (int)(getY() - getSize() / 2), getSize(), getSize());
    }
    
    // An overriden getType method which returns the String of the type "Moon"
    @Override
    public String getType() {
        return "Moon";
    }
}
