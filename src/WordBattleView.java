import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class WordBattleView
{
	public static void main(String[] args)
	{
		
		String playerName1 = JOptionPane.showInputDialog(null, "Enter Player 1 Name:", "User Input", JOptionPane.QUESTION_MESSAGE);
		String playerName2 = JOptionPane.showInputDialog(null, "Enter Player 2 Name:", "User Input", JOptionPane.QUESTION_MESSAGE);
		
		JFrame frame = new JFrame("Word Battle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920,1080);
		frame.setLayout(new BorderLayout (10,10));
		
		//Creates the grey background color for JFrame
		int red = 82;
		int green = 81;
		int blue = 81;
		Color greyColor = new Color(red, green, blue);
		frame.getContentPane().setBackground(greyColor);
		
		frame.setVisible(true);
		
		JLabel gameName = new JLabel("Word Battle", SwingConstants.CENTER);
		gameName.setFont(new Font("Arial", Font.BOLD, 50));
        gameName.setPreferredSize(new Dimension(0,100));
		frame.add(gameName, BorderLayout.NORTH);
		
		JLabel keyboard = new JLabel("Keyboard", SwingConstants.CENTER);
		keyboard.setFont(new Font("Arial", Font.BOLD, 50));
		keyboard.setPreferredSize(new Dimension(0,100));
		frame.add(keyboard, BorderLayout.SOUTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(greyColor);
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
