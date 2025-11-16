package MultiThreadingBasics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DictionaryMultiThread {
	private static final AtomicBoolean found = new AtomicBoolean(false);
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		final String password = "vjht008";
		final String[] words = ReadFile();
		final ArrayList<Thread> threads = new ArrayList<>();
		final AtomicReference<String> threadName = new AtomicReference<>();
		
		final int chunkSize = 100,
				numChunks = (int) Math.ceil((double) words.length / chunkSize);
		for (int i = 0; i < numChunks; i++) {
			final int chunkIdx = i;
			final Thread thread = new Thread(() -> {
				int start = chunkIdx * chunkSize;
				int end = Math.min(start + chunkSize, words.length);
				
				for (int j = start; j < end; j++) {
					if (found.get())
						return;
					
					final String curr = words[j];
					System.out.println(Thread.currentThread().getName() + " - " + curr);
					
					if (curr.equals(password)) {
						if (found.compareAndSet(false, true))
							threadName.set(Thread.currentThread().getName());
						return;
					}
				}
			});
			
			thread.start();
			threads.add(thread);
		}
		
		for (final Thread t : threads)
			t.join();
		
		if (!found.get())
			System.out.println("Password not found");
		else
			System.out.println("Found " + password + " by " + threadName.get());
	}
	
	private static String[] ReadFile() throws FileNotFoundException {
		final File file = new File("./src/WordLists/top_passwords.txt");
		if (!file.exists())
			throw new FileNotFoundException("Wordlist not found.");
		
		final Scanner scanner = new Scanner(file);
		final ArrayList<String> passwords = new ArrayList<>();
		
		System.out.println("Adding passwords...");
		while (scanner.hasNextLine())
			passwords.add(scanner.nextLine());
		scanner.close();
		
		System.out.println("Finished adding " + passwords.size() + " passwords");
		return passwords.toArray(new String[0]);
	}
}
