import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Random;

public class BlackHole extends Celestial {
    private double pullStrength, eventHorizonRadius, influenceRadius, rotationAngle;
    private static final Random random = new Random();
    
    public BlackHole(String name) {
        super(random.nextDouble() * 800, random.nextDouble() * 600, 0, 0); // Random position, mass/size set in init()
        init();
    }
    
    public BlackHole(double x, double y) {
        super(x, y, 0, 0);
        init();
    }
    
   private void init() {
    int newSize = 25 + random.nextInt(15);
    double newMass = newSize * 5.0;

    setSize(newSize);  // 🛠️ Use the setter
    setMass(newMass);

    this.pullStrength = newMass * 0.1;
    this.eventHorizonRadius = newSize * 0.6;
    this.influenceRadius = newSize * 8;
    this.rotationAngle = 0;
}

 
    @Override
    public void update() {
        rotationAngle = (rotationAngle + 2.0) % 360;
    }
    
    public void applyGravitationalPull(Celestial other) {
        if (other == this || other.isDestroyed()) return;
        
        double dx = getX() - other.getX();
        double dy = getY() - other.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= eventHorizonRadius) {
            other.setDestroyed(true);
            return;
        }

        if (distance <= influenceRadius && distance > 0) {
            double pullForce = pullStrength / (distance * distance);
            double dirX = dx / distance;
            double dirY = dy / distance;

            if (other instanceof Asteroid) {
                Asteroid a = (Asteroid) other;
                a.setDx(a.getDx() + dirX * pullForce);
                a.setDy(a.getDy() + dirY * pullForce);
            } else if (other instanceof Comet) {
                Comet c = (Comet) other;
                c.setDx(c.getDx() + dirX * pullForce);
                c.setDy(c.getDy() + dirY * pullForce);
            }
        }
    }

    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getX(), getY());
        g2d.rotate(Math.toRadians(rotationAngle));

        // Accretion disk
        for (int i = 3; i >= 1; i--) {
            g2d.setColor(new Color(255, 100, 0, 50 + i * 30));
            int diskSize = getSize() * (i + 1);
            g2d.fillOval(-diskSize / 2, -diskSize / 2, diskSize, diskSize);
        }

        // Black hole and event horizon
        g2d.setColor(Color.BLACK);
        g2d.fillOval(-getSize() / 2, -getSize() / 2, getSize(), getSize());
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.drawOval(-getSize() / 2, -getSize() / 2, getSize(), getSize());

        g2d.rotate(-Math.toRadians(rotationAngle));
        g2d.translate(-getX(), -getY());
    }
    public String getType() {
        return "BlackHole";
    }
    
}
