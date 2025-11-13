import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class WordBattleView
{
	
	
	private static String player1;
	private static String player2;
	private static final int ROWS = 6;
	private static final int COLS = 5;

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
            player1 = field1.getText().isBlank() ? "Player 1" : field1.getText();
            player2 = field2.getText().isBlank() ? "Player 2" : field2.getText();
        } else {
            player1 = "Player 1";
            player2 = "Player 2";
        }
		
		//Creates JFrame for main application
		JFrame frame = new JFrame("Word Battle");
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
		JLabel gameName = new JLabel("Word Battle", SwingConstants.CENTER);
		gameName.setFont(new Font("Arial", Font.BOLD, 50));
        gameName.setPreferredSize(new Dimension(0,100));
		frame.add(gameName, BorderLayout.NORTH);
		
		Keyboard keyboard = new Keyboard();
		frame.add(keyboard, BorderLayout.SOUTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(greyColor1);
		centerPanel.setLayout(new GridLayout(1,2,20,0));
		
		JPanel guessGrid1 = new JPanel(new GridLayout(ROWS, COLS, 5, 5));
		guessGrid1.setPreferredSize(new Dimension(400,400));
		JTextField[][] boxes = new JTextField[ROWS][COLS];
		for (int row = 0; row < ROWS; row++)
		{
			for (int col = 0; col < COLS; col++) 
			{
				JTextField textField = new JTextField(1);
				textField.setHorizontalAlignment(JTextField.CENTER);
				textField.setFont(new Font("Arial", Font.BOLD, 40));
				textField.setPreferredSize(new Dimension(25,25));
				
				// Add document filter to restrict to single letters only
				AbstractDocument doc = (AbstractDocument) textField.getDocument();
				doc.setDocumentFilter(new LetterOnlyFilter());
				
				// Register with keyboard
				keyboard.registerTextField(textField);
				
				boxes[row][col] = textField;
				guessGrid1.add(textField);
			}
		}
		
		
		JPanel guessGrid2 = new JPanel(new GridLayout(ROWS, COLS, 5, 5));
		guessGrid2.setPreferredSize(new Dimension(400,400));
		JTextField[][] boxes2 = new JTextField[ROWS][COLS];
		for (int row = 0; row < ROWS; row++)
		{
			for (int col = 0; col < COLS; col++) 
			{
				JTextField textField = new JTextField(1);
				textField.setHorizontalAlignment(JTextField.CENTER);
				textField.setFont(new Font("Arial", Font.BOLD, 40));
				textField.setPreferredSize(new Dimension(25,25));
				
				// Add document filter to restrict to single letters only
				AbstractDocument doc = (AbstractDocument) textField.getDocument();
				doc.setDocumentFilter(new LetterOnlyFilter());
				
				// Register with keyboard
				keyboard.registerTextField(textField);
				
				boxes2[row][col] = textField;
				guessGrid2.add(textField);
			}
		}

		
		JPanel gridWrapper1 = new JPanel();
		gridWrapper1.setBackground(greyColor1);
		gridWrapper1.setPreferredSize(new Dimension(300,300));
		gridWrapper1.add(guessGrid1);
		centerPanel.add(gridWrapper1);
		JPanel gridWrapper2 = new JPanel();
		gridWrapper2.setBackground(greyColor1);
		gridWrapper2.setPreferredSize(new Dimension(300,300));
		gridWrapper2.add(guessGrid2);
		centerPanel.add(gridWrapper2);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.setVisible(true);
		
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
			// Convert to uppercase
			String upperString = string.toUpperCase();
			// Only insert if field is empty
			if (fb.getDocument().getLength() == 0)
			{
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
			// Only replace if field currently has content
			if (fb.getDocument().getLength() > 0)
			{
				super.replace(fb, offset, length, upperText, attrs);
			}
		}
	}
	
	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
	{
		// Allow removal
		super.remove(fb, offset, length);
	}
}
