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
     * Read the file and encrypt the contents to new file
     * @param inputFilePath
     * 		Input file path
     * @param outFilePath
     * 		Output file path
     * @throws Exception
     * 		Binary file type
     */
    public void encryptFileAES(String inputFilePath, String outFilePath) throws RuntimeException, IOException
    {
    	BufferedWriter outputStream = null;
		try 
		{
			String fileString = encryptFileAES(inputFilePath);
			
			outputStream = new BufferedWriter(new FileWriter(new File(outFilePath)));
			outputStream.write(fileString);
		}
		catch (RuntimeException e)
		{	
			throw e;
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
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
				System.out.println(e.getMessage());
			}
		}
		
    }
	
    /***
     * Read the file and encrypt the contents to string type
     * @param inputFilePath
     * 		Input file path
     * @return
     * 		Encrypted file contents
     * @throws Exception
     * 		Binary file type
     */
    public String encryptFileAES(String inputFilePath) throws RuntimeException, IOException
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
	        
	        // Check File type. If a binary file type, throws exception
	        if(!isTextFile(inputFilePath, ChunkSize)) throw new RuntimeException();
	        
	        while ((byteRead = inputStream.read(readBuffer)) != -1)
	        {
	        	outputStream.write(cipher.update(readBuffer, 0, byteRead));
	        }
	        
	        // Byte Encryption
	        outputStream.write(cipher.doFinal());
	        byte[] encryptedBytes = outputStream.toByteArray();
	        
	        return Base64.getEncoder().encodeToString(encryptedBytes);
		} 
		catch (GeneralSecurityException e)
		{
			System.out.println("Cipher error " + e.getMessage());
		}
		catch (RuntimeException e)
		{
			System.out.println("Not a plane text file type");
			throw e;
		}
        catch (IOException e)
		{
			System.out.println("File I/O error");
			throw e;
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
				System.out.println("File closer error");
			}
		}
        
        return null;
	}
	
    /***
     * Read the file and decrypt the contents to new file
     * @param inputFilePath
     * 		Input file path
     * @param outFilePath
     * 		Output file path
     * @throws Exception
     * 		Binary file type
     */
    public void decryptFileAES(String inputFilePath, String outputFilePath) throws RuntimeException, IOException
	{
    	BufferedWriter outputStream = null;
		try 
		{
			String fileString = decryptFileAES(inputFilePath);
			
			outputStream = new BufferedWriter(new FileWriter(new File(outputFilePath)));
			outputStream.write(fileString);
		}
		catch (RuntimeException e)
		{	
			throw e;
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
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
				System.out.println(e.getMessage());
			}
		}
	}
	
    /***
     * Read the file and decrypt the contents to string type
     * @param inputFilePath
     * 		Input file path
     * @return
     * 		Encrypted file contents
     * @throws Exception
     * 		Binary file type
     */
    public String decryptFileAES(String path) throws RuntimeException, IOException
	{
    	BufferedInputStream inputStream = null;
    	try
    	{
    		Cipher cipher = Cipher.getInstance(alg);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            
            // Check File type. If a binary file type, throws exception
	        if(!isTextFile(path, ChunkSize)) throw new RuntimeException();
	        
    		inputStream = new BufferedInputStream(new FileInputStream(path));
    		
    		byte[] readBuffer = new byte[inputStream.available()];
    		while (inputStream.read(readBuffer) != -1) {}
    		
            // Byte Decryption
            byte[] decodedBytes = Base64.getDecoder().decode(readBuffer);
            byte[] decrypted = cipher.doFinal(decodedBytes); 
            
            return new String(decrypted, "UTF-8");
    	}
    	catch (GeneralSecurityException e)
		{
    		System.out.println("Cipher error " + e.getMessage());
		}
		catch (RuntimeException e)
		{
			System.out.println("Not a plane text file type");
			throw e;
		}
        catch (IOException e)
		{
			System.out.println("File I/O error");
			throw e;
		}
    	finally
		{
			try 
			{
				inputStream.close();
			} 
			catch (IOException e) 
			{
				System.out.println("File closer error");
			}
		}
    	
		return null;
	}
    /***
     * Checking whether the file type is a text file or a binary file
     * @param path
     * 		input file path
     * @param len
     * 		Length of the part to be inspected
     * @return 
     * 		True : Text file / False : Binary file
     */
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