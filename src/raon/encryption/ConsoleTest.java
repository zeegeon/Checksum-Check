package raon.encryption;

import java.io.File;

public class ConsoleTest {

	public static void main(String[] args) throws Exception {
		// SHA text hash test
		HashGenerator hash = new HashGenerator();
		String msg = hash.generateHash("RAONTECH");
		if(!msg.equals("f6a5cd16ea3dfa2587ab5dd3503d283a49ba039270c023d54e65d2a1ae9f4ae4"))
			System.out.println("SHA text hash error");
		
		// SHA file hash test
		File file= new File("resource//TestFile.txt");
		msg = hash.generateFileHash(file);
		if(!msg.equals("f6a5cd16ea3dfa2587ab5dd3503d283a49ba039270c023d54e65d2a1ae9f4ae4"))
			System.out.println("SHA file hash error");
		
		// AES256 Encrypt Test
		AES256Controller aes = new AES256Controller();
		msg = aes.encrypt("Test Text");
		if(!msg.equals("JbwrSuttTa9v+dQdRigFCw=="))
			System.out.println("AES256 encrypt error");
		
		// AES256 Decrypt Test
		msg = aes.decrypt("JbwrSuttTa9v+dQdRigFCw==");
		if(!msg.equals("Test Text"))
			System.out.println("AES256 decrypt error");
		
		System.out.println("Test finish");
	}
}
