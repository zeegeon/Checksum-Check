package raon.encryption;

import java.io.File;

public class ConsoleTest {

	public static void main(String[] args) throws Exception {
		HashGenerator hash = new HashGenerator();

		// SHA file hash test
		File file = new File("resource/TestFileSHA.txt");
		String msg = hash.generateFileHash(file);
		assert msg.equals("22dca5472c7e6de258c511726ba115736711e29e1726382719607c59a08a2661");
		
		// AES256 Test
		Aes256Codec aes = new Aes256Codec();
		aes.encryptFile("resource/TestFileAES.txt");
		aes.decryptFile("resource/TestFileAES.aes");
	}
}
