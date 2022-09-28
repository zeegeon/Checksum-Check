package raon.encryption;

import java.io.*;
import java.security.*;
import java.util.Base64;

import javax.crypto.*;
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
    
    private static final int ChunkSize = 10240;
	
    /***
     * Access the location of the input file and run the AES256 encrypt.
     * If can't fine file in the output file location, create the file and write.
     * 
     * @param inputFilePath
     * 		input file path
     * @param outFilePath
     * 		geneneted file path
     * @throws Exception
     */
    public void encryptFileAES(String inputFilePath, String outFilePath) throws Exception
    {
    	BufferedWriter outputStream = null;
		try 
		{
			String fileString = encryptFileAES(inputFilePath);
			
			outputStream = new BufferedWriter(new FileWriter(new File(outFilePath)));
			outputStream.write(fileString);
		}
		catch (IOException e)
		{
			System.out.println("File open error");
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try 
			{
				outputStream.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
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
    	BufferedInputStream inputStream = null;
    	ByteArrayOutputStream outputStream = null;
		try 
		{
			Cipher cipher = Cipher.getInstance(alg);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
			
			inputStream = new BufferedInputStream(new FileInputStream(inputFilePath));
			outputStream = new ByteArrayOutputStream();
			byte[] readBuffer = new byte[ChunkSize];
	        int byteRead;
	        
	        // Check File type. If not text file, Program finishes
	        if(!isTextFile(inputFilePath, ChunkSize)) throw new Exception();
	        
	        while ((byteRead = inputStream.read(readBuffer)) != -1)
	        {
	        	outputStream.write(cipher.update(readBuffer, 0, byteRead));
	        }
	        inputStream.close();
	        
	        // Byte Encryption
	        outputStream.write(cipher.doFinal());
	        
	        byte[] encryptedBytes = outputStream.toByteArray();
	        outputStream.close();
	        
	        //return new String(encryptedBytes, "UTF-8");
	        return Base64.getEncoder().encodeToString(encryptedBytes);
		} 
		catch (NoSuchAlgorithmException | NoSuchPaddingException e) 
		{
			System.out.println("Cipher library run error : " + e.getMessage());
		}
        catch (InvalidKeyException | InvalidAlgorithmParameterException e) 
        {
			System.out.println("Key parameter Error : "+ e.getMessage());
		}
		catch (FileNotFoundException e) 
		{
			System.out.println("Wrong File Path");
			//throw e;
		} 
		catch (IOException e) 
		{
			System.out.println("Stream error");
		} 
		catch (IllegalBlockSizeException | BadPaddingException e) 
		{
			System.out.println("Cipher final padding error");
		}
		catch (Exception e)
		{
			System.out.println("File type is binary");
			throw new Exception();
		}
		finally
		{
			try 
			{
				inputStream.close();
				outputStream.close();
			} 
			catch (IOException e) 
			{
				System.out.println("File close error");
			}
		}
        
        return null;
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
    	BufferedWriter outputStream = null;
		try 
		{
			String fileString = decryptFileAES(inputFilePath);
			
			outputStream = new BufferedWriter(new FileWriter(new File(outputFilePath)));
			outputStream.write(fileString);
		} 
		catch (IOException e) 
		{
			System.out.println("File open error : decryptFileAES");
		}
		catch (Exception e)
		{
			throw new Exception();
		}
		finally
		{
			try 
			{
				outputStream.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
    /***
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public String decryptFileAES(String path) throws Exception
	{
    	try
    	{
    		Cipher cipher = Cipher.getInstance(alg);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            
            // Check File type. If not text file, Program finishes
	        if(!isTextFile(path, ChunkSize)) throw new Exception();
	        
    		BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
    		
    		byte[] readBuffer = new byte[inputStream.available()];
    		while (inputStream.read(readBuffer) != -1) {}
    		
    		inputStream.close();
                
            // Byte Decryption
            byte[] decodedBytes = Base64.getDecoder().decode(readBuffer);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, "UTF-8");
    	}
    	catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) 
		{
			System.out.println("Cipher library run error : " + e.getMessage());
		}
    	catch (FileNotFoundException e) 
    	{
			e.printStackTrace();
		}
    	catch (IOException e) 
		{
			System.out.println("Stream error");
		} 
		catch (IllegalBlockSizeException | BadPaddingException e) 
		{
			System.out.println("Cipher final padding error");
		}
		return null;
		
	}
    
	private boolean isTextFile(String path, int len)
	{
		FileInputStream fileStream = null;
		try {
			fileStream = new FileInputStream(path);
			byte[] checkBuffer = new byte[len];
			int readBytes = fileStream.read(checkBuffer); 
		    
		    int count = 0; // for checking EOF
			for (byte thisByte : checkBuffer) 
			{
				if (thisByte == 0 && count < readBytes-1) return false;
				count++;
			}
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				fileStream.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	
}