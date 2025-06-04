package Basics;

public class BruteForce {
	private static final String password = "pee";
	
	public static void main(String[] args) {
		// vvv if password length is already known vvv
//        int passwordLength = password.length();
//        if (RecursiveBruteForce(new char[passwordLength], 0, passwordLength))
//            return;
		// ^^^
		
		/*
		 Ex:
		 aaa - aaz
		 aba - azz
		 baa - zzz
		*/
		
		// vvv if not vvv
		for (int i = 0; i <= 8; i++)
			if (RecursiveBruteForce(new char[i], 0, i))
				return;
		// ^^^
		
		/*
		 Ex:
		 a - z
		 aa - az
		 ba - bz
		 ...
		 za - zz
		 aaa - aaz
		 ...
		*/
		
		System.out.println("Password Not Found");
	}
	
	private static boolean RecursiveBruteForce(final char[] passwordGuess, final int position, final int length) {
		if (position == length) {
			final String passwordAttempt = new String(passwordGuess);
			System.out.println(passwordAttempt);
			
			if (passwordAttempt.equals(password)) {
				System.out.println(passwordAttempt);
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