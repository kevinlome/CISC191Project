import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class WordBattleView
{
	private static Player player1;
	private static Player player2;
	private static final int ROWS = 6;
	private static final int COLS = 5;
	private static JTextField[][] boxes;
	private static JTextField[][] boxes2;
	private static Keyboard keyboard;
	private static WordBattleModel model;
	private static JFrame frame;
	private static int currentRow1 = 0;
	private static int currentRow2 = 0;
	private static final Color CORRECT_COLOR = new Color(106, 170, 100); // Green
	private static final Color WRONG_POSITION_COLOR = new Color(181, 159, 59); // Yellow
	private static final Color NOT_IN_WORD_COLOR = new Color(58, 58, 58); // Dark Gray
	private static final Color DEFAULT_COLOR = new Color(60, 60, 60); // Default dark background
	private static boolean[] completedRows1; // Track completed rows for Player 1
	private static boolean[] completedRows2; // Track completed rows for Player 2
	private static boolean[] focusRedirecting1; // Prevent focus redirect loops for Player 1
	private static boolean[] focusRedirecting2; // Prevent focus redirect loops for Player 2

	public static void main(String[] args)
	{
		
		Color greyColor = new Color(82, 81, 81);
        Color textColor = Color.WHITE;

        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBackground(greyColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel label1 = new JLabel("Player 1 Name:");
        label1.setForeground(textColor);
        JTextField field1 = new JTextField();

        JLabel label2 = new JLabel("Player 2 Name:");
        label2.setForeground(textColor);
        JTextField field2 = new JTextField();
        
        // Add key listener to field1 to move to field2 when Enter is pressed
        field1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e)
            {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
                {
                    field2.requestFocus();
                    e.consume();
                }
            }
        });
        
        // Add key listener to field2 to confirm when Enter is pressed
        field2.addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e)
            {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
                {
                    // Trigger OK button action
                    e.consume();
                }
            }
        });

        panel.add(label1);
        panel.add(field1);
        panel.add(label2);
        panel.add(field2);

        ImageIcon icon = new ImageIcon("icon.png");
        // Scale icon to fit nicely
        Image scaled = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaled);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Word Battle",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                icon
        );

        if (result == JOptionPane.OK_OPTION) {
            String p1Name = field1.getText().isBlank() ? "Player 1" : field1.getText();
            String p2Name = field2.getText().isBlank() ? "Player 2" : field2.getText();
            player1 = new Player(p1Name);
            player2 = new Player(p2Name);
        } else {
            // User clicked Cancel - exit without starting the game
            System.exit(0);
        }
		
		// Initialize the model with the players
		model = new WordBattleModel(player1, player2);
		model.startGame();
		
		// Initialize completed rows tracking
		completedRows1 = new boolean[ROWS];
		completedRows2 = new boolean[ROWS];
		focusRedirecting1 = new boolean[ROWS];
		focusRedirecting2 = new boolean[ROWS];
		for (int i = 0; i < ROWS; i++)
		{
			completedRows1[i] = false;
			completedRows2[i] = false;
			focusRedirecting1[i] = false;
			focusRedirecting2[i] = false;
		}
		
		//Creates JFrame for main application
		frame = new JFrame("Word Battle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920,1080);
		frame.setLayout(new BorderLayout (10,10));
		
		//Creates the grey background color for JFrame
		int red = 82;
		int green = 81;
		int blue = 81;
		Color greyColor1 = new Color(red, green, blue);
		frame.getContentPane().setBackground(greyColor1);
		
		//
		// Load the logo image from the LOGO folder
		ImageIcon logoIcon = null;
		try
		{
			// Method 1: Try loading from file system first (when running from project root)
			java.io.File logoFile = new java.io.File("LOGO/WordBattleLogo.png");
			if (logoFile.exists())
			{
				logoIcon = new ImageIcon(logoFile.getAbsolutePath());
			}
			else
			{
				// Method 2: Try loading from classpath
				java.net.URL logoURL = WordBattleView.class.getResource("/WordBattleLogo.png");
				if (logoURL != null)
				{
					logoIcon = new ImageIcon(logoURL);
				}
				else
				{
					// Method 3: Try from bin folder (compiled resources)
					logoFile = new java.io.File("bin/LOGO/WordBattleLogo.png");
					if (logoFile.exists())
					{
						logoIcon = new ImageIcon(logoFile.getAbsolutePath());
					}
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("Error loading logo: " + e.getMessage());
		}
		
		// Create the label - use text if image failed to load
		JLabel gameName;
		if (logoIcon != null && logoIcon.getIconWidth() > 0)
		{
			// Scale the image to fit nicely in the header
			Image scaledLogo = logoIcon.getImage().getScaledInstance(125, 125, Image.SCALE_SMOOTH);
			logoIcon = new ImageIcon(scaledLogo);
			gameName = new JLabel(logoIcon, SwingConstants.CENTER);
		}
		else
		{
			// Fallback to text label if image loading fails
			gameName = new JLabel("Word Battle", SwingConstants.CENTER);
			gameName.setFont(new Font("Arial", Font.BOLD, 50));
			gameName.setForeground(Color.WHITE);
		}
		gameName.setPreferredSize(new Dimension(0, 120));
		frame.add(gameName, BorderLayout.NORTH);
		
		keyboard = new Keyboard();
		keyboard.setModel(model);
		keyboard.setPlayerNames(player1.getName(), player2.getName());
		frame.add(keyboard, BorderLayout.SOUTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(greyColor1);
		centerPanel.setLayout(new GridLayout(1,2,20,0));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JPanel guessGrid1 = new JPanel(new GridLayout(ROWS, COLS, 5, 5));
		guessGrid1.setBackground(Color.BLACK);
		guessGrid1.setPreferredSize(new Dimension(400,400));
		boxes = new JTextField[ROWS][COLS];
		for (int row = 0; row < ROWS; row++)
		{
			final int gridRow = row;
			for (int col = 0; col < COLS; col++) 
			{
				final int gridCol = col;
				JTextField textField = new JTextField(1);
				textField.setHorizontalAlignment(JTextField.CENTER);
				textField.setFont(new Font("Arial", Font.BOLD, 40));
				textField.setPreferredSize(new Dimension(25,25));
				textField.setBackground(DEFAULT_COLOR);
				textField.setForeground(Color.WHITE);
				textField.setCaretColor(Color.WHITE);
				textField.setFocusable(true); // Allow programmatic focus
				
				// Disable mouse clicking on grid boxes
				textField.addMouseListener(new java.awt.event.MouseAdapter()
				{
					@Override
					public void mousePressed(java.awt.event.MouseEvent e)
					{
						e.consume(); // Consume the event to prevent focus change
					}
				});
				
				// Add document filter to restrict to single letters only
				AbstractDocument doc = (AbstractDocument) textField.getDocument();
				doc.setDocumentFilter(new LetterOnlyFilter());
				
				// Add focus listener to set keyboard's current grid
				textField.addFocusListener(new java.awt.event.FocusAdapter()
				{
					@Override
					public void focusGained(java.awt.event.FocusEvent e)
					{
						// Only allow focus if this is the current row and not completed, and it's Player 1's turn
						if (model.isPlayer1Turn() && gridRow == currentRow1 && !completedRows1[gridRow])
						{
							keyboard.setCurrentGrid(boxes, gridRow);
							focusRedirecting1[gridRow] = false;
						}
						else if (model.isPlayer1Turn() && !focusRedirecting1[gridRow])
						{
							// Move focus back to the current row for Player 1 (only once to prevent loop)
							focusRedirecting1[gridRow] = true;
							boxes[currentRow1][0].requestFocus();
						}
					}
				});
				
				// Add document listener to handle grid progression within the same row
				textField.getDocument().addDocumentListener(new DocumentListener()
				{
					@Override
					public void insertUpdate(DocumentEvent e)
					{
						if (textField.getText().length() == 1)
						{
							SwingUtilities.invokeLater(() -> {
								// Only move to next cell within the same row
								int nextCol = gridCol + 1;
								if (nextCol < COLS)
								{
									boxes[gridRow][nextCol].requestFocus();
									currentRow1 = gridRow;
									keyboard.setCurrentGrid(boxes, gridRow);
								}
							});
						}
					}
					
					@Override
					public void removeUpdate(DocumentEvent e) 
					{
						// On backspace/delete, move to previous box if current is empty
						if (textField.getText().isEmpty() && gridCol > 0)
						{
							SwingUtilities.invokeLater(() -> {
								boxes[gridRow][gridCol - 1].requestFocus();
							});
						}
					}
					
					@Override
					public void changedUpdate(DocumentEvent e) {}
				});
				
				// Add action listener for ENTER key
				textField.addActionListener(e -> {
					handleGuessSubmission(boxes, gridRow, gridRow);
				});
				
				// Register with keyboard
				keyboard.registerTextField(textField, boxes, row, col);
				
				boxes[row][col] = textField;
				guessGrid1.add(textField);
			}
		}
		
		
		JPanel guessGrid2 = new JPanel(new GridLayout(ROWS, COLS, 5, 5));
		guessGrid2.setBackground(Color.BLACK);
		guessGrid2.setPreferredSize(new Dimension(400,400));
		boxes2 = new JTextField[ROWS][COLS];
		for (int row = 0; row < ROWS; row++)
		{
			final int gridRow = row;
			for (int col = 0; col < COLS; col++) 
			{
				final int gridCol = col;
				JTextField textField = new JTextField(1);
				textField.setHorizontalAlignment(JTextField.CENTER);
				textField.setFont(new Font("Arial", Font.BOLD, 40));
				textField.setPreferredSize(new Dimension(25,25));
				textField.setBackground(DEFAULT_COLOR);
				textField.setForeground(Color.WHITE);
				textField.setCaretColor(Color.WHITE);
				textField.setFocusable(true); // Allow programmatic focus only
				
				// Disable mouse clicking on grid boxes
				textField.addMouseListener(new java.awt.event.MouseAdapter()
				{
					@Override
					public void mousePressed(java.awt.event.MouseEvent e)
					{
						e.consume(); // Consume the event to prevent focus change
					}
				});
				
				// Add document filter to restrict to single letters only
				AbstractDocument doc = (AbstractDocument) textField.getDocument();
				doc.setDocumentFilter(new LetterOnlyFilter());
				
				// Add focus listener to set keyboard's current grid
				textField.addFocusListener(new java.awt.event.FocusAdapter()
				{
					@Override
					public void focusGained(java.awt.event.FocusEvent e)
					{
						// Only allow focus if this is the current row and not completed, and it's Player 2's turn
						if (!model.isPlayer1Turn() && gridRow == currentRow2 && !completedRows2[gridRow])
						{
							keyboard.setCurrentGrid(boxes2, gridRow);
							focusRedirecting2[gridRow] = false;
						}
						else if (!model.isPlayer1Turn() && !focusRedirecting2[gridRow])
						{
							// Move focus back to the current row for Player 2 (only once to prevent loop)
							focusRedirecting2[gridRow] = true;
							boxes2[currentRow2][0].requestFocus();
						}
					}
				});
				
				// Add document listener to handle grid progression within the same row
				textField.getDocument().addDocumentListener(new DocumentListener()
				{
					@Override
					public void insertUpdate(DocumentEvent e)
					{
						if (textField.getText().length() == 1)
						{
							SwingUtilities.invokeLater(() -> {
								// Only move to next cell within the same row
								int nextCol = gridCol + 1;
								if (nextCol < COLS)
								{
									boxes2[gridRow][nextCol].requestFocus();
									currentRow2 = gridRow;
									keyboard.setCurrentGrid(boxes2, gridRow);
								}
							});
						}
					}
					
					@Override
					public void removeUpdate(DocumentEvent e) 
					{
						// On backspace/delete, move to previous box if current is empty
						if (textField.getText().isEmpty() && gridCol > 0)
						{
							SwingUtilities.invokeLater(() -> {
								boxes2[gridRow][gridCol - 1].requestFocus();
							});
						}
					}
					
					@Override
					public void changedUpdate(DocumentEvent e) {}
				});
				
				// Add action listener for ENTER key
				textField.addActionListener(e -> {
					handleGuessSubmission(boxes2, gridRow, gridRow);
				});
				
				// Register with keyboard
				keyboard.registerTextField(textField, boxes2, row, col);
				
				boxes2[row][col] = textField;
				guessGrid2.add(textField);
			}
		}

		
		JPanel gridWrapper1 = new JPanel();
		gridWrapper1.setBackground(greyColor1);
		gridWrapper1.setLayout(new java.awt.BorderLayout());
		gridWrapper1.setPreferredSize(new Dimension(300,400));
		JLabel player1Label = new JLabel(player1.getName(), SwingConstants.CENTER);
		player1Label.setForeground(Color.BLACK);
		player1Label.setFont(new Font("Arial", Font.BOLD, 24));
		gridWrapper1.add(player1Label, BorderLayout.NORTH);
		gridWrapper1.add(guessGrid1, BorderLayout.CENTER);
		centerPanel.add(gridWrapper1);
		
		JPanel gridWrapper2 = new JPanel();
		gridWrapper2.setBackground(greyColor1);
		gridWrapper2.setLayout(new java.awt.BorderLayout());
		gridWrapper2.setPreferredSize(new Dimension(300,400));
		JLabel player2Label = new JLabel(player2.getName(), SwingConstants.CENTER);
		player2Label.setForeground(Color.BLACK);
		player2Label.setFont(new Font("Arial", Font.BOLD, 24));
		gridWrapper2.add(player2Label, BorderLayout.NORTH);
		gridWrapper2.add(guessGrid2, BorderLayout.CENTER);
		centerPanel.add(gridWrapper2);
		frame.add(centerPanel, BorderLayout.CENTER);
		
		// Set both grids on the keyboard so it can switch between them
		keyboard.setGrids(boxes, boxes2);
		
		frame.setVisible(true);
		
		// Set initial focus to keyboard and first player's grid
		SwingUtilities.invokeLater(() -> {
			keyboard.requestFocus();
			boxes[0][0].requestFocus();
			keyboard.setCurrentGrid(boxes, 0);
		});
		
	}
	
	/**
	 * Handle guess submission from ENTER key
	 */
	private static void handleGuessSubmission(JTextField[][] grid, int currentRow, int row)
	{
		// Collect the guess
		StringBuilder guessBuilder = new StringBuilder();
		for (int col = 0; col < COLS; col++)
		{
			String text = grid[row][col].getText();
			if (!text.isEmpty())
			{
				guessBuilder.append(text);
			}
		}
		
		String guess = guessBuilder.toString();
		
		// Must be 5 letters
		if (guess.length() != 5)
		{
			JOptionPane.showMessageDialog(null, "Please enter a complete 5-letter word!");
			return;
		}
		
		// Check if it's a valid word using WordChecker API
		if (!WordChecker.isValidWord(guess))
		{
			JOptionPane.showMessageDialog(null, "Not in word list.");
			// Clear the row
			for (int col = 0; col < COLS; col++)
			{
				grid[row][col].setText("");
			}
			grid[row][0].requestFocus();
			return;
		}
		
		// Check if it's the target word
		if (model.checkGuess(guess))
		{
			// Generate feedback using LetterView for the winning guess
			int[] feedback = model.getGuessFeedback(guess);
			updateGridFeedback(grid, row, feedback);
			
			// Lock the winning row
			for (int col = 0; col < COLS; col++)
			{
				grid[row][col].setEditable(false);
			}
			
			// Update keyboard button colors for winning guess
			for (int i = 0; i < 5; i++)
			{
				String letter = String.valueOf(guess.charAt(i)).toUpperCase();
				keyboard.updateButtonColor(letter, feedback[i]);
			}
			
			String playerName = model.isPlayer1Turn() ? player1.getName() : player2.getName();
			int choice = JOptionPane.showOptionDialog(null,
				playerName + " wins! The word was: " + guess.toUpperCase() + 
				"\n\nPlayer 1 target: " + model.getPlayer1TargetWord().toUpperCase() +
				"\nPlayer 2 target: " + model.getPlayer2TargetWord().toUpperCase(),
				"Game Over",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				new String[]{"Play Again", "Close"},
				"Play Again");
			
			model.endGame();
			if (choice == JOptionPane.YES_OPTION)
			{
				restartGame();
			}
			else
			{
				System.exit(0);
			}
			return;
		}
		
		// Generate feedback using LetterView
		int[] feedback = model.getGuessFeedback(guess);
		updateGridFeedback(grid, row, feedback);
		
		// Lock this row - disable all boxes in the completed row
		for (int col = 0; col < COLS; col++)
		{
			grid[row][col].setEditable(false);
		}
		
		// Mark row as completed
		if (grid == boxes)
		{
			completedRows1[row] = true;
		}
		else if (grid == boxes2)
		{
			completedRows2[row] = true;
		}
		
		// Update keyboard button colors
		for (int i = 0; i < 5; i++)
		{
			String letter = String.valueOf(guess.charAt(i)).toUpperCase();
			keyboard.updateButtonColor(letter, feedback[i]);
		}
		
		// Move to next turn
		model.nextTurn();
		int nextRow = model.getCurrentTurn() - 1;
		
		if (nextRow >= ROWS)
		{
			int choice = JOptionPane.showOptionDialog(null, 
				"Both players lost!\n\n" +
				"Player 1 target: " + model.getPlayer1TargetWord().toUpperCase() +
				"\nPlayer 2 target: " + model.getPlayer2TargetWord().toUpperCase(),
				"Game Over",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				new String[]{"Play Again", "Close"},
				"Play Again");
			
			model.endGame();
			if (choice == JOptionPane.YES_OPTION)
			{
				restartGame();
			}
			else
			{
				System.exit(0);
			}
			return;
		}
		
		// Switch to next player - use the correct grid based on whose turn it is
		JTextField[][] nextGrid = model.isPlayer1Turn() ? boxes : boxes2;
		
		// Update current row tracking BEFORE setting focus to allow focus listener to pass
		if (nextGrid == boxes)
		{
			currentRow1 = nextRow;
		}
		else if (nextGrid == boxes2)
		{
			currentRow2 = nextRow;
		}
		
		JOptionPane.showMessageDialog(null, (model.isPlayer1Turn() ? player1.getName() : player2.getName()) + "'s Turn!");
		
		// Update keyboard feedback to show the new player's feedback
		keyboard.updateKeyboardFeedback();
		
		// Ensure focus is set to the first input box after dialog closes
		SwingUtilities.invokeLater(() -> {
			nextGrid[nextRow][0].requestFocus();
			keyboard.setCurrentGrid(nextGrid, nextRow);
		});
	}
	
	/**
	 * Restart the game by resetting all game state
	 */
	private static void restartGame()
	{
		// Reset players and model with new target words
		player1.reset();
		player2.reset();
		model = new WordBattleModel(player1, player2);
		model.startGame();
		
		// Reset current row tracking first
		currentRow1 = 0;
		currentRow2 = 0;
		
		// Reset completed rows tracking and focus redirect flags
		for (int i = 0; i < ROWS; i++)
		{
			completedRows1[i] = false;
			completedRows2[i] = false;
			focusRedirecting1[i] = false;
			focusRedirecting2[i] = false;
		}
		
		// Clear all grid boxes and reset their state
		for (int row = 0; row < ROWS; row++)
		{
			for (int col = 0; col < COLS; col++)
			{
				// Clear Player 1 grid
				AbstractDocument doc1 = (AbstractDocument) boxes[row][col].getDocument();
				doc1.setDocumentFilter(null); // Remove filter temporarily
				boxes[row][col].setText("");
				doc1.setDocumentFilter(new LetterOnlyFilter()); // Restore filter
				boxes[row][col].setBackground(DEFAULT_COLOR);
				boxes[row][col].setEditable(true);
				boxes[row][col].setCaretColor(Color.WHITE);
				
				// Clear Player 2 grid
				AbstractDocument doc2 = (AbstractDocument) boxes2[row][col].getDocument();
				doc2.setDocumentFilter(null); // Remove filter temporarily
				boxes2[row][col].setText("");
				doc2.setDocumentFilter(new LetterOnlyFilter()); // Restore filter
				boxes2[row][col].setBackground(DEFAULT_COLOR);
				boxes2[row][col].setEditable(true);
				boxes2[row][col].setCaretColor(Color.WHITE);
			}
		}
		
		// Reset keyboard state
		keyboard.resetGame(model);
		
		// Set focus to Player 1's first cell
		SwingUtilities.invokeLater(() -> {
			boxes[0][0].requestFocus();
			keyboard.setCurrentGrid(boxes, 0);
		});
	}
	
	/**
	 * Update grid cell colors based on guess feedback
	 */
	public static void updateGridFeedback(JTextField[][] grid, int row, int[] feedback)
	{
		for (int col = 0; col < COLS && col < feedback.length; col++)
		{
			Color cellColor = DEFAULT_COLOR;
			switch (feedback[col])
			{
				case 2: // Correct
					cellColor = CORRECT_COLOR;
					break;
				case 1: // Wrong position
					cellColor = WRONG_POSITION_COLOR;
					break;
				case 3: // Not in word
					cellColor = NOT_IN_WORD_COLOR;
					break;
				default: // Unused or default
					cellColor = DEFAULT_COLOR;
			}
			JTextField cell = grid[row][col];
			cell.setOpaque(true);
			cell.setBackground(cellColor);
			cell.setCaretColor(cellColor); // Hide cursor with same color as background
			cell.repaint(); // Force repaint
		}
	}
	
}

/**
 * Document filter that only allows single uppercase letters
 */
class LetterOnlyFilter extends DocumentFilter
{
	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
			throws BadLocationException
	{
		if (string == null)
			return;
		
		// Only allow if it's a single character and it's a letter
		if (string.length() == 1 && Character.isLetter(string.charAt(0)))
		{
			// Only insert if field is empty
			if (fb.getDocument().getLength() == 0)
			{
				// Convert to uppercase and insert
				String upperString = string.toUpperCase();
				super.insertString(fb, offset, upperString, attr);
			}
		}
	}
	
	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException
	{
		if (text == null)
			return;
		
		// Only allow if it's a single character and it's a letter
		if (text.length() == 1 && Character.isLetter(text.charAt(0)))
		{
			// Convert to uppercase
			String upperText = text.toUpperCase();
			// Replace with the uppercase letter
			super.replace(fb, offset, length, upperText, attrs);
		}
	}
	
	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
	{
		// Allow removal
		super.remove(fb, offset, length);
	}
}
