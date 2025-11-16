package Basics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * <h1>Advanced Dictionary Search Attack</h1>
 * <p>
 * This class implements a dictionary attack that searches through a wordlist file
 * to find a matching password. It's considered "advanced" because it uses an external
 * file-based wordlist rather than a hardcoded array, making it more practical for
 * real-world scenarios.
 * <p>
 * Algorithm:
 * <ol>
 *     <li>Loads passwords from a text file wordlist</li>
 *     <li>Compares each word against the target password</li>
 *     <li>Stops immediately when a match is found</li>
 * </ol>
 * Use Case: Effective against users who use common passwords found in wordlists.
 * Time Complexity: O(n) where n is the number of words in the wordlist.
 * Space Complexity: O(1) - only stores one word at a time during scanning.
 * <p>
 * Limitations:
 * <ul>
 *     <li>Only finds exact matches (no password modifications)</li>
 *     <li>Dependent on quality and completeness of wordlist</li>
 *     <li>Linear search - no optimizations for large files</li>
 * </ul>
 *
 * @see Dictionary
 * @see MultiThreadingBasics.DictionaryMultiThread
 */
public class AdvDictionary {
	public static void main(String[] args) throws FileNotFoundException {
		final String password = "mypassword";
		
		// Load a wordlist file containing common passwords
		final File file = new File("src/WordLists/top_passwords.txt");
		if (!file.exists())
			throw new FileNotFoundException("Wordlist not found.");
		
		final Scanner scanner = new Scanner(file);
		
		// Iterate through each word in the wordlist
		while (scanner.hasNextLine()) {
			final String word = scanner.nextLine();
			
			// Compare current word with target password
			if (word.equals(password)) {
				System.out.println("Password Found: " + word);
				scanner.close(); // Clean up resources
				return; // Exit when password is found
			}
		}
		
		// Only reached if no match found in entire wordlist
		System.out.println("Password not found");
		scanner.close(); // Clean up resources
	}
}
