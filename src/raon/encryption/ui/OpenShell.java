package raon.encryption.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

import raon.encryption.FileEncryptor;
import raon.encryption.FileHashChecker;

public class OpenShell extends Shell 
{	
	public OpenShell(Display display) 
	{
		super(display);

		this.setText("File Integrity Check");
		this.setImage(new Image(display, "resource/logo.png"));

		// Get primary monitor size
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = getBounds();

		// Shell location set in center
		this.setLocation((bounds.width - rect.width) / 2, (bounds.height - rect.height) / 2);
		this.setSize(bounds.width * 2 / 5, bounds.height / 3);

		this.setLayout(new GridLayout(1, false));
		this.layout(true, true);
		// Composite compositeMain = new Composite(shell, SWT.NONE);
		// compositeMain.setBounds(0, 0, 250, 288);
		// compositeMain.setLayout(layout);

		// ===================================== Tab Folder 1
		TabFolder tbFolder = new TabFolder(this, SWT.NONE);
		tbFolder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		tbFolder.setBounds(0, 0, 500, 288);
	
		initializeHash(tbFolder);
		
		initializeAES(tbFolder);
	}
	
	private void initializeHash(TabFolder parent)
	{
		TabItem tabHashTab = new TabItem(parent, SWT.NONE);
		tabHashTab.setText("Hash Generator");

		Composite compositeHash = new Composite(parent, SWT.NONE);
		tabHashTab.setControl(compositeHash);

		Label lbGenHash = new Label(compositeHash, SWT.NONE);
		lbGenHash.setSize(60, 20);
		lbGenHash.setAlignment(SWT.CENTER);
		lbGenHash.setText("Gen Hash");
		// lbGenHash.setBounds(0, getSize().y-160, 60, 20);
		lbGenHash.setLocation(0, 108);
		Label lbHashCheck = new Label(compositeHash, SWT.NONE);
		lbHashCheck.setAlignment(SWT.CENTER);
		lbHashCheck.setText("Hash Check");
		lbHashCheck.setBounds(0, 131, 60, 20);

		Text tbGenHash = new Text(compositeHash, SWT.BORDER | SWT.READ_ONLY);
		tbGenHash.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tbGenHash.setBounds(70, getSize().y - 160, getSize().x - 115, 20);

		Text tbHashCheck = new Text(compositeHash, SWT.BORDER);
		tbHashCheck.setBounds(70, getSize().y - 135, getSize().x - 115, 20);

		tbGenHash.addModifyListener(new ModifyListener() 
		{
			public void modifyText(ModifyEvent arg0) 
			{
				if (tbHashCheck.getText().equals(tbGenHash.getText())) 
				{
					tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
				} 
				else 
				{
					tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
				}
			}
		});

		tbHashCheck.addModifyListener(new ModifyListener() 
		{
			public void modifyText(ModifyEvent arg0) 
			{
				if (tbHashCheck.getText().equals(tbGenHash.getText())) 
				{
					tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
				} 
				else 
				{
					tbHashCheck.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
				}
			}
		});

		ProgressBar pbHashBar = new ProgressBar(compositeHash, SWT.SMOOTH);
		pbHashBar.setBounds(70, getSize().y - 105, getSize().x - 115, 20);

		Label lbProgressbar = new Label(compositeHash, SWT.NONE);
		lbProgressbar.setText(0 + " %");
		lbProgressbar.setBounds(25, getSize().y - 105, 40, 20);

		Label lbShowHashDnd = new Label(compositeHash, SWT.CENTER);
		lbShowHashDnd.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
		lbShowHashDnd.setEnabled(false);
		lbShowHashDnd.setBounds(10, 39, 467, 27);
		lbShowHashDnd.setText("Drag and Drop File");

		Label lbGenHashDnd = new Label(compositeHash, SWT.HORIZONTAL | SWT.CENTER);
		lbGenHashDnd.setToolTipText("Drag the file here and drop");
		lbGenHashDnd.setBounds(0, 0, 477, 154);

		// File Drag and Drop
		DropTarget target = new DropTarget(lbGenHashDnd, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
		target.setTransfer(new Transfer[] { FileTransfer.getInstance(), TextTransfer.getInstance() });
		target.addDropListener(new DropTargetAdapter() {
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
					int progressPercent = 0;
					lbProgressbar.setText(progressPercent + " %");
					pbHashBar.setSelection(progressPercent);
					if (files != null && files.length > 0) 
					{ // 파일이 크기가 0 인지 아닌지 검사하는 구문
						//File file = new File(files[0]); // files[0] -> 파일 절대경로
						//String[] files = (String[]) e.data;
						
						new Thread() 
						{
							@Override
							public void run() 
							{
								FileHashChecker hasher = new FileHashChecker();
								String text = hasher.generateFileHash(files[0]);
								
								Display.getDefault().asyncExec(new Runnable() 
								{
									@Override
									public void run() 
									{
										tbGenHash.setText(text);
										lbProgressbar.setText(100 + " %");
										pbHashBar.setSelection(100);
//										try {
//											MessageDigest md = MessageDigest.getInstance("SHA-256");
//											FileInputStream fis = new FileInputStream(file);
//											int speed = 4096;
//											int byteCount = 0;
//											int cycle = ((int) file.length() / speed) + 1;
//											int ccount = 0;
//											byte[] byteArray = new byte[speed];
//
//											pbHashBar.setMaximum(cycle);
//
//											while ((byteCount = fis.read(byteArray)) != -1) {
//												md.update(byteArray, 0, byteCount);
//												ccount++;
//												pbHashBar.setSelection(ccount);
//												lbProgressbar.setText(100 * ccount / cycle + " %");
//											}
//											lbProgressbar.setText(100 + " %");
//											fis.close();
//											tbGenHash.setText(bytesToHex(md.digest()));
//										} catch (Exception e) {
//											System.out.println("Thread error occured");
//										}
										
									}
								});
							}
						}.start();
						
					}
				}
			}
			
		});
	}
	
	private void initializeAES(TabFolder parent)
	{
		// ===================================== Tab Folder 2
		TabItem tbAesEncoder = new TabItem(parent, 0);
		tbAesEncoder.setText("AES256 Encoder");

		Composite compositeAes = new Composite(parent, SWT.NONE);
		tbAesEncoder.setControl(compositeAes);

		Label lbShowAESDnd = new Label(compositeAes, SWT.CENTER);
		lbShowAESDnd.setEnabled(false);
		lbShowAESDnd.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
		lbShowAESDnd.setBounds(10, 55, 467, 27);
		lbShowAESDnd.setText("Drag and Drop File");

		Button btnEncrypt = new Button(compositeAes, SWT.NONE);
		btnEncrypt.setToolTipText("Convert file to .aes file");
		btnEncrypt.setEnabled(false);
		btnEncrypt.setText("Encrypt");
		btnEncrypt.setBounds(10, 163, 220, 27);

		Button btnAESDecrypt = new Button(compositeAes, SWT.NONE);
		btnAESDecrypt.setToolTipText("Convert .aes file to .txt file");
		btnAESDecrypt.setEnabled(false);
		btnAESDecrypt.setText("Decrypt");
		btnAESDecrypt.setBounds(240, 163, 222, 27);

		Label lbOutput = new Label(compositeAes, SWT.NONE);
		lbOutput.setText("Output (.aes)");
		lbOutput.setBounds(10, 137, 65, 20);

		Text tbOutputText = new Text(compositeAes, SWT.BORDER);
		tbOutputText.setBounds(80, 136, getSize().x - 130, 20);

		Label lbAesDnd = new Label(compositeAes, SWT.NONE);
		lbAesDnd.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lbAesDnd.setBounds(0, 0, 465, 130);

		// File Drag and Drop
		DropTarget target2 = new DropTarget(lbAesDnd, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
		target2.setTransfer(new Transfer[] { FileTransfer.getInstance(), TextTransfer.getInstance() });

		target2.addDropListener(new DropTargetAdapter() 
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
						String pathAES = files[0]; // 파일 이름 받기
						String buffer = pathAES.substring(pathAES.lastIndexOf("."), pathAES.length()); // 확장자 확인
						tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
						
						if (buffer.equals(".aes")) 
						{ // 확장자 비교 조건문
							btnEncrypt.setEnabled(false);
							btnAESDecrypt.setEnabled(true);
							tbOutputText.setText(pathAES);
							lbOutput.setText("Output (.txt)");
						} 
						else 
						{
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
		btnEncrypt.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				FileEncryptor ac = new FileEncryptor();
				try 
				{
					if (!ac.encryptFileAES(tbOutputText.getText())) 
					{
						tbOutputText.setText("Not support file type, Access denied");
						tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					}
				} catch (Exception e2) {}
			}
		});

		btnAESDecrypt.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				FileEncryptor ac = new FileEncryptor();
				try 
				{
					if (!ac.decryptFile(tbOutputText.getText())) 
					{
						tbOutputText.setText("Not support file type, Access denied");
						tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					}
				} catch (Exception e2) {}
			}
			
		});
	
	}
	
	@Override
	protected void checkSubclass() 
	{
	    //  allow subclass
	    System.out.println("info   : checking menu subclass");
	}
	
}
