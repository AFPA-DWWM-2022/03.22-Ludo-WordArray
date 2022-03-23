/*
 * WordArray.java
 * Copyright (C) 2022 Ludovic Fernandez <http://github.com/SirWrexes>
 *
 * Distributed under terms of the MIT license.
 */

package dwwm.WordArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class WordArray extends ArrayList<String> {
    private int maxLength = 0;
    private int chars = 0;

    /** Add words from an array of Strings */
    public boolean addAll(String[] words) {
        for (String w : words) {
            int len = w.length();
            if (len > maxLength)
                maxLength = len;
            chars += len;
        }
        return super.addAll(Arrays.asList(words));
    }

    /** Possible formats when using toString() method */
    enum format {
        LINE,
        PAGE,
        TABLE,
    }

    //@formatter:off
    /**
     * Create a string from words
     * Possible `format` values:
     *
     * LINE
     *    +-----+--------+-----+
     *    | moo | foobar | baz |
     *    +-----+--------+-----+
     *
     * PAGE
     *    +--------+
     *    | moo    |
     *    | foobar |
     *    | baz    |
     *    +--------+
     *
     * TABLE
     *    +--------+
     *    | moo    |
     *    +--------+
     *    | foobar |
     *    +--------+
     *    | baz    |
     *    +--------+
     **/
    //@formatter:on
    public String toString(format format) {
        switch (format) {
            case LINE:
                return toLine(this);
            case PAGE:
                return toPage(this);
            case TABLE:
                return toTable(this);
            default: // In case `format` is none of the above
                throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    /**
     * Represent a WordArray as a LINE:
     * +-----+--------+-----+
     * | moo | foobar | baz |
     * +-----+--------+-----+
     **/
    public static String toLine(WordArray words) {
        // This will help us help put stuff together to create the string for output
        StringBuilder sb = new StringBuilder();

        // Line length =
        // 1 => Leading '+'
        // + total count of characters for every word
        // + 3 * word count => for each word, leading '-' & trailing "-+"
        // + 1 => Final '\n'
        int length = 1 + words.chars + 3 * words.size() + 1;
        char[] buffer = new char[length]; // This will contain the decorative line
        int i = 0; // Index to move around in `buffer`

        /* 1) Make the line */
        // Start with the first character
        buffer[i++] = '+';

        // For every word in our WordArray
        for (String w : words) {
            // Add one '-' to the line
            buffer[i++] = '-';
            // For every character in current word + 1
            for (int offset = i; i - offset < w.length() + 1; i += 1) {
                // Add a '-' to the line
                buffer[i] = '-';
            }
            // Add the '+' separator to the line
            buffer[i++] = '+';
        }
        // Add the line break at the end
        buffer[i] = '\n';

        /* 2) Append the line to the string, this will be the top one */
        sb.append(buffer);

        /* 3) Add the words to the string, this will be in the middle */
        // Start with the first character
        sb.append("|");

        // For every word in our WordArray
        for (String w : words) {
            // Add the word to the string, with separator
            sb.append(" " + w + " |");
        }

        // Add the the line break
        sb.append('\n');

        /* 4) Finally, append the line again, this will be on the bottom */
        sb.append(buffer);

        /* 5) We are done, return the resulting string. */
        return sb.toString();
    }

    // @formatter:off
    /**
     * Represent a WordArray as a PAGE:
     * +--------+
     * | moo    |
     * | foobar |
     * | baz    |
     * +--------+
     **/
    // @formatter:on
    public static String toPage(WordArray words) {
        // This will help us help put stuff together to create the string for output
        StringBuilder sb = new StringBuilder();

        // Line length equals
        // 4 => Leading "+-" & trailing "-+"
        // + `maxLength` => longest word size
        // + 1 => final '\n'
        int length = "|  |".length() + words.maxLength + 1;
        char[] buffer = new char[length]; // This will contain the decorative line
        int i = 0; // Index to move around in `buffer`

        /* 1) Make the line */
        // Start with "+-"
        buffer[i++] = '+';
        buffer[i++] = '-';

        // For every character in the longest word of the array + 1
        for (int offset = i; i - offset < words.maxLength + 1; i += 1)
            // Add a '-' to the line
            buffer[i] = '-';

        // End with '+'
        buffer[i++] = '+';

        // Don't forget the line break !
        buffer[i] = '\n';

        /* 2) Append the line to the string, this will be the top one */
        sb.append(buffer);

        /* 3) Add the words to the string, this will be in the middle */
        // For every word in our WordArray
        for (String w : words) {
            // Add the word to the string
            sb.append("| " + w);
            // If the current word is smaller than the longest word
            if (w.length() < words.maxLength) {
                // Create a temporary buffer
                char[] tmp = new char[words.maxLength - w.length()];
                // Fill it with spaces
                Arrays.fill(tmp, ' ');
                // Append it to the current line
                sb.append(tmp);
            }
            // Ad a space, the closing '|' and a line break
            sb.append(" |\n");
        }

        /* 4) Finally, append the line again, this will be on the bottom */
        sb.append(buffer);

        /* 5) We are done, return the resulting string. */
        return sb.toString();
    }

    // @formatter:off
    /**
     * Represent a WordArray as a TABLE:
     * +--------+
     * | moo    |
     * | foobar |
     * | baz    |
     * +--------+
     *
     * To see how it works, check toPage():
     * The only difference is that we add a separator line between words.
     **/
    // @formatter:on
    public static String toTable(WordArray words) {
        StringBuilder sb = new StringBuilder();
        int length = "|  |".length() + words.maxLength + 1;
        char[] buffer = new char[length];
        int i = 0;

        buffer[i++] = '+';
        buffer[i++] = '-';
        for (int offset = i; i - offset < words.maxLength + 1; i += 1)
            buffer[i] = '-';
        buffer[i++] = '+';
        buffer[i] = '\n';
        sb.append(buffer);
        for (String w : words) {
            sb.append("| " + w);
            if (w.length() < words.maxLength) {
                char[] tmp = new char[words.maxLength - w.length()];
                Arrays.fill(tmp, ' ');
                sb.append(tmp);
            }
            sb.append(" |\n");
            sb.append(buffer);
        }
        return sb.toString();
    }

    /** Prompt the user for input and make a word array from it */
    public static WordArray fromUserInput() {
        // Create an instance of the class
        WordArray words = new WordArray();

        // Using syntax `try (SOME_RESOURCE) {}` lets the resource close automatically
        // when leaving the try/catch block.
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Type in a list of words.");
            System.out.println("Input stops when you enter an empty line.");

            // Loop "forever"
            // Realistically, we loop indefinitely until the user enters an empty line
            while (true) {
                System.out.print("$> ");
                String raw = in.nextLine(); // Get the unedited user input
                String str = raw.trim(); // Remove leading and trailing whitespaces

                // If the user has typed an empty line (includes lines containing only spaces)
                if (str.isEmpty())
                    break; // Leave the loop now

                // Split `str` into an array of strings, using spaces (\s) as delimiters
                // Add all the srtings in the created array to the class instance
                words.addAll(str.split("\\s+"));
            }
        } catch (NoSuchElementException e) {
            // Standard input has been closed by the user
            // Print a line break to align [END OF INPUT] display properly
            System.out.print('\n');
        } finally {
            // Shift the cursor back up a line (\033[A)
            // Erase the line (\033[2K)
            // This doesn't work on Windows, I think.
            System.out.print("\033[A\033[2K");
            System.out.println("$> [END OF INPUT]");
        }
        return words;
    }
}
