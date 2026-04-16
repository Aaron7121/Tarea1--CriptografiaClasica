public class VigenereCipher {
    public static String encrypt(String plaintext, String key) {
        String result = "";
        key = key.toUpperCase();
        plaintext = plaintext.toUpperCase();
        int keyIndex = 0;
        for (char c : plaintext.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                char shift = key.charAt(keyIndex % key.length());
                char encryptedChar = (char) ((c + shift - 2 * 'A') % 26 + 'A');
                result += encryptedChar;
                keyIndex++;
            } else {
                result += c;
            }
        }
        return result;
    }

    public static String decrypt(String ciphertext, String key) {
        String result = "";
        key = key.toUpperCase();
        ciphertext = ciphertext.toUpperCase();
        int keyIndex = 0;
        for (char c : ciphertext.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                char shift = key.charAt(keyIndex % key.length());
                char decryptedChar = (char) ((c - shift + 26) % 26 + 'A');
                result += decryptedChar;
                keyIndex++;
            } else {
                result += c;
            }
        }
        return result;
    }
}

