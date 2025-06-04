package MultiThreadingBasics;

import java.util.ArrayList;

public class BruteForceMultiThread {
	private static volatile boolean found;
	private static final String password = "yes";
	private static final Object lock = new Object();
	
	public static void main(String[] args) throws InterruptedException {
		final ArrayList<Thread> threads = new ArrayList<>();
		
		for (int i = 0; i <= 8; i++) {
			final char[] passwordGuess = new char[i];
			final int length = i;
			
			final Thread thread = new Thread(() -> RecursiveBruteForce(passwordGuess, 0, length));
			
			thread.start();
			threads.add(thread);
		}
		
		for (final Thread t : threads)
			t.join();
		
		if (!found)
			System.out.println("Password Not Found");
	}
	
	private static boolean RecursiveBruteForce(final char[] passwordGuess,
	                                           final int position, final int length) {
		if (found) return false;
		
		if (position == length) {
			final String passwordAttempt = new String(passwordGuess);
			System.out.println(Thread.currentThread().getName() + ": " + passwordAttempt);
			
			if (passwordAttempt.equals(password)) {
				synchronized (lock) {
					if (!found) {
						found = true;
						System.out.println("Found " + passwordAttempt + " by " + Thread.currentThread().getName());
					}
				}
				return true;
			}
			
			return false;
		}
		
		for (final char c : ("abcdefghijklmnopqrstuvwxyz" +
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
				"0123456789" +
				"~`!@#$%^&*()_+-={}|[]\\ \";'<>?,./").toCharArray()) {
			passwordGuess[position] = c;
			if (RecursiveBruteForce(passwordGuess, position + 1, length))
				return true;
		}
		
		return false;
	}
}