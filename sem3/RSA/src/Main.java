import java.math.BigInteger;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String publicKey = "";
        String publicKeyExponent = "";
        String privateKey = "";
        String personalNumber = "";
        StringBuilder ciphertext = new StringBuilder();
        StringBuilder decryptedMessage = new StringBuilder();

        //bloky musi mit urcitou delku aby decrypt vedel s jak velkym cislem pocitat
        int blockLength = publicKey.length();

        BigInteger message = new BigInteger(personalNumber).multiply(new BigInteger("2025").pow(60));
        System.out.println("message: " + message);


       ArrayList<String> messageDivision = divide(message.toString(), blockLength, publicKey);
        for(String msg: messageDivision) {
            ciphertext.append(padd(encrypt(msg, publicKeyExponent, publicKey), blockLength));
        }

        System.out.println("ciphertext: " + ciphertext);
        ArrayList<String> cipherDivision = divide(ciphertext.toString(), blockLength, publicKey);
        for(String msg: cipherDivision) {
            decryptedMessage.append(decrypt(msg, privateKey, publicKey));
        }

        System.out.println("decrypted message: " + decryptedMessage);
        //vydelime 2025^60 pro kontrolu
        System.out.println(new BigInteger(decryptedMessage.toString()).divide(new BigInteger("2025").pow(60)));
    }


    public static ArrayList<String> divide(String message, int length, String publicKey) {
        //rozdeli zpravu na podretezce delky n
        ArrayList<String> division = new ArrayList<>();
        int cursor = 0;
        String div;
        while(cursor + length < message.length()) {
            div = message.substring(cursor, cursor + length);

            //pro pripad ze by byla vcast msg vetsi nez mod
            if(0 < new BigInteger(div).compareTo(new BigInteger(publicKey))) {
                div = message.substring(cursor, cursor + length - 1);
                division.add("0");
                division.add(div);
                cursor += length - 1;
            }
            else {
                division.add(div);
                cursor += length;
            }
        }
        division.add(message.substring(cursor));    //ten posledni podretezec co je kratsi nez length - 1
        return division;
    }

    public static String encrypt(String message, String publicKeyExponent, String publicKey) {

        return new BigInteger(message).modPow(new BigInteger(publicKeyExponent), new BigInteger(publicKey)).toString();
    }

    public static String decrypt(String ciphertext, String privateKey, String publicKey) {
        //cipher ^ d mod n
        return new BigInteger(ciphertext).modPow(new BigInteger(privateKey), new BigInteger(publicKey)).toString();
    }

    public static String padd(String msg, int blockLength) {
        //prida nuly tak, aby byl blok zarovnany
        int diff = blockLength - msg.length();
        StringBuilder padded = new StringBuilder(msg);
        if(diff != 0) {
            padded.insert(0,  new StringBuilder().repeat('0', diff));
        }
        return padded.toString();
    }
}
