package MultiThreadingBasics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DictionaryMultiThread {
	private static volatile boolean found;
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		final String password = "mypassword";
		final ArrayList<String> words = ReadFile();
		final ArrayList<Thread> threads = new ArrayList<>();
		final Object lock = new Object();
		
		for (int i = 0; i < words.size(); i += 100) {
			final int finalI = i;
			final Thread thread = new Thread(() -> {
				for (int j = finalI; j < Math.min(finalI + 100, words.size()); j++) {
					if (found) return;
					
					final String curr = words.get(j);
					System.out.println(Thread.currentThread().getName() + " - " + curr);
					
					if (curr.equals(password)) {
						synchronized (lock) {
							if (!found) {
								found = true;
								System.out.println("Found " + curr + " by " + Thread.currentThread().getName());
							}
						}
						return;
					}
				}
			});
			
			thread.start();
			threads.add(thread);
		}
		
		for (final Thread t : threads)
			t.join();
		
		if (!found)
			System.out.println("Password not found");
	}
	
	private static ArrayList<String> ReadFile() throws FileNotFoundException {
		final File file = new File("src/WordLists/top_passwords.txt");
		if (!file.exists())
			System.exit(1);
		
		final Scanner scanner = new Scanner(file);
		final ArrayList<String> passwords = new ArrayList<>();
		
		System.out.println("Adding passwords...");
		while (scanner.hasNextLine())
			passwords.add(scanner.nextLine());
		
		System.out.println("Finished adding passwords");
		return passwords;
	}
}