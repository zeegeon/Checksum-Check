package raon.encryption.ui;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import raon.encryption.AES;
import raon.encryption.RSA;
import raon.encryption.SHA;

public class Main {
	
	// Encryption variable
	static String testkey = "RAONTECH";
	
	static AES aes = new AES();
	static SHA hash = new SHA();
	static RSA rsa = new RSA();
    
    public static void main(String[] args) throws Exception {
        Display display = new Display();
        Shell shell = new Shell(display);
        
        // Shell setup
        shell.setText("File integrity check");
        shell.setImage(new Image(display, "resource//logo.png"));
        shell.setSize(500, 300);
        
        // Get primary monitor size
        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shell.getBounds();
        
        // Shell location set in center
        shell.setLocation((bounds.width - rect.width) / 2, (bounds.height - rect.height) / 2);

        shell.setLayout(new FillLayout());

        
        TabFolder folder = new TabFolder(shell, SWT.NONE);
        folder.setBounds(100,100,100,100);
        
        // Tab 1 
        TabItem tab1 = new TabItem(folder, SWT.NONE);
        tab1.setText("SHA-256");
        SashForm sashForm = new SashForm(folder, SWT.NONE);
       
        Text textBox1 = new Text(sashForm, SWT.SINGLE | SWT.BORDER);
        textBox1.setFont(new Font(display, "Arial", 12, 0));
        
        Text textBox2 = new Text(sashForm, SWT.SINGLE | SWT.BORDER);
        textBox2.setFont(new Font(display, "Arial", 12, 0));
        
        Label label1 = new Label(sashForm, SWT.READ_ONLY);
        label1.setFont(new Font(display, "Arial", 10, 0));
        label1.setText("Gen Hash");
        
        Label label2 = new Label(sashForm, SWT.READ_ONLY);
        label2.setFont(new Font(display, "Arial", 10, 0));
        label2.setText("Hash check");
        
        tab1.setControl(sashForm);
        
        
        // Tab 2
        TabItem tab2 = new TabItem(folder, SWT.NONE);
        tab2.setText("AES-256");

        Group group = new Group(folder, SWT.NONE);
        group.setText("Group in Tab 2");

        Button button = new Button(group, SWT.NONE);
        button.setText("Button in Tab 2");
        button.setBounds(10, 50, 130, 30);

        Text text = new Text(group, SWT.BORDER);
        text.setText("Text in Tab 2");
        text.setBounds(10, 90, 200, 20);
        
        tab2.setControl(group);
              
        
        // Tab 3
        TabItem tab3 = new TabItem(folder, SWT.NONE);
        tab3.setText("RSA");

        Group group2 = new Group(folder, SWT.NONE);
        group2.setText("RSA");

        Button button5 = new Button(group2, SWT.NONE);
        button5.setText("Button in Tab 3");
        button5.setBounds(10, 50, 130, 30);

        Text text5 = new Text(group2, SWT.BORDER);
        text5.setText("Text in Tab 2");
        text5.setBounds(10, 90, 200, 20);
        
        tab3.setControl(group2);
        
//        Canvas canvas = new Canvas(shell, SWT.NONE);
//
//        canvas.addPaintListener(new PaintListener() {
//          public void paintControl(PaintEvent e) {
//
//            e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
//            e.gc.drawText("I'm in blue!",10,10);        
//          }
//        });
        
//        
        // Hashing test
        System.out.println("SHA-256 hash : " + hash.generateHash(testkey));
        
        // AES_256 test
        System.out.println("AES-256 decrypt : " + aes.encrypt(testkey));
        
        // RSA test
        KeyPair keyPair = rsa.genRSAKeyPair();
        PrivateKey key_private = keyPair.getPrivate();
        PublicKey key_public = keyPair.getPublic();
        
        System.out.println("PUB : " + key_public);
        System.out.println("PRI : " + key_private);
        System.out.println("RSA encrypt : " + rsa.encryptRSA(testkey, key_public));

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
