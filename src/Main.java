
import control.GameController;
import game_logic.Dice;

import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        displayMainMenu();
        System.out.print("\nChoose game mode (1-4): ");
        int choice = getIntInput(scanner, 1, 4);

        switch (choice) {
            case 1:
                playGame(3, false, scanner);
                break;
            case 2:
                playGame(3, true, scanner);
                break;
            case 3:
                selectDifficulty(scanner);
                break;
            case 4:
                Dice.printProbabilityTable();
                System.out.print("\nPress Enter to return...");
                scanner.nextLine();
                main(args);
                break;
        }

        scanner.close();
    }
    private static void displayMainMenu() {
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                               ║");
        System.out.println("║                     Senet Game                                ║");
        System.out.println("║               Ancient Egyptian Board Game                     ║");
        System.out.println("║                                                               ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║                     Choose Game Mode:                         ║");
        System.out.println("║                                                               ║");
        System.out.println("║  1. Normal Play (Medium difficulty)                           ║");
        System.out.println("║  2. Play with AI details                                      ║");
        System.out.println("║  3. Select difficulty                                         ║");
        System.out.println("║  4. Show stick throw probability table                        ║");
        System.out.println("║                                                               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
    private static void selectDifficulty(Scanner scanner) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println(  "║                    Select Difficulty Level                    ║");
        System.out.println(  "╠═══════════════════════════════════════════════════════════════╣");
        System.out.println(  "║  1. Easy    (Depth = 2)                                       ║");
        System.out.println(  "║  2. Medium  (Depth = 3)                                       ║");
        System.out.println(  "║  3. Hard    (Depth = 4)                                       ║");
        System.out.println(  "║  4. Expert  (Depth = 5)                                       ║");
        System.out.println(  "╚═══════════════════════════════════════════════════════════════╝");
        System.out.print("\nChoose level (1-4): ");
        int level = getIntInput(scanner, 1, 4);

        int depth = level + 1;

        System.out.print("Show AI details? (y/n): ");
        String showDetails = scanner.nextLine().trim().toLowerCase();
        boolean verbose = showDetails.equals("y") || showDetails.equals("yes");

        playGame(depth, verbose, scanner);
    }
    private static void playGame(int depth, boolean showAIDetails, Scanner scanner) {
        GameController controller = new GameController(depth, showAIDetails);
        controller.startGame();

        System.out.print("\nDo you want to play again? (y/n): ");
        String playAgain = scanner.nextLine().trim().toLowerCase();

        if (playAgain.equals("y") || playAgain.equals("yes")) {
            main(new String[0]);
        } else {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                               ║");
            System.out.println("║               Thank you for playing! Goodbye               ║");
            System.out.println("║                                                               ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝\n");
        }

        controller.close();
    }

    private static int getIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf(" Please enter a number between %d and %d: ", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.print(" Please enter a valid number: ");
            }
        }
    }

}
