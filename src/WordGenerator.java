import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordGenerator {
    private List<String> wordList;
    private Random random;
    
    public WordGenerator() {
        this.wordList = new ArrayList<>();
        this.random = new Random();
        loadWords();
    }
    
    /**
     * Loads words from the clean_word_list.txt file
     * Searches in multiple locations: current directory, parent directory, and resources folder
     */
    private void loadWords() {
        String[] possiblePaths = {
            "clean_word_list.txt",
            "../clean_word_list.txt",
            "resources/clean_word_list.txt",
            "../resources/clean_word_list.txt"
        };
        
        for (String path : possiblePaths) {
            File file = new File(path);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (!line.isEmpty() && !line.startsWith("//")) {
                            wordList.add(line.toLowerCase());
                        }
                    }
                    System.out.println("Successfully loaded " + wordList.size() + " words from " + path);
                    return;
                } catch (IOException e) {
                    System.err.println("Error loading word list from " + path + ": " + e.getMessage());
                }
            }
        }
        System.err.println("Warning: Could not find clean_word_list.txt in any expected location");
    }
    
    /**
     * Gets a random word from the word list for a player
     * @return a random word from the clean word list
     */
    public String getRandomWord() {
        if (wordList.isEmpty()) {
            return null;
        }
        return wordList.get(random.nextInt(wordList.size()));
    }
    
    /**
     * Checks if a word is valid (exists in the word list)
     * @param word the word to validate
     * @return true if the word is in the clean word list, false otherwise
     */
    public boolean isValidWord(String word) {
        return wordList.contains(word.toLowerCase());
    }
    
    /**
     * Gets the total number of words in the word list
     * @return the size of the word list
     */
    public int getWordListSize() {
        return wordList.size();
    }
    
    /**
     * Generates two different random words for both players
     * @return an array of two random words for the players, or null if list is too small
     */
    public String[] generateWordsForPlayers() {
        if (wordList.size() < 2) {
            return null;
        }
        
        String word1 = getRandomWord();
        String word2;
        
        // Ensure word2 is different from word1
        do {
            word2 = getRandomWord();
        } while (word2.equals(word1));
        
        return new String[]{word1, word2};
    }
}
