package game_logic;

import java.util.*;

public class Dice {
    private static final Random random = new Random();

    private static final Map<Integer, Double> PROBABILITIES = new HashMap<>();
    private static final int[] POSSIBLE_ROLLS = {1, 2, 3, 4, 5};
    
    static {

        PROBABILITIES.put(1, 4.0 / 16.0);
        PROBABILITIES.put(2, 6.0 / 16.0);
        PROBABILITIES.put(3, 4.0 / 16.0);

        PROBABILITIES.put(4,  1.0 / 16.0);
        PROBABILITIES.put(5, 1.0 / 16.0);
    }

    public static double getProbability(int roll) {
        return PROBABILITIES.getOrDefault(roll, 0.0);
    }
    

    public static int[] getPossibleRolls() {
        return POSSIBLE_ROLLS;
    }
    

    public static Map<Integer, Double> getAllProbabilities() {
        return new HashMap<>(PROBABILITIES);
    }
    

    public static void printProbabilityTable() {
        System.out.println("\n=== Stick Throw Probability Table ===");
        System.out.println("Roll\t| Probability\t| Distribution");
        System.out.println("--------|---------------|------------");
        
        for (int roll : POSSIBLE_ROLLS) {
            double prob = getProbability(roll);
            int percentage = (int)(prob * 100);
            String bar = "█".repeat(percentage / 5);
            System.out.printf("%d\t| %.4f (%.1f%%)\t| %s\n",
                roll, prob, prob * 100, bar);
        }
        
        System.out.println("\nExplanation:");
        System.out.println("- Roll 1: 4 cases (1 light, 3 dark) = 4/16 = 25%");
        System.out.println("- Roll 2: 6 cases (2 light, 2 dark) = 6/16 = 37.5%");
        System.out.println("- Roll 3: 4 cases (3 light, 1 dark) = 4/16 = 25%");
        System.out.println("- Roll 4: 1 cases (4 drak or 0 light) = 1/16 = 6.25%");
        System.out.println("- Roll 5: 1 cases (0 dark or 4 light) = 1/16 = 6.25%");
    }

    public static int throwSticksWithVisualization() {
        System.out.print("\nThrowing sticks: ");
        int darkSide = 0;
        
        for (int i = 0; i < 4; i++) {
            boolean isdark = random.nextBoolean();
            if (isdark) {
                System.out.print("○ ");
                darkSide++;
            } else {
                System.out.print("● ");
            }
        }

        int result = (darkSide == 0 ) ? 5 : darkSide;
        System.out.printf("-> Result: %d\n", result);
        return result;
    }
}