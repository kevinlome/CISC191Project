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
 * Player class represents a player in the Word Battle game
 */
public class Player
{
	private String name;
	private String targetWord;
	private int[] guessHistory;
	private int currentGuessCount;
	private static final int MAX_GUESSES = 6;

	/**
	 * Constructor for Player with default name
	 */
	public Player()
	{
		this("Player");
	}

	/**
	 * Constructor for Player with specified name
	 * 
	 * @param name The player's name
	 */
	public Player(String name)
	{
		this.name = name != null && !name.isBlank() ? name : "Player";
		this.guessHistory = new int[MAX_GUESSES];
		this.currentGuessCount = 0;
	}

	/**
	 * Gets the player's name
	 * 
	 * @return the player's name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the player's name
	 * 
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name != null && !name.isBlank() ? name : "Player";
	}

	/**
	 * Gets the player's target word to be guessed
	 * 
	 * @return the target word
	 */
	public String getTargetWord()
	{
		return targetWord;
	}

	/**
	 * Sets the player's target word
	 * 
	 * @param targetWord the word for opponent to guess
	 */
	public void setTargetWord(String targetWord)
	{
		this.targetWord = targetWord;
	}

	/**
	 * Records a guess attempt
	 * 
	 * @param feedbackCode the feedback code for this guess
	 * @return true if recording was successful, false if max guesses reached
	 */
	public boolean recordGuess(int feedbackCode)
	{
		if (currentGuessCount < MAX_GUESSES)
		{
			guessHistory[currentGuessCount] = feedbackCode;
			currentGuessCount++;
			return true;
		}
		return false;
	}

	/**
	 * Gets the number of guesses made by this player
	 * 
	 * @return the current guess count
	 */
	public int getGuessCount()
	{
		return currentGuessCount;
	}

	/**
	 * Resets player state for a new game
	 */
	public void reset()
	{
		targetWord = null;
		guessHistory = new int[MAX_GUESSES];
		currentGuessCount = 0;
	}

	/**
	 * Checks if player has reached max guesses
	 * 
	 * @return true if max guesses reached, false otherwise
	 */
	public boolean hasMaxGuesses()
	{
		return currentGuessCount >= MAX_GUESSES;
	}
}


