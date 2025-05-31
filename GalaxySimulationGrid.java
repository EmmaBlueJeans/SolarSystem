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
    
    public GalaxySimulationGrid(int numStars, int numPlanets, int numMoons, int numAsteroids, int numComets, int numBlackholes) {
        //calculate the grid size based on total object count
        int totalObjects = numStars + numPlanets + numAsteroids + numBlackholes + numComets;
        int gridSize = (int) Math.ceil(Math.sqrt(totalObjects * 4));  //~25% fill
        this.width = gridSize;
        this.height = gridSize;
    

        //Create the grid
        grid = new Celestial[width][height];

        //Place the objects
        Random rand = new Random();
        placeStars(numStars, rand);
        placePlanets(numPlanets, rand);
        placeAsteroids(numAsteroids, rand);
        placeBlackHoles(numBlackholes, rand);
        placeMoons(numMoons, rand);
        placeComets(numComets, rand);

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
            grid[x][y] = new Planet(x, y);
        }
    }

    private void placeMoons(int count, Random rand) {
        for (int i = 0; i < count; i++) {
            //try to find a planet to orbit
            boolean placed = false;
            for (int attempt = 0; attempt < 100 && !placed; attempt++) {
                int x = rand.nextInt(width);
                int y = rand.nextInt(height);
                if (grid[x][y] instanceof Planet) {
                    //try to place moon in adjacent cell
                    int[][] direction = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                    for (int[] d : direction) {
                        int nx = x + d[0];
                        int ny = y + d[1];
                        if (inBounds(nx, ny) && grid[nx][ny] == null) {
                            grid[nx][ny] = new Moon(nx, ny, (Planet) grid[x][y]);
                            placed = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    //place Asteroids, Black holes with similar logic

    private void placeAsteroids(int count, Random rand) {
        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                //randomly choose edge positions
                if (rand.nextBoolean()) {
                    x = rand.nextBoolean() ? 0 : width -1;
                    y = rand.nextInt(height);
                } else {
                    x = rand.nextInt(width);
                    y = rand.nextBoolean() ? 0 : height -1;
                }
            } while (grid[x][y] != null);

            grid[x][y] = new Asteroid(x, y);
        }
    }

    private void placeComets(int count, Random rand) {
        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                //randomly choose edge positions
                if (rand.nextBoolean()) {
                    x = rand.nextBoolean() ? 0 : width -1;
                    y = rand.nextInt(height);
                } else {
                    x = rand.nextInt(width);
                    y = rand.nextBoolean() ? 0 : height -1;
                }
            } while (grid[x][y] != null);

            grid[x][y] = new Comet(x, y);
        }
    }

    //black holes - not placed in center
    private void placeBlackHoles(int count, Random rand) {
    for (int i = 0; i < count; i++) {
        int x, y;
        do {
            x = rand.nextInt(width);
            y = rand.nextInt(height);
        } while ((Math.abs(x - width/2) < 3 && Math.abs(y - height/2) < 3) || grid[x][y] != null);

        grid[x][y] = new BlackHole(x, y);
    }
}

    public void update() {
        //create a new 2D grid for updated positions
        Celestial[][] newGrid = new Celestial[width][height];
        //loop over grid and call update() on each object
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Celestial obj = grid[x][y];
                 if (obj != null) {
                    obj.update(this);  // update its (x, y) internally
                    int newX = obj.getX();
                    int newY = obj.getY();
                    if (inBounds(newX, newY)) {
                        if (newGrid[newX][newY] == null) {
                            newGrid[newX][newY] = obj;
                         } else {
                    resolveCollision(obj, newGrid[newX][newY]);
                    //still need to define the object classes (plnet, star etc) to define update logic
                    //planes orbit
                    //asteroids/comets drift in
                    //black holes stay put but pull nearby.
                }
            }
        }
    }
}
    grid = newGrid;

    private void resolveCollision(Celestial obj1, Celestial obj2) {
    // Later: use instanceof to decide who wins
    //need to implement collisions rules for:
    //planet hits asteroid - asteroid destroyed
    //asteroid hits planet- 50/50 change
    //comet hits anything - both destroyed
    //Black hole eats up to ??? 
    }

    //helper method for inBounds
    private boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
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