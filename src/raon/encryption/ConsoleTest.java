package raon.encryption;

import java.io.FileInputStream;

public class ConsoleTest 
{
	public static void main(String[] args) throws Exception 
	{
		String path = "resource/test1.txt";
		String path2 = "resource/test1.aes";
		
		// File Hash checker
		FileHashChecker hash = new  FileHashChecker();
		String hashValue = "f6a5cd16ea3dfa2587ab5dd3503d283a49ba039270c023d54e65d2a1ae9f4ae4";
		assert hash.generateFileHash(path).equals(hashValue);
		
		// FileEncryptor checker
		FileEncryptor aes = new FileEncryptor();
		
		FileInputStream fileStream = new FileInputStream(path);
		byte[] readBuffer = new byte[fileStream.available()];
		while (fileStream.read(readBuffer) != -1) {}
		fileStream.close();
		
		assert aes.encryptFileAES(path);
		assert aes.decryptFile(path2);
		
		FileInputStream fileStream2 = new FileInputStream(path);
		byte[] readBuffer2 = new byte[fileStream2.available()];
		while (fileStream2.read(readBuffer2) != -1) {}
		fileStream2.close();
		
		assert java.util.Arrays.equals(readBuffer, readBuffer2);
		
		System.out.println("All correct");
	}

}
