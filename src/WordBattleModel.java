
public class WordBattleModel
{
	private Player player1;
	private Player player2;
	private int currentTurn;
	private boolean isGameActive;
	private boolean player1Won;
	private boolean player2Won;
	private boolean isPlayer1Turn;
	private static final int MAX_TURNS = 10;
	
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
		this.player1Won = false;
		this.player2Won = false;
		this.isPlayer1Turn = true;
	}
	
	/**
	 * Starts the game
	 */
	public void startGame()
	{
		isGameActive = true;
		currentTurn = 1;
		player1Won = false;
		player2Won = false;
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
	 * Gets the current player
	 * @return the player whose turn it is
	 */
	public Player getCurrentPlayer()
	{
		return isPlayer1Turn ? player1 : player2;
	}
	
	/**
	 * Gets the other player (not the current player)
	 * @return the player who is not taking their turn
	 */
	public Player getOtherPlayer()
	{
		return isPlayer1Turn ? player2 : player1;
	}
	
	/**
	 * Marks player 1 as having won
	 */
	public void setPlayer1Won()
	{
		player1Won = true;
		checkGameEnd();
	}
	
	/**
	 * Marks player 2 as having won
	 */
	public void setPlayer2Won()
	{
		player2Won = true;
		checkGameEnd();
	}
	
	/**
	 * Checks if player 1 has won
	 * @return true if player 1 won, false otherwise
	 */
	public boolean hasPlayer1Won()
	{
		return player1Won;
	}
	
	/**
	 * Checks if player 2 has won
	 * @return true if player 2 won, false otherwise
	 */
	public boolean hasPlayer2Won()
	{
		return player2Won;
	}
	
	/**
	 * Checks the game end condition:
	 * Game ends when one player guesses their target word and the other does not
	 * If both have won or both have not won on the same turn, game continues
	 */
	private void checkGameEnd()
	{
		// Only check if both players have completed their turn in this round
		if (isPlayer1Turn)
		{
			// Player 2 just won, but we need to wait for Player 1's turn
			return;
		}
		
		// Both players have now taken their turns in this round
		// Game ends if exactly one player has won
		if (player1Won && !player2Won)
		{
			endGame();
		}
		else if (player2Won && !player1Won)
		{
			endGame();
		}
		// If both won or neither won, continue the game
	}
	
	/**
	 * Gets the winner of the game
	 * @return player1 if player1 won, player2 if player2 won, null if game hasn't ended or it's a tie
	 */
	public Player getWinner()
	{
		if (!isGameActive && player1Won && !player2Won)
		{
			return player1;
		}
		else if (!isGameActive && player2Won && !player1Won)
		{
			return player2;
		}
		return null;
	}
}
