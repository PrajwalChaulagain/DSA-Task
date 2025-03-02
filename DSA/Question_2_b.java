// This algorithm solves the "Closest Pair" problem, where given two arrays representing x and y coordinates of points,
// the goal is to find the pair of points that have the minimum Manhattan distance between them. The Manhattan distance 
// between two points (x1, y1) and (x2, y2) is calculated as |x1 - x2| + |y1 - y2|. The algorithm compares all pairs 
// of points, computes their Manhattan distance, and returns the indices of the pair with the smallest distance. If multiple
// pairs have the same minimum distance, the pair with the smaller indices is returned.

public class Question_2_b {

    // This function takes two arrays of x and y coordinates and returns the indices of the closest pair of points.
    // The Manhattan distance is used to calculate the distance between points.
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;  // Get the number of points
        int minDistance = Integer.MAX_VALUE;  // Initialize the minimum distance as a large value
        int[] result = new int[]{-1, -1};  // Initialize the result array to store the indices of the closest pair

        // Iterate through each pair of points and calculate the Manhattan distance
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                // Calculate the Manhattan distance between point i and point j
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Update the result if a smaller distance is found or if the distance is the same but a lexicographically smaller pair
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance;  // Update the minimum distance
                    result[0] = i;  // Update the first point index
                    result[1] = j;  // Update the second point index
                }
            }
        }
        return result;  // Return the indices of the closest pair
    }

    // Main function to test the findClosestPair function with a set of x and y coordinates.
    public static void main(String[] args) {

        int[] x_coords = {1, 2, 3, 2, 4};  // x coordinates of the points
        int[] y_coords = {2, 3, 1, 2, 3};  // y coordinates of the points

        // Call the function to find the closest pair of points
        int[] closestPair = findClosestPair(x_coords, y_coords);

        // Print the result: indices of the closest pair of points
        System.out.println("Closest Pair: [" + closestPair[0] + ", " + closestPair[1] + "]");
    }
}

// Summary:
// This algorithm successfully solves the "Closest Pair" problem by using a brute-force approach to compare all pairs of points. 
// It calculates the Manhattan distance between each pair and keeps track of the pair with the smallest distance. If multiple 
// pairs have the same distance, it selects the pair with smaller indices. The algorithm works as expected and provides the correct 
// result for the given test case, correctly identifying the pair of points with the minimum distance.
