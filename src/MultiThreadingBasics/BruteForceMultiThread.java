package MultiThreadingBasics;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <h1>Multithreaded Brute Force Password Attack</h1>
 * <p>
 * This class implements a parallelized brute force attack that distributes the workload
 * across multiple threads, with each thread searching a different password length.
 * This significantly reduces the time required to search through large keyspace.
 * <p>
 * Algorithm:
 * <ol>
 *     <li>Creates one thread for each password length from 1 to 8</li>
 *     <li>Each thread independently searches its assigned length space</li>
 *     <li>Uses atomic variables for thread-safe coordination</li>
 *     <li>Early termination when any thread finds the password</li>
 * </ol>
 * Parallelization Strategy: Length-based partitioning
 * <ul>
 *     <li>Thread 1: searches all 1-character passwords</li>
 *     <li>Thread 2: searches all 2-character passwords</li>
 *     <li>...</li>
 *     <li>Thread 8: searches all 8-character passwords</li>
 * </ul>
 * Use Case: Dramatically speeds up brute force attacks on multicore systems.
 * Time Complexity: O(m^n) per thread, but parallelized across threads.
 * Advantages: Linear speedup with CPU cores, efficient workload distribution.
 * <p>
 * Thread Safety: Achieved through AtomicBoolean and AtomicReference
 * <ul>
 *     <li>AtomicBoolean: ensures only one thread can claim "found" status</li>
 *     <li>AtomicReference: safely records which thread found the password</li>
 * </ul>
 *
 * @see Basics.BruteForce
 */
public class BruteForceMultiThread {
	// Thread-safe flag to coordinate early termination across all threads
	private static final AtomicBoolean found = new AtomicBoolean(false);
	// Thread-safe container to record which thread found the password
	private static final AtomicReference<String> threadName = new AtomicReference<>();
	private static final String password = "pas";
	
	public static void main(String[] args) throws InterruptedException {
		final ArrayList<Thread> threads = new ArrayList<>();
		
		// Create one thread for each password length (1-8)
		for (int i = 1; i <= 8; i++) {
			final int length = i;
			// Each thread searches its assigned length space independently
			final Thread thread = new Thread(() -> RecursiveBruteForce(new char[length], 0, length));
			
			thread.start();
			threads.add(thread);
		}
		
		// Wait for all threads to complete (or be interrupted by early termination)
		for (final Thread t : threads)
			t.join();
		
		// Report final result
		if (!found.get())
			System.out.println("Password Not Found");
		else
			System.out.println("Found " + password + " by " + threadName.get());
	}
	
	/**
	 * Recursively generates character combinations for brute force attack
	 * with thread-safe early termination checks
	 *
	 * @param passwordGuess character array representing current guess being built
	 * @param position      current position in the array being filled
	 * @param length        target length of password to generate
	 * @return true if password is found, false otherwise
	 */
	private static boolean RecursiveBruteForce(final char[] passwordGuess,
	                                           final int position, final int length) {
		// Early termination check - stop if another thread already found the password
		if (found.get())
			return false;
		
		// Base case: completed a password attempt of current length
		if (position == length) {
			final String passwordAttempt = new String(passwordGuess);
			System.out.println(Thread.currentThread().getName() + ": " + passwordAttempt);
			
			if (passwordAttempt.equals(password)) {
				// Atomic compare-and-set: only one thread can successfully set found=true
				if (found.compareAndSet(false, true))
					threadName.set(Thread.currentThread().getName()); // Winning thread
				return true;
			}
			
			return false;
		}
		
		// Character set for brute force attack
		final String lower = "abcdefghijklmnopqrstuvwxyz",
				upper = lower.toUpperCase(),
				numbers = "0123456789",
				symbols = "~`!@#$%^&*()_+-={}|[]\\ \";'<>?,./";
		
		// Try every possible character at current position
		final String allChars = lower + upper + numbers + symbols;
		for (final char c : allChars.toCharArray()) {
			passwordGuess[position] = c;
			// Recursively build remaining positions
			if (RecursiveBruteForce(passwordGuess, position + 1, length))
				return true; // Propagate success up recursion stack
		}
		
		return false; // No match found in this branch
	}
}
