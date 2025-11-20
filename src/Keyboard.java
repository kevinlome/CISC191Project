import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

public class Keyboard extends JPanel implements KeyListener
{
	private static final Color GREY = new Color(82, 81, 81);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color BUTTON_COLOR = new Color(129, 131, 132);
	private static final Color CORRECT_COLOR = new Color(106, 170, 100); // Green
	private static final Color WRONG_POSITION_COLOR = new Color(181, 159, 59); // Yellow
	private static final Color NOT_IN_WORD_COLOR = new Color(58, 58, 58); // Dark Gray
	
	private String[] row1 = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
	private String[] row2 = {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
	private String[] row3 = {"DELETE", "Z", "X", "C", "V", "B", "N", "M", "ENTER"};
	
	private Map<String, JButton> letterButtons;
	private Map<String, Integer> letterStatus; // 0=unused, 1=wrong position, 2=correct, 3=not in word
	private Map<String, Integer> player1LetterStatus; // Stored feedback for Player 1
	private Map<String, Integer> player2LetterStatus; // Stored feedback for Player 2
	private JTextField currentTextField;
	private JTextField[][] currentGridPlayer1;
	private JTextField[][] currentGridPlayer2;
	private WordBattleModel model;
	private int currentRow;
	private int currentCol;
	private static final int GRID_ROWS = 6;
	private static final int GRID_COLS = 5;
	private String player1Name = "Player 1";
	private String player2Name = "Player 2";
	
	public Keyboard()
	{
		this.letterButtons = new HashMap<>();
		this.letterStatus = new HashMap<>();
		this.player1LetterStatus = new HashMap<>();
		this.player2LetterStatus = new HashMap<>();
		
		// Initialize all letters with unused status (0)
		for (String letter : row1)
		{
			if (!letter.equals("DELETE") && !letter.equals("ENTER"))
			{
				letterStatus.put(letter, 0);
				player1LetterStatus.put(letter, 0);
				player2LetterStatus.put(letter, 0);
			}
		}
		for (String letter : row2)
		{
			if (!letter.equals("DELETE") && !letter.equals("ENTER"))
			{
				letterStatus.put(letter, 0);
				player1LetterStatus.put(letter, 0);
				player2LetterStatus.put(letter, 0);
			}
		}
		for (String letter : row3)
		{
			if (!letter.equals("DELETE") && !letter.equals("ENTER"))
			{
				letterStatus.put(letter, 0);
				player1LetterStatus.put(letter, 0);
				player2LetterStatus.put(letter, 0);
			}
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(GREY);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setPreferredSize(new Dimension(0, 300));
		
		// Add key listener for physical keyboard support
		setFocusable(true);
		addKeyListener(this);
		
		createKeyboardRows();
	}
	
	/**
	 * Set the model for guess checking
	 */
	public void setModel(WordBattleModel model)
	{
		this.model = model;
	}
	
	/**
	 * Set the player names for display purposes
	 */
	public void setPlayerNames(String player1, String player2)
	{
		this.player1Name = player1;
		this.player2Name = player2;
	}
	
	/**
	 * Set both grids for the game
	 */
	public void setGrids(JTextField[][] gridPlayer1, JTextField[][] gridPlayer2)
	{
		this.currentGridPlayer1 = gridPlayer1;
		this.currentGridPlayer2 = gridPlayer2;
	}
	
	/**
	 * Set the current grid and row based on whose turn it is
	 */
	public void setCurrentGrid(JTextField[][] grid, int row)
	{
		// Update the appropriate grid based on which grid was passed
		if (grid == currentGridPlayer1)
		{
			currentGridPlayer1 = grid;
		}
		else if (grid == currentGridPlayer2)
		{
			currentGridPlayer2 = grid;
		}
		currentRow = row;
	}
	
	/**
	 * Get the current grid based on whose turn it is
	 */
	private JTextField[][] getCurrentGrid()
	{
		return model != null && model.isPlayer1Turn() ? currentGridPlayer1 : currentGridPlayer2;
	}
	
	/**
	 * Update letter status based on game feedback
	 * status: 0=unused, 1=wrong position (yellow), 2=correct (green), 3=not in word (gray)
	 * Stores feedback for both current player and their history
	 */
	public void updateLetterStatus(String letter, int status)
	{
		// Only update if the new status is "worse" (higher number) than current
		Integer currentStatus = letterStatus.get(letter);
		if (currentStatus == null || status > currentStatus)
		{
			letterStatus.put(letter, status);
			
			// Also store in the player-specific map
			if (model != null && model.isPlayer1Turn())
			{
				Integer p1Status = player1LetterStatus.get(letter);
				if (p1Status == null || status > p1Status)
				{
					player1LetterStatus.put(letter, status);
				}
			}
			else if (model != null)
			{
				Integer p2Status = player2LetterStatus.get(letter);
				if (p2Status == null || status > p2Status)
				{
					player2LetterStatus.put(letter, status);
				}
			}
			
			updateButtonColor(letter);
		}
	}
	
	/**
	 * Update the button color based on letter status
	 */
	private void updateButtonColor(String letter)
	{
		JButton button = letterButtons.get(letter);
		if (button != null)
		{
			Integer status = letterStatus.get(letter);
			if (status != null)
			{
				switch (status)
				{
					case 2: // Correct
						button.setBackground(CORRECT_COLOR);
						break;
					case 1: // Wrong position
						button.setBackground(WRONG_POSITION_COLOR);
						break;
					case 3: // Not in word
						button.setBackground(NOT_IN_WORD_COLOR);
						break;
					default: // Unused
						button.setBackground(BUTTON_COLOR);
				}
				button.setOpaque(true);
			}
		}
	}
	
	private void createKeyboardRows()
	{
		// Row 1
		JPanel row1Panel = createRow(row1);
		add(row1Panel);
		add(Box.createVerticalStrut(2));
		
		// Row 2
		JPanel row2Panel = createRow(row2);
		row2Panel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
		add(row2Panel);
		add(Box.createVerticalStrut(2));
		
		// Row 3
		JPanel row3Panel = createRow(row3);
		row3Panel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));
		add(row3Panel);
	}
	
