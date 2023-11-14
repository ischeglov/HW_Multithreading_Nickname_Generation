import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger counterThree = new AtomicInteger();
    public static AtomicInteger counterFour = new AtomicInteger();
    public static AtomicInteger counterFive = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindrome = new Thread(
                () -> {
                    for (String text : texts) {
                        if (isPalindrome(text) && !isSameLetter(text)) {
                            incrementCounter(text.length());
                        }
                    }
                });
        palindrome.start();

        Thread sameLetter = new Thread(
                () -> {
                    for (String text : texts) {
                        if (isSameLetter(text)) {
                            incrementCounter(text.length());
                        }
                    }
                });
        sameLetter.start();

        Thread lettersAscending = new Thread(
                () -> {
                    for (String text : texts) {
                        if (!isPalindrome(text) && isLettersAscending(text)) {
                            incrementCounter(text.length());
                        }
                    }
                });
        lettersAscending.start();

        palindrome.join();
        sameLetter.join();
        lettersAscending.join();

        System.out.println("Красивых слов с длиной 3: " + counterThree);
        System.out.println("Красивых слов с длиной 4: " + counterFour);
        System.out.println("Красивых слов с длиной 5: " + counterFive);
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean isPalindrome(String text) {
        return text.equals(new StringBuilder(text).reverse().toString());
    }

    public static boolean isSameLetter(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != text.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static boolean isLettersAscending(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static void incrementCounter(int textLength) {
        if (textLength == 3) {
            counterThree.getAndIncrement();
        } else if (textLength == 4) {
            counterFour.getAndIncrement();
        } else {
            counterFive.getAndIncrement();
        }
    }
}
