package Basics;

/**
 * <h1>Hybrid Dictionary-Brute Force Attack</h1>
 * <p>
 * This class combines dictionary and brute force techniques by taking base words
 * from a dictionary and appending numeric suffixes. This approach targets common
 * password patterns where users add numbers to dictionary words.
 * <p>
 * Algorithm:
 * <ol>
 *     <li>Start with dictionary words (base keywords)</li>
 *     <li>For each base word, generate combinations with numeric suffixes</li>
 *     <li>Suffix length varies from 0 to (password_length - base_word_length)</li>
 *     <li>Systematic numeric suffix generation using brute force</li>
 * </ol>
 * Use Case: Effective against passwords like "password123", "hello2023", etc.
 * Time Complexity: O(d * 10^m) where d is dictionary size, m is max suffix length.
 * Space Complexity: O(m) for recursion stack.
 * <p>
 * Advantages:
 * <ul>
 *     <li>More efficient than pure brute force</li>
 *     <li>Catches common password patterns</li>
 *     <li>Practical for real-world password cracking</li>
 * </ul>
 * Limitations:
 * <ul>
 *     <li>Only appends numbers (no prefixes or mixed modifications)</li>
 *     <li>Dependent on quality of base word dictionary</li>
 * </ul>
 *
 * @see MultiThreadingBasics.HybridMultiThread
 */
public class Hybrid {
	private static final String password = "wordpass99";
	
	public static void main(String[] args) {
		// Base words to try - in practice, this would be a larger dictionary
		String[] possibleKeywords = { "password", "wordpass" };
		
		// Try each base word with various numeric suffixes
		for (final String word : possibleKeywords) {
			System.out.println("Base word: " + word);
			
			// First, check if base word itself is the password
			if (word.equals(password)) {
				System.out.println("Password: " + word);
				return;
			}
			
			// Calculate maximum suffix length based on remaining characters
			final int maxComboLength = password.length() - word.length();
			
			// Generate combinations with numeric suffixes
			if (HybridAttack(word, new char[maxComboLength], 0, maxComboLength))
				return; // Exit if password found
			
			/*
			 * Attack Pattern Examples:
			 * Base: "password" → password00, password01... password99
			 * Base: "wordpass" → wordpass00, wordpass01... wordpass99
			 */
		}
		
		System.out.println("Password not found");
	}
	
	/**
	 * Recursively generates numeric suffixes to append to base words
	 *
	 * @param baseWord the dictionary word being used as base
	 * @param append   character array for building numeric suffix
	 * @param position current position in suffix array
	 * @param length   maximum suffix length to generate
	 * @return {@code true} if password is found, {@code false} otherwise
	 */
	private static boolean HybridAttack(final String baseWord, final char[] append,
	                                    final int position, final int length) {
		// Base case: suffix complete, test the combination
		if (position == length) {
			final String passwordAttempt = baseWord + new String(append);
			System.out.println(passwordAttempt);
			
			if (passwordAttempt.equals(password)) {
				System.out.println("Password Found: " + passwordAttempt);
				return true;
			}
			return false;
		}
		
		// Only use numeric characters for suffix generation
		for (final char c : "0123456789".toCharArray()) {
			append[position] = c; // Add digit to current suffix position
			
			// Recursively build the rest of the suffix
			if (HybridAttack(baseWord, append, position + 1, length))
				return true;
		}
		
		return false; // No match found for this suffix pattern
	}
}
