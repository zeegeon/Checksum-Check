package raon.encryption.ui;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ProgressBar;

public class MainUi {
	
	// Encryption variable
	static String testkey = "RAONTECH";
	
	static AES aes = new AES();
	static SHA hash = new SHA();
	static RSA rsa = new RSA();
	private static Text textBar1;
	private static Text textBar2;
	private static Text textBar3;
    
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
        
        Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayout(new FormLayout());
        
        TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
        FormData fd_tabFolder = new FormData();
        fd_tabFolder.bottom = new FormAttachment(0, 258);
        fd_tabFolder.right = new FormAttachment(0, 478);
        fd_tabFolder.top = new FormAttachment(0, 10);
        fd_tabFolder.left = new FormAttachment(0, 10);
        tabFolder.setLayoutData(fd_tabFolder);
        
        TabItem tabHash = new TabItem(tabFolder, SWT.NONE);
        tabHash.setImage(SWTResourceManager.getImage(MainUi.class, "/javax/swing/plaf/metal/icons/ocean/collapsed.gif"));
        tabHash.setText("Hash Generator");
        
        Composite composite_1 = new Composite(tabFolder, SWT.NONE);
        tabHash.setControl(composite_1);
        
        Label Label1 = new Label(composite_1, SWT.NONE);
        Label1.setBounds(10, 144, 68, 15);
        Label1.setText("Gen Hash");
        
        Label Label2 = new Label(composite_1, SWT.NONE);
        Label2.setBounds(10, 165, 68, 15);
        Label2.setText("Hash Check");
        
        textBar1 = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        textBar1.setEnabled(false);
        textBar1.setBounds(101, 141, 339, 18);
        
        textBar2 = new Text(composite_1, SWT.BORDER | SWT.WRAP);
        textBar2.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        textBar2.setBounds(101, 165, 339, 18);
        
        DragSource dragSource = new DragSource(composite_1, DND.DROP_MOVE);
        
        DropTarget dropTarget = new DropTarget(composite_1, DND.DROP_MOVE);
        
        Label Label3 = new Label(composite_1, SWT.NONE);
        Label3.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        Label3.setAlignment(SWT.CENTER);
        Label3.setBounds(0, 53, 464, 25);
        Label3.setText("Drag and Drop File");
        
        ProgressBar progressBar = new ProgressBar(composite_1, SWT.NONE);
        progressBar.setBounds(10, 196, 339, 18);
        
        Label progressLabel = new Label(composite_1, SWT.NONE);
        progressLabel.setBounds(365, 199, 50, 15);
        progressLabel.setText("0 %");
        
        TabItem tabItem = new TabItem(tabFolder, 0);
        tabItem.setText("AES256 Cryption");
        tabItem.setImage(SWTResourceManager.getImage(MainUi.class, "/com/sun/java/swing/plaf/windows/icons/HardDrive.gif"));
        
        Composite composite_2 = new Composite(tabFolder, SWT.NONE);
        tabItem.setControl(composite_2);
        
        Button Button2 = new Button(composite_2, SWT.NONE);
        Button2.setBounds(10, 182, 444, 32);
        Button2.setText("Decrypt");
        
        Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
        btnNewButton_1.setSelection(true);
        btnNewButton_1.setBounds(10, 144, 444, 32);
        btnNewButton_1.setText("Encrypt");
        
        Label Label4 = new Label(composite_2, SWT.NONE);
        Label4.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL));
        Label4.setText("Output (.aes)");
        Label4.setBounds(10, 112, 84, 15);
        
        textBar3 = new Text(composite_2, SWT.BORDER);
        textBar3.setBounds(112, 112, 342, 18);
        
        Label Label5 = new Label(composite_2, SWT.NONE);
        Label5.setText("Drag and Drop File");
        Label5.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        Label5.setAlignment(SWT.CENTER);
        Label5.setBounds(0, 40, 464, 32);

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
        //System.out.println("AES-256 decrypt : " + aes.encrypt(testkey));
        
        shell.open();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
