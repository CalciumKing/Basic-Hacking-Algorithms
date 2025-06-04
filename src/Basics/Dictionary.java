package Basics;

public class Dictionary {
	public static void main(String[] args) {
		final String[] words = { "password", "123456", "admin", "abc", "letmein" };
		final String password = "abc";
		
		for (final String word : words) {
			if (word.equals(password)) {
				System.out.println("Password Found: " + word);
				return;
			}
		}
		
		System.out.println("Password not found");
	}
}