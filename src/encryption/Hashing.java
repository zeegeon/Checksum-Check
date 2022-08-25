package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

	public String sha256(String msg) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(msg.getBytes());
		
		return bytesToHex(md.digest());
	}
	
	public static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b: bytes) 
		{
			builder.append(String.format("%02x", b));
		}
		
		return builder.toString();
	}
}
