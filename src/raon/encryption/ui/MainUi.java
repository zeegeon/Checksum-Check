package raon.encryption.ui;


import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

import raon.encryption.AES256Controller;
import raon.encryption.HashGenerator;
import raon.encryption.IntegrityCheckUtil;

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class MainUI {
	
	private static Text textBar1;
	private static Text textBar2;
	private static Text tbOutputText;
	
	private static int progressPercent=0;
	private static String hashSHA;
	private static String pathAES;
	private static String AESString;
	
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
        
        // =============================================== Tab Folder 1 =====================================
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
        
        Label Label1 = new Label(composite_1, SWT.NONE);
        Label1.setAlignment(SWT.CENTER);
        Label1.setBounds(0, 141, 62, 21);
        Label1.setText("Gen Hash");
        
        Label Label2 = new Label(composite_1, SWT.NONE);
        Label2.setAlignment(SWT.CENTER);
        Label2.setBounds(0, 165, 62, 21);
        Label2.setText("Hash Check");

        textBar1 = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        textBar1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        textBar1.setBounds(68, 141, 386, 18);
        
        textBar1.addModifyListener(new ModifyListener() {
        	public void modifyText(ModifyEvent arg0) {
                if(textBar2.getText().equals(hashSHA)) {
                	textBar2.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE)); //32 자리 넘을 때 막는 코드
                }
                else {
                	textBar2.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
                }
        	}
        });
        
        textBar2 = new Text(composite_1, SWT.BORDER | SWT.WRAP);
        textBar2.setBounds(68, 165, 386, 18);
        
        textBar2.addModifyListener(new ModifyListener() {
        	public void modifyText(ModifyEvent arg0) {
        		System.out.println(textBar2.getText());
        		
                if(textBar2.getText().equals(hashSHA)) {
                	textBar2.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
                }
                else {
                	textBar2.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
                }
        	}
        });
        
        ProgressBar progressBar = new ProgressBar(composite_1, SWT.NONE);
        progressBar.setBounds(69, 196, 339, 18);
        
        Label progressLabel = new Label(composite_1, SWT.NONE);
        progressLabel.setBounds(24, 196, 38, 18);
        progressLabel.setText(progressPercent + " %");
        progressBar.setMinimum(99);
		
        Label lbDrag = new Label(composite_1, SWT.NONE);
        lbDrag.setToolTipText("Drag the file here and drop");
        lbDrag.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        lbDrag.setAlignment(SWT.CENTER);
        lbDrag.setBounds(0, 0, 464, 135);
        lbDrag.setText("\r\n\r\n\r\nDrag and Drop File");
        
        // Drag n Drop
        DropTarget target = new DropTarget(lbDrag, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
        target.setTransfer(new Transfer[]{FileTransfer.getInstance(),
				TextTransfer.getInstance()});
        target.addDropListener(new DropTargetAdapter() {
        	FileTransfer fileTransfer = FileTransfer.getInstance();
        	public void dragEnter(DropTargetEvent e) {
        		if (e.detail == DND.DROP_DEFAULT) 
        			e.detail = DND.DROP_COPY;
        	}

        	public void dragOperationChanged(DropTargetEvent e) {}

        	public void drop(DropTargetEvent e) {
        		if (fileTransfer.isSupportedType(e.currentDataType)) {
        			String[] files = (String[]) e.data;
        			progressPercent = 0;
					progressLabel.setText(progressPercent + " %");
					progressBar.setMaximum(100*files.length);
					progressBar.setSelection(100*files.length);
        			if (files != null && files.length > 0) {
        				File file = new File(files[0]);
        				HashGenerator hg = new HashGenerator();
						
        				try {
        					hashSHA = hg.generateFileHash(file);
							textBar1.setText(hashSHA);
							
							progressLabel.setText(100 + " %");
						} catch (NoSuchAlgorithmException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
        			}
        		}
        	}
        });
		
        // ======================================= Tab2 ========================================
        TabItem tbtmAesEncryption = new TabItem(tabFolder, 0);
        tbtmAesEncryption.setText("AES256 Encryption");
        tbtmAesEncryption.setImage(SWTResourceManager.getImage(MainUI.class, "/com/sun/java/swing/plaf/windows/icons/HardDrive.gif"));
        
        Composite composite_2 = new Composite(tabFolder, SWT.NONE);
        tbtmAesEncryption.setControl(composite_2);
        
        Button btnEncrypt = new Button(composite_2, SWT.PUSH);
        btnEncrypt.setEnabled(false);
        btnEncrypt.setSelection(true);
        btnEncrypt.setBounds(10, 156, 216, 58);
        btnEncrypt.setText("Encrypt");
        
        Button btnAESDecrypt = new Button(composite_2, SWT.NONE);
        btnAESDecrypt.setEnabled(false);
        btnAESDecrypt.setBounds(238, 156, 216, 58);
        btnAESDecrypt.setText("Decrypt");
        
        Label lbOutput = new Label(composite_2, SWT.NONE);
        lbOutput.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL));
        lbOutput.setText("Output (.aes)");
        lbOutput.setBounds(22, 133, 84, 15);
        
        tbOutputText = new Text(composite_2, SWT.BORDER);
        tbOutputText.setBounds(112, 132, 342, 18);
        
        Label Label5 = new Label(composite_2, SWT.NONE);
        Label5.setText("\r\n\r\n\r\nDrag and Drop File");
        Label5.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        Label5.setAlignment(SWT.CENTER);
        Label5.setBounds(0, 0, 464, 130);
        
        // Drag n Drop
        DropTarget target2 = new DropTarget(Label5, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
        target2.setTransfer(new Transfer[]{FileTransfer.getInstance(),
				TextTransfer.getInstance()});
        target2.addDropListener(new DropTargetAdapter() {
        	FileTransfer fileTransfer = FileTransfer.getInstance();
        	public void dragEnter(DropTargetEvent e) {
        		if (e.detail == DND.DROP_DEFAULT) 
        			e.detail = DND.DROP_COPY;
        	}

        	public void dragOperationChanged(DropTargetEvent e) {}

        	public void drop(DropTargetEvent e) {
        		if (fileTransfer.isSupportedType(e.currentDataType)) {
        			String[] files = (String[]) e.data;
        			
        			if (files != null && files.length > 0) {
        				//File file = new File(files[0]);
        				pathAES = files[0];
    					
    					String buffer;
    					buffer = pathAES.substring(pathAES.length()-4, pathAES.length());
    					
    					if(buffer.equals(".aes"))
    					{
    						btnEncrypt.setEnabled(false);
    						btnAESDecrypt.setEnabled(true);
    						buffer = ".txt";
    						//pathAES = pathAES.substring(0, pathAES.length()-4).concat(buffer);
    						tbOutputText.setText(pathAES.substring(0, pathAES.length()-4).concat(buffer));
    					}
    					else
    					{
    						btnEncrypt.setEnabled(true);
    						btnAESDecrypt.setEnabled(false);
    						buffer = ".aes";
    						//pathAES = pathAES.substring(0, pathAES.length()-4).concat(buffer);
    						tbOutputText.setText(pathAES.substring(0, pathAES.length()-4).concat(buffer));
    					}
        			}
        		}
        	}
        });
        
        btnEncrypt.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseUp(MouseEvent e) {
        		System.out.println("btn1 down");
        		AES256Controller ac = new AES256Controller();
        		//pathAES = tbOutputText.getText();
        		
        		System.out.println("AES file path : " + pathAES);
        		
        		try {
					ac.encryptFile(pathAES, tbOutputText.getText());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
        	}
        });
        
        btnAESDecrypt.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseUp(MouseEvent e) {
        		System.out.println("btn2 down");
        		AES256Controller ac = new AES256Controller();
        		
        		try {
					ac.decryptFile(pathAES, tbOutputText.getText());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        });
        
        shell.open();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
