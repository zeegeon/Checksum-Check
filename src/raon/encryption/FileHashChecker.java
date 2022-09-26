package raon.encryption;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHashChecker
{
	private static final int ChunkSize = 20480;
	
	public interface HashCallback
	{
		public void process(int prog);
	}
	
	private HashCallback callback;
	
	public void SetCallback(HashCallback callback)
	{
		this.callback = callback;
	}
	
	/***
	 * Create a hash value from the file
	 * 
	 * @param inputFilePath
	 * 		Enter the location of the file to generate the Hash value.
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public byte[] generateFileHash(String inputFilePath) throws IOException, NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFilePath));
		
		int readBytes = 0;
		long totalSize = Files.size(Paths.get(inputFilePath));
		byte[] byteArray = new byte[ChunkSize];
		long totalReadBytes = 0;
		while((readBytes = inputStream.read(byteArray)) != -1)
		{
			totalReadBytes += readBytes;
			md.update(byteArray, 0, readBytes);
			if (callback != null) callback.process((int)(totalReadBytes*100.0/totalSize));
		}
		inputStream.close();
		
		return md.digest();
	}
	
	/***
	 * A method for generating a hash value of a file as a string type.
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public String generateFileHashString(String inputFilePath) throws IOException, NoSuchAlgorithmException
	{
		return  bytesToHex(generateFileHash(inputFilePath));
	}
	/***
	 * Method that converts the input byte type into a string
	 * @param inputBytes
	 * @return
	 */
	private String bytesToHex(byte[] inputBytes)
	{
		StringBuilder bytesToStingBuilder = new StringBuilder();
		for (byte b: inputBytes)
		{
			bytesToStingBuilder.append(String.format("%02x", b));
		}
		return bytesToStingBuilder.toString();
	}
}
