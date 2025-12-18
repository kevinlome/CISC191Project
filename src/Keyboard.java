import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Keyboard - On-screen keyboard UI for Word Battle
 * Handles letter input and special keys (DELETE, ENTER)
 */
public class Keyboard extends JPanel implements KeyListener
{
	private static final Color GREY = new Color(82, 81, 81);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color BUTTON_COLOR = new Color(129, 131, 132);
	
	private String[] row1 = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
	private String[] row2 = {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
	private String[] row3 = {"DELETE", "Z", "X", "C", "V", "B", "N", "M", "ENTER"};
	
	private Map<String, JButton> letterButtons;
	private JTextField currentTextField;
	private JTextField[][] currentGridPlayer1;
	private JTextField[][] currentGridPlayer2;
	private JTextField[][] activeGrid; // Track which grid is currently active
	private int currentRow;
	private String player1Name;
	private String player2Name;
	private WordBattleModel model;
	private Map<String, Integer> player1Feedback; // Feedback for Player 1's letters
	private Map<String, Integer> player2Feedback; // Feedback for Player 2's letters
	private static final int GRID_COLS = 5;
	
	public Keyboard()
	{
		this.letterButtons = new HashMap<>();
		this.player1Feedback = new HashMap<>();
		this.player2Feedback = new HashMap<>();
		this.activeGrid = null;
		
		// Initialize feedback maps with all letters set to 0 (unused)
		String[] allLetters = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", 
		                       "A", "S", "D", "F", "G", "H", "J", "K", "L",
		                       "Z", "X", "C", "V", "B", "N", "M"};
		for (String letter : allLetters)
		{
			player1Feedback.put(letter, 0);
			player2Feedback.put(letter, 0);
		}
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(GREY);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setPreferredSize(new Dimension(0, 300));
		
		setFocusable(true);
		addKeyListener(this);
		
		createKeyboardRows();
	}
	
	/**
	 * Set both player grids for keyboard access
	 */
	public void setGrids(JTextField[][] player1Grid, JTextField[][] player2Grid)
	{
		this.currentGridPlayer1 = player1Grid;
		this.currentGridPlayer2 = player2Grid;
	}
	
	/**
	 * Set the current grid being used
	 */
	public void setCurrentGrid(JTextField[][] grid, int row)
	{
		this.activeGrid = grid;
		this.currentRow = row;
	}
	
	/**
	 * Set the current text field that keyboard will input to
	 */
	public void setCurrentTextField(JTextField textField)
	{
		this.currentTextField = textField;
	}
	
	/**
	 * Register a text field with focus listeners
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
				setCurrentGrid(grid, row);
			}
		});
		
		textField.addKeyListener(new java.awt.event.KeyAdapter()
		{
			@Override
			public void keyPressed(java.awt.event.KeyEvent e)
			{
				if (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE)
				{
					handleDelete();
					e.consume();
				}
				else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
				{
					handleEnter();
					e.consume();
				}
				// For letter keys, don't consume - let the text field handle it normally
				// through DocumentFilter to avoid duplicate input
			}
		});
	}
	
	/**
	 * Update keyboard button color based on feedback
	 */
	public void updateButtonColor(String letter, int feedbackCode)
	{
		JButton button = letterButtons.get(letter);
		if (button != null)
		{
			Color color;
			switch (feedbackCode)
			{
				case 2:
					color = new Color(106, 170, 100);
					break;
				case 1:
					color = new Color(181, 159, 59);
					break;
				case 3:
					color = new Color(58, 58, 58);
					break;
				default:
					color = BUTTON_COLOR;
			}
			button.setBackground(color);
			button.setOpaque(true);
			button.repaint();
			
			// Store in the appropriate player's feedback map
			if (model != null)
			{
				if (model.isPlayer1Turn())
				{
					player1Feedback.put(letter, feedbackCode);
				}
				else
				{
					player2Feedback.put(letter, feedbackCode);
				}
			}
		}
	}
	
	/**
	 * Update keyboard to show feedback for current player's turn
	 */
	public void updateKeyboardFeedback()
	{
		if (model == null)
			return;
		
		// Get the feedback map for the current player
		Map<String, Integer> currentFeedback = model.isPlayer1Turn() ? player1Feedback : player2Feedback;
		
		// Update all button colors to reflect current player's feedback
		for (String letter : letterButtons.keySet())
		{
			Integer feedbackCode = currentFeedback.getOrDefault(letter, 0);
			JButton button = letterButtons.get(letter);
			if (button != null)
			{
				Color color;
				switch (feedbackCode)
				{
					case 2:
						color = new Color(106, 170, 100);
						break;
					case 1:
						color = new Color(181, 159, 59);
						break;
					case 3:
						color = new Color(58, 58, 58);
						break;
					default:
						color = BUTTON_COLOR;
				}
				button.setBackground(color);
				button.setOpaque(true);
				button.repaint();
			}
		}
	}
	
