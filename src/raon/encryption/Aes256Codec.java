package raon.encryption;

import java.io.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Aes256Codec {
	
    private String alg = "AES/CBC/PKCS5Padding";
    
    private static final byte[] key = new byte[] {
		(byte)0xf6, (byte)0xa5, (byte)0xcd, (byte)0x16, (byte)0xea, (byte)0x3d, (byte)0xfa, (byte)0x25,
		(byte)0x87, (byte)0xab, (byte)0x5d, (byte)0xd3, (byte)0x50, (byte)0x3d, (byte)0x28, (byte)0x3a,
		(byte)0x49, (byte)0xba, (byte)0x03, (byte)0x92, (byte)0x70, (byte)0xc0, (byte)0x23, (byte)0xd5,
		(byte)0x4e, (byte)0x65, (byte)0xd2, (byte)0xa1, (byte)0xae, (byte)0x9f, (byte)0x4a, (byte)0xe4
    };

    private static final byte[] iv = new byte[] {
    		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
    		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
        };
    
	// File Encrypt
	public boolean encryptFile(String path) throws Exception {
		Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
        
        // check Binary file - if not, program finishes
        int checkSize = 1024;
        BinaryCheck Bcheck =  new BinaryCheck();
		if(Bcheck.checkBinary(path, checkSize)) {
			return false;
		}
		
		// Read file
		FileInputStream fileStream = new FileInputStream(path);
        byte[] readBuffer = new byte[1048];
        int byteRead;
        byte[] textBytes = null;
        
        while ((byteRead = fileStream.read(readBuffer)) != -1) {
        	textBytes = cipher.update(readBuffer, 0, byteRead);
        }
        fileStream.close();
		
        // Byte Encryption
        byte[] encryptedBytes = cipher.doFinal(textBytes);
        String writeString = Base64.getEncoder().encodeToString(encryptedBytes);
        
        // File Write
        String changeFileName  = path.substring(0, path.lastIndexOf(".")).concat(".aes");
		try {
			File file = new File(changeFileName);
			
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			PrintWriter writer = new PrintWriter(fw);
			writer.write(writeString);
			writer.close();
		} catch (IOException e) {}
		return true;
	}
	
	// File Decrypt
	public boolean decryptFile(String path) throws Exception {
		Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
        
        // check Binary file - if not, program finishes
        int checkSize = 1024;
        BinaryCheck Bcheck =  new BinaryCheck();
		if(Bcheck.checkBinary(path, checkSize)) {
			System.out.println("Bcheck.checkBinary");
			return false;
		}

        FileInputStream fileStream = new FileInputStream(path);
        byte[] readBuffer = new byte[fileStream.available()];
        while (fileStream.read(readBuffer) != -1){}
        fileStream.close();
		
        
//		FileInputStream fileStream = new FileInputStream(path);
//		byte[] readBuffer = new byte[1024];
//		int byteRead;
//		byte[] textBytes = null;
//      
//		int pos = 0;
//		while ((byteRead = fileStream.read(readBuffer)) != -1) {
//			System.out.println("byteRead : "+ byteRead);
//			System.out.println("readBuffer : " + IntegrityCheckUtil.bytesToHex(readBuffer));
//			
//        	System.out.println("textBytes" + textBytes);
//        	pos += byteRead;
//		}
//		System.out.println("textBytes : "+ IntegrityCheckUtil.bytesToHex(textBytes));
//		fileStream.close();
        
        
        // Byte Decryption
        byte[] decodedBytes = Base64.getDecoder().decode(readBuffer);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        String writeString = new String(decrypted, "UTF-8");
        
        // File Write
        String changeFileName  = path.substring(0, path.lastIndexOf(".")).concat(".txt");
		try {
			File file = new File(changeFileName);
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			PrintWriter writer = new PrintWriter(fw);
			writer.write(writeString);
			writer.close();
		} catch (IOException e) {}
		return true;
	}
}