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
 * Abstract superclass for letter feedback
 */
public abstract class Letter
{
	protected char letter;
	protected int position;

	public Letter(char letter, int position)
	{
		this.letter = letter;
		this.position = position;
	}

	public char getLetter()
	{
		return letter;
	}

	public int getPosition()
	{
		return position;
	}

	public abstract int getFeedbackCode();

	@Override
	public abstract String toString();
}
