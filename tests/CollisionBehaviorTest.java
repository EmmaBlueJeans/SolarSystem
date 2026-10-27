public class CollisionBehaviorTest {
    public static void main(String[] args) {
        testAsteroidCometOrderIndependence();
        testAsteroidBouncesOffStar();
        testSweptHeadOnCollision();
        System.out.println("Collision behavior checks passed.");
    }

    private static void testAsteroidCometOrderIndependence() {
        Asteroid asteroid = new Asteroid(100, 100, 1, 0);
        Comet comet = new Comet(109, 100, -1, 0);
        GalaxySimulation simulation = new GalaxySimulation();
        simulation.addObject(asteroid); // Original list ordering: asteroid before comet
        simulation.addObject(comet);
        simulation.update();

        require(asteroid.getDx() < 1, "Asteroid/comet collision did not affect asteroid velocity");
        require(comet.getDx() > -1, "Asteroid/comet collision did not affect comet velocity");
    }

    private static void testAsteroidBouncesOffStar() {
        Star star = new Star(100, 100);
        Asteroid asteroid = new Asteroid(80, 100, 5, 0);
        GalaxySimulation simulation = new GalaxySimulation();
        simulation.addObject(star);
        simulation.addObject(asteroid);
        simulation.update();

        require(asteroid.getDx() < 0, "Asteroid did not bounce away from star");
    }

    private static void testSweptHeadOnCollision() {
        Asteroid left = new Asteroid(0, 100, 60, 0);
        Asteroid right = new Asteroid(100, 100, -60, 0);
        GalaxySimulation simulation = new GalaxySimulation();
        simulation.addObject(left);
        simulation.addObject(right);
        simulation.update();

        require(left.getDx() < 0, "Swept collision did not reverse left object");
        require(right.getDx() > 0, "Swept collision did not reverse right object");
        double distance = Math.hypot(left.getX() - right.getX(), left.getY() - right.getY());
        require(distance >= 10.0 - 0.0001, "Objects remained overlapped after collision");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
