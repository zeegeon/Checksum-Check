package raon.encryption;

import java.io.File;

public class ConsoleTest {

	public static void main(String[] args) throws Exception {
		// SHA text hash test
		HashGenerator hash = new HashGenerator();
		String msg = hash.generateHash("Test Text");
		if(!msg.equals("22dca5472c7e6de258c511726ba115736711e29e1726382719607c59a08a2661"))
			System.out.println("SHA text hash error");
		
		// SHA file hash test
		File file = new File("resource//TestFileSHA.txt");
		msg = hash.generateFileHash(file);
		if(!msg.equals("22dca5472c7e6de258c511726ba115736711e29e1726382719607c59a08a2661"))
			System.out.println("SHA file hash error");
		
		// AES256 Encrypt Text Test
		Aes256Codec aes = new Aes256Codec();
		msg = aes.encryptString("Test Text");
		if(!msg.equals("JbwrSuttTa9v+dQdRigFCw=="))
			System.out.println("AES256 encrypt error");
		
		// AES256 Decrypt Text Test
		msg = aes.decryptString("JbwrSuttTa9v+dQdRigFCw==");
		if(!msg.equals("Test Text"))
			System.out.println("AES256 decrypt error");
		
		System.out.println("Test finish");
	}
}
