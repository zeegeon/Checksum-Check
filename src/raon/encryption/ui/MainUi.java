package raon.encryption.ui;


import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

import raon.encryption.Aes256Codec;
import raon.encryption.HashGenerator;

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;

public class MainUI {
	private static String shaHash;
	private static String pathAES;
	
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.addDragDetectListener(new DragDetectListener() {
        	public void dragDetected(DragDetectEvent arg0) {
        		System.out.println("drag");
        	}
        });
                
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
        
        FormData test = new FormData();
        test.bottom = new FormAttachment(100, 0);
        test.right = new FormAttachment(100, -10);
        int progressPercent = 0;
        
        Composite compositeMain = new Composite(shell, SWT.NONE);
        compositeMain.setLayout(new FormLayout());
        
        // ===================================== Tab Folder 1 =====================================
        TabFolder TabFolder = new TabFolder(compositeMain, SWT.NONE);
        FormData fd_TabFolder = new FormData();
        fd_TabFolder.bottom = new FormAttachment(100, -10);
        fd_TabFolder.right = new FormAttachment(100, -10);
        fd_TabFolder.top = new FormAttachment(0, 10);
        fd_TabFolder.left = new FormAttachment(0, 10);
        TabFolder.setLayoutData(fd_TabFolder);
        //test.top = new FormAttachment(0, 10);
        //test.left = new FormAttachment(0, 10);
//        FormData fd_TabFolder = new FormData();
//        fd_TabFolder.bottom = new FormAttachment(0, 258);
//        fd_TabFolder.right = new FormAttachment(0, 478);
//        fd_TabFolder.top = new FormAttachment(0, 10);
//        fd_TabFolder.left = new FormAttachment(0, 10);
//        TabFolder.setLayoutData(fd_TabFolder);
        
        
        TabItem tabHashTab = new TabItem(TabFolder, SWT.NONE);
        tabHashTab.setImage(SWTResourceManager.getImage(MainUI.class, "/javax/swing/plaf/metal/icons/ocean/collapsed.gif"));
        tabHashTab.setText("Hash Generator");
        
        Composite compositeHash = new Composite(TabFolder, SWT.NONE);
        tabHashTab.setControl(compositeHash);
        
        Label lbGenHash = new Label(compositeHash, SWT.NONE);
        lbGenHash.setAlignment(SWT.CENTER);
        lbGenHash.setBounds(0, 141, 62, 21);
        lbGenHash.setText("Gen Hash");
        
        Label lbHashCheck = new Label(compositeHash, SWT.NONE);
        lbHashCheck.setAlignment(SWT.CENTER);
        lbHashCheck.setBounds(0, 165, 62, 21);
        lbHashCheck.setText("Hash Check");
        
