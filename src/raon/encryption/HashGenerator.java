package raon.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class HashGenerator {
	
	// File Hash generator
	public String generateFileHash(File file) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			FileInputStream fis = new FileInputStream(file);
			int speed = 2048;
			int byteCount = 0;
			byte[] byteArray = new byte[speed];
			
			while((byteCount = fis.read(byteArray)) != -1) {
				md.update(byteArray, 0, byteCount);
			}
			fis.close();
			
			return IntegrityCheckUtil.bytesToHex(md.digest());
		} catch (Exception e) {
			System.out.println("Exception occured");
		}
		return null;
	}
}
