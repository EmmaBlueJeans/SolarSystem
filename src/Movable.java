// Anything that flies in a straight line (asteroids, comets) and can be pushed around.
public interface Movable {
    double getDx();
    double getDy();
    void setDx(double dx);
    void setDy(double dy);
}
