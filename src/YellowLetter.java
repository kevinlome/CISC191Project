/**
 * YellowLetter - Letter is in word but wrong position
 */
public class YellowLetter extends Letter {
    
    public YellowLetter(char letter, int position) {
        super(letter, position);
    }
    
    @Override
    public int getFeedbackCode() {
        return 1; // Yellow = 1
    }
    
    @Override
    public String toString() {
        return "YELLOW: " + letter + " at position " + position;
    }
}
