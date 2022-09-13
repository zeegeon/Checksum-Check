package raon.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileReadThread extends Thread {
	private File file;
	private String Hash; 
	private int count=0;
	
	public FileReadThread(File file) {
		this.file = file;
		System.out.println("File read Thread run");
	}
	
	@Override
	public void run() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			FileInputStream fis = new FileInputStream(file);
			
			int speed = 1024;
			
			byte[] byteArray = new byte[speed];
			int byteCount = 0;
			
			int c = (int)file.length() / speed+1;
			
			int ccount = 0;
			
			while((byteCount = fis.read(byteArray)) != -1) {
				md.update(byteArray, 0, byteCount);
				ccount ++;
				this.count = 100 * ccount / c;
				System.out.println("count : "+ count);
			}
			//System.out.println(file.length() + " bytes");
			Hash = IntegrityCheckUtil.bytesToHex(md.digest());
			
			fis.close();
		} catch (NoSuchAlgorithmException | IOException e) {
		}  
	}
	
	public String getResult() {
		System.out.println("File read Thread dead");
		return this.Hash;
	}
	public int getCount() {
		return this.count;
	}
}
 // Æó±â =================================================================================