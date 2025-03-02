import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This program finds the k-th lowest combined return from two sets of investment returns.
 * The algorithm computes all possible products of elements from two arrays, sorts them,
 * and retrieves the k-th smallest product.
 */
public class Question_1_b {
    
    /**
     * Computes all possible products of elements from two given arrays,
     * sorts them in ascending order, and returns the k-th lowest product.
     * 
     * @param returns1 First array of returns.
     * @param returns2 Second array of returns.
     * @param k The k-th smallest product to retrieve.
     * @return The k-th lowest combined return.
     */
    public static int kthLowestCombinedReturn(int[] returns1, int[] returns2, int k) {
        
        List<Integer> combinedReturns = new ArrayList<>();
        
        // Compute all possible products of elements from returns1 and returns2
        for (int r1 : returns1) {
            for (int r2 : returns2) {
                combinedReturns.add(r1 * r2);
            }
        }
        
        // Sort the computed products in ascending order
        Collections.sort(combinedReturns);
        
        // Return the k-th smallest product (1-based index)
        return combinedReturns.get(k - 1);
    }
    
    /**
     * Main method to test kthLowestCombinedReturn function with example cases.
     */
    public static void main(String[] args) {
        // Test Case 1
        int[] returns1_example1 = {2, 5};
        int[] returns2_example1 = {3, 4};
        int k1 = 2;
        int result1 = kthLowestCombinedReturn(returns1_example1, returns2_example1, k1);
        System.out.println("Test Case 1 - Expected Output: 8, Actual Output: " + result1);
        
        // Test Case 2
        int[] returns1_example2 = {-4, -2, 0, 3};
        int[] returns2_example2 = {2, 4};
        int k2 = 6;
        int result2 = kthLowestCombinedReturn(returns1_example2, returns2_example2, k2);
        System.out.println("Test Case 2 - Expected Output: 0, Actual Output: " + result2);

        // Test Case 3
        int[] returns1_example3 = {1, 3, 5, 7};
        int[] returns2_example3 = {2, 5};
        int k3 = 4;
        int result3 = kthLowestCombinedReturn(returns1_example3, returns2_example3, k3);
        System.out.println("Test Case 3 - Expected Output: 10, Actual Output: " + result3);
        
    }
}

/**
 * Summary:
 * - The algorithm iterates through both arrays, computing all possible pairwise products.
 * - It then sorts the resulting list in ascending order.
 * - Finally, it retrieves and returns the k-th smallest product.
 * - The algorithm works as expected, as verified by the test cases, which produce the correct outputs.
 */

