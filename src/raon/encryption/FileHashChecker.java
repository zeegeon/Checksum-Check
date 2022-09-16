package raon.encryption;

import java.io.FileInputStream;
import java.security.MessageDigest;

public class FileHashChecker
{
	public interface HashCallback
	{
		public void process(int prog, String msg);
	}
	
	private HashCallback callback;
	public void SetCallback(HashCallback callback)
	{
		this.callback = callback;
	}
	
	// File Hash generator
	public String generateFileHash(String path)
	{
		try 
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			FileInputStream fis = new FileInputStream(path); // bufferIO / NIO buffer
			int speed = 2048;
			int byteCount = 0;
			int totalSize = fis.available();
			byte[] byteArray = new byte[speed];
			int val =0;
			while((byteCount = fis.read(byteArray)) != -1)
			{
				val += byteCount;
				md.update(byteArray, 0, byteCount);
				if (callback != null) callback.process((int)(val*100.0/totalSize), null);
			}
			fis.close();
			
			return bytesToHex(md.digest());
		} 
		catch (Exception e)
		{
			System.out.println("Hash file open error" + e.getMessage());
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
