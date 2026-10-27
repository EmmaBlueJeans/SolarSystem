import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * The "Brains" of the galaxy. Contains/manages all celestial objects.
 *
 * Collision rules:
 *   Movable  vs Movable  -> mass-based elastic bounce (only if approaching)
 *   Movable  vs Orbiter  -> Movable bounces off; the orbiter stays on its track
 *                           (Planets, Moons and Stars are on "rails", so we treat
 *                           them as infinitely heavy)
 *   BlackHole vs anything -> gravitational pull / swallow
 *
 * A swept test (closest approach during the tick) is used so fast objects
 * can't skip through each other between frames.
 */
public class GalaxySimulation {
    private List<Celestial> objects;
    private final Map<Celestial, double[]> prevPos = new HashMap<>();

    public GalaxySimulation() {
        objects = new ArrayList<>();
    }

    public void addObject(Celestial obj) {
        objects.add(obj);
    }

    public List<Celestial> getObjects() {
        return objects;
    }

    public void update() {
        // 1. Remember where everything was, then move everything
        prevPos.clear();
        for (Celestial obj : objects) {
            prevPos.put(obj, new double[]{obj.getX(), obj.getY()});
        }
        for (Celestial obj : objects) {
            obj.update();
        }

        // 2. Check every pair once
        for (int i = 0; i < objects.size(); i++) {
            Celestial a = objects.get(i);
            if (a.isDestroyed()) continue;

            for (int j = i + 1; j < objects.size(); j++) {
                Celestial b = objects.get(j);
                if (b.isDestroyed()) continue;

                if (a instanceof BlackHole) {
                    ((BlackHole) a).applyGravitationalPull(b);
                } else if (b instanceof BlackHole) {
                    ((BlackHole) b).applyGravitationalPull(a);
                } else if (a instanceof Movable && b instanceof Movable) {
                    if (sweptHit(a, b)) bounceMovables(a, b);
                } else if (a instanceof Movable && isOrbiter(b)) {
                    if (sweptHit(a, b)) bounceOffOrbiter(a, b);
                } else if (b instanceof Movable && isOrbiter(a)) {
                    if (sweptHit(b, a)) bounceOffOrbiter(b, a);
                }
                // Orbiter vs Orbiter: ignored on purpose (they're on fixed tracks)
            }
        }

        objects.removeIf(Celestial::isDestroyed);
    }

    private boolean isOrbiter(Celestial c) {
        return c instanceof Orbit || c instanceof Star;
    }

    // Velocity this tick, measured from actual movement (works for orbiters too)
    private double[] velocity(Celestial c) {
        double[] p = prevPos.get(c);
        if (p == null) return new double[]{0, 0};
        return new double[]{c.getX() - p[0], c.getY() - p[1]};
    }

    // Did the two objects come within touching distance at any point during this tick?
    private boolean sweptHit(Celestial a, Celestial b) {
        double r = (a.getSize() + b.getSize()) / 2.0;
        double[] pa = prevPos.get(a), pb = prevPos.get(b);
        if (pa == null || pb == null) {
            return Math.hypot(a.getX() - b.getX(), a.getY() - b.getY()) < r;
        }
        // relative position at start (s) and end (e) of the tick
        double sx = pa[0] - pb[0], sy = pa[1] - pb[1];
        double ex = a.getX() - b.getX(), ey = a.getY() - b.getY();
        double mx = ex - sx, my = ey - sy;
        double len2 = mx * mx + my * my;
        double t = len2 == 0 ? 1 : Math.max(0, Math.min(1, -(sx * mx + sy * my) / len2));
        double cx = sx + mx * t, cy = sy + my * t;
        return cx * cx + cy * cy < r * r;
    }

    // Unit vector pointing from b to a
    private double[] normal(Celestial a, Celestial b) {
        double nx = a.getX() - b.getX(), ny = a.getY() - b.getY();
        double d = Math.hypot(nx, ny);
        if (d == 0) return new double[]{1, 0};
        return new double[]{nx / d, ny / d};
    }

    // For a swept collision, the end positions may already be on opposite sides.
    // Use the approach-side normal when the end-position normal would incorrectly
    // make the objects look as though they were already moving apart.
    private double[] collisionNormal(Celestial a, Celestial b) {
        double[] n = normal(a, b);
        Movable ma = (Movable) a;
        double bvx = b instanceof Movable ? ((Movable) b).getDx() : velocity(b)[0];
        double bvy = b instanceof Movable ? ((Movable) b).getDy() : velocity(b)[1];
        double along = (ma.getDx() - bvx) * n[0] + (ma.getDy() - bvy) * n[1];
        if (along < 0) return n;

        double[] pa = prevPos.get(a), pb = prevPos.get(b);
        if (pa == null || pb == null) return n;
        double nx = pa[0] - pb[0], ny = pa[1] - pb[1];
        double d = Math.hypot(nx, ny);
        return d == 0 ? n : new double[]{nx / d, ny / d};
    }

    // Push 'mover' out along n so the two are just touching (prevents sticking/jitter)
    private void separate(Celestial mover, Celestial other, double[] n, double share) {
        double r = (mover.getSize() + other.getSize()) / 2.0;
        double d = Math.hypot(mover.getX() - other.getX(), mover.getY() - other.getY());
        double overlap = r - d;
        if (overlap > 0) {
            mover.setX(mover.getX() + n[0] * overlap * share);
            mover.setY(mover.getY() + n[1] * overlap * share);
        }
    }

    private void bounceMovables(Celestial a, Celestial b) {
        Movable ma = (Movable) a, mb = (Movable) b;
        double[] n = collisionNormal(a, b);
        double rvx = ma.getDx() - mb.getDx(), rvy = ma.getDy() - mb.getDy();
        double along = rvx * n[0] + rvy * n[1];
        if (along < 0) { // only bounce if they're moving toward each other
            double m1 = a.getMass(), m2 = b.getMass();
            double j = -2 * along / (1 / m1 + 1 / m2);
            ma.setDx(ma.getDx() + j / m1 * n[0]);
            ma.setDy(ma.getDy() + j / m1 * n[1]);
            mb.setDx(mb.getDx() - j / m2 * n[0]);
            mb.setDy(mb.getDy() - j / m2 * n[1]);
        }
        double r = (a.getSize() + b.getSize()) / 2.0;
        double midX = (a.getX() + b.getX()) / 2.0;
        double midY = (a.getY() + b.getY()) / 2.0;
        a.setX(midX + n[0] * r / 2.0);
        a.setY(midY + n[1] * r / 2.0);
        b.setX(midX - n[0] * r / 2.0);
        b.setY(midY - n[1] * r / 2.0);
    }

    private void bounceOffOrbiter(Celestial moverObj, Celestial orbiter) {
        Movable m = (Movable) moverObj;
        double[] n = collisionNormal(moverObj, orbiter);
        double[] u = velocity(orbiter);              // the planet/moon is moving too!
        double rvx = m.getDx() - u[0], rvy = m.getDy() - u[1];
        double along = rvx * n[0] + rvy * n[1];
        if (along < 0) {
            // reflect the relative velocity: the orbiter "bats" the mover away
            m.setDx(m.getDx() - 2 * along * n[0]);
            m.setDy(m.getDy() - 2 * along * n[1]);
        }
        double r = (moverObj.getSize() + orbiter.getSize()) / 2.0;
        moverObj.setX(orbiter.getX() + n[0] * r);
        moverObj.setY(orbiter.getY() + n[1] * r);
        // Alternative behavior: moverObj.setDestroyed(true);  // burns up on impact
    }
}
