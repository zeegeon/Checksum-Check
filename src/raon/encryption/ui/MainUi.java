package raon.encryption.ui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import raon.encryption.AES256Controller;
import raon.encryption.HashGenerator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;

public class MainUI {
	
	// Encryption variable
	static String testkey = "RAONTECH";
	private static Text textBar1;
	private static Text textBar2;
	private static Text tbOutputText;
	private static Text dropText;
	
	public void dragDrop() {
        
    }
	
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        
        // Shell setup
        shell.setText("File Integrity Check");
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
        tabHash.setImage(SWTResourceManager.getImage(MainUI.class, "/javax/swing/plaf/metal/icons/ocean/collapsed.gif"));
        tabHash.setText("Hash Generator");
        
        Composite composite_1 = new Composite(tabFolder, SWT.NONE);
        tabHash.setControl(composite_1);
        
        String hashCheckString = "";
        String genHashString = "12345";
        boolean hashCheck = false;
        
        Label Label1 = new Label(composite_1, SWT.NONE);
        Label1.setAlignment(SWT.CENTER);
        Label1.setBounds(22, 141, 62, 21);
        Label1.setText("Gen Hash");
        
        Label Label2 = new Label(composite_1, SWT.NONE);
        Label2.setAlignment(SWT.CENTER);
        Label2.setBounds(22, 168, 62, 21);
        Label2.setText("Hash Check");
        
        textBar2 = new Text(composite_1, SWT.BORDER);
        textBar2.addVerifyListener(new VerifyListener() {
        	public void verifyText(VerifyEvent arg0) {
        		//hashCheckString = hashCheckString.substring(0) + arg0.character;
        		System.out.println(arg0.character);
        	}
        });
        textBar2.setBounds(101, 165, 339, 18);
        
        ProgressBar progressBar = new ProgressBar(composite_1, SWT.SMOOTH);
        progressBar.setToolTipText("");
        progressBar.setBounds(22, 196, 339, 18);
        
        Label progressLabel = new Label(composite_1, SWT.NONE);
        progressLabel.setBounds(389, 196, 29, 15);
        progressLabel.setText("0 %");
        
        textBar1 = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        textBar1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        textBar1.setBounds(101, 141, 339, 18);
        
        hashCheck = hashCheckString.equals(genHashString);
        if(hashCheck) {
        	textBar2.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
        }
        else {
        	textBar2.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        }
        
        dropText = new Text(composite_1, SWT.MULTI);
        dropText.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        dropText.setEditable(false);
        dropText.setText("Drag and Drop File");
        dropText.setBounds(0, 0, 464, 135);
        
        DropTarget target = new DropTarget(dropText, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
        target.setTransfer(new Transfer[]{FileTransfer.getInstance(),
				TextTransfer.getInstance()});
		target.addDropListener(new DropTargetAdapter() 
		{
			FileTransfer fileTransfer = FileTransfer.getInstance();
			public void dragEnter(DropTargetEvent e) 
			{
				if (e.detail == DND.DROP_DEFAULT) 
					e.detail = DND.DROP_COPY;
			}

			public void dragOperationChanged(DropTargetEvent e) {}

			public void drop(DropTargetEvent e) 
			{
				if (fileTransfer.isSupportedType(e.currentDataType)) 
				{
					String[] files = (String[]) e.data;
					if (files != null && files.length > 0) 
					{
						System.out.println(files[0]);
					}
				}
			}
		});
		
        TabItem tabItem = new TabItem(tabFolder, 0);
        tabItem.setText("AES256 Cryption");
        tabItem.setImage(SWTResourceManager.getImage(MainUI.class, "/com/sun/java/swing/plaf/windows/icons/HardDrive.gif"));
        
        Composite composite_2 = new Composite(tabFolder, SWT.NONE);
        tabItem.setControl(composite_2);
        
        Button Button1 = new Button(composite_2, SWT.NONE);
        Button1.setSelection(true);
        Button1.setBounds(10, 156, 216, 58);
        Button1.setText("Encrypt");
        
        Button btnAESDecrypt = new Button(composite_2, SWT.NONE);
        btnAESDecrypt.setBounds(238, 156, 216, 58);
        btnAESDecrypt.setText("Decrypt");
        
        Label lbOutput = new Label(composite_2, SWT.NONE);
        lbOutput.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL));
        lbOutput.setText("Output (.aes)");
        lbOutput.setBounds(22, 133, 84, 15);
        
        tbOutputText = new Text(composite_2, SWT.BORDER);
        tbOutputText.setBounds(112, 132, 342, 18);
        
        Label Label5 = new Label(composite_2, SWT.NONE);
        Label5.setText("Drag and Drop File");
        Label5.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        Label5.setAlignment(SWT.CENTER);
        Label5.setBounds(0, 64, 464, 26);
        
        DropTarget target2 = new DropTarget(composite_2, DND.DROP_COPY);

        shell.open();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
