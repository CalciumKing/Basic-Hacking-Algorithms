package Basics;

/**
 * <h1>Basic Dictionary Attack</h1>
 * <p>
 * This class implements a simple dictionary attack using a hardcoded array of
 * common passwords. It demonstrates the fundamental concept of dictionary attacks
 * without file I/O complexity.
 * <p>
 * Algorithm:
 * <ol>
 *     <li>Uses predefined array of common passwords</li>
 *     <li>Linear search through the array</li>
 *     <li>Case-sensitive exact matching</li>
 *     <li>Stops at first match</li>
 * </ol>
 * Use Case: Educational demonstration of dictionary attack principle.
 * Time Complexity: O(n) where n is number of words in dictionary.
 * Space Complexity: O(n) for storing the word array.
 * <p>
 * Advantages:
 * <ul>
 *     <li>Simple to implement and understand</li>
 *     <li>Very fast for small dictionaries</li>
 *     <li>No external dependencies</li>
 * </ul>
 * <p>
 * Limitations:
 * <ul>
 *     <li>Limited by hardcoded wordlist size</li>
 *     <li>No variations or modifications to words</li>
 *     <li>Not practical for real attacks without extensive wordlists</li>
 * </ul>
 *
 * @see AdvDictionary
 * @see MultiThreadingBasics.DictionaryMultiThread
 */
public class Dictionary {
	public static void main(String[] args) {
		// Common passwords dictionary - in real scenarios, this would be much larger
		final String[] words = { "password", "123456", "admin", "abc", "letmein" };
		final String password = "abc";
		
		// Linear search through dictionary
		for (final String word : words) {
			if (word.equals(password)) {
				System.out.println("Password Found: " + word);
				return; // Exit early when match found
			}
		}
		
		// If loop completes, no match was found
		System.out.println("Password not found");
	}
}