	private JPanel createRow(String[] letters)
	{
		JPanel rowPanel = new JPanel();
		rowPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
		rowPanel.setBackground(GREY);
		
		for (String letter : letters)
		{
			JButton button = new JButton(letter);
			button.setFont(new Font("Arial", Font.BOLD, 16));
			
			// Make DELETE and ENTER buttons larger
			if (letter.equals("DELETE"))
			{
				button.setPreferredSize(new Dimension(130, 65));
				button.setMinimumSize(new Dimension(130, 65));
				button.setMaximumSize(new Dimension(130, 65));
				button.addActionListener(e -> handleDelete());
			}
			else if (letter.equals("ENTER"))
			{
				button.setPreferredSize(new Dimension(130, 65));
				button.setMinimumSize(new Dimension(130, 65));
				button.setMaximumSize(new Dimension(130, 65));
				button.addActionListener(e -> handleEnter());
			}
			else
			{
				button.setPreferredSize(new Dimension(65, 65));
				button.setMinimumSize(new Dimension(65, 65));
				button.setMaximumSize(new Dimension(65, 65));
				String finalLetter = letter;
				button.addActionListener(e -> handleLetterInput(finalLetter));
			}
			
			button.setBackground(BUTTON_COLOR);
			button.setForeground(TEXT_COLOR);
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setOpaque(true);
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setFocusable(false);
			
			letterButtons.put(letter, button);
			rowPanel.add(button);
		}
		
		return rowPanel;
	}
	