	private void createKeyboardRows()
	{
		JPanel row1Panel = createRow(row1);
		add(row1Panel);
		add(Box.createVerticalStrut(2));
		
		JPanel row2Panel = createRow(row2);
		row2Panel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
		add(row2Panel);
		add(Box.createVerticalStrut(2));
		
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
			
			if (letter.equals("DELETE") || letter.equals("ENTER"))
			{
				button.setPreferredSize(new Dimension(130, 65));
				button.setMinimumSize(new Dimension(130, 65));
				button.setMaximumSize(new Dimension(130, 65));
			}
			else
			{
				button.setPreferredSize(new Dimension(65, 65));
				button.setMinimumSize(new Dimension(65, 65));
				button.setMaximumSize(new Dimension(65, 65));
			}
			
			button.setBackground(BUTTON_COLOR);
			button.setForeground(TEXT_COLOR);
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setOpaque(true);
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setFocusable(false);
			
			String finalLetter = letter;
			button.addActionListener(e -> handleButtonClick(finalLetter));
			
			letterButtons.put(letter, button);
			rowPanel.add(button);
		}
		
		return rowPanel;
	}
	
	private void handleButtonClick(String letter)
	{
		if (currentTextField == null)
			return;
		
		if (letter.equals("DELETE"))
		{
			handleDelete();
		}
		else if (letter.equals("ENTER"))
		{
			handleEnter();
		}
		else
		{
			handleLetterInput(letter);
		}
	}
	
	private void handleLetterInput(String letter)
	{
		if (currentTextField.getText().isEmpty())
		{
			try
			{
				currentTextField.getDocument().insertString(0, letter, null);
				
				SwingUtilities.invokeLater(() -> {
					JTextField[][] currentGrid = getCurrentGrid();
					if (currentGrid != null && currentRow < currentGrid.length)
					{
						// Move to next field in the same row
						for (int col = 0; col < currentGrid[currentRow].length - 1; col++)
						{
							if (currentGrid[currentRow][col] == currentTextField)
							{
								currentGrid[currentRow][col + 1].requestFocus();
								break;
							}
						}
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
		if (currentTextField != null)
		{
			if (currentTextField.getText().isEmpty())
			{
				// Move to previous field if current is empty
				JTextField[][] currentGrid = getCurrentGrid();
				if (currentGrid != null && currentRow >= 0 && currentRow < currentGrid.length)
				{
					for (int col = currentGrid[currentRow].length - 1; col >= 0; col--)
					{
						if (currentGrid[currentRow][col] == currentTextField)
						{
							// Found current position, move to previous
							if (col > 0)
							{
								JTextField prevField = currentGrid[currentRow][col - 1];
								clearTextField(prevField);
								SwingUtilities.invokeLater(() -> prevField.requestFocus());
							}
							break;
						}
					}
				}
			}
			else
			{
				clearTextField(currentTextField);
			}
		}
	}
	
	private void handleEnter()
	{
		if (currentTextField != null)
		{
			KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			SwingUtilities.invokeLater(() -> {
				currentTextField.postActionEvent();
			});
		}
	}
	
	private void clearTextField(JTextField field)
	{
		try
		{
			int length = field.getDocument().getLength();
			if (length > 0)
			{
				field.getDocument().remove(0, length);
			}
		}
		catch (javax.swing.text.BadLocationException e)
		{
			e.printStackTrace();
		}
	}
	
	private JTextField[][] getCurrentGrid()
	{
		// Return the actively set grid
		if (activeGrid != null)
		{
			return activeGrid;
		}
		// Fallback to model-based determination if activeGrid is null
		if (model != null && currentGridPlayer1 != null && currentGridPlayer2 != null)
		{
			return model.isPlayer1Turn() ? currentGridPlayer1 : currentGridPlayer2;
		}
		// Final fallback
		return (currentGridPlayer1 != null) ? currentGridPlayer1 : currentGridPlayer2;
	}
	
	public void setModel(WordBattleModel model){
		this.model = model;
	}
	
	public void setPlayerNames(String player1Name, String player2Name){
		this.player1Name = player1Name;
		this.player2Name = player2Name;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		char keyChar = e.getKeyChar();
		int keyCode = e.getKeyCode();
		
		if (Character.isLetter(keyChar))
		{
			handleLetterInput(String.valueOf(keyChar).toUpperCase());
			e.consume();
		}
		else if (keyCode == KeyEvent.VK_BACK_SPACE)
		{
			handleDelete();
			e.consume();
		}
		else if (keyCode == KeyEvent.VK_ENTER)
		{
			handleEnter();
			e.consume();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	/**
	 * Reset the keyboard state for a new game
	 */
	public void resetGame(WordBattleModel newModel)
	{
		this.model = newModel;
		this.activeGrid = null;
		
		// Reset all letter button colors
		for (JButton button : letterButtons.values())
		{
			button.setBackground(BUTTON_COLOR);
			button.setForeground(TEXT_COLOR);
		}
		
		// Reset feedback maps
		String[] allLetters = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", 
		                       "A", "S", "D", "F", "G", "H", "J", "K", "L",
		                       "Z", "X", "C", "V", "B", "N", "M"};
		for (String letter : allLetters)
		{
			player1Feedback.put(letter, 0);
			player2Feedback.put(letter, 0);
		}
		
		// Reset current position
		currentRow = 0;
		currentTextField = null;
	}
}