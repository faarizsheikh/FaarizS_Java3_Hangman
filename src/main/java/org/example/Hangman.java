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

/* SOURCES:
    - Bro code (@brocodez):
        (https://www.youtube.com/watch?v=xk4_1vDrzzo) - Recalling Java basics.
        (https://www.youtube.com/watch?v=0Nn8PZrWZKE) - Hangman + ASCII art for hangman.
    - W3Schools (Website): Arrays/ArrayList, String methods in Java.
        (https://www.w3schools.com/java/java_arrays.asp)
        (https://www.w3schools.com/java/java_arraylist.asp)
        (https://www.w3schools.com/java/java_set.asp)
        (https://www.w3schools.com/java/java_ref_string.asp)
    - Baeldung (Website): Immutable set.
        (https://www.baeldung.com/java-immutable-set) -
 */
public class Hangman {

    /* === CONSTANTS === */

    private static final int MAX_WRONG_GUESSES = 6;

    private static final String[] WORDS = {
            "angular", "boolean", "bug", "class", "coding",
            "computer", "conditions", "css", "cybersecurity", "developer",
            "error", "glitch", "gradle", "html", "internet",
            "java", "javascript", "javafx", "jquery", "json",
            "kotlin", "looping", "maven", "networking", "node",
            "object", "php", "program", "python", "ruby",
            "sql", "technology", "typescript", "xml"
    };

    private static final String[] GAME_OVER_MESSAGES = {
            "(Not quite. The word remains undefeated.)",
            "(The code wins this round.)",
            "(Try again. Victory favors the persistent.)"
    };

    private static final Set<String> YES_RESPONSES = Set.of(
            "yes", "yea", "yeh", "yah", "ye", "ya", "yup", "yep", "y"
    ); // Fixed that's why I used immutable set.

    /* === MAIN === */

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        boolean playAgain = true;

        while (playAgain) {

            int topicChoice = showMenu(scanner);
            String word = getRandomWord(rand);
            int wrongGuesses = 0;

            ArrayList<Character> wordState = new ArrayList<>();
            Set<Character> guessedLetters = new HashSet<>();

            String victoryMessage = """
            The word was indeed "%s".
            Hang in there, buddy. Help is on the way!
            ...
            """.formatted(word);

            String freed = """
                           |___
                           |   |
                           |
                           |
                           |     O
                           |   / | \\
                           |     |
                         __|__ /   \\
                         """;

            for (int i = 0; i < word.length(); i++) { // Fills wordState with underscores (_) based on word length.
                wordState.add('_');
            }

            // WELCOME:
            String border = "\t\t\t\t\t******************************";
            System.out.printf("%n%s%n\t\t\t\t\t Welcome to Java Hangman! :D%n%s%n", border, border);

            // MAIN LOOP:
            while (wrongGuesses < MAX_WRONG_GUESSES) {
                System.out.print("Word: ");
                for (char c : wordState) { System.out.print(c + " "); } // Prints each blank or char without spaces.
                System.out.println();

                if (!guessedLetters.isEmpty()) {
                    System.out.print("Guessed letters: ");
                    List<Character> sorted = new ArrayList<>(guessedLetters);
                    Collections.sort(sorted);
                    for (char c : sorted) { System.out.print(c + " "); } // Displays guessed letters.
                    System.out.println();
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
                        if (word.charAt(i) == guess) { wordState.set(i, guess); } // Replaces "_" with correct letter.
                    }

                    if (!wordState.contains('_')) {  // No remaining "_" -> player wins!
                        System.out.printf("%nYOU WON!%n%s%n%s%nThere.",
                                victoryMessage, freed);
                        break;
                    }

                } else {
                    wrongGuesses++; // Wrong guess -> incremented variable.
                    System.out.println("(Wrong guess!)\n");

                    if (wrongGuesses < MAX_WRONG_GUESSES) { System.out.println(getHangmanArt(wrongGuesses)); }
                }
            }

            // END: Round
            if (wrongGuesses >= MAX_WRONG_GUESSES) { // Player runs out of guesses -> player loses!
                System.out.printf("%nGAME OVER!%n%n%s%nThe word was \"%s\".",
                        getHangmanArt(wrongGuesses), word);
                System.out.println(GAME_OVER_MESSAGES[rand.nextInt(GAME_OVER_MESSAGES.length)]);
            }

            // ASK: Replay
            System.out.print("\nDo you want to guess another word? (Y/N): ");
            String response = scanner.next().trim().toLowerCase();
            playAgain = YES_RESPONSES.contains(response);
        }
        scanner.close();
    }

    /* === METHODS === */

    private static String getRandomWord(Random rand) {
        return WORDS[rand.nextInt(WORDS.length)];
    }

    private static int showMenu(Scanner scanner) {

        System.out.println("\nSelect Topic:");
        System.out.println("1 - Technology");
        System.out.println("2 - Animals");
        System.out.println("3 - Food");
        System.out.print("Choice: ");

        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Enter 1-3: ");
        }

        return scanner.nextInt();
    }

    static String getHangmanArt(int wrongGuesses) {
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
