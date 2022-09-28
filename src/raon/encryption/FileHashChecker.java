package raon.encryption;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	 * Create a hash value from the input file path
	 * 
	 * @param inputFilePath
	 * 		Path of the file.
	 * @return 
	 * 		Save the contents of the file by reading it in byte type
	 */
	public byte[] generateFileHash(String inputFilePath)
	{
		MessageDigest md = null;
		BufferedInputStream inputStream = null;
		try
		{
			md = MessageDigest.getInstance("SHA-256");
			inputStream = new BufferedInputStream(new FileInputStream(inputFilePath));
			
			int readBytes = 0;
			long totalReadBytes = 0;
			long totalSize = Files.size(Paths.get(inputFilePath));

			byte[] byteArray = new byte[ChunkSize];
			while((readBytes = inputStream.read(byteArray)) != -1)
			{
				totalReadBytes += readBytes;
				md.update(byteArray, 0, readBytes);
				if (callback != null) callback.process((int)(totalReadBytes*100.0/totalSize));
			}
			return md.digest();
		}
		catch (NoSuchAlgorithmException e) 
		{
			System.out.println(e.getMessage());
		} 
		catch (FileNotFoundException e)
		{
			System.out.println("Null Pointer Exception occured");
		} 
		catch (IOException e) 
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			try 
			{
				inputStream.close();
			}
			catch (NullPointerException e)
			{
				System.out.println("inputStream is null");
			}
			catch (IOException e) 
			{
				System.out.println(e.getMessage());
			}
		}
		return null;
	}
	
	/***
	 * A method for generating a hash value of a file as a string type.
	 * @param inputFilePath
	 * 		Path of the file.
	 * @return 
	 * 		Returns the generated hash value as a string type
	 */
	public String generateFileHashString(String inputFilePath)
	{
		return  bytesToHex(generateFileHash(inputFilePath));
	}
	/***
	 * Convert the input byte type into a string
	 * @param inputBytes
	 * 		bytes input
	 * @return 
	 * 		Byte type convert to a string.
	 * 		If Null Pointer Exception occurs, return null
	 */
	private String bytesToHex(byte[] inputBytes)
	{
		try 
		{
			StringBuilder bytesToStingBuilder = new StringBuilder();
			for (byte b: inputBytes)
			{
				bytesToStingBuilder.append(String.format("%02x", b));
			}
			return bytesToStingBuilder.toString();
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}
}
