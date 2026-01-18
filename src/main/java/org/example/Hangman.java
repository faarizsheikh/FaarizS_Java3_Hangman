package org.example;

import java.util.*;
import java.io.*;

/*
    Name: Faariz Haider Sheikh,
    Course: MAD300-26W-001 JAVA PROGRAMMING 3,
    Program: Computer Programming (T850),
    Professor: Câi Filiault,
    Created: January 14, 2026,
    Last Updated: January 15, 2026
 */

/* Sources I used:
    - Bro code (@brocodez):
        (https://www.youtube.com/watch?v=xk4_1vDrzzo) - Recalling Java basics (full video)
        (https://www.youtube.com/watch?v=0Nn8PZrWZKE) - Hangman + ASCII art for hangman.

    - W3Schools (Website):
        (https://www.w3schools.com/java/java_arrays.asp) - Java Arrays.
        (https://www.w3schools.com/java/java_arraylist.asp) - Java ArrayLists.
        (https://www.w3schools.com/java/java_set.asp) - Sets in Java.
        (https://www.w3schools.com/java/java_ref_string.asp) - String Methods in Java.

    - Baeldung (Website):
        (https://www.baeldung.com/java-immutable-set) - Immutable Sets in Java.
        (https://www.baeldung.com/java-getresourceasstream-vs-fileinputstream) - getResourceAsStream
 */

public class Hangman {

    /* === CONSTANTS === */
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
            int topicChoice = showMenu(scanner);

            String filePath;

            switch (topicChoice) {
                case 1 -> filePath = "topic-animal.txt";
                case 2 -> filePath = "topic-food.txt";
                case 3 -> filePath = "topic-tech.txt";
                default -> filePath = "";
            }

            ArrayList<String> words = loadWords(filePath);

            if (words.isEmpty()) {
                System.out.println("No words found. Exiting game.");
                return;
            }

            String word = words.get(rand.nextInt(words.size()));
            System.out.println();

            int wrongGuesses = 0;
            ArrayList<Character> wordState = new ArrayList<>();
            Set<Character> guessedLetters = new HashSet<>();

            String freed = """
                      |___
                      |   \\
                      |
                      |
                      |     O
                      |   / | \\
                      |     |
                    __|__ /   \\
                    """;

            String victoryMessage = """
                    Nice!
                    Hang in there, buddy. Help is on the way!...
                    """;

            for (int i = 0; i < word.length(); i++) wordState.add('_'); // Fills wordState with _ per word length.

            // MAIN LOOP:
            while (wrongGuesses < MAX_WRONG_GUESSES) {
                System.out.print("Word: ");

                for (char c : wordState) System.out.print(c + " "); // Prints each blank or char without spaces.
                System.out.println();

                if (!guessedLetters.isEmpty()) {
                    System.out.print("Already guessed: ");

                    List<Character> sorted = new ArrayList<>(guessedLetters);
                    Collections.sort(sorted);

                    for (char c : sorted) System.out.print(c + " "); // Displays guessed letters.
                    System.out.print("\n\n");
                }

                // GET: Input
                System.out.print("Enter a letter: ");
                char guess = scanner.next().trim().toLowerCase().charAt(0); // Takes 1st char, convert -> lowercase.

                if (!Character.isLetter(guess)) { // Validates input and only allow letters of the English alphabet.
                    System.out.println("(Please enter a letter A-Z only!)\n");
                    continue;
                }

                if (guessedLetters.contains(guess)) { // Validates input and doesn't count repeated guesses.
                    System.out.println("(You already guessed that letter!)\n");
                    continue;
                }
                guessedLetters.add(guess);

                // CHECKS: Guess, to see if correct or incorrect
                if (word.indexOf(guess) >= 0) {
                    System.out.println("(Correct guess!)\n");

                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == guess) wordState.set(i, guess); // Replaces "_" with correct letter.
                    }

                    // End Round: Victory!
                    if (!wordState.contains('_')) { // No remaining "_" -> player wins!
                        System.out.printf("%s%n%s%nThe word was indeed \"%s\".",
                                victoryMessage,
                                freed,
                                word);
                        break;
                    }

                } else {
                    wrongGuesses++; // Wrong guess -> incremented variable.
                    System.out.println("(Wrong guess!)\n");

                    if (wrongGuesses < MAX_WRONG_GUESSES) System.out.println(getHangmanArt(wrongGuesses));
                }
            }

            // End Round: Defeat!
            if (wrongGuesses == MAX_WRONG_GUESSES) { // Player runs out of guesses -> player loses!
                System.out.println(GAME_OVER_MESSAGES[rand.nextInt(GAME_OVER_MESSAGES.length)]);
                System.out.printf("%n%s%nThe word was \"%s\".",
                        getHangmanArt(wrongGuesses), word);
            }

            // ASK: Replay
            System.out.print("\nDo you want to guess another word? (Y/N): ");
            String response = scanner.next().trim().toLowerCase();
            playAgain = YES_RESPONSES.contains(response);
        }
        scanner.close();
    }

    /* === METHODS === */

    private static final String[] GAME_OVER_MESSAGES = {
            "Not quite. The word remains undefeated.",
            "The code wins this round.",
            "Try again. Victory favors the persistent."
    };

    private static final Set<String> YES_RESPONSES = Set.of(
            "y", "ya", "yah", "ye", "yea", "yeah", "yeh", "yep", "yes", "yup"
    ); // Fixed set that's why I used immutable.

    private static ArrayList<String> loadWords(String resourceName) {
        ArrayList<String> words = new ArrayList<>();

        InputStream inputStream = Hangman.class.getResourceAsStream("/" + resourceName);

        if (inputStream == null) {
            System.out.println("Resource not found: " + resourceName);
            return words;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) words.add(line.trim().toLowerCase());
            }

        } catch (Exception e) {
            System.out.println("Error loading resource: " + resourceName);
        }
        return words;
    }

    private static int showMenu(Scanner scanner) {
        int choice;

        while (true) {
            System.out.println("\nSelect Topic:\n1- Animals\n2- Food\n3- Technology");
            System.out.print("Choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();

                if (choice >= 1 && choice <= 3) break;

            } else {
                scanner.next();
            }
            System.out.println("Invalid input. Please enter 1, 2, or 3.");
        }
        return choice;
    }

    private static String getHangmanArt(int wrongGuesses) { // ASCII Hangman art learned from @brocodez YT channel.
        return switch (wrongGuesses) {

            case 0 -> "\n\n\n";

            case 1 -> """
                      |___
                      |   |
                      |   O
                      |
                      |
                      |
                      |
                    __|__
                    """;

            case 2 -> """
                      |___
                      |   |
                      |   O
                      |   |
                      |   |
                      |
                      |
                    __|__
                    """;

            case 3 -> """
                      |___
                      |   |
                      |   O
                      | / |
                      |   |
                      |
                      |
                    __|__
                    """;

            case 4 -> """
                      |___
                      |   |
                      |   O
                      | / | \\
                      |   |
                      |
                      |
                    __|__
                    """;

            case 5 -> """
                      |___
                      |   |
                      |   O
                      | / | \\
                      |   |
                      |  /
                      |
                    __|__
                    """;

            case 6 -> """
                      |___
                      |   |
                      |   O
                      | / | \\
                      |   |
                      |  / \\
                      |
                    __|__
                    """;

            default -> "";
        };
    }
}
