package raon.encryption.test;

import java.io.FileInputStream;

import raon.encryption.FileEncryptor;
import raon.encryption.FileHashChecker;
import raon.encryption.FileHashChecker.HashCallback;

public class ConsoleTest 
{
	public static void main(String[] args) throws Exception 
	{
		// File Hash checker
		String path = "resource/test1.txt";
		FileHashChecker hash = new  FileHashChecker();
		hash.SetCallback(new HashCallback() 
		{
			@Override
			public void process(int prog, String msg)
			{
				System.out.format("%d%%\n", prog);
			}
		});
		
		Thread hashTh = new Thread() 
		{
			@Override
			public void run() 
			{
				hash.generateFileHash(path);
			}
		};
		hashTh.start();
		hashTh.join();
		
		// FileEncryptor checker
		String path2 = "resource/test1.aes";
		FileEncryptor aes = new FileEncryptor();
		
		FileInputStream fileStream = new FileInputStream(path);
		byte[] readBuffer = new byte[fileStream.available()];
		while (fileStream.read(readBuffer) != -1) {}
		fileStream.close();
		
		aes.encryptFileAES(path);
		aes.decryptFileAES(path2);
		
		FileInputStream fileStream2 = new FileInputStream(path);
		byte[] readBuffer2 = new byte[fileStream2.available()];
		while (fileStream2.read(readBuffer2) != -1) {}
		fileStream2.close();
		
		assert java.util.Arrays.equals(readBuffer, readBuffer2);
		
		System.out.println("All correct");
	}
}
