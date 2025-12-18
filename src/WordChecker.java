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
 * WordChecker validates player guesses using Dictionary API
 */
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * WordChecker validates player guesses using Dictionary API
 * Ensures only real English 5-letter words are accepted
 */
public class WordChecker {
    
    private static final String DICTIONARY_API = "https://api.dictionaryapi.dev/api/v1/entries/en/";
    
    /**
     * Validates if a guess is a real English 5-letter word using Dictionary API
     * @param guess The word to validate
     * @return true if the word is a valid English word, false otherwise
     */
    public static boolean isValidWord(String guess) {
        if (guess == null || guess.trim().isEmpty()) {
            return false;
        }
        
        String word = guess.trim().toLowerCase();
        
        // Must be exactly 5 letters
        if (word.length() != 5) {
            return false;
        }
        
        try {
            URL url = new URL(DICTIONARY_API + word);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            
            // 200 = word exists, 404 = word not found
            return responseCode == 200;
            
        } catch (Exception e) {
            System.err.println("Error checking word with API: " + e.getMessage());
            return false;
        }
    }
}