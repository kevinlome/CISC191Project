import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

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
	
	public Keyboard()
	{
		this.letterButtons = new HashMap<>();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(GREY);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setPreferredSize(new Dimension(0, 300));
		
		// Add key listener for physical keyboard support
		setFocusable(true);
		addKeyListener(this);
		
		createKeyboardRows();
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
			
			letterButtons.put(letter, button);
			rowPanel.add(button);
		}
		
		return rowPanel;
	}
	
	private void handleLetterInput(String letter)
	{
		if (currentTextField != null && currentTextField.getText().isEmpty())
		{
			try
			{
				// Insert through the document to trigger the filter properly
				currentTextField.getDocument().insertString(0, letter, null);
				// Ensure the text field maintains focus after button click
				currentTextField.requestFocus();
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
			currentTextField.setText("");
			currentTextField.requestFocus();
		}
	}
	
	private void handleEnter()
	{
		if (currentTextField != null)
		{
			// Move to next text field or handle submission
			KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			manager.focusNextComponent();
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
		textField.addFocusListener(new java.awt.event.FocusAdapter()
		{
			@Override
			public void focusGained(java.awt.event.FocusEvent e)
			{
				setCurrentTextField(textField);
			}
			
			@Override
			public void focusLost(java.awt.event.FocusEvent e)
			{
				setCurrentTextField(null);
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
