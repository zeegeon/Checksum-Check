package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
	public static void main(String[] args) throws Exception {
		System.out.println(sha256("RAONTECH"));
	}
	
	public static String sha256(String msg) throws NoSuchAlgorithmException 
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(msg.getBytes());
		
		return bytesToHex(md.digest());
		}
	
	public static String bytesToHex(byte[] bytes) 
	{
		StringBuilder builder = new StringBuilder();
		for (byte b: bytes) 
		{
			builder.append(String.format("%02x", b));
		}
		
		return builder.toString();
	}
}
