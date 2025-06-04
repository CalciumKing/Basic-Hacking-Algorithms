package Basics;

@SuppressWarnings("SpellCheckingInspection")
public class Hybrid {
	private static final String password = "wordpass99";
	
	public static void main(String[] args) {
		for (final String word : new String[] { "password", "wordpass" }) {
			System.out.println(word);
			if (word.equals(password)) {
				System.out.println("Password: " + word);
				return;
			}
			
			int maxComboLength = password.length() - word.length();
			if (HybridAttack(word, new char[maxComboLength], 0, maxComboLength))
				return;
			
			/*
			 Ex:
			 password00 - password99
			 wordpass00 - wordpass99
			*/
		}
		
		System.out.println("Password not found");
	}
	
	private static boolean HybridAttack(final String baseWord, final char[] append,
	                                    final int position, final int length) {
		if (position == length) {
			final String passwordAttempt = baseWord + new String(append);
			System.out.println(passwordAttempt);
			if (passwordAttempt.equals(password)) {
				System.out.println("Password: " + passwordAttempt);
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