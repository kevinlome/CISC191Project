/**
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
