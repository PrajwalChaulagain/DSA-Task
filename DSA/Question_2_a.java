// This algorithm solves the "Minimum Rewards" problem, where we are given an array of ratings representing 
// the performance of children. The goal is to distribute the minimum number of rewards such that:
// 1. Every child gets at least one reward.
// 2. A child with a higher rating than their neighbor(s) must receive more rewards than those neighbors.
// The algorithm uses a greedy approach with two passes over the ratings array to assign the minimum number of rewards
// that satisfy these conditions.

public class Question_2_a {
    
    // This function takes an array of ratings as input and returns the minimum number of rewards needed
    // for all children based on the ratings.
    public static int minRewards(int[] ratings) {
        int n = ratings.length;  // Get the length of the ratings array
        if (n == 0) return 0;    // If the array is empty, return 0 as no rewards are needed.
        
        int[] rewards = new int[n];  // Create an array to store the rewards for each child
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;  // Initialize each child with 1 reward initially.
        }
        
        // First pass: From left to right, adjust rewards based on increasing ratings.
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;  // If the current rating is higher, give one more reward than the previous child.
            }
        }
        
        // Second pass: From right to left, adjust rewards based on decreasing ratings.
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);  // Ensure the current child has more rewards than the next child, if necessary.
            }
        }
        
        // Calculate the total number of rewards by summing the rewards array.
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;  // Add each reward to the total.
        }
        
        return totalRewards;  // Return the total number of rewards.
    }
    
    // Main function to test the minRewards function with two test cases.
    public static void main(String[] args) {
        int[] ratings1 = {1, 0, 2};  // Test case 1: Ratings of 1, 0, and 2
        int result1 = minRewards(ratings1);  // Call the function to compute the result for test case 1
        System.out.println("Test Case 1 - Expected Output: 5, Actual Output: " + result1);  // Print the result for test case 1

        int[] ratings2 = {1, 2, 2};  // Test case 2: Ratings of 1, 2, and 2
        int result2 = minRewards(ratings2);  // Call the function to compute the result for test case 2
        System.out.println("Test Case 2 - Expected Output: 4, Actual Output: " + result2);  // Print the result for test case 2
    }
}

// Summary:
// This algorithm successfully solves the "Minimum Rewards" problem by using a two-pass greedy approach. In the first pass, it ensures that any child with a higher rating than their left neighbor receives more rewards, and in the second pass, it ensures that any child with a higher rating than their right neighbor also receives more rewards. After the two passes, the algorithm sums up the rewards to get the minimum total. The algorithm works as expected and provides the correct results for the test cases provided.
