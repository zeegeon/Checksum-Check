package raon.encryption;

import java.io.File;
import java.util.Scanner;

public class Test {
	// Encryption variable
	static AES aes = new AES();
	static SHA hash = new SHA();
	static RSA rsa = new RSA();
		
	static String buffer ="";

	public static void main(String[] args) throws Exception {
		
		Scanner sc = new Scanner(System.in);
		
        System.out.println("========== Encryption Test Bench ===========");
        
		while(true) {
			System.out.print("Select Encryption Type : 1. SHA  2. AES : ");
			int mode = sc.nextInt();
			
			switch(mode) {
				// Hashing test
				case 1:
					System.out.println("\nSelected Encryption Type : SHA-256");
					 // f6a5cd16ea3dfa2587ab5dd3503d283a49ba039270c023d54e65d2a1ae9f4ae4
						System.out.print("String :");
						buffer = sc.next();
				        System.out.println("SHA-256 hash : " + hash.generateHash(buffer));
				        
				        // C:\\Users\\Milo\\Desktop\\aes256.txt
				        System.out.print("File location : ");
				        buffer = sc.next();
				        File buffer2 = new File(buffer);
				        System.out.println("SHA-256 hash : " + hash.generateFileHash(buffer2) + "\n");
					break;
					
				// AES-256 test
				case 2:
					System.out.println("\nSelected Encryption Type : AES-256");
					System.out.print("1. Encryption 2. Decryption : ");
					mode = sc.nextInt();
					
					if(mode==1) {
						System.out.print("String :");
						// Base 64 / CBC / 256WZAzIMHNcxhWJjiVgExfvQ==
						// IV : f6a5cd16ea3dfa25
						buffer = sc.next();
				        System.out.println("AES-256 decrypt : " + aes.encrypt(buffer));
					}
					// Zc5VJmZjja3B3YlVzQVsAA==
					else if(mode==2) {
						System.out.print("String :");
						buffer = sc.next();
				        System.out.println("AES-256 decrypt : " + aes.decrypt(buffer) + "\n");
					}
					break;
				default:
					System.out.println("Wrong input! Try again");
					break;
			}
		}
	}
}
