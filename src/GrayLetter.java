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
 * GrayLetter - Letter is not in word
 */
public class GrayLetter extends Letter
{

	public GrayLetter(char letter, int position)
	{
		super(letter, position);
	}

	@Override
	public int getFeedbackCode()
	{
		return 3; // Gray = 3
	}

	@Override
	public String toString()
	{
		return "GRAY: " + letter + " at position " + position;
	}
}
