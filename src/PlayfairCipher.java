import java.util.*;

public class PlayfairCipher {
    private static final String ALPHABET = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
    private char[][] keyTable;
    private Map<Character, int[]> charPosition;
    
    public PlayfairCipher(String key) {
        keyTable = new char[5][5];
        charPosition = new HashMap<>();
        generateKeyTable(key);
    }
    
    private void generateKeyTable(String key) {
        Set<Character> used = new LinkedHashSet<>();
        String processedKey = (key + ALPHABET).toUpperCase();
        
        for (char c : processedKey.toCharArray()) {
            if (c >= 'A' && c <= 'Z' && c != 'J' && !used.contains(c)) {
                used.add(c);
            }
        }
        
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                char c = (Character) used.toArray()[index++];
                keyTable[i][j] = c;
                charPosition.put(c, new int[]{i, j});
            }
        }
    }
    
    public String encrypt(String plaintext) {
        plaintext = plaintext.replaceAll("[^a-zA-Z]", "").toUpperCase().replace('J', 'I');
        if (plaintext.length() % 2 != 0) plaintext += 'X';
        
        StringBuilder result = new StringBuilder();
        StringBuilder visual = new StringBuilder();
        visual.append("Pares: ");
        
        for (int i = 0; i < plaintext.length(); i += 2) {
            char c1 = plaintext.charAt(i);
            char c2 = plaintext.charAt(i + 1);
            
            visual.append(c1).append(c2).append(" ");
            
            int[] pos1 = charPosition.get(c1);
            int[] pos2 = charPosition.get(c2);
            
            char enc1, enc2;
            
            if (pos1[0] == pos2[0]) {
                enc1 = keyTable[pos1[0]][(pos1[1] + 1) % 5];
                enc2 = keyTable[pos2[0]][(pos2[1] + 1) % 5];
            } else if (pos1[1] == pos2[1]) {
                enc1 = keyTable[(pos1[0] + 1) % 5][pos1[1]];
                enc2 = keyTable[(pos2[0] + 1) % 5][pos2[1]];
            } else {
                enc1 = keyTable[pos1[0]][pos2[1]];
                enc2 = keyTable[pos2[0]][pos1[1]];
            }
            
            result.append(enc1).append(enc2);
        }
        
        return result.toString();
    }
    
    public String decrypt(String ciphertext) {
        ciphertext = ciphertext.replaceAll("[^a-zA-Z]", "").toUpperCase();
        
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < ciphertext.length(); i += 2) {
            char c1 = ciphertext.charAt(i);
            char c2 = ciphertext.charAt(i + 1);
            
            int[] pos1 = charPosition.get(c1);
            int[] pos2 = charPosition.get(c2);
            
            char dec1, dec2;
            
            if (pos1[0] == pos2[0]) {
                dec1 = keyTable[pos1[0]][(pos1[1] - 1 + 5) % 5];
                dec2 = keyTable[pos2[0]][(pos2[1] - 1 + 5) % 5];
            } else if (pos1[1] == pos2[1]) {
                dec1 = keyTable[(pos1[0] - 1 + 5) % 5][pos1[1]];
                dec2 = keyTable[(pos2[0] - 1 + 5) % 5][pos2[1]];
            } else {
                dec1 = keyTable[pos1[0]][pos2[1]];
                dec2 = keyTable[pos2[0]][pos1[1]];
            }
            
            result.append(dec1).append(dec2);
        }
        
        return result.toString();
    }
    
    public String getKeyTableDisplay() {
        StringBuilder sb = new StringBuilder("Tabla Playfair:\n");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                sb.append(keyTable[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
