package raon.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {
	// String Hash generator
	public String generateHash(String msg) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(msg.getBytes());
		
		return IntegrityCheckUtil.bytesToHex(md.digest());
	}
	
	// File Hash generator
	public String generateFileHash(File file) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			FileInputStream fis = new FileInputStream(file);
			int speed = 1024;
			int byteCount = 0;
			byte[] byteArray = new byte[speed];
			
			while((byteCount = fis.read(byteArray)) != -1) {
				md.update(byteArray, 0, byteCount);
			}
			fis.close();
			
			return IntegrityCheckUtil.bytesToHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return null;
	}
}
