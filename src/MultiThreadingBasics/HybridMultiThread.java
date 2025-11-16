package MultiThreadingBasics;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <h1>Multithreaded Hybrid Dictionary-Brute Force Attack</h1>
 * <p>
 * This class parallelizes hybrid attacks by assigning each base dictionary word
 * to a separate thread. Each thread then generates numeric suffixes for its assigned
 * base word, searching for password combinations in parallel.
 * <p>
 * Algorithm:
 * <ol>
 *     <li>Takes a list of base dictionary words (keywords)</li>
 *     <li>Creates one thread per base word</li>
 *     <li>Each thread generates all possible numeric suffix combinations for its word</li>
 *     <li>Threads work concurrently on different base words</li>
 *     <li>Early termination when any thread finds the password</li>
 * </ol>
 * Parallelization Strategy: Task partitioning (word-based)
 * <ul>
 *     <li>Thread 1: searches "password" + "00", "password" + "01", ... "password" + "99"</li>
 *     <li>Thread 2: searches "wordpass" + "00", "wordpass" + "01", ... "wordpass" + "99"</li>
 * </ul>
 * Each thread handles a different base word with all numeric variations
 * <p>
 * Use Case: Parallel cracking of common password patterns with numeric suffixes.
 * Time Complexity: O(d * 10^m / t) where d is dictionary size, m is suffix length, t is threads.
 * Advantages: Excellent for targeting organization-specific password policies.
 * <p>
 * Thread Safety: Atomic variables enable lock-free coordination
 *
 * @see Basics.Hybrid
 */
public class HybridMultiThread {
	// Thread-safe coordination variables
	private static final AtomicBoolean found = new AtomicBoolean(false);
	private static final AtomicReference<String> threadName = new AtomicReference<>();
	private static final String password = "wordpass99";
	
	public static void main(String[] args) throws InterruptedException {
		final ArrayList<Thread> threads = new ArrayList<>();
		
		// Base words to attack - each gets its own thread
		String[] possibleKeywords = { "password", "wordpass" };
		
		// Create one thread per base word
		for (final String word : possibleKeywords) {
			final int maxComboLength = password.length() - word.length();
			// Each thread handles one base word with all numeric suffix combinations
			final Thread thread = new Thread(() -> HybridAttack(word, new char[maxComboLength], 0, maxComboLength));
			
			thread.start();
			threads.add(thread);
		}
		
		// Wait for all threads to complete
		for (final Thread t : threads)
			t.join();
		
		// Report final result
		if (!found.get())
			System.out.println("No password found");
		else
			System.out.println("Found " + password + " by " + threadName.get());
	}
	
	/**
	 * Recursively generates numeric suffixes for hybrid attack with thread-safe coordination
	 *
	 * @param baseWord the dictionary word being used as base
	 * @param append   character array for building numeric suffix
	 * @param position current position in suffix array
	 * @param length   maximum suffix length to generate
	 * @return {@code true} if password is found, {@code false} otherwise
	 */
	private static boolean HybridAttack(final String baseWord, final char[] append,
	                                    final int position, final int length) {
		// Early termination check - stop if password already found
		if (found.get())
			return false;
		
		// Base case: suffix complete, test the full combination
		if (position == length) {
			final String passwordAttempt = baseWord + new String(append);
			System.out.println(Thread.currentThread().getName() + ": " + passwordAttempt);
			
			if (passwordAttempt.equals(password)) {
				// Atomic claim: only one thread can successfully set the found flag
				if (found.compareAndSet(false, true))
					threadName.set(Thread.currentThread().getName());
				return true;
			}
			
			return false;
		}
		
		// Generate all numeric suffix combinations (0-9 at each position)
		for (final char c : "0123456789".toCharArray()) {
			append[position] = c;
			// Recursively build the rest of the numeric suffix
			if (HybridAttack(baseWord, append, position + 1, length))
				return true; // Propagate success
		}
		
		return false; // No match found for this base word
	}
}
