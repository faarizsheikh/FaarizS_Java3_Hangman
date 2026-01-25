package org.example;

import java.util.*;
import java.io.*;

/*
    Student: Faariz Haider Sheikh (0856155),
    Course: MAD300-26W-001 JAVA PROGRAMMING III,
    Program: Computer Programming (T850),
    Professor: Câi Filiault,
    Created: January 14, 2026,
    Last Updated: January 25, 2026
 */

public class Hangman {
    private static final int MAX_WRONG_GUESSES = 6;

    /* === MAIN === */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        boolean playAgain = true;

        // WELCOME:
        String border = "\t\t\t\t\t******************************";
        System.out.printf("%n%s%n\t\t\t\t\t Welcome to Java Hangman! :D%n%s%n", border, border);

        while (playAgain) {
            String filePath, line;
            int topicChoice;

            while (true) {
                System.out.print("\nSelect Topic:\n1- Animals\n2- Food\n3- Technology\nChoice: ");

                if (scanner.hasNextInt()) {
                    topicChoice = scanner.nextInt();

                    if (topicChoice >= 1 && topicChoice <= 3)
                        break;
                } else {
                    scanner.next(); // Discards invalid input.
                }
                System.out.println("Invalid input. Please enter 1, 2, or 3.");
            }

            switch (topicChoice) {
                case 1 -> filePath = "topic-animal.txt";
                case 2 -> filePath = "topic-food.txt";
                case 3 -> filePath = "topic-tech.txt";
                default -> filePath = "";
            }

            ArrayList<String> words = new ArrayList<>();
            InputStream inputStream = Hangman.class.getResourceAsStream("/" + filePath);

            if (inputStream == null) {
                System.out.println("Resource not found: " + filePath);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((line = reader.readLine()) != null) {
                    if (!line.isBlank())
                        words.add(line.trim().toLowerCase());
                }
            } catch (Exception e) {
                System.out.println("Error loading resource: " + filePath);
            }

            if (words.isEmpty()) {
                System.out.println("No words found. Exiting game.");
                return;
            }
            String word = words.get(rand.nextInt(words.size()));
            System.out.println();

            int wrongGuesses = 0;
            ArrayList<Character> wordState = new ArrayList<>();
            ArrayList<Character> haveGuessed = new ArrayList<>();

            String freed = """
                       |_ _ _
                       |     \\
                       |
                       |
                       |       O
                       |     / | \\
                       |       |
                    _ _|_ _  /   \\
                    """;

            String victoryMessage = "Nice!\nHang in there, buddy. Help is on the way!...";

            for (int i = 0; i < word.length(); i++)
                wordState.add('_'); // Fills it with _ based on word length.

            // MAIN LOOP:
            while (wrongGuesses < MAX_WRONG_GUESSES) {
                System.out.print("Word: ");

                for (char c : wordState)
                    System.out.print(c + " "); // Prints each blank or char without spaces.
                System.out.println();

                if (!haveGuessed.isEmpty()) {
                    System.out.print("[ Guessed: ");

                    ArrayList<Character> sorted = new ArrayList<>(haveGuessed); // Keeps the original list.
                    Collections.sort(sorted);

                    for (char c : sorted)
                        System.out.print(c + " "); // Displays guessed letters.
                    System.out.print("]\n\n");
                }

                // GET & VALIDATE: Input
                System.out.print("Enter a letter: ");
                char guess = scanner.next().trim().toLowerCase().charAt(0); // Takes 1st char & convert -> lowercase.

                if (!Character.isLetter(guess)) { // Only allows alphabet.
                    System.out.println("(Please enter a letter A-Z only!)\n");
                    continue;
                }
                if (haveGuessed.contains(guess)) { // Will avoid counting repeated guesses.
                    System.out.println("(You already tried that.)\n");
                    continue;
                }
                haveGuessed.add(guess);

                // CHECKS: Guess, to see if correct or incorrect
                if (word.indexOf(guess) >= 0) {
                    System.out.println("(Correct guess!)\n");

                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == guess)
                            wordState.set(i, guess); // Replaces "_" with correct letter.
                    }

                    // End Round: Victory!
                    if (!wordState.contains('_')) { // No remaining "_" -> player wins!
                        System.out.printf("%s%n%n%s%nThe word was indeed \"%s\".", victoryMessage, freed, word);
                        break;
                    }
                } else {
                    wrongGuesses++;
                    System.out.println("(Wrong guess!)\n");

                    if (wrongGuesses < MAX_WRONG_GUESSES)
                        System.out.println(Art(wrongGuesses));
                }
            }

            // End Round: Defeat!
            if (wrongGuesses == MAX_WRONG_GUESSES) { // Player runs out of guesses -> player loses!
                System.out.printf("Game over!%n%n%s%nThe word was \"%s\".", Art(wrongGuesses), word);
            }
            // ASK: Replay
            System.out.print("\nDo you want to guess another word? (Y/N): ");
            String response = scanner.next().trim().toLowerCase();
            playAgain = !response.isEmpty() && response.charAt(0) == 'y';
            scanner.nextLine();
        }
        scanner.close();
    }

    private static String Art(int wrongGuesses) {
        return switch (wrongGuesses) {

            case 0 -> "\n\n\n";

            case 1 -> """
                       |_ _ _
                       |     |
                       |     O
                       |
                       |
                       |
                       |
                    _ _|_ _
                    """;

            case 2 -> """
                       |_ _ _
                       |     |
                       |     O
                       |     |
                       |     |
                       |
                       |
                    _ _|_ _
                    """;

            case 3 -> """
                       |_ _ _
                       |     |
                       |     O
                       |   / |
                       |     |
                       |
                       |
                    _ _|_ _
                    """;

            case 4 -> """
                       |_ _ _
                       |     |
                       |     O
                       |   / | \\
                       |     |
                       |
                       |
                    _ _|_ _
                    """;

            case 5 -> """
                       |_ _ _
                       |     |
                       |     O
                       |   / | \\
                       |     |
                       |   /
                       |
                    _ _|_ _
                    """;

            case 6 -> """
                       |_ _ _
                       |     |
                       |     O
                       |   / | \\
                       |     |
                       |   /   \\
                       |
                    _ _|_ _
                    """;

            default -> "";
        };
    }
}
