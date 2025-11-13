import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Keyboard extends JPanel
{
	private static final Color GREY = new Color(82, 81, 81);
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Color BUTTON_COLOR = new Color(129, 131, 132);
	
	private String[] row1 = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"};
	private String[] row2 = {"A", "S", "D", "F", "G", "H", "J", "K", "L"};
	private String[] row3 = {"Z", "X", "C", "V", "B", "N", "M"};
	
	private Map<String, JButton> letterButtons;
	
	public Keyboard()
	{
		this.letterButtons = new HashMap<>();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(GREY);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setPreferredSize(new Dimension(0, 300));
		
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
			button.setPreferredSize(new Dimension(65, 65));
			button.setMinimumSize(new Dimension(65, 65));
			button.setMaximumSize(new Dimension(65, 65));
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
}
