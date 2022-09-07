package raon.encryption.ui;

import org.eclipse.swt.widgets.*;

public class MainUI {
	
    public static void main(String[] args) {
		Display display = new Display();
		OpenShell mainShell = new OpenShell();
		
    	mainShell.openNewShell(display);
    	
//    	Thread thread = new Thread(new Runnable() {
//    		public void run() {
//    			System.out.println("Thread");
//    			OpenShell mainShell = new OpenShell(display);
//    			System.out.println("Thread dead");
//    		}
//    	});
//    	thread.run();
    	
        display.dispose();
        }
}
