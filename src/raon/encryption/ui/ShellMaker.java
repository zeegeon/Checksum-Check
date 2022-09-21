package raon.encryption.ui;

import java.io.FileNotFoundException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import raon.encryption.FileEncryptor;
import raon.encryption.FileHashChecker;
import raon.encryption.FileHashChecker.HashCallback;

public class ShellMaker extends Shell 
{	
	public ShellMaker(Display display) 
	{
		super(display);
		setImage(SWTResourceManager.getImage(ShellMaker.class, "/raon/encryption/ui/icon.ico"));

		this.setText("File Integrity Check");
		this.setLocation(400,250);
		this.setSize(500, 240);
		this.setLayout(new GridLayout(1, true));
		
		TabFolder tfFolder = new TabFolder(this, SWT.NONE);
		tfFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	
		initializeHash(tfFolder);
		
		initializeAES(tfFolder);
	}
	
	private void initializeHash(TabFolder parent)
	{
		TabItem tiHashTab = new TabItem(parent, SWT.NONE);
		tiHashTab.setText("Hash Checksum");

		Composite compositeHash = new Composite(parent, SWT.NONE);
		compositeHash.setSize(this.getSize());
		tiHashTab.setControl(compositeHash);

		Label lbHashFileGen = new Label(compositeHash, SWT.RIGHT);
		lbHashFileGen.setText("Gen Hash");
		lbHashFileGen.setBounds(0, this.getSize().y*2/5, 60, 20);
		
		Text tbHashFileGen = new Text(compositeHash, SWT.BORDER | SWT.READ_ONLY);
		tbHashFileGen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		tbHashFileGen.setBounds(this.getSize().x*1/7, this.getSize().y*2/5, this.getSize().x*3/4 + 15, 20);
		
		Label lbHashCheck = new Label(compositeHash, SWT.RIGHT);
		lbHashCheck.setText("Check");
		lbHashCheck.setBounds(0, this.getSize().y*1/2, 60, 20);
		
		Text tbHashCheck = new Text(compositeHash, SWT.BORDER);
		tbHashCheck.setBounds(this.getSize().x*1/7, this.getSize().y*1/2, this.getSize().x*3/4 + 15, 20);
		
		tbHashFileGen.addModifyListener(new ModifyListener() 
		{
			public void modifyText(ModifyEvent arg0) 
			{
				if (tbHashCheck.getText().equals(tbHashFileGen.getText())) 
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
				if (tbHashCheck.getText().equals(tbHashFileGen.getText())) 
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
		pbHashBar.setBounds(this.getSize().x*1/7, this.getSize().y*3/5, this.getSize().x*3/4 + 15, 20);
		pbHashBar.setMaximum(100);
		Label lbHashProgress = new Label(compositeHash, SWT.RIGHT);
		lbHashProgress.setText(0 + " %");
		lbHashProgress.setBounds(0, this.getSize().y*3/5, 60, 20);

		Label lbHashDndText = new Label(compositeHash, SWT.CENTER);
		lbHashDndText.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
		lbHashDndText.setEnabled(false);
		lbHashDndText.setBounds(0, this.getSize().y/5, this.getSize().x, 30);
		lbHashDndText.setText("Drag and Drop File");

		Label lbHashDndBox = new Label(compositeHash, SWT.NONE);
		lbHashDndBox.setToolTipText("Drag and drop the file to here");
		lbHashDndBox.setBounds(0, 0, this.getSize().x, this.getSize().y*2/5);

		// File Drag and Drop
		DropTarget dtHashDnd = new DropTarget(lbHashDndBox, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
		dtHashDnd.setTransfer(new Transfer[] { FileTransfer.getInstance(), TextTransfer.getInstance() });
		dtHashDnd.addDropListener(new DropTargetAdapter() 
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
						FileHashChecker fhc = new FileHashChecker();	
						fhc.SetCallback(new HashCallback() 
						{
							@Override
							public void process(int prog, String msg)
							{
								Display.getDefault().syncExec(new Runnable()
								{
									@Override
									public void run() 
									{
										lbHashProgress.setText(prog + " %");
										pbHashBar.setSelection(prog);
										tbHashFileGen.setText(msg);
									}
								});	
							}
						});
						
						new Thread() 
						{
							@Override
							public void run() 
							{
								fhc.generateFileHash(files[0]);
							}
						}.start();
					}
					
				}
			}
			
		});
	}
	
