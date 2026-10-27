# Solar System Simulation

An interactive galaxy simulation developed in Java for the CIS 142 final project at Edmonds College. The program lets a user assemble a galaxy from stars, planets, moons, asteroids, comets, and black holes, then launches a Swing visualization that animates their movement and interactions.

![Solar System simulation showing stars, planets, moons, asteroids, comets, and a black hole](assets/solar-system-simulation.png)

## What it does

1. **Build a galaxy** — use a console menu to add celestial objects within defined limits.
2. **Create orbital systems** — planets orbit a star, and moons follow a moving planet.
3. **Animate independent motion** — asteroids and comets enter from the edges and travel through the simulation.
4. **Model interactions** — moving objects use simplified collision responses, while black holes attract nearby asteroids and comets and remove objects that cross the event horizon.
5. **Render the result** — a Java Swing interface redraws the simulation on a timer.

## Technologies and concepts

- Java
- Java Swing and AWT
- Object-oriented programming
- Inheritance and polymorphism
- Encapsulation
- Collections and object lifecycle management
- Event-driven programming with Swing `Timer`
- 2D coordinate math and circular motion
- Swept collision detection and simplified physics
- Deterministic collision behavior checks

## Project structure

```text
src/
├── GalaxyMain.java        # Console menu, object creation, and application entry point
├── GalaxySimulation.java  # Simulation state, updates, collisions, and gravity
├── GalaxyGUI.java         # Swing rendering panel and animation timer
├── Celestial.java         # Base class for all celestial objects
├── Orbit.java             # Reusable circular-orbit behavior
├── Movable.java           # Shared velocity contract for asteroids and comets
├── Star.java
├── Planet.java
├── Moon.java
├── Asteroid.java
├── Comet.java
└── BlackHole.java

tests/
└── CollisionBehaviorTest.java  # Deterministic collision regression checks
```

## Clone and run locally

**Prerequisites:**

- Git
- Java Development Kit (JDK) 17 or later
- A desktop environment capable of displaying a Java Swing window

Confirm that both Java commands are available:

```text
java -version
javac -version
```

### Windows PowerShell

```powershell
git clone https://github.com/EmmaBlueJeans/SolarSystem.git
cd SolarSystem
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem src -Filter *.java).FullName
java -cp out GalaxyMain
```

### macOS or Linux

```bash
git clone https://github.com/EmmaBlueJeans/SolarSystem.git
cd SolarSystem
mkdir -p out
javac -d out src/*.java
java -cp out GalaxyMain
```

If Git is unavailable, use GitHub's **Code → Download ZIP** option, extract the archive, open a terminal in the extracted folder, and begin with the appropriate compile command above.

### Using the simulation

1. Use the numbered console menu to add celestial objects.
2. Add at least one star before adding planets or launching the simulation.
3. Add a planet before adding moons.
4. Select **Simulate Current Galaxy** to open the animated Swing window.
5. Closing the visualization window exits the application.

## Testing

The GitHub Actions workflow compiles the complete project with JDK 17 and runs deterministic checks for:

- Asteroid–comet collisions regardless of list order
- Incoming objects bouncing off orbiting or fixed bodies
- Fast, head-on objects being detected between animation frames
- Collision separation preventing objects from remaining overlapped

Run the checks locally with:

```bash
javac -d out src/*.java tests/*.java
java -cp out CollisionBehaviorTest
```

## Engineering decisions

### Shared object hierarchy

All simulation objects inherit position, mass, size, drawing, and lifecycle behavior from `Celestial`. `Orbit` adds reusable circular-motion logic for planets and moons. This kept common behavior centralized while allowing each subclass to control its own appearance and movement.

### Separate model and view

`GalaxySimulation` owns the object collection and advances the simulation, while `GalaxyGUI` handles rendering. The GUI asks each object to draw itself, using polymorphism instead of a large type-specific rendering method.

### Two movement models

Planets and moons use angle-based circular motion around a changing center. Asteroids and comets instead use velocity vectors so they can enter from outside the display and respond to simplified collisions or gravitational pull.

### Timer-driven animation

A Swing `Timer` updates the model and repaints the panel every 100 milliseconds. This keeps animation within Swing's event system and avoids managing a separate rendering thread.

### Collision and interaction model

Asteroids and comets implement a shared `Movable` interface so the simulation can handle moving-object collisions without depending on their order in the object list. A swept closest-approach check detects collisions that occur between animation frames. Moving objects use a mass-based elastic response and are repositioned after impact to prevent sticking or repeated jitter.

Stars, planets, and moons remain on fixed tracks and are treated as effectively immovable during impact: an incoming asteroid or comet bounces away while the orbiting body continues on its path. Black holes use a separate simplified force calculation and destroy objects that cross the event horizon. These choices prioritize visible interaction and object-oriented design rather than physical accuracy.

## Known limitations

- Stars, planets, and moons remain on predetermined tracks; impacts redirect the incoming object but do not alter an orbit.
- Multi-object pileups are resolved pair by pair and can still produce imperfect results in rare crowded interactions.
- New planets orbit the first star, and new moons orbit the first planet.
- Console input expects a valid number and does not yet recover from non-numeric input.
- The project does not currently have a browser-based demonstration.

## Project status

The original course project has been preserved and refined as a portfolio project. The application compiles automatically in GitHub Actions, includes deterministic collision checks, and implements the major object, orbit, collision, gravity, and rendering behaviors. A browser-based demonstration and more advanced multi-body physics remain possible future improvements.

