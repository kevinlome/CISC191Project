/**
 * Lead Author(s):
 * @author Kevin Lome
 * @author Nick Damion
 *
 * Other contributors:
 *
 * References:
 * Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented Problem Solving.
 * Retrieved from https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 *
 * Version/date: December 18, 2025
 *
 * Responsibilities:
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
