import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class WordBattleView
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Word Battle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920,1080);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout (10,10));
		JLabel gameName = new JLabel("Word Battle");
        gameName.setPreferredSize();
		frame.add(gameName, BorderLayout.NORTH);
		
	}
}
