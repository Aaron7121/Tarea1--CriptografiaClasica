public class AtbashCipher {
    public static String encrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                if (Character.isUpperCase(c)) {
                    result.append((char) ('Z' - (c - 'A')));
                } else {
                    result.append((char) ('z' - (c - 'a')));
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String decrypt(String text) {
        return encrypt(text);
    }
}
