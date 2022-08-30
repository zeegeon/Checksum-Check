package raon.encryption;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

public class Test {
	// Encryption variable
	static AES aes = new AES();
	static SHA hash = new SHA();
	static RSA rsa = new RSA();
		
	static String buffer ="";
	//static File buffer2 = new File(buffer);

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
					 //f6a5cd16ea3dfa2587ab5dd3503d283a49ba039270c023d54e65d2a1ae9f4ae4
						System.out.print("String :");
						buffer = sc.next();
				        System.out.println("SHA-256 hash : " + hash.generateHash(buffer));
				        
				        buffer = sc.next();
				        //C:\\Users\\Milo\\Desktop\\aes256.txt
				        File buffer2 = new File(buffer);
				        System.out.println("File hash : aes.txt");
				        System.out.println("SHA-256 hash : " + hash.getFileChecksum(buffer2) + "\n");
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
