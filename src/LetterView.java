/**
 * LetterView contains feedback classes for guess feedback
 */
public class LetterView {
    
    /**
     * GreenLetter - Letter is in correct position
     */
    public static class GreenLetter {
        private char letter;
        private int position;
        
        public GreenLetter(char letter, int position) {
            this.letter = letter;
            this.position = position;
        }
        
        public char getLetter() {
            return letter;
        }
        
        public int getPosition() {
            return position;
        }
        
        public int getFeedbackCode() {
            return 2; // Green = 2
        }
        
        @Override
        public String toString() {
            return "GREEN: " + letter + " at position " + position;
        }
    }
    
    /**
     * YellowLetter - Letter is in word but wrong position
     */
    public static class YellowLetter {
        private char letter;
        private int guessPosition;
        
        public YellowLetter(char letter, int guessPosition) {
            this.letter = letter;
            this.guessPosition = guessPosition;
        }
        
        public char getLetter() {
            return letter;
        }
        
        public int getGuessPosition() {
            return guessPosition;
        }
        
        public int getFeedbackCode() {
            return 1; // Yellow = 1
        }
        
        @Override
        public String toString() {
            return "YELLOW: " + letter + " at position " + guessPosition;
        }
    }
    
    /**
     * GrayLetter - Letter is not in word
     */
    public static class GrayLetter {
        private char letter;
        private int position;
        
        public GrayLetter(char letter, int position) {
            this.letter = letter;
            this.position = position;
        }
        
        public char getLetter() {
            return letter;
        }
        
        public int getPosition() {
            return position;
        }
        
        public int getFeedbackCode() {
            return 3; // Gray = 3
        }
        
        @Override
        public String toString() {
            return "GRAY: " + letter + " at position " + position;
        }
    }
    
    /**
     * Analyzes a guess and returns feedback
     * @param guess The guessed word (lowercase)
     * @param target The target word (lowercase)
     * @return int[] array where each index contains feedback code: 2=green, 1=yellow, 3=gray
     */
    public static int[] analyzeFeedback(String guess, String target) {
        if (guess == null || target == null || guess.length() != 5 || target.length() != 5) {
            return new int[5];
        }
        
        int[] feedback = new int[5];
        boolean[] targetUsed = new boolean[5];
        
        // First pass: mark exact matches (green - 2)
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                feedback[i] = 2; // Green
                targetUsed[i] = true;
            }
        }
        
        // Second pass: mark wrong position (yellow - 1) and not in word (gray - 3)
        for (int i = 0; i < 5; i++) {
            if (feedback[i] == 0) { // Not already marked as correct
                char letterGuess = guess.charAt(i);
                boolean found = false;
                
                // Look for this letter in unused positions of target word
                for (int j = 0; j < 5; j++) {
                    if (!targetUsed[j] && letterGuess == target.charAt(j)) {
                        feedback[i] = 1; // Yellow
                        targetUsed[j] = true;
                        found = true;
                        break;
                    }
                }
                
                if (!found) {
                    feedback[i] = 3; // Gray
                }
            }
        }
        
        return feedback;
    }
}