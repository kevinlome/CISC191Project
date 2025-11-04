import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class WordBattleView
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Word Battle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920,1080);
		
		//Creates the grey background color for JFrame
		int red = 82;
		int green = 81;
		int blue = 81;
		Color greyColor = new Color(red, green, blue);
		frame.getContentPane().setBackground(greyColor);
		
		frame.setVisible(true);
		frame.setLayout(new BorderLayout (10,10));
		JLabel gameName = new JLabel("Word Battle", SwingConstants.CENTER);
		gameName.setFont(new Font("Arial", Font.BOLD, 50));
        gameName.setPreferredSize(new Dimension(0,100));
		frame.add(gameName, BorderLayout.NORTH);
		
		JLabel keyboard = new JLabel("Keyboard", SwingConstants.CENTER);
		keyboard.setFont(new Font("Arial", Font.BOLD, 50));
		keyboard.setPreferredSize(new Dimension(0,100));
		frame.add(keyboard, BorderLayout.SOUTH);
		
		JLabel guessGrid1 = new JLabel("guessGrid1", SwingConstants.CENTER);
		guessGrid1.setFont(new Font("Arial", Font.BOLD, 50));
		guessGrid1.setPreferredSize(new Dimension(0,100));
		frame.add(guessGrid1, BorderLayout.CENTER);
		
		JLabel guessGrid2 = new JLabel("guessGrid2", SwingConstants.CENTER);
		guessGrid2.setFont(new Font("Arial", Font.BOLD, 50));
		frame.add(guessGrid2, BorderLayout.CENTER);
		
	}
}
