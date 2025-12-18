/**
 * Lead Author(s):
 * @author Kevin Lome
 * @author Nick Damion
 *
 * Other contributors:
 *
 * References:
 * Morelli, R., & Walde, R. (2016). Java, Java, Java: Object-Oriented Problem Solving.
 * Retrieved from https://open.umn.edu/opentextbooks/textbooks/java-java-java-object-oriented-problem-solving
 *
 * Version/date: December 18, 2025
 *
 * Responsibilities:
 * WordGenerator class for managing and retrieving words from a word list
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * WordGenerator class for managing and retrieving words from a word list.
 * 
 * This class loads words from a file (clean_word_list.txt) and provides methods
 * to retrieve random words, validate words, and generate word pairs for
 * players.
 * The class searches for the word list file in multiple locations to ensure
 * compatibility with different project structures.
 * 
 * @author Word Battle Game
 * @version 1.0
 */
public class WordGenerator
{
	/** List of valid words loaded from the word list file */
	private List<String> wordList;

	/** Random number generator for selecting words */
	private Random random;

	/**
	 * Constructs a WordGenerator object and initializes the word list.
	 * 
	 * Upon construction, this class automatically loads words from the
	 * clean_word_list.txt file
	 * and initializes the random number generator. If the word list file cannot
	 * be found,
	 * a warning message is printed but the program continues with an empty word
	 * list.
	 */
	public WordGenerator()
	{
		this.wordList = new ArrayList<>();
		this.random = new Random();
		loadWords();
	}

	/**
	 * Loads words from the clean_word_list.txt file.
	 * 
	 * This private method searches for the word list file in multiple locations
	 * to accommodate
	 * different project structures and execution contexts. It reads each line
	 * from the file,
	 * trims whitespace, and adds non-empty lines to the word list. Lines
	 * starting with "//"
	 * are treated as comments and ignored.
	 * 
	 * The method searches in the following order:
	 * <ul>
	 * <li>clean_word_list.txt (current directory)</li>
	 * <li>../clean_word_list.txt (parent directory)</li>
	 * <li>resources/clean_word_list.txt (resources folder)</li>
	 * <li>../resources/clean_word_list.txt (parent resources folder)</li>
	 * </ul>
	 * 
	 * If the file is found, words are loaded and a success message is printed
	 * to System.out.
	 * If no file is found in any location, a warning is printed to System.err
	 * but execution continues.
	 * 
	 * @see #wordList
	 */
	private void loadWords()
	{
		String[] possiblePaths = { "clean_word_list.txt",
				"../clean_word_list.txt", "resources/clean_word_list.txt",
				"../resources/clean_word_list.txt" };

		for (String path : possiblePaths)
		{
			File file = new File(path);
			if (file.exists())
			{
				try (BufferedReader reader = new BufferedReader(
						new FileReader(file)))
				{
					String line;
					while ((line = reader.readLine()) != null)
					{
						line = line.trim();
						if (!line.isEmpty() && !line.startsWith("//"))
						{
							wordList.add(line.toLowerCase());
						}
					}
					System.out.println("Successfully loaded " + wordList.size()
							+ " words from " + path);
					return;
				}
				catch (IOException e)
				{
					System.err.println("Error loading word list from " + path
							+ ": " + e.getMessage());
				}
			}
		}
		System.err.println(
				"Warning: Could not find clean_word_list.txt in any expected location");
	}

	/**
	 * Gets a random word from the word list for a player.
	 * 
	 * Selects a random word from the loaded word list using the Random object.
	 * This method is useful for assigning secret words to players during game
	 * initialization.
	 * 
	 * @return a random word from the clean word list, or null if the word list
	 *         is empty
	 */
	public String getRandomWord()
	{
		if (wordList.isEmpty())
		{
			return null;
		}
		return wordList.get(random.nextInt(wordList.size()));
	}

	/**
	 * Checks if a word is valid (exists in the word list).
	 * 
	 * This method performs a case-insensitive search to determine if the
	 * provided word
	 * exists in the loaded word list. All words in the word list are stored in
	 * lowercase
	 * for consistent comparison.
	 * 
	 * @param word the word to validate
	 * @return true if the word is in the clean word list, false otherwise
	 */
	public boolean isValidWord(String word)
	{
		return wordList.contains(word.toLowerCase());
	}

	/**
	 * Gets the total number of words in the word list.
	 * 
	 * This method returns the size of the word list, which represents the total
	 * number
	 * of valid words that can be selected for gameplay.
	 * 
	 * @return the number of words in the word list
	 */
	public int getWordListSize()
	{
		return wordList.size();
	}

	/**
	 * Generates two different random words for both players.
	 * 
	 * This method selects two unique random words from the word list, ensuring
	 * that
	 * each player receives a different secret word for the game. The method
	 * guarantees
	 * that the two returned words are distinct from each other.
	 * 
	 * @return an array of two unique random words {word1, word2} for the
	 *         players,
	 *         or null if the word list contains fewer than 2 words
	 * 
	 * @throws NullPointerException if the word list is not initialized
	 */
	public String[] generateWordsForPlayers()
	{
		if (wordList.size() < 2)
		{
			return null;
		}

		String word1 = getRandomWord();
		String word2;

		// Ensure word2 is different from word1
		do
		{
			word2 = getRandomWord();
		} while (word2.equals(word1));

		return new String[] { word1, word2 };
	}
}
