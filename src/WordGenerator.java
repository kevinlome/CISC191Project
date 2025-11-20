
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordGenerator
{
	private List<String> wordList;
	private Random random;
	private static final String API_URL = "https://api.datamuse.com/words?sp=?????&max=1000";
	
	/**
	 * Constructor that initializes the word list from the dictionary API
	 */
	public WordGenerator()
	{
		this.random = new Random();
		this.wordList = new ArrayList<>();
		// Fetch words on a background thread to avoid blocking the UI
		Thread fetchThread = new Thread(() -> fetchFiveLetterWords());
		fetchThread.setDaemon(true);
		fetchThread.start();
	}
	
	/**
	 * Fetches 5-letter words from the dictionary API
	 */
	private void fetchFiveLetterWords()
	{
		try
		{
			URL url = new URL(API_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			
			while ((line = reader.readLine()) != null)
			{
				response.append(line);
			}
			reader.close();
			
			// Parse the JSON array manually using String methods
			String jsonResponse = response.toString();
			
			// Extract words from JSON objects in format: [{"word":"abate","score":...},...]
			int startIdx = 0;
			while (true)
			{
				// Find "word":" pattern
				int wordFieldStart = jsonResponse.indexOf("\"word\":\"", startIdx);
				if (wordFieldStart == -1)
					break;
				
				// Move past the "word":" part to find the actual word value
				int valueStart = wordFieldStart + 8; // Length of "word":"
				int valueEnd = jsonResponse.indexOf('"', valueStart);
				
				if (valueEnd != -1)
				{
					String word = jsonResponse.substring(valueStart, valueEnd).trim();
					if (word.length() == 5)
					{
						wordList.add(word.toUpperCase());
					}
					startIdx = valueEnd + 1;
				}
				else
				{
					break;
				}
			}
			
			System.out.println("Successfully loaded " + wordList.size() + " five-letter words from DataMuse API");
		}
		catch (Exception e)
		{
			System.err.println("Error fetching words from API: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Waits for words to be loaded from the API
	 */
	private void waitForWordsLoaded()
	{
		// Wait up to 30 seconds for words to load
		long startTime = System.currentTimeMillis();
		while (wordList.isEmpty() && System.currentTimeMillis() - startTime < 30000)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
				break;
			}
		}
	}
	
	/**
	 * Gets a random 5-letter word for a player
	 * @return A random 5-letter word, or null if no words available
	 */
	public String getRandomWord()
	{
		waitForWordsLoaded();
		if (wordList.isEmpty())
		{
			return null;
		}
		return wordList.get(random.nextInt(wordList.size()));
	}
	
	/**
	 * Gets two different random 5-letter words for player1 and player2
	 * @return An array containing two different words [player1Word, player2Word], or null if not enough words available
	 */
	public String[] generateWordsForPlayers()
	{
		waitForWordsLoaded();
		if (wordList.size() < 2)
		{
			return null;
		}
		
		String player1Word = getRandomWord();
		String player2Word;
		
		// Ensure player2 gets a different word than player1
		do
		{
			player2Word = getRandomWord();
		} while (player2Word.equals(player1Word));
		
		return new String[] { player1Word, player2Word };
	}
	
	/**
	 * Gets the total number of available 5-letter words
	 * @return The size of the word list
	 */
	public int getWordCount()
	{
		return wordList.size();
	}
}
