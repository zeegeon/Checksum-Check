package raon.encryption;

import java.io.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptor
{
    private String alg = "AES/CBC/PKCS5Padding";
    
    private static final byte[] key = new byte[]
    {
		(byte)0xf6, (byte)0xa5, (byte)0xcd, (byte)0x16, (byte)0xea, (byte)0x3d, (byte)0xfa, (byte)0x25,
		(byte)0x87, (byte)0xab, (byte)0x5d, (byte)0xd3, (byte)0x50, (byte)0x3d, (byte)0x28, (byte)0x3a,
		(byte)0x49, (byte)0xba, (byte)0x03, (byte)0x92, (byte)0x70, (byte)0xc0, (byte)0x23, (byte)0xd5,
		(byte)0x4e, (byte)0x65, (byte)0xd2, (byte)0xa1, (byte)0xae, (byte)0x9f, (byte)0x4a, (byte)0xe4
    };

    private static final byte[] iv = new byte[]
    {
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
		(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
    };
    
    private static final int ChunkSize = 1024;
	
    /***
     * Access the location of the input file and run the AES256 encrypt.
     * If can't fine file in the output file location, create the file and write the contents in the file.
     * 
     * @param inputFilePath
     * 		input file path
     * @param outFilePath
     * 		geneneted file path
     * @throws Exception
     */
    public void encryptFileAES(String inputFilePath, String outFilePath) throws Exception
    {
		File file = new File(outFilePath);
		if(!file.exists()) 
		{
			file.createNewFile();
		}
		
		BufferedWriter outputStream = new BufferedWriter(new FileWriter(file));
		outputStream.write(encryptFileAES(inputFilePath));
		outputStream.close();
    }
	
    /***
     * Access the location of the input file and read contents as byte data.
     * Apply AES256 algorithm and convert to Base64 string type
     * 
     * @param inputFilePath
     * 		relative file path 
     * @return
     * 		Encrypted file contents
     * @throws Exception
     */
    public String encryptFileAES(String inputFilePath) throws Exception 
	{
    	Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
        
        // Check File type. If not text file, Program finishes
        if(!checkFileType(inputFilePath, ChunkSize))
        {
        	return "";
        }
        
		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFilePath));
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] readBuffer = new byte[ChunkSize];
        int byteRead;
        
        while ((byteRead = inputStream.read(readBuffer)) != -1)
        {
        	outputStream.write(cipher.update(readBuffer, 0, byteRead));
        }
        inputStream.close();
		
        // Byte Encryption
        outputStream.write(cipher.doFinal());
        
        byte[] encryptedBytes = outputStream.toByteArray();
        outputStream.close();
        
        return Base64.getEncoder().encodeToString(encryptedBytes);
	}
	
	/***
	 * 
	 * @param inputFilePath
	 * @param outputFilePath
	 * @return
	 * @throws Exception
	 */
    public void decryptFileAES(String inputFilePath, String outputFilePath) throws Exception 
	{
		File file = new File(outputFilePath);
		if(!file.exists()) 
		{
			file.createNewFile();
		}
		
		BufferedWriter outputStream = new BufferedWriter(new FileWriter(file));
		outputStream.write(decryptFileAES(inputFilePath));
		outputStream.close();
	}
	
    /***
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public String decryptFileAES(String path) throws Exception 
	{
		Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
        
        // Check File type. If not text file, Program finishes
        if(!checkFileType(path, ChunkSize))
        {
        	System.out.println("FALSE");
        	return "";
        }

		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
		
		byte[] readBuffer = new byte[inputStream.available()];
		while (inputStream.read(readBuffer) != -1) {}
		
		inputStream.close();
            
        // Byte Decryption
        byte[] decodedBytes = Base64.getDecoder().decode(readBuffer);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
	}
    
	private boolean checkFileType(String path, int len) throws IOException 
	{
		FileInputStream fileStream = new FileInputStream(path);
		
		byte[] checkBuffer = new byte[len];
		int readBytes = fileStream.read(checkBuffer); 
	    fileStream.close();
	    
	    int count = 0; // for checking EOF
		for (byte thisByte : checkBuffer) 
		{
			if (thisByte == 0 && count < readBytes-1) 
			{ 
				return false;
			}
			count++;
		}
		return true;
	}
	
}