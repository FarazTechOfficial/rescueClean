# Rescue Clean

A disaster recovery system. After an earthquake, rescue vehicles are sent out to save people who are trapped inside buildings.

## The Scenario

After a disaster, roads get blocked and buildings collapse. We know the original city map, but the real road network is damaged and partly unknown. Our job is to send autonomous rescue vehicles to reach trapped people and bring them back to base safely.

Key points:

- Some roads are blocked and can't be used
- Some buildings collapse and can't be entered
- A vehicle that goes through a collapsed location is destroyed
- Vehicles tell us when they arrive, halt, or return
- We only have a limited number of vehicles
- The goal is to rescue as many people as possible before time runs out

## What Our Code Does

1. Reads the map file and builds a graph of roads and intersections
2. Listens for rescue requests coming from the simulator
3. Picks a free vehicle for each mission
4. Finds the best path using Dijkstra or A* algorithm
5. Sends the vehicle along that path
6. Tracks each vehicle's location and status
7. Dispatches again when a vehicle returns to base

## Project Structure

```
rescueClean/
  src/
    sim/        - Simulator framework (runs the scenario)
    solution/   - Our rescue logic (the responder)
    util/       - Helper classes and config tools
  cfg/          - Simulation settings (sim.cfg)
  data/         - Map files in GraphML format
  lib/          - Libraries the project needs
```

## How to Run

1. Open this project in IntelliJ IDEA
2. Run `sim.Simulator` as the main class (memory limit `-Xmx2G` is already set)
3. Watch the vehicles go out and rescue people!

## Settings

All settings live in `cfg/sim.cfg`. Here is what each one controls:

| Setting | What it does |
|---------|-------------|
| MAP | Which map to use (0 to 5, small to large) |
| ALGORITHM | Path finder: `DIJKSTRA` or `ASTAR` |
| RESPONDER_CLASS | The responder class to run |
| DURATION | Total length of the simulation |
| STARTUP_PERIOD | Grace period for setup before messages arrive |
| VEHICLE_SPEED | How fast vehicles move |
| STDOUT_MESSAGES | Show simulator messages on screen (true/false) |
| NUM_RESCUES | Total number of rescue requests |
| ROAD_DAMAGE | How many roads are blocked (0.0 to 1.0) |
| LOCATION_DAMAGE | How many locations collapse (0.0 to 1.0) |
| RESCUE_DURATION | Time before a rescue location collapses |

To switch the path finding algorithm, change `ALGORITHM=DIJKSTRA` to `ALGORITHM=ASTAR` in the config.

## Messages

The responder and simulator talk using fixed message formats:

**Sent to the simulator:**
- `PATH|VEHICLE|vehicleNo|WAYPOINTS|list` - send a vehicle along a path
- `HALT|VEHICLE|vehicleNo` - stop a vehicle

**Received from the simulator:**
- `RESCUE|LOCATION|loc|PEOPLE|n` - a new rescue request
- `ROAD|FROM|a|TO|b|STATUS|status` - road is CLEAR or BLOCKED
- `LOCATION|loc|COLLAPSED` - a location has collapsed
- Vehicle arrived, halted, returned, or destroyed updates

## Technologies

- Java
- JDOM (for reading GraphML map files)
- Dijkstra algorithm (default path finding)
- A* algorithm (alternative path finding)
