package org.example;

import java.util.*;

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
        (https://www.youtube.com/watch?v=xk4_1vDrzzo) - Recalling Java basics.
        (https://www.youtube.com/watch?v=0Nn8PZrWZKE) - Hangman + ASCII art for hangman.

    - W3Schools (Website):
        (https://www.w3schools.com/java/java_arrays.asp) - Java Arrays.
        (https://www.w3schools.com/java/java_arraylist.asp) - Java ArrayLists.
        (https://www.w3schools.com/java/java_set.asp) - Sets in Java.
        (https://www.w3schools.com/java/java_ref_string.asp) - String Methods in Java.

    - Baeldung (Website):
        (https://www.baeldung.com/java-immutable-set) - Immutable Sets in Java.
 */

public class Hangman {

    /* === CONSTANTS === */
    private static final int MAX_WRONG_GUESSES = 6;

    private static final String[] ANIMALS = {
            "cat", "dog", "eagle", "horse", "lion", "pig", "rabbit", "zebra"
    };

    private static final String[] FOOD = {
            "biryani", "karahi", "kebab", "pierogies", "poutine", "roast", "spaghetti", "spinach"
    };

    private static final String[] TECH_WORDS = {
            "computer", "developer", "html", "internet", "java", "json", "networking", "python"
    };

    private static final String[] GAME_OVER_MESSAGES = {
            "Not quite. The word remains undefeated.",
            "The code wins this round.",
            "Try again. Victory favors the persistent."
    };

    private static final Set<String> YES_RESPONSES = Set.of(
            "yes", "yea", "yeh", "yah", "ye", "ya", "yup", "yep", "y"
    ); // Fixed set that's why I used immutable.

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

            String word;
            switch (topicChoice) {
                case 1 -> word = ANIMALS[rand.nextInt(ANIMALS.length)];
                case 2 -> word = FOOD[rand.nextInt(FOOD.length)];
                case 3 -> word = TECH_WORDS[rand.nextInt(TECH_WORDS.length)];
                default -> word = "";
            }
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

            for (int i = 0; i < word.length(); i++) {
                wordState.add('_');
            } // Fills wordState with _ per word length.

            // MAIN LOOP:
            while (wrongGuesses < MAX_WRONG_GUESSES) {
                System.out.print("Word: ");

                for (char c : wordState) {
                    System.out.print(c + " ");
                } // Prints each blank or char without spaces.
                System.out.println();

                if (!guessedLetters.isEmpty()) {
                    System.out.print("Guessed letters: ");

                    List<Character> sorted = new ArrayList<>(guessedLetters);
                    Collections.sort(sorted);

                    for (char c : sorted) {
                        System.out.print(c + " ");
                    } // Displays guessed letters.
                    System.out.print("\n\n");
                }

                // GET: Input
                System.out.print("Guess a letter: ");
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
                        if (word.charAt(i) == guess) {
                            wordState.set(i, guess);
                        } // Replaces "_" with correct letter.
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

                    if (wrongGuesses < MAX_WRONG_GUESSES) {
                        System.out.println(getHangmanArt(wrongGuesses));
                    }
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

    static String getHangmanArt(int wrongGuesses) { // Hangman ASCII art learned from @brocodez YT channel.
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
