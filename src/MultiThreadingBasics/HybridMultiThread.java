package MultiThreadingBasics;

import java.util.ArrayList;

@SuppressWarnings("SpellCheckingInspection")
public class HybridMultiThread {
	private static volatile boolean found;
	private static final String password = "wordpass99";
	private static final Object lock = new Object();
	
	public static void main(String[] args) throws InterruptedException {
		final ArrayList<Thread> threads = new ArrayList<>();
		
		for (final String word : new String[] { "password", "wordpass" }) {
			final int maxComboLength = password.length() - word.length();
			final Thread thread = new Thread(() -> HybridAttack(word, new char[maxComboLength], 0, maxComboLength));
			
			thread.start();
			threads.add(thread);
		}
		
		for (final Thread t : threads)
			t.join();
		
		if (!found)
			System.out.println("No password found");
	}
	
	private static boolean HybridAttack(final String baseWord, final char[] append,
	                                    final int position, final int length) {
		if (found) return false;
		
		if (position == length) {
			final String passwordAttempt = baseWord + new String(append);
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
		
		for (final char c : "0123456789".toCharArray()) {
			append[position] = c;
			if (HybridAttack(baseWord, append, position + 1, length))
				return true;
		}
		
		return false;
	}
}