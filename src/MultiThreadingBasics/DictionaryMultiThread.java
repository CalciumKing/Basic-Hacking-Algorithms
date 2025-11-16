package MultiThreadingBasics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <h1>Multithreaded Dictionary Attack</h1>
 * <p>
 * This class parallelizes dictionary attacks by dividing the wordlist into chunks
 * and processing each chunk in a separate thread. This provides near-linear speedup
 * for large wordlists on multicore systems.
 * <p>
 * Algorithm:
 * <ol>
 *     <li>Loads wordlist from file into memory</li>
 *     <li>Divides wordlist into equal-sized chunks (default: 100 words per chunk)</li>
 *     <li>Assigns each chunk to a separate worker thread</li>
 *     <li>Threads search their assigned chunks concurrently</li>
 *     <li>Early termination when any thread finds the password</li>
 * </ol>
 * Parallelization Strategy: Data partitioning (chunk-based)
 * <ul>
 *     <li>Thread 1: words[0] to words[99]</li>
 *     <li>Thread 2: words[100] to words[199]</li>
 *     <li>...</li>
 *     <li>Thread N: words[(N-1)*100] to words[length-1]</li>
 * </ul>
 * Use Case: High-speed dictionary attacks against large wordlists.
 * Time Complexity: O(n/t) where n is wordlist size, t is number of threads.
 * Advantages: Excellent scalability, efficient memory usage (shared wordlist).
 * <p>
 * Thread Safety: Atomic variables coordinate termination without locking
 *
 * @see Basics.Dictionary
 * @see Basics.AdvDictionary
 */
public class DictionaryMultiThread {
	// Thread-safe flag for coordination across all worker threads
	private static final AtomicBoolean found = new AtomicBoolean(false);
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		final String password = "vjht008";
		final String[] words = ReadFile(); // Load wordlist
		final ArrayList<Thread> threads = new ArrayList<>();
		final AtomicReference<String> threadName = new AtomicReference<>(); // Track winning thread
		
		// Divide wordlist into chunks for parallel processing
		final int chunkSize = 100,
				numChunks = (int) Math.ceil((double) words.length / chunkSize);
		
		// Create one thread per chunk
		for (int i = 0; i < numChunks; i++) {
			final int chunkIdx = i;
			final Thread thread = new Thread(() -> {
				// Calculate this thread's assigned range
				int start = chunkIdx * chunkSize;
				int end = Math.min(start + chunkSize, words.length);
				
				// Search through assigned chunk
				for (int j = start; j < end; j++) {
					// Check for early termination before each word
					if (found.get())
						return;
					
					final String curr = words[j];
					System.out.println(Thread.currentThread().getName() + " - " + curr);
					
					if (curr.equals(password)) {
						// Only one thread can successfully claim the "found" status
						if (found.compareAndSet(false, true))
							threadName.set(Thread.currentThread().getName());
						return; // This thread's work is done
					}
				}
			});
			
			thread.start();
			threads.add(thread);
		}
		
		// Wait for all threads to complete (or be interrupted by early termination)
		for (final Thread t : threads)
			t.join();
		
		// Report final result
		if (!found.get())
			System.out.println("Password not found");
		else
			System.out.println("Found " + password + " by " + threadName.get());
	}
	
	/**
	 * Loads wordlist from file into memory for parallel processing
	 *
	 * @return array containing all passwords from the wordlist
	 * @throws FileNotFoundException if wordlist file doesn't exist
	 */
	private static String[] ReadFile() throws FileNotFoundException {
		final File file = new File("./src/WordLists/top_passwords.txt");
		if (!file.exists())
			throw new FileNotFoundException("Wordlist not found.");
		
		final Scanner scanner = new Scanner(file);
		final ArrayList<String> passwords = new ArrayList<>();
		
		System.out.println("Adding passwords...");
		while (scanner.hasNextLine())
			passwords.add(scanner.nextLine());
		scanner.close(); // Important: release file handle
		
		System.out.println("Finished adding " + passwords.size() + " passwords");
		return passwords.toArray(new String[0]);
	}
}
