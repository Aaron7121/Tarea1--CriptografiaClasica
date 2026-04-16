public class RailFenceCipher {
    public static String encrypt(String text, int rails) {
        if (rails <= 1) return text;
        
        text = text.replaceAll("[^a-zA-Z]", "");
        StringBuilder[] fence = new StringBuilder[rails];
        for (int i = 0; i < rails; i++) {
            fence[i] = new StringBuilder();
        }
        
        int rail = 0;
        int direction = 1;
        
        for (char c : text.toCharArray()) {
            fence[rail].append(c);
            if (rail == 0) direction = 1;
            else if (rail == rails - 1) direction = -1;
            rail += direction;
        }
        
        StringBuilder result = new StringBuilder();
        for (StringBuilder sb : fence) {
            result.append(sb);
        }
        return result.toString();
    }
    
    public static String decrypt(String text, int rails) {
        if (rails <= 1) return text;
        
        int len = text.length();
        int[] railLengths = new int[rails];
        
        int rail = 0;
        int direction = 1;
        for (int i = 0; i < len; i++) {
            railLengths[rail]++;
            if (rail == 0) direction = 1;
            else if (rail == rails - 1) direction = -1;
            rail += direction;
        }
        
        String[] fenceText = new String[rails];
        int index = 0;
        for (int i = 0; i < rails; i++) {
            fenceText[i] = text.substring(index, index + railLengths[i]);
            index += railLengths[i];
        }
        
        StringBuilder result = new StringBuilder();
        int[] fenceIndex = new int[rails];
        rail = 0;
        direction = 1;
        
        for (int i = 0; i < len; i++) {
            result.append(fenceText[rail].charAt(fenceIndex[rail]++));
            if (rail == 0) direction = 1;
            else if (rail == rails - 1) direction = -1;
            rail += direction;
        }
        
        return result.toString();
    }
    
    public static String visualize(String text, int rails) {
        if (rails <= 1) return text;
        
        text = text.replaceAll("[^a-zA-Z]", "");
        int len = text.length();
        int[] positions = new int[rails];
        
        StringBuilder visual = new StringBuilder();
        int rail = 0;
        int direction = 1;
        
        for (int r = 0; r < rails; r++) {
            rail = 0;
            direction = 1;
            for (int i = 0; i < len; i++) {
                if (rail == r) {
                    visual.append(text.charAt(i)).append(" ");
                } else {
                    visual.append("  ");
                }
                if (rail == 0) direction = 1;
                else if (rail == rails - 1) direction = -1;
                rail += direction;
            }
            visual.append("\n");
        }
        return visual.toString();
    }
}
