import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Random;

public class Asteroid extends Celestial implements Movable {
    private double dx, dy, rotationAngle, rotationSpeed;
    private static final Random random = new Random();
    
    public Asteroid(double x, double y) {
        super(x, y, 20, 10);
        init();
        initializeFromEdge();
        initializeMovement();
    }
    
    public Asteroid(double x, double y, double dx, double dy) {
        super(x, y, 20, 10);
        this.dx = dx;
        this.dy = dy;
        init();
    }
    
    private void init() {
        this.rotationAngle = random.nextDouble() * 360;
        this.rotationSpeed = (random.nextDouble() - 0.5) * 4;
    }
    
    private void initializeFromEdge() {
        int edge = random.nextInt(4);
        double[] pos = {random.nextDouble() * 800, -getSize(), 800 + getSize(), random.nextDouble() * 600,
                       random.nextDouble() * 800, 600 + getSize(), -getSize(), random.nextDouble() * 600};
        setX(pos[edge * 2]);
        setY(pos[edge * 2 + 1]);
    }
    
    private void initializeMovement() {
        double dirX = 400 - getX(), dirY = 300 - getY();
        double dist = Math.sqrt(dirX * dirX + dirY * dirY);
        double speed = 1.0 + random.nextDouble() * 2.0;
        
        this.dx = (dirX / dist) * speed + (random.nextDouble() - 0.5) * 0.5;
        this.dy = (dirY / dist) * speed + (random.nextDouble() - 0.5) * 0.5;
    }
    
    @Override
    public void update() {
        setX(getX() + dx);
        setY(getY() + dy);
        rotationAngle = (rotationAngle + rotationSpeed + 360) % 360;
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getX(), getY());
        g2d.rotate(Math.toRadians(rotationAngle));
        
        // Draw asteroid shape
        int s = getSize();
        g2d.setColor(new Color(139, 69, 19));
        int[] xPoints = {-s/2, -s/3, s/3, s/2, s/3, -s/4};
        int[] yPoints = {-s/3, -s/2, -s/2, 0, s/2, s/2};
        g2d.fillPolygon(xPoints, yPoints, 6);
        
        // Add craters
        g2d.setColor(new Color(101, 67, 33));
        g2d.fillOval(-s/4, -s/6, s/3, s/4);
        g2d.fillOval(s/6, s/8, s/4, s/4);
        
        g2d.rotate(-Math.toRadians(rotationAngle));
        g2d.translate(-getX(), -getY());
    }
    
    // Collision detection
    public boolean collidesWith(Celestial other) {
        double distance = Math.sqrt(Math.pow(getX() - other.getX(), 2) + Math.pow(getY() - other.getY(), 2));
        return distance < (getSize() + other.getSize()) / 2;
    }
    
    // Handle collision response (bounce off each other)
    public void handleCollision(Asteroid other) {
        // Simple elastic collision - swap velocities
        double tempDx = this.dx;
        double tempDy = this.dy;
        this.dx = other.dx;
        this.dy = other.dy;
        other.dx = tempDx;
        other.dy = tempDy;
    }
    
    public double getDx() { return dx; }
    public double getDy() { return dy; }
    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }
}
