package raon.encryption.ui;

import raon.encryption.Aes256Codec;
import raon.encryption.HashGenerator;
import raon.encryption.IntegrityCheckUtil;

public class ProgressThread extends Thread {
	private int gauge;
	private String hash;
	
	public ProgressThread(int val) {
		System.out.println("");
		this.gauge = val;
	}
	
	@Override
	public void run() {
		int val2 = gauge / 100;
		System.out.println(val2);
		//pbHashBar.setSelection(100);
	}
}
