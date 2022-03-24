package dwwm.WordArray;

import dwwm.WordArray.WordArray.format;

public class Main {
    public static void main(String av[]) {
        WordArray words = WordArray.fromUserInput();

        System.out.println(words.toString(format.LINE));
        System.out.println(words.toString(format.PAGE));
        System.out.println(words.toString(format.TABLE));
    }
}
