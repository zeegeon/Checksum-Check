package raon.encryption.ui;

import org.eclipse.swt.widgets.*;

public class Main
{
    public static void main(String[] args) 
    {
		Display display = new Display();
		
		ShellMaker shell = new ShellMaker(display);
    	shell.open();
    	
		while (!shell.isDisposed())
		{
		    if (!display.readAndDispatch()) 
		    {
		        display.sleep();
		    }
		}
        
		display.dispose();
    }
    
}
