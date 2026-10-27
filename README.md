# Solar System Simulation

An interactive galaxy simulation developed in Java for the CIS 142 final project at Edmonds College. The program lets a user assemble a galaxy from stars, planets, moons, asteroids, comets, and black holes, then launches a Swing visualization that animates their movement and interactions.

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
- Basic collision detection and simplified physics

## Project structure

```text
src/
├── GalaxyMain.java        # Console menu, object creation, and application entry point
├── GalaxySimulation.java  # Simulation state, updates, collisions, and gravity
├── GalaxyGUI.java         # Swing rendering panel and animation timer
├── Celestial.java         # Base class for all celestial objects
├── Orbit.java             # Reusable circular-orbit behavior
├── Star.java
├── Planet.java
├── Moon.java
├── Asteroid.java
├── Comet.java
└── BlackHole.java
```

## Run locally

**Prerequisite:** Java Development Kit (JDK) 17 or later.

From the repository root:

```bash
mkdir out
javac -d out src/*.java
java -cp out GalaxyMain
```

Use the numbered console menu to add objects. Add at least one star before selecting **Simulate Current Galaxy**. Closing the visualization window exits the application.

## Engineering decisions

### Shared object hierarchy

All simulation objects inherit position, mass, size, drawing, and lifecycle behavior from `Celestial`. `Orbit` adds reusable circular-motion logic for planets and moons. This kept common behavior centralized while allowing each subclass to control its own appearance and movement.

### Separate model and view

`GalaxySimulation` owns the object collection and advances the simulation, while `GalaxyGUI` handles rendering. The GUI asks each object to draw itself, using polymorphism instead of a large type-specific rendering method.

### Two movement models

Planets and moons use angle-based circular motion around a changing center. Asteroids and comets instead use velocity vectors so they can enter from outside the display and respond to simplified collisions or gravitational pull.

### Timer-driven animation

A Swing `Timer` updates the model and repaints the panel every 100 milliseconds. This keeps animation within Swing's event system and avoids managing a separate rendering thread.

### Simplified interaction model

The project uses distance-based collision detection, basic velocity exchanges, and an intentionally simplified black-hole force calculation. These choices kept the final project focused on object-oriented design, animation, and interaction rather than attempting a physically accurate orbital simulator.

## Known limitations

- Orbiting stars, planets, and moons follow predetermined paths and do not currently respond correctly when struck by incoming objects.
- Collision handling is limited to asteroids and comets, and the comet–asteroid case needs to be made independent of object ordering.
- New planets orbit the first star, and new moons orbit the first planet.
- Console input expects a valid number and does not yet recover from non-numeric input.
- The project currently has no automated tests or browser-based demonstration.

## Project status

The original course project is preserved and documented as a portfolio project. The application architecture and major object behaviors are implemented; collision behavior is the primary area identified for future refinement.

