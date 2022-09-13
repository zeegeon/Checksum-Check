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
        shell.setLayout(null); // 여기부터 레이아웃 종류 정해서 수정하기
        
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
        lbGenHash.setLocation(10, 108);
        Label lbHashCheck = new Label(compositeHash, SWT.NONE);
        lbHashCheck.setAlignment(SWT.CENTER);
        lbHashCheck.setText("Hash Check");
        lbHashCheck.setBounds(0, 135, 60, 20);
        
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
        
        Label lblSsdasdasd = new Label(compositeHash, SWT.CENTER);
        lblSsdasdasd.setBounds(0, 69, 467, 15);
        lblSsdasdasd.setText("ssdasdasd");
        
        Label lbGenHashDnd = new Label(compositeHash, SWT.HORIZONTAL | SWT.CENTER);
        lbGenHashDnd.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
        lbGenHashDnd.setToolTipText("Drag the file here and drop");
        lbGenHashDnd.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        lbGenHashDnd.setText("Drag and Drop File");
        lbGenHashDnd.setBounds(0, 0, 477, 154);
        
        new Thread() {
        	@Override
        	public void run() {
        		Display.getDefault().asyncExec(new Runnable() {
                	@Override
                	public void run() {
                		//pbHashBar.setSelection(100);
                	} 
                });
        	} 
        }.start();
        
        Display.getDefault().syncExec(new Runnable() {
        	@Override
        	public void run() {
        		System.out.println("Thread sync 1");
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
        			if (files != null && files.length > 0) {   	// 파일이 크기가 0 인지 아닌지 검사하는 구문
        				File file = new File(files[0]); 		// files[0] -> 파일경로 
						Display.getDefault().syncExec(new Runnable() {
    	                	@Override
    	                	public void run() {
								try {
									MessageDigest md = MessageDigest.getInstance("SHA-256");
									FileInputStream fis = new FileInputStream(file);
									int speed = 20480;
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
									fis.close();
									tbGenHash.setText(IntegrityCheckUtil.bytesToHex(md.digest()));
								} catch (NoSuchAlgorithmException e) {
								} catch (FileNotFoundException e) {
								} catch (IOException e) {
								}
    	                	}
						});
        			}
        		}
        	}
        });
        
     // ===================================== Tab Folder 2 =====================================
        TabItem tabAesEncoder = new TabItem(TabFolder, 0);
        tabAesEncoder.setText("AES256 Encoder");
        
        Composite compositeAes = new Composite(TabFolder, SWT.NONE);
        tabAesEncoder.setControl(compositeAes);
        
        Button btnEncrypt = new Button(compositeAes, SWT.NONE);
        btnEncrypt.setToolTipText("Convert file to .aes file");
        btnEncrypt.setEnabled(false);
        btnEncrypt.setText("Encrypt");
        btnEncrypt.setBounds(10, 168, 220, 27);
        
        Button btnAESDecrypt = new Button(compositeAes, SWT.NONE);
        btnAESDecrypt.setToolTipText("Convert .aes file to .txt file");
        btnAESDecrypt.setEnabled(false);
        btnAESDecrypt.setText("Decrypt");
        btnAESDecrypt.setBounds(240, 168, 210, 27);
        
        Label lbOutput = new Label(compositeAes, SWT.NONE);
        lbOutput.setText("Output (.aes)");
        lbOutput.setBounds(10, 142, 65, 20);
        
        Text tbOutputText = new Text(compositeAes, SWT.BORDER);
        tbOutputText.setBounds(80, 141, shell.getSize().x-130, 20);
        
        Label lbAesDnd = new Label(compositeAes, SWT.NONE);
        lbAesDnd.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
        lbAesDnd.setText("\r\n\r\n\r\nDrag and Drop File");
        lbAesDnd.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
        lbAesDnd.setAlignment(SWT.CENTER);
        lbAesDnd.setBounds(0, 0, 465, 130);
        
        // File Drag and Drop
        DropTarget target2 = new DropTarget(lbAesDnd, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
        target2.setTransfer(new Transfer[]{FileTransfer.getInstance(),
				TextTransfer.getInstance()});
        
        ProgressBar pbAesBar = new ProgressBar(compositeAes, SWT.SMOOTH);
        pbAesBar.setBounds(10, shell.getSize().y - 100, shell.getSize().x-60, 20);
        pbAesBar.setMinimum(90);
        
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
        				String pathAES = files[0];
    					String buffer = pathAES.substring(pathAES.lastIndexOf("."), pathAES.length());
    					tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
    					if(buffer.equals(".aes")) {
    						btnEncrypt.setEnabled(false);
    						btnAESDecrypt.setEnabled(true);
    						buffer = ".txt";
    						tbOutputText.setText(pathAES.substring(0, pathAES.lastIndexOf(".")).concat(buffer));
    						lbOutput.setText("Output (.txt)");
    					}
    					else {
    						btnEncrypt.setEnabled(true);
    						btnAESDecrypt.setEnabled(false);
    						buffer = ".aes";
    						tbOutputText.setText(pathAES.substring(0, pathAES.lastIndexOf(".")).concat(buffer));
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
        		pbAesBar.setSelection(0);
        		Aes256Codec ac = new Aes256Codec();
        		try {
					ac.encryptFile(tbOutputText.getText());
					pbAesBar.setSelection(100);
				} catch (Exception e2) {
					tbOutputText.setText("Not support file type, Access denied");
					tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
				}
        	}
        });
        
        btnAESDecrypt.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		pbAesBar.setSelection(0);
        		Aes256Codec ac = new Aes256Codec();
        		try {
					ac.decryptFile(tbOutputText.getText());
					pbAesBar.setSelection(100);
					System.out.println(e);
				} catch (Exception e2) {
					tbOutputText.setText("Not support file type, Access denied");
					tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
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
