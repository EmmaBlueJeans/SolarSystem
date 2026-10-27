import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;


public class Comet extends Celestial implements Movable {
    private double dx, dy, tailLength;
    private static final Random random = new Random();
    
    public Comet() {
        super(0, 0, 0, 0); //we'll initialize size and mass below

        int size = 6 + random.nextInt(8);
        double mass = size * 0.3;
        this.tailLength = size * 3;

        // Use setters to update inherited fields
        setSize(size);
        setMass(mass);

        initializeFromEdge();
        initializeMovement();
    }
    
    public Comet(double x, double y, double dx, double dy) {
        super(x, y, 0, 0); // Placeholder for mass/size
        int newSize = 6 + random.nextInt(8);
        double newMass = newSize * 0.3;
        this.tailLength = newSize * 3;

        setSize(newSize);
        setMass(newMass);

        this.dx = dx;
        this.dy = dy;
    }
    
    private void initializeFromEdge() {
        int edge = random.nextInt(4);
        double[] pos = {
            random.nextDouble() * 800, -getSize(),
            800 + getSize(), random.nextDouble() * 600,
            random.nextDouble() * 800, 600 + getSize(),
            -getSize(), random.nextDouble() * 600
        };
        setX(pos[edge * 2]);
        setY(pos[edge * 2 + 1]);
    }
    
    private void initializeMovement() {
        double dirX = 400 - getX(), dirY = 300 - getY();
        double dist = Math.sqrt(dirX * dirX + dirY * dirY);
        double speed = 2.0 + random.nextDouble() * 3.0;

        this.dx = (dirX / dist) * speed + (random.nextDouble() - 0.5);
        this.dy = (dirY / dist) * speed + (random.nextDouble() - 0.5);
    }
    
    @Override
    public void update() {
       setX(getX() + dx);
       setY(getY() + dy);
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        double x = getX();
        double y = getY();
        int size = getSize();

        // Draw tail
        double tailDx = -dx, tailDy = -dy;
        double tailMag = Math.sqrt(tailDx * tailDx + tailDy * tailDy);
        if (tailMag > 0) {
            tailDx = (tailDx / tailMag) * tailLength;
            tailDy = (tailDy / tailMag) * tailLength;
            
            for (int i = 0; i < 10; i++) {
                double alpha = 1.0 - i / 10.0;
                int tailX = (int)(x + tailDx * i / 10.0);
                int tailY = (int)(y + tailDy * i / 10.0);
                g2d.setColor(new Color(255, 255, 200, (int)(alpha * 100)));
                g2d.fillOval(tailX - (size + i)/2, tailY - (size + i)/2, size + i, size + i);
            }
        }
        
        // Draw comet nucleus
        g2d.setColor(new Color(200, 200, 255));
        g2d.fillOval((int)(x - size/2), (int)(y - size/2), size, size);
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)(x - size/4), (int)(y - size/4), size/2, size/2);
    }
    
    // Collision detection
    public boolean collidesWith(Celestial other) {
        double distance = Math.sqrt(Math.pow(getX() - other.getX(), 2) + Math.pow(getY() - other.getY(), 2));
        return distance < (getSize() + other.getSize()) / 2;
    }
    
    // Handle collision response (bounce off each other)
    public void handleCollision(Comet other) {
        // Simple elastic collision - swap velocities
        double tempDx = this.dx;
        double tempDy = this.dy;
        this.dx = other.dx;
        this.dy = other.dy;
        other.dx = tempDx;
        other.dy = tempDy;
    }
    
    // Handle collision with asteroid (different masses)
    public void handleCollision(Asteroid asteroid) {
        // Conservation of momentum (simplified)
       double totalMass = getMass() + asteroid.getMass();
        double newDx = (getMass() * this.dx + asteroid.getMass() * asteroid.getDx()) / totalMass;
        double newDy = (getMass() * this.dy + asteroid.getMass() * asteroid.getDy()) / totalMass;
        
        this.dx = newDx;
        this.dy = newDy;
        asteroid.setDx(newDx);
        asteroid.setDy(newDy);
    }
    
    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }
}
