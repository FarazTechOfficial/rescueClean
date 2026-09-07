
package solution;

import java.util.*;

public class MyGraph implements Graph {

    int v;
    int[][] graph;

    public MyGraph(int v) {
        this.v = v;
        graph = new int[v][v];
    }

    public void addEdge(int source, int target, int weight) {
        graph[source][target] = weight;
        graph[target][source] = weight;
    }
 public List<Integer> Dijkstra(int source, int destination) {

        int[] distance = new int[v];
        boolean[] visited = new boolean[v];
        int[] path = new int[v];

        for (int i = 0; i < v; i++) {
            distance[i] = Integer.MAX_VALUE;
            visited[i] = false;
            path[i] = -1;
        }

        distance[source] = 0;

        for (int count = 0; count < v - 1; count++) {

            int min = Integer.MAX_VALUE;
            int u = -1;

            for (int i = 0; i < v; i++) {
                if (!visited[i] && distance[i] < min) {
                    min = distance[i];
                    u = i;
                }
            }

            if (u == -1) break;

            visited[u] = true;

            for (int j = 0; j < v; j++) {

                if (!visited[j] && graph[u][j] != 0 && distance[u] != Integer.MAX_VALUE) {

                    int newDist = distance[u] + graph[u][j];

                    if (newDist < distance[j]) {
                        distance[j] = newDist;
                        path[j] = u;
                    }
                }
            }
        }

        List<Integer> result = new ArrayList<>();

        if (distance[destination] == Integer.MAX_VALUE) {
            return result;
        }

        for (int vtx = destination; vtx != -1; vtx = path[vtx]) {
            result.add(0, vtx);
        }

        return result;
    }

    @Override
    public void getEdge(int i) {
    }
}