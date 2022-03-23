package dwwm.WordArray;

// Import the `LINE`, `PAGE` and `TABLE` enum
import dwwm.WordArray.WordArray.format;

public class Main {
    public static void main(String av[]) {
        WordArray words = WordArray.fromUserInput();
        System.out.println(words.toString(format.LINE));
        System.out.println(words.toString(format.PAGE));
        System.out.println(words.toString(format.TABLE));
    }
}
