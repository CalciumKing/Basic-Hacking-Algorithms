package Basics;

/**
 * <h1>Brute Force Password Attack</h1>
 * <p>
 * This class implements a brute force attack that systematically generates all possible
 * character combinations to find the target password. It explores the entire keyspace
 * by trying every possible combination of characters for increasing password lengths.
 * <p>
 * Algorithm:
 * <ol>
 *     <li>Generates combinations for password lengths 1 through 8</li>
 *     <li>Uses recursive backtracking to build character sequences</li>
 *     <li>Character set includes: lowercase, uppercase, numbers, and symbols</li>
 *     <li>Tries shorter passwords before longer ones (length-first approach)</li>
 * </ol>
 * Use Case: Effective when no information about password is known.
 * Time Complexity: O(m^n) where m is character set size, n is max password length.
 * Space Complexity: O(n) for recursion stack.
 * <p>
 * Character Set Size: 26(lower) + 26(upper) + 10(numbers) + 30(symbols) ≈ 92 characters
 * Total Combinations for length 8: 92^8 ≈ 5.13 × 10^15 (computationally intensive)
 * <p>
 * Limitations:
 * <ul>
 *     <li>Exponentially grows with password length</li>
 *     <li>Impractical for passwords longer than 8 characters</li>
 *     <li>No intelligence about common patterns</li>
 * </ul>
 *
 * @see MultiThreadingBasics.BruteForceMultiThread
 */
public class BruteForce {
	private static final String password = "pass";
	
	public static void main(String[] args) {
		// OPTION 1: If password length is known (more efficient)
//		 int passwordLength = password.length();
//		 if (RecursiveBruteForce(new char[passwordLength], 0, passwordLength))
//		     return;
		
		/*
		 * Search Pattern Example for length 3:
		 * aaa, aab, aac... aaz, aba, abb... zzz
		 * This covers the entire keyspace systematically
		 */
		
		// OPTION 2: Search all lengths from 1 to 8 (comprehensive)
		for (int i = 1; i <= 8; i++)
			if (RecursiveBruteForce(new char[i], 0, i))
				return;
		
		/*
		 * Length-First Search Pattern:
		 * Length 1: a, b, c... z
		 * Length 2: aa, ab, ac... az, ba, bb... zz
		 * Length 3: aaa, aab, aac... zzz
		 * etc.
		 */
		
		System.out.println("Password Not Found");
	}
	
	/**
	 * Recursively generates all possible character combinations for brute force attack
	 *
	 * @param passwordGuess character array representing current guess being built
	 * @param position      current position in the array being filled
	 * @param length        target length of password to generate
	 * @return {@code true} if password is found, {@code false} otherwise
	 */
	private static boolean RecursiveBruteForce(final char[] passwordGuess, final int position, final int length) {
		// Base case: reached the end of current password length
		if (position == length) {
			final String passwordAttempt = new String(passwordGuess);
			System.out.println(passwordAttempt); // Display attempt for demonstration
			
			if (passwordAttempt.equals(password)) {
				System.out.println("Password Found: " + passwordAttempt);
				return true;
			}
			return false;
		}
		
		// Define character set for brute force attack
		final String lower = "abcdefghijklmnopqrstuvwxyz",
				upper = lower.toUpperCase(),
				numbers = "0123456789",
				symbols = "~`!@#$%^&*()_+-={}|[]\\ \";'<>?,./";
		
		// Combine all character sets and try each character at current position
		final String allChars = lower + upper + numbers + symbols;
		for (final char c : allChars.toCharArray()) {
			passwordGuess[position] = c; // Try character at current position
			
			// Recursively fill remaining positions
			if (RecursiveBruteForce(passwordGuess, position + 1, length))
				return true; // Propagate success up recursion stack
		}
		
		return false; // No match found for this branch
	}
}