	private void handleLetterInput(String letter)
	{
		// Get the current grid based on whose turn it is
		JTextField[][] currentGrid = getCurrentGrid();
		
		// Only allow input in the row corresponding to the current turn
		if (model != null && currentRow != (model.getCurrentTurn() - 1))
		{
			System.out.println("Input blocked: Current turn is " + model.getCurrentTurn() + ", but trying to input in row " + currentRow);
			return;
		}
		
		if (currentTextField != null && currentTextField.getText().isEmpty())
		{
			try
			{
				// Insert through the document to trigger the filter properly
				currentTextField.getDocument().insertString(0, letter, null);
				System.out.println("Letter inserted: " + letter + " at [" + currentRow + "][" + currentCol + "]");
				
				// Move focus to the next cell
				SwingUtilities.invokeLater(() -> {
					int nextCol = currentCol + 1;
					int nextRow = currentRow;
					
					// If we've reached the end of the row, stay in same row (don't wrap)
					if (nextCol >= GRID_COLS)
					{
						nextCol = GRID_COLS - 1;
					}
					
					if (currentGrid != null && nextRow < GRID_ROWS && nextCol < GRID_COLS)
					{
						currentGrid[nextRow][nextCol].requestFocus();
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void handleDelete()
	{
		JTextField[][] currentGrid = getCurrentGrid();
		
		if (currentTextField != null)
		{
			// If current cell is empty, move to previous cell and clear it
			if (currentTextField.getText().isEmpty() && currentGrid != null)
			{
				// Move to previous cell
				final int prevCol = currentCol - 1;
				final int prevRow = currentRow;
				
				// Calculate final position
				int finalCol = prevCol;
				int finalRow = prevRow;
				
				// If at the beginning of a row, move to end of previous row
				if (finalCol < 0)
				{
					finalRow--;
					finalCol = GRID_COLS - 1;
				}
				
				// If we went before the start, stay at the beginning
				if (finalRow < 0)
				{
					finalRow = 0;
					finalCol = 0;
				}
				
				final int targetRow = finalRow;
				final int targetCol = finalCol;
				
				// Clear the previous cell and move focus there
				clearTextField(currentGrid[targetRow][targetCol]);
				SwingUtilities.invokeLater(() -> {
					currentGrid[targetRow][targetCol].requestFocus();
					// Update current position to the previous cell
					currentRow = targetRow;
					currentCol = targetCol;
					currentTextField = currentGrid[targetRow][targetCol];
				});
				System.out.println("DELETE: Moved to previous cell [" + targetRow + "][" + targetCol + "] and cleared it");
			}
			else
			{
				// If current cell has content, just clear it
				clearTextField(currentTextField);
				System.out.println("DELETE: Cleared current cell");
				SwingUtilities.invokeLater(() -> currentTextField.requestFocus());
			}
		}
	}
	
	/**
	 * Helper method to clear a text field by removing all its content
	 */
	private void clearTextField(JTextField textField)
	{
		try
		{
			int length = textField.getDocument().getLength();
			if (length > 0)
			{
				textField.getDocument().remove(0, length);
			}
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}
	
	private void handleEnter()
	{
		JTextField[][] currentGrid = getCurrentGrid();
		
		if (currentTextField != null && model != null && currentGrid != null)
		{
			// Get the complete word from the current row
			StringBuilder guessBuilder = new StringBuilder();
			for (int col = 0; col < currentGrid[currentRow].length; col++)
			{
				String text = currentGrid[currentRow][col].getText();
				if (!text.isEmpty())
				{
					guessBuilder.append(text);
				}
			}
			
			String guess = guessBuilder.toString();
			
			// Check if the guess is complete (5 letters)
			if (guess.length() == 5)
			{
				// Check if the guess matches the target word
				if (model.checkGuess(guess))
				{
					// Victory! Display message
					String playerName = model.isPlayer1Turn() ? "Player 1" : "Player 2";
					JOptionPane.showMessageDialog(null,
							playerName + " wins! The word was: " + guess,
							"Victory!",
							JOptionPane.INFORMATION_MESSAGE);
					
					// Mark all letters as correct
					for (char c : guess.toCharArray())
					{
						updateLetterStatus(String.valueOf(c).toUpperCase(), 2);
					}
					
					// End the game
					model.endGame();
					SwingUtilities.invokeLater(() -> currentTextField.requestFocus());
					return;
				}
				else
				{
					// Incorrect guess - analyze and update letter colors
					analyzeGuess(guess, currentGrid, currentRow);
					
					// Move to next turn
					model.nextTurn();
					currentRow = model.getCurrentTurn() - 1;
					
					if (currentRow >= 6)
					{
						// Game over - max turns reached
						JOptionPane.showMessageDialog(null,
								"Game Over! You didn't guess the word.",
								"Game Over",
								JOptionPane.INFORMATION_MESSAGE);
						model.endGame();
						SwingUtilities.invokeLater(() -> currentTextField.requestFocus());
						return;
					}
					
					// Get the next player's grid
					JTextField[][] nextPlayerGrid = model.isPlayer1Turn() ? currentGridPlayer1 : currentGridPlayer2;
					
					// Switch to next player with popup and focus
					switchPlayerTurn(nextPlayerGrid, currentRow);
				}
			}
			else
			{
				// Incomplete guess
				JOptionPane.showMessageDialog(null,
						"Please enter a complete 5-letter word!",
						"Incomplete Word",
						JOptionPane.WARNING_MESSAGE);
				SwingUtilities.invokeLater(() -> currentTextField.requestFocus());
			}
		}
		else if (currentTextField != null)
		{
			// Fallback if model not set
			KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			manager.focusNextComponent();
		}
	}
	
	/**
	 * Analyze guess and update letter colors based on Wordle rules
	 * Only updates keyboard feedback for the current player
	 * Also updates the grid cell colors with feedback
	 */
	private void analyzeGuess(String guess, JTextField[][] grid, int row)
	{
		String targetWord = model.isPlayer1Turn() ? model.getPlayer1TargetWord() : model.getPlayer2TargetWord();
		
		// Create feedback array: 0=unused, 1=wrong position (yellow), 2=correct (green), 3=not in word (gray)
		int[] feedback = new int[guess.length()];
		
		// First pass: mark correct letters (green)
		for (int i = 0; i < guess.length(); i++)
		{
			char letter = guess.charAt(i);
			if (letter == targetWord.charAt(i))
			{
				updateLetterStatus(String.valueOf(letter), 2); // Green
				feedback[i] = 2; // Correct position
			}
		}
		
		// Second pass: mark wrong position and not in word
		for (int i = 0; i < guess.length(); i++)
		{
			char letter = guess.charAt(i);
			if (letter != targetWord.charAt(i)) // Not in correct position
			{
				if (targetWord.contains(String.valueOf(letter)))
				{
					updateLetterStatus(String.valueOf(letter), 1); // Yellow
					feedback[i] = 1; // Wrong position
				}
				else
				{
					updateLetterStatus(String.valueOf(letter), 3); // Gray
					feedback[i] = 3; // Not in word
				}
			}
		}
		
		// Update the grid with feedback colors
		WordBattleView.updateGridFeedback(grid, row, feedback);
	}
	
	/**
	 * Clear and restore keyboard letter statuses based on player
	 * When switching players, restores the feedback for the new player
	 */
	public void restoreKeyboardFeedback()
	{
		if (model == null)
			return;
		
		Map<String, Integer> playerFeedback = model.isPlayer1Turn() ? player1LetterStatus : player2LetterStatus;
		
		for (String letter : letterStatus.keySet())
		{
			Integer status = playerFeedback.get(letter);
			if (status != null)
			{
				letterStatus.put(letter, status);
			}
			else
			{
				letterStatus.put(letter, 0);
			}
			updateButtonColor(letter);
		}
	}
	
	/**
	 * Show turn indicator popup and focus on the correct grid
	 */
	public void switchPlayerTurn(JTextField[][] nextPlayerGrid, int nextRow)
	{
		String nextPlayerName = model.isPlayer1Turn() ? player1Name : player2Name;
		
		// Show popup message
		JOptionPane.showMessageDialog(null,
				nextPlayerName + "'s Turn!",
				"Turn Switch",
				JOptionPane.INFORMATION_MESSAGE);
		
		// Restore keyboard feedback for the new player
		restoreKeyboardFeedback();
		
		// Focus on the first cell of the player's current row
		if (nextPlayerGrid != null && nextRow < nextPlayerGrid.length)
		{
			SwingUtilities.invokeLater(() -> {
				nextPlayerGrid[nextRow][0].requestFocus();
				currentTextField = nextPlayerGrid[nextRow][0];
				currentRow = nextRow;
				currentCol = 0;
			});
		}
	}
	
	/**
	 * Add an action listener to all buttons
	 */
	public void addLetterListener(ActionListener listener)
	{
		for (JButton button : letterButtons.values())
		{
			button.addActionListener(listener);
		}
	}
	
	/**
	 * Get a specific letter button
	 */
	public JButton getLetterButton(String letter)
	{
		return letterButtons.get(letter.toUpperCase());
	}
	
	/**
	 * Set the current text field that the keyboard will input to
	 */
	public void setCurrentTextField(JTextField textField)
	{
		this.currentTextField = textField;
	}
	
	/**
	 * Register a text field with focus listeners so keyboard knows which field is active
	 */
	public void registerTextField(JTextField textField)
	{
		registerTextField(textField, null, 0, 0);
	}
	
	/**
	 * Register a text field with grid tracking for delete functionality
	 */
	public void registerTextField(JTextField textField, JTextField[][] grid, int row, int col)
	{
		textField.addFocusListener(new java.awt.event.FocusAdapter()
		{
			@Override
			public void focusGained(java.awt.event.FocusEvent e)
			{
				setCurrentTextField(textField);
				currentRow = row;
				currentCol = col;
				System.out.println("Text field focused: [" + row + "][" + col + "]");
			}
			
			@Override
			public void focusLost(java.awt.event.FocusEvent e)
			{
				// Only clear if focus went to a non-keyboard component
				if (!e.isTemporary())
				{
					System.out.println("Text field lost focus (permanent)");
					setCurrentTextField(null);
				}
			}
		});
		
		// Add a key listener to handle backspace key
		textField.addKeyListener(new java.awt.event.KeyAdapter()
		{
			@Override
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				if (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE)
				{
					setCurrentTextField(textField);
					currentRow = row;
					currentCol = col;
					handleDelete();
					e.consume();
				}
			}
		});
	}
	
	/**
	 * Handle physical keyboard input
	 */
	private void handleKeyboardInput(KeyEvent e)
	{
		char keyChar = e.getKeyChar();
		int keyCode = e.getKeyCode();
		
		// Handle letter keys - don't consume, let text field handle it
		if (Character.isLetter(keyChar))
		{
			// Just let it pass through to the text field
			return;
		}
		// Handle BACKSPACE as DELETE
		else if (keyCode == 8) // KeyEvent.VK_BACKSPACE
		{
			handleDelete();
			e.consume();
		}
		// Handle ENTER
		else if (keyCode == 10) // KeyEvent.VK_ENTER
		{
			handleEnter();
			e.consume();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		handleKeyboardInput(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}
}
