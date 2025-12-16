/**
 * GrayLetter - Letter is not in word
 */
public class GrayLetter extends Letter {
    
    public GrayLetter(char letter, int position) {
        super(letter, position);
    }
    
    @Override
    public int getFeedbackCode() {
        return 3; // Gray = 3
    }
    
    @Override
    public String toString() {
        return "GRAY: " + letter + " at position " + position;
    }
}
