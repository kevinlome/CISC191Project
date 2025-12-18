/**
 * GreenLetter - Letter is in correct position
 */
public class GreenLetter extends Letter
{

	public GreenLetter(char letter, int position)
	{
		super(letter, position);
	}

	@Override
	public int getFeedbackCode()
	{
		return 2; // Green = 2
	}

	@Override
	public String toString()
	{
		return "GREEN: " + letter + " at position " + position;
	}
}
