package raon.encryption;

public class IntegrityCheckUtil {
	
	public String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b: bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
}
