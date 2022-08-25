package encryption;

import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.PaintEvent;
//import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
//import java.security.MessageDigest;

public class Main {
    public static Text textBox1 = null;
    public static Text textBox2 = null;
    public static Text textBox3 = null;
    public static Text textBox4 = null;
    
    public static void main(String[] args) {
    	
        Display display = new Display();
        Shell shell = new Shell(display);
        
        // Shell setup
        shell.setText("Integrity check");
        shell.setImage(new Image(display, "C:\\Users\\Milo\\eclipse-workspace-19.9\\Encryption_proj\\resource\\logo.bmp"));
        shell.setSize(500, 300);
        
        // Get primary monitor size
        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shell.getBounds();
        
        // Shell set in center 
        shell.setLocation((bounds.width - rect.width) / 2, (bounds.height - rect.height) / 2);

        shell.setLayout(new Layout() 
        {

            @Override
            protected void layout(Composite arg0, boolean arg1) 
            {
                arg0.setSize(shell.getSize().x ,shell.getSize().y);
                
                // change Text box size by win size 
                try 
                {
                    if (textBox1 != null & textBox2 !=null & textBox3 !=null & textBox4 !=null) 
                    {
                        textBox1.setBounds(120, arg0.getSize().y-140, arg0.getSize().x - 150, 20);
                        textBox2.setBounds(120, arg0.getSize().y-110, arg0.getSize().x - 150, 20);
                        textBox3.setBounds(10, arg0.getSize().y-140, 100, 20);
                        textBox4.setBounds(10, arg0.getSize().y-110, 100, 20);
                    }
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }

            // return size of recommend size to show all contents of the Control object 
            @Override
            protected Point computeSize(Composite arg0, int arg1, int arg2, boolean arg3) 
            {
                return null;
            }
        });

        // make textbox
        textBox1 = new Text(shell, SWT.SINGLE | SWT.BORDER);
        //textBox1.setLayoutData(new GridData(1808));
        textBox1.setFont(new Font(display, "Arial", 12, 0));
        
        textBox2 = new Text(shell, SWT.SINGLE | SWT.BORDER);
        textBox2.setFont(new Font(display, "Arial", 12, 0));
        
        textBox3 = new Text(shell, SWT.READ_ONLY);
        textBox3.setFont(new Font(display, "Arial", 10, 0));
        textBox3.setText("Gen Hash");
        
        textBox4 = new Text(shell, SWT.READ_ONLY);
        textBox4.setFont(new Font(display, "Arial", 10, 0));
        textBox4.setText("Hash check");
        
//        Canvas canvas = new Canvas(shell, SWT.NONE);
//
//        canvas.addPaintListener(new PaintListener() {
//          public void paintControl(PaintEvent e) {
//
//            e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
//            e.gc.drawText("I'm in blue!",10,10);        
//          }
//        });
        
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
