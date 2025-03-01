import java.io.*;
import java.security.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {

        System.out.println("SHA256 hash:\n" + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA256").digest("barbora cigankova".getBytes())));

        try (Reader reader = new FileReader("/home/barbora/LS/BEZIT/sem2/cipher");
             BufferedReader brd = new BufferedReader(reader)) {

            String cipher = brd.readLine().toLowerCase(); //cela cipher je jedna radka
            printLastnReversed(countLetterFrequency(cipher), 10);
            printLastnReversed(countBigramFrequency(cipher), 20);
            printLastnReversed(countDoublesFrequency(cipher), 10);

            char[] bindings = "btkxzvwymojnpiaqluscdhregf".toCharArray();
            System.out.println(constructPlaintext(cipher, bindings));

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*

        Hadani

        cipher = cipher.replaceAll("x", "e");
        cipher = cipher.replaceAll("b", "t");
        cipher = cipher.replaceAll("v", "h"); //temer stoprocentne to jsou tyhle znaky podle letterFrequency a bigramu

        cipher = cipher.replaceAll("o", "a");  //?
        cipher = cipher.replaceAll("l", "n");
        cipher = cipher.replaceAll("w", "r");
        cipher = cipher.replaceAll("q", "l");
        cipher = cipher.replaceAll("j", "o");

        cipher = cipher.replaceAll("g", "w");
        //cipher = cipher.replaceAll("n", "i");
        cipher = cipher.replaceAll("y", "g");
        cipher = cipher.replaceAll("u", "d");
        cipher = cipher.replaceAll("i", "m");
        cipher = cipher.replaceAll("f", "v");

        a = b
        b = t
        c = k
        d = x
        e = z
        f = v
        g = w
        h = y
        i = m
        j = o
        k = j
        l = n
        m = p
        n = i
        o = a
        p = q
        q = l
        r = u
        s = s
        t = c
        u = d
        v = h
        w = r
        x = e
        y = g
        z = f
         */
    }

    static void printLastnReversed(String[] array, int n) {
        for(int i = 1; i <= n; i++)
            System.out.print(array[array.length - i] + " ");
        System.out.println();
    }

    static String[] countLetterFrequency(String cipher) {
        int counter = 0;
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String[] letterFrequency = new String[26];

        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < cipher.length(); j++) {
                if (alphabet.charAt(i) == cipher.charAt(j))
                    counter++;
            }
            letterFrequency[i] = alphabet.charAt(i) + ": " + String.format("%.2f", (((double) counter / cipher.toLowerCase().length()) * 100));   //prida pismeno po lepsi citelnost
            counter = 0;
        }
        Arrays.sort(letterFrequency, Comparator.comparingDouble(n -> Double.parseDouble(n.split(": ")[1])));
        return letterFrequency;
    }

    static String[] countBigramFrequency(String cipher) {
        int counter = 0;
        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        String[] bigramFrequency = new String[26 * 26];
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                for (int k = 0; k < cipher.length() - 1; k++) {
                    if (alphabet.charAt(i) == cipher.charAt(k) && alphabet.charAt(j) == cipher.charAt(k + 1))
                        counter++;
                }
                bigramFrequency[i * 26 + j] = alphabet.charAt(i) + "_" + alphabet.charAt(j) + ": " + String.format("%.2f", (((double) counter / cipher.toLowerCase().length()) * 100));
                counter = 0;
            }
        }
        Arrays.sort(bigramFrequency, Comparator.comparingDouble(n -> Double.parseDouble(n.split(": ")[1])));
        return bigramFrequency;
    }

    static String[] countDoublesFrequency(String cipher) {
        int counter = 0;
        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        String[] doublesFrequency = new String[26];
        for (int i = 0; i < 26; i++) {
            for (int k = 0; k < cipher.length() - 1; k++) {
                if (alphabet.charAt(i) == cipher.charAt(k) && alphabet.charAt(i) == cipher.charAt(k + 1))
                    counter++;
            }
            doublesFrequency[i] = alphabet.charAt(i) + "_" + alphabet.charAt(i) + ": " + String.format("%.2f", (((double) counter / cipher.toLowerCase().length()) * 100));
            counter = 0;
        }
        Arrays.sort(doublesFrequency, Comparator.comparingDouble(n -> Double.parseDouble(n.split(": ")[1])));
        return doublesFrequency;
    }

    static char[] constructPlaintext(String cipher, char[] bindings) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        char[] cipherChar = cipher.toCharArray();
        for(int i = 0; i < cipher.length(); i++) {
            for(int j = 0; j < 26; j++) {
                if(cipher.charAt(i) == alphabet.charAt(j)) {
                    cipherChar[i] = bindings[j];
                }
            }
        }
        return cipherChar;
    }
}
