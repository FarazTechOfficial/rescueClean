# Rescue Clean

This is a disaster rescue simulator. It sends vehicles to save people after disasters happen.

## What It Does

- A disaster happens on a map (like an earthquake)
- People need help at different places
- The program sends rescue vehicles to save them
- Vehicles find the shortest road to reach people
- Then they bring people back to safety

## How It Works

1. The simulator loads a map with roads and locations
2. Rescue requests come in from different places
3. The program picks a free vehicle
4. It finds the best path using Dijkstra or A* algorithm
5. The vehicle goes there, saves people, and comes back

## Project Structure

```
rescueClean/
  src/
    sim/        - Simulator code (runs everything)
    solution/   - Our rescue logic
    util/       - Helper tools
  cfg/          - Settings file
  data/         - Map files (GraphML)
  lib/          - Libraries we use
```

## How to Run

1. Open this project in IntelliJ IDEA
2. Run `sim.Simulator` as the main class
3. Watch vehicles go save people!

## Settings

You can change settings in `cfg/sim.cfg`. To change the path finding algorithm, set `ALGORITHM=ASTAR` instead of `ALGORITHM=DIJKSTRA` in the config file.

| Setting | What it does |
|---------|-------------|
| MAP | Which map to use |
| ALGORITHM | Path finding method: `DIJKSTRA` or `ASTAR` |
| NUM_RESCUES | Number of rescue vehicles |
| VEHICLE_SPEED | How fast vehicles move |
| DURATION | How long the simulation runs |

## Technologies

- Java
- JDOM (for reading map files)
- Dijkstra algorithm (for shortest path)