                Text tbGenHash = new Text(compositeHash, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
                tbGenHash.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
                tbGenHash.setBounds(68, 141, 386, 18);
                
                Text tbHashCheck = new Text(compositeHash, SWT.BORDER | SWT.WRAP);
                tbHashCheck.setBounds(68, 165, 386, 18);
                
                tbGenHash.addModifyListener(new ModifyListener() {
	    	public void modifyText(ModifyEvent arg0) {
	            if(tbHashCheck.getText().equals(shaHash)) {
	            	tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
	            }
	            else {
	            	tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
	            }
	    	}
                });
                
                tbHashCheck.addModifyListener(new ModifyListener() {
                	public void modifyText(ModifyEvent arg0) {
                        if(tbHashCheck.getText().equals(shaHash)) {
                        	tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
                        }
                        else {
                        	tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
                        }
                	}
                });
                
                ProgressBar pbHashBar = new ProgressBar(compositeHash, SWT.NONE);
                pbHashBar.setBounds(69, 196, 339, 18);
                
                Label lbProgressbar = new Label(compositeHash, SWT.NONE);
                lbProgressbar.setBounds(24, 196, 38, 18);
                lbProgressbar.setText(progressPercent + " %");
                pbHashBar.setMinimum(99);
                
        Label lbGenHashDnd = new Label(compositeHash, SWT.NONE);
        lbGenHashDnd.setToolTipText("Drag the file here and drop");
        lbGenHashDnd.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        lbGenHashDnd.setAlignment(SWT.CENTER);
        lbGenHashDnd.setBounds(0, 0, 464, 135);
        lbGenHashDnd.setText("\r\n\r\n\r\nDrag and Drop File");
        
        // File Drag and Drop
        DropTarget target = new DropTarget(lbGenHashDnd, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
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
        			int progressPercent = 0;
					lbProgressbar.setText(progressPercent + " %");
					pbHashBar.setMaximum(100*files.length);
					pbHashBar.setSelection(100*files.length);
        			if (files != null && files.length > 0) {
        				File file = new File(files[0]);
        				HashGenerator hg = new HashGenerator();
						
        				try {
        					shaHash = hg.generateFileHash(file);
							tbGenHash.setText(shaHash);
							
							lbProgressbar.setText(100 + " %");
						} catch (NoSuchAlgorithmException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
        			}
        		}
        	}
        });
        
     // ===================================== Tab Folder 2 =====================================
        TabItem tabAesEncoder = new TabItem(TabFolder, 0);
        tabAesEncoder.setText("AES256 Encoder");
        
        Composite compositeAes = new Composite(TabFolder, SWT.NONE);
        tabAesEncoder.setControl(compositeAes);
        
        Button btnEncrypt = new Button(compositeAes, SWT.PUSH);
        btnEncrypt.setSelection(true);
        btnEncrypt.setToolTipText("Convert file to .aes file");
        btnEncrypt.setEnabled(false);
        btnEncrypt.setBounds(10, 156, 216, 36);
        btnEncrypt.setText("Encrypt");
        
        Button btnAESDecrypt = new Button(compositeAes, SWT.NONE);
        btnAESDecrypt.setSelection(true);
        btnAESDecrypt.setToolTipText("Convert .aes file to .txt file");
        btnAESDecrypt.setEnabled(false);
        btnAESDecrypt.setBounds(238, 156, 216, 36);
        btnAESDecrypt.setText("Decrypt");
        
        Label lbOutput = new Label(compositeAes, SWT.NONE);
        lbOutput.setFont(SWTResourceManager.getFont("Arial", 11, SWT.NORMAL));
        lbOutput.setText("Output (.aes)");
        lbOutput.setBounds(22, 133, 84, 15);
        
        Text tbOutputText = new Text(compositeAes, SWT.BORDER);
        tbOutputText.setBounds(112, 132, 342, 18);
        
        Label lbAesDnd = new Label(compositeAes, SWT.NONE);
        lbAesDnd.setText("\r\n\r\n\r\nDrag and Drop File");
        lbAesDnd.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        lbAesDnd.setAlignment(SWT.CENTER);
        lbAesDnd.setBounds(0, 0, 464, 130);
        
        // File Drag and Drop
        DropTarget target2 = new DropTarget(lbAesDnd, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
        target2.setTransfer(new Transfer[]{FileTransfer.getInstance(),
				TextTransfer.getInstance()});
        
        ProgressBar pbAesBar = new ProgressBar(compositeAes, SWT.SMOOTH);
        pbAesBar.setMinimum(90);
        pbAesBar.setBounds(10, 199, 444, 15);
        
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
        				pathAES = files[0];
    					
    					String buffer;
    					buffer = pathAES.substring(pathAES.length()-4, pathAES.length());
    					
    					if(buffer.equals(".aes"))
    					{
    						btnEncrypt.setEnabled(false);
    						btnAESDecrypt.setEnabled(true);
    						buffer = ".txt";
    						tbOutputText.setText(pathAES.substring(0, pathAES.length()-4).concat(buffer));
    					}
    					else
    					{
    						btnEncrypt.setEnabled(true);
    						btnAESDecrypt.setEnabled(false);
    						buffer = ".aes";
    						tbOutputText.setText(pathAES.substring(0, pathAES.length()-4).concat(buffer));
    					}
        			}
        		}
        	}
        });
        btnEncrypt.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		pbAesBar.setSelection(0);
        		Aes256Codec ac = new Aes256Codec();
        		
        		try {
					ac.encryptFile(pathAES);
					pbAesBar.setSelection(100);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
        	}
        });
        
        btnAESDecrypt.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		pbAesBar.setSelection(0);
        		Aes256Codec ac = new Aes256Codec();
        		
        		try {
					ac.decryptFile(pathAES);
					pbAesBar.setSelection(100);
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
