/*
 * The brains of the simulation - this file will sotre the grid of celestial objects (Celestial[][] grid)
 * it will place the objects at the beginning of the simulation
 * it will advance the sijmulation each tick (by calling each objects update())
 * and will provide methods for the GUI to access the grid
 */
import java.util.Random;

public class GalaxySimulationGrid {
    private Celestial[][] grid;
    private int width;
    private int height;
    
    public GalaxySimulationGrid(int numbStars, int numPlanets, int numAsteroids, in numBlackholes) {
        //calculate the grid size based on total object count
        int totalObjects = numStars + numPlanets + numAsteroids + numBlackHoles;
        int igridSize = (int) Math.ceil(Math.sqrt(totalObjects * 4));  //~25% fill
        this.width = gridSize;
        this.height = gridSize;
    

        //Create the grid
        grid = new Celestial[width][height];

        //Place the objects
        Random rand = new Random();
        placeStars(numStars, rand);
        placePlanets(numPlanets, rand);
        placeAsteroids(numAsteroids, rand);
        placeBlackHoles(numBlackHoles, rand);

}

    //Example placement methods:
    private void placeStars(int count, Random rand) {
        //place in center-ish location
        for (int i = 0; i < count; i++) {
            int x = width / 2 + rand.nextInt(3) -1;
            int y = height / 2 + rand.nextInt(3) -1;
            grid[x][y] = new Star(x, y);
        }
    }    

    private void placePlanets(int count, Random rand) {
        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                x = rand.nextInt(width);
                y = rand.nextInt(height);
            } while (grid[x][y] != null);
            grid[x][y] = new Planet(x, y)
        }
    }

    //place Asteroids, Black holes with similar logic
    public void update() {
        //loop over grid and call update() one ach object
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y] != null) {
                    grid[x][y].update(this);
                }
            }
        }
        //placeholder - handle collisions here
    }

    //Accessors for GUI
    public Celestial getObjectAt(int x, int y) {
        return grid[x][y];
    }

    public int getGridWidth() {
        return width;
    }

    public int getGridHeight() {
        return height;
    }


}