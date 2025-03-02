// This algorithm is designed to find the minimum number of roads required to transport all packages, given the roads and package locations in a city.
// The city is represented as a graph where nodes are locations, and roads between locations are edges.
// The goal is to determine the fewest steps required to cover all the packages, with each step involving visiting a location and gathering all packages that are within two steps from that location.
// This problem can be viewed as a variation of the "minimum set cover" problem, where we try to find the fewest nodes that can collectively cover all the packages.

import java.util.*;

public class Question_4_b {

    // This class represents a state in the search algorithm with a node, a bitmask representing the packages collected, and the number of steps taken so far.
    private static class State {
        int node;  // Current node (location)
        int mask;  // Bitmask representing which packages are collected (1 for collected, 0 for not collected)
        int steps; // Number of steps taken so far

        // Constructor to initialize a state
        public State(int node, int mask, int steps) {
            this.node = node;
            this.mask = mask;
            this.steps = steps;
        }
    }

    // This function builds an adjacency list from the given roads. Each node (location) will be connected to its neighbors.
    private static List<List<Integer>> buildAdjacencyList(int n, int[][] roads) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] road : roads) {
            int u = road[0], v = road[1];
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        return adj;
    }

    // This function returns all nodes that are within two steps (two hops) of a given node.
    private static Set<Integer> getNodesWithinTwoSteps(int u, List<List<Integer>> adj) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(u);
        visited.add(u);
        int steps = 0;

        while (!queue.isEmpty() && steps < 2) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                for (int neighbor : adj.get(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
            steps++;
        }
        return visited;
    }

    // This function finds the minimum number of roads required to collect all packages.
    // It uses a breadth-first search (BFS) with a state-space representation where each state consists of a node and a bitmask of collected packages.
    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length;
        List<Integer> packageIndices = new ArrayList<>();
        // Collect all package indices (nodes that have a package)
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) packageIndices.add(i);
        }
        // If there are no packages, return 0
        if (packageIndices.isEmpty()) return 0;

        int totalPackages = packageIndices.size();
        int fullMask = (1 << totalPackages) - 1;  // Bitmask representing all packages collected
        List<List<Integer>> adj = buildAdjacencyList(n, roads);  // Build the adjacency list (graph)

        // Precompute the coverage mask for each node, i.e., which packages can be collected by visiting that node
        int[] coverageMasks = new int[n];
        for (int u = 0; u < n; u++) {
            Set<Integer> withinTwoSteps = getNodesWithinTwoSteps(u, adj);  // Get all nodes within two steps of node u
            int mask = 0;
            for (int i = 0; i < totalPackages; i++) {
                if (withinTwoSteps.contains(packageIndices.get(i))) {
                    mask |= (1 << i);  // Set the corresponding bit for packages within two steps
                }
            }
            coverageMasks[u] = mask;  // Store the mask for node u
        }

        int minSteps = Integer.MAX_VALUE;
        // Try starting from each node and perform a BFS to find the minimum number of steps to cover all packages
        for (int start = 0; start < n; start++) {
            int initialMask = coverageMasks[start];
            if (initialMask == fullMask) {
                return 0;  // If starting from this node already covers all packages, no steps are needed
            }

            // Initialize the distance array for each state (node and bitmask)
            int[][] dist = new int[n][1 << totalPackages];
            for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
            Queue<State> queue = new LinkedList<>();
            queue.add(new State(start, initialMask, 0));  // Start BFS from node `start` with the initial mask
            dist[start][initialMask] = 0;

            // Perform BFS to explore all possible states (node, collected packages)
            while (!queue.isEmpty()) {
                State current = queue.poll();
                if (current.node == start && current.mask == fullMask) {
                    minSteps = Math.min(minSteps, current.steps);  // If all packages are collected, update minSteps
                    break;
                }
                // Explore all neighbors of the current node
                for (int neighbor : adj.get(current.node)) {
                    int newMask = current.mask | coverageMasks[neighbor];  // Update the mask with packages collected by visiting `neighbor`
                    int newSteps = current.steps + 1;  // Increment the number of steps
                    if (newSteps < dist[neighbor][newMask]) {
                        dist[neighbor][newMask] = newSteps;
                        queue.add(new State(neighbor, newMask, newSteps));  // Add the new state to the queue
                    }
                }
            }
        }
        return minSteps;  // Return the minimum number of steps found
    }

    public static void main(String[] args) {
        // Test case 1
        int[] packages1 = {1, 0, 0, 0, 0, 1};
        int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
        System.out.println("Test Case 1 - Expected: 2, Actual: " + minRoads(packages1, roads1));

        // Test case 2
        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
        System.out.println("Test Case 2 - Expected: 2, Actual: " + minRoads(packages2, roads2));
    }
}

// Summary:
// The algorithm successfully solves the problem of finding the minimum number of roads required to collect all packages in the city. 
// It uses a breadth-first search (BFS) approach to explore the state space, where each state consists of a node and a bitmask representing the collected packages.
// The BFS ensures that the fewest steps are taken to cover all packages, and the algorithm returns the minimum number of steps required. 
// The algorithm works as expected and passes the test cases.
