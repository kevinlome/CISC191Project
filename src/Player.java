import java.util.Scanner;

public class Player
{
	private String player1;
	private String player2;
	private String targetWord1;
	private String targetWord2;

	/**
	 * 
	 * Purpose: Obtains the names of each player
	 * 
	 * @param names
	 */
	public static void main(String[] names){
		Scanner scanner = new Scanner(System.in);

		System.out.println("Player 1 Enter Name:");
		String player1Name = scanner.nextLine();
		System.out.println("Player 2 Enter Name: ");
		String player2Name = scanner.nextLine();
		scanner.close();
	}


}