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
    private int _maxLength = 0;

    public int maxLength() {
        return _maxLength;
    }

    /** Add words from an array of Strings */
    public boolean addAll(String[] words) {
        for (String w : words)
            _maxLength = Math.max(_maxLength, w.length());
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
    public String toString(format format, int width) {
        switch (format) {
            case LINE:
                return toLine(this, width);
            case PAGE:
                return toPage(this);
            case TABLE:
                return toTable(this);
            default: // In case `format` is none of the above
                throw new IllegalArgumentException("Unknown format: " + format);
        }
    }

    public String toString(format format) {
        return this.toString(format, -1);
    }

    /** Private method to help evaluate the length of a LINE with padded fields */
    private int _paddedLength(int width) {
        int len = 0;

        for (String word : this)
            len += Math.max(width, word.length());
        return len;
    }

    /**
     * Represent a WordArray as a LINE:
     * +-----+--------+-----+
     * | moo | foobar | baz |
     * +-----+--------+-----+
     **/
    public static String toLine(WordArray words, int width) {
        // If given width is a negative value, use the longest word's length
        if (width < 0)
            width = words._maxLength;

        // This will help us help put stuff together to create the string for output
        StringBuilder sb = new StringBuilder();

        // Line length =
        // 1 => Leading '+'
        // + total count of characters for every word + padding
        // + 3 * word count => for each word, leading '-' & trailing "-+"
        // + 1 => Final '\n'
        int length = 1 + words._paddedLength(width) + 3 * words.size() + 1;
        char[] buffer = new char[length]; // This will contain the decorative line
        int i = 0; // Index variable to move around in `buffer`

        /**
         * 1) Make the top and bottom decorator
         **/
        // Start with the first character
        buffer[i++] = '+';
        // For every word in our WordArray
        for (String w : words) {
            // Add one '-' to the line
            buffer[i++] = '-';
            // For the longest among either word width or padding width
            for (int offset = i; i - offset < Math.max(width, w.length()) + 1; i += 1) {
                // Add a '-' to the line
                buffer[i] = '-';
            }
            // Add the '+' separator to the line
            buffer[i++] = '+';
        }
        // Add a line break ad the end
        buffer[i] = '\n';
        // Append the top decorator to the final string
        sb.append(buffer);

        /**
         * 2) Element fields
         **/
        // Start with the first character
        sb.append("|");
        // For every word in our WordArray
        for (String w : words) {
            int pad = 0; // This will store the number of spaces around the words
            int len = w.length(); // Word length
            char[] spaces = null; // Similar to `buffer` but this one will store space characters

            // Add the first space
            sb.append(" ");
            // If padding width is bigger than word length
            if (w.length() < width) {
                // Get the count of spaces to put around the word
                pad = (width - len) / 2;
                // Create an empty array of size `pad`
                spaces = new char[pad];
                // Fill it with spaces
                Arrays.fill(spaces, ' ');
            }
            // If a padding size has been determined
            if (pad != 0)
                // Add the spaces to the string
                sb.append(spaces);
            // Add the current word to the string
            sb.append(w);
            // If there is a padding and the field width for current word is odd
            if (spaces != null && (width - len) % 2 != 0)
                // Add a space to line things up properly
                sb.append(' ');
            // If a padding size has been determined
            if (pad != 0)
                // Add the spaces again
                sb.append(spaces);
            // Add the separator
            sb.append(" |");
        }
        // Don't forget the line break !
        sb.append('\n');

        /**
         * 3) Add the bottom decorator to the string
         **/
        sb.append(buffer);

        /**
         * 4) Return the final result
         **/
        return sb.toString();
    }

    public static String toLine(WordArray words) {
        return toLine(words, -1);
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
        int length = "|  |".length() + words._maxLength + 1;
        char[] buffer = new char[length]; // This will contain the decorative line
        int i = 0; // Index to move around in `buffer`

        /* 1) Make the line */
        // Start with "+-"
        buffer[i++] = '+';
        buffer[i++] = '-';

        // For every character in the longest word of the array + 1
        for (int offset = i; i - offset < words._maxLength + 1; i += 1)
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
            if (w.length() < words._maxLength) {
                // Create a temporary buffer
                char[] tmp = new char[words._maxLength - w.length()];
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
        int length = "|  |".length() + words._maxLength + 1;
        char[] buffer = new char[length];
        int i = 0;

        buffer[i++] = '+';
        buffer[i++] = '-';
        for (int offset = i; i - offset < words._maxLength + 1; i += 1)
            buffer[i] = '-';
        buffer[i++] = '+';
        buffer[i] = '\n';
        sb.append(buffer);
        for (String w : words) {
            sb.append("| " + w);
            if (w.length() < words._maxLength) {
                char[] tmp = new char[words._maxLength - w.length()];
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
