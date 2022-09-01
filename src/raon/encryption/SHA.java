package raon.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {

	public String generateHash(String msg) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(msg.getBytes());
		
		IntegrityCheckUtil util = new IntegrityCheckUtil();
		
		return util.bytesToHex(md.digest());
	}

	public String generateFileHash(File file) throws IOException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		FileInputStream fis = new FileInputStream(file);
		
		byte[] byteArray = new byte[1024];
		int byteCount = 0;
		
		while((byteCount = fis.read(byteArray)) != -1) {
			md.update(byteArray, 0, byteCount);
		};
		
		fis.close();
		
		byte[] bytes = md.digest();
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< bytes.length ;i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return sb.toString();
	}
}
