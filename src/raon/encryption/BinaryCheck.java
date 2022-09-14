package raon.encryption;

import java.io.FileInputStream;

public class BinaryCheck {
	public boolean checkBinary(String path, int len) {
		try {
			FileInputStream fileStream = new FileInputStream(path);
			
			byte[] checkBuffer = new byte[len];
			int readBytes = fileStream.read(checkBuffer); // ���� ���� ����Ʈ �� ����
		    fileStream.close();
		    
		    int count = 0; // for checking EOF
			for (byte thisByte : checkBuffer) {
				if (thisByte == 0 && count < readBytes-1) { // NULL �� ��! NULL = ascii 0
					return true;
				}
				count++;
			}
		} catch (Exception e) {
			System.out.println("BinaryCheck-File not found");
			return true;
		}
		return false;
	}
}