	private void initializeAES(TabFolder parent)
	{
		TabItem tiAESEncoder = new TabItem(parent, SWT.NONE);
		tiAESEncoder.setText("AES256 Encoder");

		Composite compositeAES = new Composite(parent, SWT.NONE);
		compositeAES.setSize(this.getSize());
		tiAESEncoder.setControl(compositeAES);

		Label lbAESDndText = new Label(compositeAES, SWT.CENTER);
		lbAESDndText.setEnabled(false);
		lbAESDndText.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
		lbAESDndText.setBounds(0, this.getSize().y/5, this.getSize().x, 30);
		lbAESDndText.setText("Drag and Drop File");

		Button btnEncrypt = new Button(compositeAES, SWT.NONE);
		btnEncrypt.setToolTipText("Convert file to .aes file");
		btnEncrypt.setEnabled(false);
		btnEncrypt.setText("Encrypt");
		btnEncrypt.setBounds(10, this.getSize().y*10/20, this.getSize().x*9/10, 20); 

		Button btnAESDecrypt = new Button(compositeAES, SWT.NONE);
		btnAESDecrypt.setToolTipText("Convert .aes file to .txt file");
		btnAESDecrypt.setEnabled(false);
		btnAESDecrypt.setText("Decrypt");
		btnAESDecrypt.setBounds(10, this.getSize().y*12/20, this.getSize().x*9/10, 20);

		Label lbOutput = new Label(compositeAES, SWT.CENTER);
		lbOutput.setText("Output (.aes)");
		lbOutput.setBounds(10, this.getSize().y*2/5, this.getSize().x*2/10, 20);

		Text tbOutputText = new Text(compositeAES, SWT.BORDER);
		tbOutputText.setBounds(this.getSize().x*2/10+10, 96, this.getSize().x*7/10, 20);

		Label lbAesDndBox = new Label(compositeAES, SWT.NONE);
		lbAesDndBox.setBounds(0, 0, this.getSize().x, this.getSize().y*2/5);

		// File Drag and Drop
		DropTarget dtAESDnd = new DropTarget(lbAesDndBox, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
		dtAESDnd.setTransfer(new Transfer[] { FileTransfer.getInstance(), TextTransfer.getInstance() });

		dtAESDnd.addDropListener(new DropTargetAdapter() 
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
						String pathAES = files[0];
						String buffer = pathAES.substring(pathAES.lastIndexOf("."), pathAES.length());
						tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
						
						if (buffer.equals(".aes")) 
						{
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

		// Button action method
		btnEncrypt.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String pathAESFile = tbOutputText.getText();
				new Thread() 
				{
					@Override
					public void run() 
					{	
						FileEncryptor ac = new FileEncryptor();
						try 
						{
							if(!ac.encryptFileAES(pathAESFile))
							{
								Display.getDefault().asyncExec(new Runnable()
								{
									@Override
									public void run() 
									{
										tbOutputText.setText("Not support file type, Access denied");
										tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
									}
								});	
							}
						} 
						catch (NullPointerException npe)
						{
							// pass
						}
						catch(FileNotFoundException mbe)
						{
							// file log.txt
						}
						catch (Exception e) 
						{
							MessageBox msg = new MessageBox(getShell(), SWT.ICON_WARNING);
							msg.setText("Warning");
							msg.setMessage("Error");
							msg.open();
						}
					}
				}.start();
			}
			
		});

		btnAESDecrypt.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String pathAESFile = tbOutputText.getText();
				
				new Thread() 
				{
					@Override
					public void run() 
					{	
						FileEncryptor ac = new FileEncryptor();
						try 
						{
							if(!ac.decryptFileAES(pathAESFile))
							{
								Display.getDefault().asyncExec(new Runnable()
								{
									@Override
									public void run() 
									{
										tbOutputText.setText("Not support file type, Access denied");
										tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
									}
								});	
							}
						} 
						catch (Exception e) 
						{
							System.out.println("btnDecrypt.addSelectionListener - " + e.getMessage());
						}
					}
				}.start();
			}
			
		});
	
	}
	
	@Override
	protected void checkSubclass() 
	{
	    //  allow subclass
	    System.out.println("Info : checking menu subclass");
	}
	
}
