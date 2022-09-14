package raon.encryption.ui;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

import raon.encryption.Aes256Codec;
import raon.encryption.IntegrityCheckUtil;

public class OpenShell {
	/**
	 * @wbp.parser.entryPoint
	 */
	public void openNewShell(Display display) {
		Shell shell = new Shell(display);
        
        // Shell setup
        shell.setText("File Integrity Check");
        shell.setImage(new Image(display, "resource/logo.png"));
        
        // Get primary monitor size
        Monitor primary = display.getPrimaryMonitor();
        Rectangle bounds = primary.getBounds();
        Rectangle rect = shell.getBounds();
        
        // Shell location set in center
        shell.setLocation((bounds.width - rect.width) / 2, (bounds.height - rect.height) / 2);
        shell.setSize(bounds.width*2/5,bounds.height/3);
        
        shell.addControlListener(new ControlAdapter() {
        	@Override
        	public void controlResized(ControlEvent e) {
        		shell.redraw();
        	}
        });
        shell.setLayout(null); // ������� ���̾ƿ� ���� ���ؼ� �����ϱ�
        
        //Composite compositeMain = new Composite(shell, SWT.NONE);
        //compositeMain.setBounds(0, 0, 250, 288);
        //compositeMain.setLayout(layout);
        
        // ===================================== Tab Folder 1 =====================================
        TabFolder TabFolder = new TabFolder(shell, SWT.NONE);
        TabFolder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
        TabFolder.setBounds(10, 10, 480, 268);
        //TabFolder.setBounds(0, 0, 500, 288);
        
        TabItem tabHashTab = new TabItem(TabFolder, SWT.NONE);
        tabHashTab.setText("Hash Generator");
        
        Composite compositeHash = new Composite(TabFolder, SWT.NONE);
        tabHashTab.setControl(compositeHash);
        
        Label lbGenHash = new Label(compositeHash, SWT.NONE);
        lbGenHash.setSize(60, 20);
        lbGenHash.setAlignment(SWT.CENTER);
        lbGenHash.setText("Gen Hash");
        //lbGenHash.setBounds(0, shell.getSize().y-160, 60, 20);
        lbGenHash.setLocation(0, 108);
        Label lbHashCheck = new Label(compositeHash, SWT.NONE);
        lbHashCheck.setAlignment(SWT.CENTER);
        lbHashCheck.setText("Hash Check");
        lbHashCheck.setBounds(0, 131, 60, 20);
        
        Text tbGenHash = new Text(compositeHash, SWT.BORDER | SWT.READ_ONLY);
        tbGenHash.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        tbGenHash.setBounds(70, shell.getSize().y-160, shell.getSize().x-115, 20);
        
        Text tbHashCheck = new Text(compositeHash, SWT.BORDER);
        tbHashCheck.setBounds(70, shell.getSize().y-135, shell.getSize().x-115, 20);
        
        tbGenHash.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
		        if(tbHashCheck.getText().equals(tbGenHash.getText())) {
		        	tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
		        }
		        else {
		        	tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		        }
			}
		        });
        
        tbHashCheck.addModifyListener(new ModifyListener() {
        	public void modifyText(ModifyEvent arg0) {
                if(tbHashCheck.getText().equals(tbGenHash.getText())) {
                	tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
                }
                else {
                	tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
                }
        	}
        });
        
        ProgressBar pbHashBar = new ProgressBar(compositeHash, SWT.SMOOTH);
        pbHashBar.setBounds(70, shell.getSize().y-105, shell.getSize().x-115, 20);
        
        Label lbProgressbar = new Label(compositeHash, SWT.NONE);
        lbProgressbar.setText(0 + " %");
        lbProgressbar.setBounds(25, shell.getSize().y-105, 40, 20);
        
        Label lbShowHashDnd = new Label(compositeHash, SWT.CENTER);
        lbShowHashDnd.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        lbShowHashDnd.setEnabled(false);
        lbShowHashDnd.setBounds(10, 39, 467, 27);
        lbShowHashDnd.setText("Drag and Drop File");
        
        Label lbGenHashDnd = new Label(compositeHash, SWT.HORIZONTAL | SWT.CENTER);
        lbGenHashDnd.setToolTipText("Drag the file here and drop");
        lbGenHashDnd.setBounds(0, 0, 477, 154);
        
        new Thread() {
        	@Override
        	public void run() {
        		Display.getDefault().asyncExec(new Runnable() {
                	@Override
                	public void run() {
                	} 
                });
        	} 
        }.start();
        
        Display.getDefault().syncExec(new Runnable() {
        	@Override
        	public void run() {
        	} 
        });
       
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
					pbHashBar.setSelection(progressPercent);
        			if (files != null && files.length > 0) {   	// ������ ũ�Ⱑ 0 ���� �ƴ��� �˻��ϴ� ����
        				File file = new File(files[0]); 		// files[0] -> ���� ������
        				
        				new Thread() {
        		        	@Override
        		        	public void run() {
        		        		Display.getDefault().asyncExec(new Runnable() {
        		                	@Override
        		                	public void run() {
        		                		try {
        									MessageDigest md = MessageDigest.getInstance("SHA-256");
        									FileInputStream fis = new FileInputStream(file);
        									int speed = 4096;
        									int byteCount = 0;
        									int cycle = ((int)file.length() / speed)+1;
        									int ccount = 0;
        									byte[] byteArray = new byte[speed];
        									
        									pbHashBar.setMaximum(cycle);
        									
        									while((byteCount = fis.read(byteArray)) != -1) {
        										md.update(byteArray, 0, byteCount);
        										ccount++;
        										pbHashBar.setSelection(ccount);
        								        lbProgressbar.setText(100 * ccount / cycle + " %");
        									}
        									lbProgressbar.setText(100 + " %");
        									fis.close();
        									tbGenHash.setText(IntegrityCheckUtil.bytesToHex(md.digest()));
        								} catch (NoSuchAlgorithmException e) {
        								} catch (FileNotFoundException e) {
        								} catch (IOException e) {
        									lbShowHashDnd.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        									lbShowHashDnd.setText("Drag and Drop File");
        									lbShowHashDnd.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
        								}
        		                	} 
        		                });
        		        	} 
        		        }.start();
        			}
        		}
        	}
        });
        
     // ===================================== Tab Folder 2 =====================================
        TabItem tabAesEncoder = new TabItem(TabFolder, 0);
        tabAesEncoder.setText("AES256 Encoder");
        
        Composite compositeAes = new Composite(TabFolder, SWT.NONE);
        tabAesEncoder.setControl(compositeAes);
        
        Label lbShowAESDnd = new Label(compositeAes, SWT.CENTER);
        lbShowAESDnd.setEnabled(false);
        lbShowAESDnd.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        lbShowAESDnd.setBounds(10, 55, 467, 27);
        lbShowAESDnd.setText("Drag and Drop File");
        
        Button btnEncrypt = new Button(compositeAes, SWT.NONE);
        btnEncrypt.setToolTipText("Convert file to .aes file");
        btnEncrypt.setEnabled(false);
        btnEncrypt.setText("Encrypt");
        btnEncrypt.setBounds(10, 168, 220, 27);
        
        Button btnAESDecrypt = new Button(compositeAes, SWT.NONE);
        btnAESDecrypt.setToolTipText("Convert .aes file to .txt file");
        btnAESDecrypt.setEnabled(false);
        btnAESDecrypt.setText("Decrypt");
        btnAESDecrypt.setBounds(240, 168, 222, 27);
        
        Label lbOutput = new Label(compositeAes, SWT.NONE);
        lbOutput.setText("Output (.aes)");
        lbOutput.setBounds(10, 142, 65, 20);
        
        Text tbOutputText = new Text(compositeAes, SWT.BORDER);
        tbOutputText.setBounds(80, 141, shell.getSize().x-130, 20);
        
        Label lbAesDnd = new Label(compositeAes, SWT.NONE);
        lbAesDnd.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
        lbAesDnd.setBounds(0, 0, 465, 130);
        
        // File Drag and Drop
        DropTarget target2 = new DropTarget(lbAesDnd, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
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
        				String pathAES = files[0];		// ���� �̸� �ޱ�
    					String buffer = pathAES.substring(pathAES.lastIndexOf("."), pathAES.length()); //Ȯ���� Ȯ��
    					tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
    					if(buffer.equals(".aes")) {		// Ȯ���� �� ���ǹ�
    						btnEncrypt.setEnabled(false);
    						btnAESDecrypt.setEnabled(true);
    						tbOutputText.setText(pathAES);
    						lbOutput.setText("Output (.txt)");
    					}
    					else {
    						btnEncrypt.setEnabled(true);
    						btnAESDecrypt.setEnabled(false);
    						tbOutputText.setText(pathAES);
    						lbOutput.setText("Output (.aes)");
    					}
        			}
        		}
        	}
        });
        
        // Button action method override
        btnEncrypt.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		Aes256Codec ac = new Aes256Codec();
        		try {
					if(!ac.encryptFile(tbOutputText.getText())) {
						tbOutputText.setText("Not support file type, Access denied");
						tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					}
				} catch (Exception e2) {}
        	}
        });
        
        btnAESDecrypt.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		Aes256Codec ac = new Aes256Codec();
        		try {
        			if(!ac.decryptFile(tbOutputText.getText())) {
						tbOutputText.setText("Not support file type, Access denied");
						tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        			}
				} catch (Exception e2) {}
        	}
        });
        
        shell.open();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        //display.dispose();
	}
}
