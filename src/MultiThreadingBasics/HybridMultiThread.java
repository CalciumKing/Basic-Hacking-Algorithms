package MultiThreadingBasics;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class HybridMultiThread {
	private static final AtomicBoolean found = new AtomicBoolean(false);
	private static final AtomicReference<String> threadName = new AtomicReference<>();
	private static final String password = "wordpass99";
	
	public static void main(String[] args) throws InterruptedException {
		final ArrayList<Thread> threads = new ArrayList<>();
		
		String[] possibleKeywords = { "password", "wordpass" };
		for (final String word : possibleKeywords) {
			final int maxComboLength = password.length() - word.length();
			final Thread thread = new Thread(() -> HybridAttack(word, new char[maxComboLength], 0, maxComboLength));
			
			thread.start();
			threads.add(thread);
		}
		
		for (final Thread t : threads)
			t.join();
		
		if (!found.get())
			System.out.println("No password found");
		else
			System.out.println("Found " + password + " by " + threadName.get());
	}
	
	private static boolean HybridAttack(final String baseWord, final char[] append,
	                                    final int position, final int length) {
		if (found.get())
			return false;
		
		if (position == length) {
			final String passwordAttempt = baseWord + new String(append);
			System.out.println(Thread.currentThread().getName() + ": " + passwordAttempt);
			
			if (passwordAttempt.equals(password)) {
				if (found.compareAndSet(false, true))
					threadName.set(Thread.currentThread().getName());
				return true;
			}
			
			return false;
		}
		
		for (final char c : "0123456789".toCharArray()) {
			append[position] = c;
			if (HybridAttack(baseWord, append, position + 1, length))
				return true;
		}
		
		return false;
	}
}
