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
 * WordBattleModel handles the game logic for the Word Battle game
 */

public class WordBattleModel
{
	private Player player1;
	private Player player2;
	private int currentTurn;
	private boolean isGameActive;
	private boolean isPlayer1Turn;
	private static final int MAX_TURNS = 6;
	private WordGenerator wordGenerator;
	private String player1TargetWord;
	private String player2TargetWord;
	
	/**
	 * Constructor for WordBattleModel
	 * @param player1 First player
	 * @param player2 Second player
	 */
	public WordBattleModel(Player player1, Player player2)
	{
		this.player1 = player1;
		this.player2 = player2;
		this.currentTurn = 1;
		this.isGameActive = false;
		this.isPlayer1Turn = true;
		this.wordGenerator = new WordGenerator();
		initializeTargetWords();
	}
	
	/**
	 * Initializes target words for both players from the WordGenerator
	 */
	private void initializeTargetWords()
	{
		String[] words = wordGenerator.generateWordsForPlayers();
		if (words != null && words.length == 2)
		{
			this.player1TargetWord = words[0];
			this.player2TargetWord = words[1];
		}
	}
	
	/**
	 * Starts the game
	 */
	public void startGame()
	{
		isGameActive = true;
		currentTurn = 1;
		isPlayer1Turn = true;
	}
	
	/**
	 * Ends the game
	 */
	public void endGame()
	{
		isGameActive = false;
	}
	
	/**
	 * Checks if the game is currently active
	 * @return true if game is active, false otherwise
	 */
	public boolean isGameActive()
	{
		return isGameActive;
	}
	
	/**
	 * Switches to the next turn
	 * Moves to the next player or advances the turn round
	 */
	public void nextTurn()
	{
		if (!isPlayer1Turn)
		{
			// Player 2 just finished, move to next turn round
			currentTurn++;
			if (currentTurn > MAX_TURNS)
			{
				endGame();
			}
		}
		// Toggle the current player
		isPlayer1Turn = !isPlayer1Turn;
	}
	
	/**
	 * Gets the current turn number
	 * @return the current turn number
	 */
	public int getCurrentTurn()
	{
		return currentTurn;
	}
	
	/**
	 * Checks if it is player 1's turn
	 * @return true if it's player 1's turn, false if it's player 2's turn
	 */
	public boolean isPlayer1Turn()
	{
		return isPlayer1Turn;
	}
	
	/**
	 * Marks player 1 as having won
	 */
	public void setPlayer1Won()
	{
		// TODO: Implement win tracking if needed
	}
	
	/**
	 * Marks player 2 as having won
	 */
	public void setPlayer2Won()
	{
		// TODO: Implement win tracking if needed
	}
	
	/**
	 * Checks if player 1 has won
	 * @return true if player 1 won, false otherwise
	 */
	public boolean hasPlayer1Won()
	{
		return false;
	}
	
	/**
	 * Checks if player 2 has won
	 * @return true if player 2 won, false otherwise
	 */
	public boolean hasPlayer2Won()
	{
		return false;
	}
	
	/**
	 * Gets the target word for the current player
	 * @return the target word that the current player is trying to guess
	 */
	public String getCurrentPlayerTargetWord()
	{
		return isPlayer1Turn ? player1TargetWord : player2TargetWord;
	}
	
	/**
	 * Gets the target word that player 1 is trying to guess
	 * @return the target word for player 1
	 */
	public String getPlayer1TargetWord()
	{
		return player1TargetWord;
	}
	
	/**
	 * Gets the target word that player 2 is trying to guess
	 * @return the target word for player 2
	 */
	public String getPlayer2TargetWord()
	{
		return player2TargetWord;
	}
	
	/**
	 * Checks if the current player's guess matches their target word
	 * If it matches, marks the player as won and checks for game end
	 * @param guess the word guessed by the current player
	 * @return true if the guess matches the target word, false otherwise
	 */
	public boolean checkGuess(String guess)
	{
		if (guess == null)
		{
			return false;
		}
		
		String normalizedGuess = guess.trim().toLowerCase();
		
		if (isPlayer1Turn)
		{
			if (normalizedGuess.equals(player1TargetWord))
			{
				setPlayer1Won();
				return true;
			}
		}
		else
		{
			if (normalizedGuess.equals(player2TargetWord))
			{
				setPlayer2Won();
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the feedback for a guess using LetterView analysis
	 * @param guess the guessed word
	 * @return array of feedback codes: 2=correct, 1=wrong position, 3=not in word
	 */
	public int[] getGuessFeedback(String guess)
	{
		String targetWord = isPlayer1Turn ? player1TargetWord : player2TargetWord;
		return LetterView.analyzeFeedback(guess.toLowerCase(), targetWord.toLowerCase());
	}
}
