package solution;

import sim.Message;
import util.ConfigurationInfo;

import java.util.*;

public class MyDisasterResponder extends DisasterResponder {

    private MyGraph graph;
    private Queue<Integer> rescueQueue;

    private boolean[] vehicleAvailable;
    private int[] vehicleLocation;

    private int baseLocation;
    private int totalVehicles = 20;

    private int totalSaved = 0;

    public MyDisasterResponder() {

        rescueQueue = new LinkedList<>();
        vehicleAvailable = new boolean[totalVehicles + 1];
        vehicleLocation = new int[totalVehicles + 1];

        for (int i = 1; i <= totalVehicles; i++) {
            vehicleAvailable[i] = true;
            vehicleLocation[i] = 1;
        }

        System.out.println("Responder Started");
    }

    @Override
    protected void setup() {

        try {
            String mapFile = ConfigurationInfo.getMapFile(configFile);
            baseLocation = Integer.parseInt(ConfigurationInfo.getOrigin(configFile));

            graph = (MyGraph) GraphBuilder.buildFromGraphML(mapFile);

            System.out.println("Map Loaded. Base = " + baseLocation);

        } catch (Exception e) {
            System.out.println("Setup Error: " + e.getMessage());
        }
    }

    @Override
    protected void handle(Message msg) {

        String text = msg.text;

        if (text.startsWith("RESCUE")) {
            handleRescue(text);
        }

        else if (text.startsWith("VEHICLE")) {
            handleVehicle(text);
        }

        else if (text.startsWith("ROAD")) {
            handleRoad(text);
        }

        else if (text.startsWith("LOCATION")) {
            handleLocation(text);
        }
    }
    private void handleRescue(String msg) {

        String[] parts = msg.split("\\|");
        int location = Integer.parseInt(parts[2]);

        rescueQueue.add(location);
        dispatch();
    }

    private void dispatch() {

        if (graph == null) return;

        while (!rescueQueue.isEmpty()) {

            int vehicle = getFreeVehicle();
            if (vehicle == -1) return;

            int destination = rescueQueue.poll();

            int start = vehicleLocation[vehicle];

            List<Integer> path = graph.Dijkstra(start, destination);

            if (path == null || path.size() < 2) {
                rescueQueue.add(destination);
                return;
            }

            sendPath(vehicle, path);

            vehicleAvailable[vehicle] = false;
        }
    }

    private int getFreeVehicle() {
        for (int i = 1; i <= totalVehicles; i++) {
            if (vehicleAvailable[i]) return i;
        }
        return -1;
    }

    private void sendPath(int vehicle, List<Integer> path) {

        StringBuilder sb = new StringBuilder();

        sb.append("PATH|VEHICLE|")
                .append(vehicle)
                .append("|WAYPOINTS|");

        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i != path.size() - 1) sb.append(",");
        }

        try {
            outMessageQueue.put(new Message(sb.toString()));
        } catch (Exception e) {
            System.out.println("Send Error: " + e.getMessage());
        }
    }

    private void handleVehicle(String msg) {

        String[] parts = msg.split("\\|");
        int vehicle = Integer.parseInt(parts[1]);

        if (msg.contains("ARRIVED")) {
            int location = Integer.parseInt(parts[4]);
            vehicleLocation[vehicle] = location;
            // 1 = b (Arrived);
        }

        else if (msg.contains("RETURNED")) {

            int rescued = Integer.parseInt(parts[parts.length - 1]);
            totalSaved += rescued;

            vehicleAvailable[vehicle] = true;
            vehicleLocation[vehicle] = baseLocation;

            System.out.println("VEHICLE " + vehicle +
                    " RETURNED | SAVED: " + rescued +
                    " | TOTAL: " + totalSaved);

            dispatch();
        }

        else if (msg.contains("HALTED")) {
            vehicleAvailable[vehicle] = true;
        }

        else if (msg.contains("DESTROYED")) {
            vehicleAvailable[vehicle] = false;
            System.out.println("Vehicle destroyed: " + vehicle);
        }
    }

    private void handleRoad(String msg) {
        // extend later
    }

    private void handleLocation(String msg) {
        // extend later
    }
}