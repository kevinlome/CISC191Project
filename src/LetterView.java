/**
 * LetterView contains feedback logic for guess feedback
 */
public class LetterView {
    
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