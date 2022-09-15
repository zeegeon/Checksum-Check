package raon.encryption;

import java.io.FileInputStream;
import java.security.MessageDigest;

public class FileHashChecker
{
	// File Hash generator
	public String generateFileHash(String path) 
	{
		try 
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			FileInputStream fis = new FileInputStream(path);
			int speed = 2048;
			int byteCount = 0;
			byte[] byteArray = new byte[speed];
			
			while((byteCount = fis.read(byteArray)) != -1)
			{
				md.update(byteArray, 0, byteCount);
			}
			fis.close();
			
			return bytesToHex(md.digest());
		} catch (Exception e)
		{
			System.out.println("File not found");
		}
		return null;
	}
	
	private String bytesToHex(byte[] bytes)
	{
		StringBuilder builder = new StringBuilder();
		for (byte b: bytes)
		{
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
	
}
