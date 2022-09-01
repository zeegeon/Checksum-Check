package raon.encryption;

import java.io.File;

public class Test {

	public static void main(String[] args) throws Exception {
		// SHA text hash test
		SHA hash = new SHA();
		String msg = hash.generateHash("RAONTECH");
		if(!msg.equals("f6a5cd16ea3dfa2587ab5dd3503d283a49ba039270c023d54e65d2a1ae9f4ae4"))
			System.out.println("SHA text hash error");
		
		// SHA file hash test
		File file= new File("resource//TestFile.txt");
		msg = hash.generateFileHash(file);
		if(!msg.equals("f6a5cd16ea3dfa2587ab5dd3503d283a49ba039270c023d54e65d2a1ae9f4ae4"))
			System.out.println("SHA file hash error");
		
		// AES256 Test
		AES aes = new AES();
		msg = aes.encrypt("Test Text");
		if(!msg.equals("JbwrSuttTa9v+dQdRigFCw=="))
			System.out.println("AES256 file hash error");
		
		System.out.println("Test finish");
	}
}
