import java.util.Random;
import java.util.Scanner;

public class GuessingGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        boolean tryAgain = true;
        int totalScore = 0;
        int round = 0;

        while (tryAgain) {
            int randomNumber = random.nextInt(100) + 1;
            int attempts = 0;
            int maxAttempts = 5;
            boolean guessedCorrectly = false;
            System.out.println("Round " + (round + 1));
            System.out.println("I have generated a number between 1 and 100. Try to guess it!");

            while (attempts < maxAttempts && !guessedCorrectly) {
                System.out.print("Enter your guess: ");
                int userGuess = sc.nextInt();
                attempts++;

                if (userGuess == randomNumber) {
                    System.out.println("Congratulations! You have guessed the correct number.");
                    guessedCorrectly = true;
                    totalScore += maxAttempts - attempts + 1;
                } else if (userGuess < randomNumber) {
                    System.out.println("Sorry, but your guess is too low!");
                } else {
                    System.out.println("Sorry, but your guess is too high!");
                }
            }

            if (!guessedCorrectly) {
                System.out.println("Sorry, you have used all your attempts. The correct answer was " + randomNumber);
            }

            round++;
            System.out.println("Your total score is " + totalScore);
            System.out.print("Do you want to play another round? (yes/no): ");
            String response = sc.next();
            tryAgain = response.equalsIgnoreCase("yes");
        }

        System.out.println("Thanks for playing this game!");
        System.out.println("Your final score is " + totalScore);
        sc.close();
    }
}
