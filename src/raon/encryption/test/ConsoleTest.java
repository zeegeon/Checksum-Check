package raon.encryption.test;

import java.io.FileInputStream;

import raon.encryption.FileEncryptor;
import raon.encryption.FileHashChecker;

public class ConsoleTest 
{
	public void doHashChecksumTest(String binaryFilePath)
	{
		final FileHashChecker hash = new FileHashChecker();
		
		String checkSum = hash.generateFileHashString(binaryFilePath);
		assert checkSum.equals("f761cfe3496c611d6bff6cf23c72cf943adb7e9adca61fca838f52f2e3c82e7e");
	}
	
	public void doFileEncoderTest(String textFilePath) throws Exception
	{
		final String tempFilePath = "res/temp.aes";
		final String resultFilePath = "res/result.txt";
		final FileEncryptor aes = new FileEncryptor();

		aes.encryptFileAES(textFilePath, tempFilePath); 
		aes.decryptFileAES(tempFilePath, resultFilePath);
	
		FileInputStream fileStream = new FileInputStream(textFilePath);
		byte[] leftBuffer = new byte[fileStream.available()];
		while (fileStream.read(leftBuffer) != -1);
		fileStream.close();
		
		fileStream = new FileInputStream(resultFilePath);
		byte[] rightBuffer = new byte[fileStream.available()];
		while (fileStream.read(rightBuffer) != -1);
		fileStream.close();
		
		assert java.util.Arrays.equals(leftBuffer, rightBuffer);
	}
	
	public static void main(String[] args) 
	{
		ConsoleTest tester = new ConsoleTest();
		try 
		{
			tester.doHashChecksumTest("res/sample.bin");
			tester.doFileEncoderTest("res/sample.txt");
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
		System.out.println("Test complete");
	}
}
