package raon.encryption.ui;

import org.eclipse.swt.widgets.*;

public class Main 
{
    public static void main(String[] args) 
    {
		Display display = new Display();
		
		OpenShell shell1 = new OpenShell(display);
    	shell1.open();
    	 
		while (!shell1.isDisposed()) 
		{
		    if (!display.readAndDispatch()) 
		    {
		        display.sleep();
		    }
		}
        
		display.dispose();
    }
    
}
