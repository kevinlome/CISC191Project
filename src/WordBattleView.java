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

public class WordBattleView
{
	
	
	private static Object player1;
	private static Object player2;

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
                "Enter Player Names",
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
//		String playerName1 = JOptionPane.showInputDialog(null, "Enter Player 1 Name:", "Word Battle", JOptionPane.QUESTION_MESSAGE);
//		String playerName2 = JOptionPane.showInputDialog(null, "Enter Player 2 Name:", "Word Battle", JOptionPane.QUESTION_MESSAGE);
		
		//Creates JFrame for application
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
		
		JLabel keyboard = new JLabel("Keyboard", SwingConstants.CENTER);
		keyboard.setFont(new Font("Arial", Font.BOLD, 50));
		keyboard.setPreferredSize(new Dimension(0,100));
		frame.add(keyboard, BorderLayout.SOUTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(greyColor1);
		centerPanel.setLayout(new GridLayout(1,2,20,0));
		
		JLabel guessGrid1 = new JLabel("guessGrid1", SwingConstants.CENTER);
		guessGrid1.setFont(new Font("Arial", Font.BOLD, 50));
		guessGrid1.setPreferredSize(new Dimension(0,100));
		//frame.add(guessGrid1, BorderLayout.CENTER);
		
		JLabel guessGrid2 = new JLabel("guessGrid2", SwingConstants.CENTER);
		guessGrid2.setFont(new Font("Arial", Font.BOLD, 50));
		guessGrid2.setPreferredSize(new Dimension(0,100));
		//frame.add(guessGrid2, BorderLayout.CENTER);
		
		centerPanel.add(guessGrid1);
		centerPanel.add(guessGrid2);
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.setVisible(true);
		
		
	}
}
