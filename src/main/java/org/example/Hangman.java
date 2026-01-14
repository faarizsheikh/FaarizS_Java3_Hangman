package org.example;
import java.util.*;

/*
    Name: Faariz Haider Sheikh,
    Course: MAD300-26W-001 JAVA PROGRAMMING 3,
    Program: Computer Programming (T850),
    Professor: Câi Filiault,
    Created: January 14, 2026,
    Last Updated: January 14, 2026
 */

/* SOURCES:
    - @brocodez (YouTube): Recalling Java basics; Java Hangman basics and ASCII art.
    - W3Schools (Website): Arrays, ArrayList, String methods in Java.
 */

public class Hangman {
    private static final int MAX_WRONG_GUESSES = 6;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // INITIALIZATION:
        int wrongGuesses = 0;
        String word = "pizza"; // I will extend this later into an array.

        ArrayList<Character> wordState = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) { // Fills wordState with underscores (_) based on word length.
            wordState.add('_');
        }

        // WELCOME:
        System.out.println("\t\t\t\t\t******************************");
        System.out.println("\t\t\t\t\t Welcome to Java Hangman! :D");
        System.out.println("\t\t\t\t\t******************************");

        // MAIN LOOP:
        while (wrongGuesses < MAX_WRONG_GUESSES) {
            System.out.println(getHangmanArt(wrongGuesses)); // Hangman art shown based on guesses.

            System.out.print("Word: ");
            for (char c : wordState) {
                System.out.print(c + ""); // Prints each blank or char without spaces.
            }
            System.out.println();

            // INPUT:
            System.out.print("Guess a letter: ");
            char guess = scanner.next().toLowerCase().charAt(0); // Takes first character, convert to lowercase.

            // CHECK GUESS:
            if (word.indexOf(guess) >= 0) {
                System.out.print("(Correct guess!)\n");

                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == guess) {
                        wordState.set(i, guess); // Replaces _ with correct letter.
                    }
                }

                if (!wordState.contains('_')) { // No remaining underscores -> player wins!
                    System.out.println("\nYOU WON!");
                    System.out.println(getHangmanArt(wrongGuesses));
                    System.out.println("The word was indeed \"" + word + "\".");
                    break;
                }
            }
            else {
                wrongGuesses++; // Wrong guess -> incremented variable.
                System.out.println("(Wrong guess!)");
            }
        }

        if (wrongGuesses >= MAX_WRONG_GUESSES) { // Player runs out of guesses -> player loses!
            System.out.println("\nGAME OVER!\n");
            System.out.println(getHangmanArt(wrongGuesses));
            System.out.println("The word was " + word + ".");
            System.out.println("(You left my guy hanging... V_V)");
        }
        scanner.close();
    }

    // FUNCTIONS:
    public static String getHangmanArt(int wrongGuesses) {
        return switch(wrongGuesses) { // Returns Hangman ASCII art based on wrong guesses.
            case 0 -> """
                      
                      
                      
                      """;
            case 1 -> """
                       O
                      
                      
                      """;
            case 2 -> """
                       O
                       |
                      
                      """;
            case 3 -> """
                       O
                      /|
                      
                      """;
            case 4 -> """
                       O
                      /|\\
                      
                      
                      """;
            case 5 -> """
                       O
                      /|\\
                      /
                      """;
            case 6 -> """
                       O
                      /|\\
                      / \\
                      """;
            default -> "";
        };
    }
}
