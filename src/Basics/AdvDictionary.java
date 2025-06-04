package Basics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AdvDictionary {
	public static void main(String[] args) throws FileNotFoundException {
		final String password = "mypassword";
		
		final File file = new File("src/WordLists/top_passwords.txt");
		if (!file.exists())
			System.exit(1);
		
		final Scanner scanner = new Scanner(file);
		
		while (scanner.hasNextLine()) {
			final String word = scanner.nextLine();
			if (word.equals(password)) {
				System.out.println("Password Found: " + word);
				return;
			}
		}
		
		System.out.println("Password not found");
	}
}