package MultiThreadingBasics;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class BruteForceMultiThread {
	private static final AtomicBoolean found = new AtomicBoolean(false);
	private static final AtomicReference<String> threadName = new AtomicReference<>();
	private static final String password = "pas";
	
	public static void main(String[] args) throws InterruptedException {
		final ArrayList<Thread> threads = new ArrayList<>();
		
		for (int i = 1; i <= 8; i++) {
			final int length = i;
			final Thread thread = new Thread(() -> RecursiveBruteForce(new char[length], 0, length));
			
			thread.start();
			threads.add(thread);
		}
		
		for (final Thread t : threads)
			t.join();
		
		if (!found.get())
			System.out.println("Password Not Found");
		else
			System.out.println("Found " + password + " by " + threadName.get());
	}
	
	private static boolean RecursiveBruteForce(final char[] passwordGuess,
	                                           final int position, final int length) {
		if (found.get())
			return false;
		
		if (position == length) {
			final String passwordAttempt = new String(passwordGuess);
			System.out.println(Thread.currentThread().getName() + ": " + passwordAttempt);
			
			if (passwordAttempt.equals(password)) {
				if (found.compareAndSet(false, true))
					threadName.set(Thread.currentThread().getName());
				return true;
			}
			
			return false;
		}
		
		final String lower = "abcdefghijklmnopqrstuvwxyz",
				upper = lower.toUpperCase(),
				numbers = "0123456789",
				symbols = "~`!@#$%^&*()_+-={}|[]\\ \";'<>?,./";
		
		for (final char c : (lower + upper + numbers + symbols).toCharArray()) {
			passwordGuess[position] = c;
			if (RecursiveBruteForce(passwordGuess, position + 1, length))
				return true;
		}
		
		return false;
	}
}
