package raon.encryption.ui;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;

import raon.encryption.FileEncryptor;
import raon.encryption.FileHashChecker;
import raon.encryption.FileHashChecker.HashCallback;

public class ShellMaker extends Shell 
{
	private String inputAESFilePath;
	
	public ShellMaker(Display display) 
	{
		super(display, SWT.SHELL_TRIM);
		this.setMinimumSize(new Point(250, 180));
		
		setImage(SWTResourceManager.getImage("/res/icon.ico"));
		InputStream is =  Main.class.getClassLoader().getResourceAsStream("icon.ico");
		if (is != null) 
			setImage(new Image(display, is));
		else 
			setImage(new Image(display, "res/icon.ico"));

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
		compositeHash.setLayout(new GridLayout(2, false));
		
		Composite compositeHashDnd = new Composite(compositeHash, SWT.NONE);
		compositeHashDnd.setLayout(new GridLayout(1, false));
		compositeHashDnd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Label lbHashDndBox = new Label(compositeHashDnd, SWT.NONE);
		lbHashDndBox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		lbHashDndBox.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lbHashDndBox.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
		lbHashDndBox.setText("Drag and Drop File");
		
		Label lbHashFileGen = new Label(compositeHash, SWT.RIGHT);
		lbHashFileGen.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbHashFileGen.setText("Gen Hash");
		
		Text tbHashFileGen = new Text(compositeHash, SWT.BORDER | SWT.READ_ONLY);
		tbHashFileGen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		tbHashFileGen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		Label lbHashCheck = new Label(compositeHash, SWT.RIGHT);
		lbHashCheck.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbHashCheck.setText("Check Hash");
		
		Text tbHashCheck = new Text(compositeHash, SWT.BORDER);
		tbHashCheck.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lbHashProgress = new Label(compositeHash, SWT.RIGHT);
		GridData gd_lbHashProgress = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lbHashProgress.widthHint = 50;
		lbHashProgress.setLayoutData(gd_lbHashProgress);
		lbHashProgress.setText("0 %");
		
		ProgressBar pbHashBar = new ProgressBar(compositeHash, SWT.SMOOTH);
		pbHashBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		pbHashBar.setState(SWT.PAUSED);
		
		DropTarget dtHashDnd = new DropTarget(compositeHashDnd, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
		dtHashDnd.setTransfer(new Transfer[] { FileTransfer.getInstance(), TextTransfer.getInstance() });
		dtHashDnd.addDropListener(new DropTargetAdapter() 
		{
			public void dragEnter(DropTargetEvent e) 
			{
				if (e.detail == DND.DROP_DEFAULT)
					e.detail = DND.DROP_COPY;
			}

			public void dragOperationChanged(DropTargetEvent e) {}

			public void drop(DropTargetEvent e)
			{
				String[] inputFilePath = (String[]) e.data;
				
				if (inputFilePath != null && inputFilePath.length > 0)
				{
					FileHashChecker fhc = new FileHashChecker();
					fhc.SetCallback(new HashCallback() 
					{
						@Override
						public void process(int prog) 
						{
							Display.getDefault().asyncExec(new Runnable()
							{
								@Override
								public void run()
								{
									pbHashBar.setSelection(prog);
									lbHashProgress.setText(prog + " %");
								}
							});
						}
					});
					
					new Thread() 
					{
						@Override
						public void run() 
						{
							String hash = fhc.generateFileHashString(inputFilePath[0]);
							
							if(hash != null)
							{
								Display.getDefault().syncExec(new Runnable()
								{
									@Override
									public void run() 
									{
										tbHashFileGen.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
										tbHashFileGen.setText(hash);
									}
								});
							}
							else
							{
								Display.getDefault().syncExec(new Runnable()
								{
									@Override
									public void run() 
									{
										tbHashFileGen.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
										tbHashFileGen.setText("Permission denied : " + inputFilePath[0]);
										pbHashBar.setSelection(0);
										lbHashProgress.setText("0 %");
									}
								});
							}
							
						}
					}.start();
				}
			}
			
		});
		
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
		
	}
	
	private void initializeAES(TabFolder parent)
	{
		TabItem tiAESEncoder = new TabItem(parent, SWT.NONE);
		tiAESEncoder.setText("AES256 Encoder");

		Composite compositeAES = new Composite(parent, SWT.NONE);
		compositeAES.setSize(this.getSize());
		tiAESEncoder.setControl(compositeAES);
		compositeAES.setLayout(new GridLayout(2, false));
		
		Composite compositeAESDnd = new Composite(compositeAES, SWT.NONE);
		compositeAESDnd.setLayout(new GridLayout(1, false));
		compositeAESDnd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Label lbAesDndBox = new Label(compositeAESDnd, SWT.NONE);
		lbAesDndBox.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		lbAesDndBox.setForeground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		lbAesDndBox.setFont(SWTResourceManager.getFont("Arial", 15, SWT.NORMAL));
		lbAesDndBox.setText("Drag and Drop File");
				
		Label lbOutput = new Label(compositeAES, SWT.CENTER);
		lbOutput.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lbOutput.setText("Output (.aes)");
		
		Text tbOutputText = new Text(compositeAES, SWT.BORDER);
		tbOutputText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnEncrypt = new Button(compositeAES, SWT.NONE);
		btnEncrypt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		btnEncrypt.setEnabled(false);
		btnEncrypt.setText("Encrypt");

		Button btnAESDecrypt = new Button(compositeAES, SWT.NONE);
		btnAESDecrypt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		btnAESDecrypt.setEnabled(false);
		btnAESDecrypt.setText("Decrypt");
		
		DropTarget dtAESDnd = new DropTarget(compositeAESDnd, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);
		dtAESDnd.setTransfer(new Transfer[] { FileTransfer.getInstance(), TextTransfer.getInstance() });
		dtAESDnd.addDropListener(new DropTargetAdapter()
		{
			public void dragEnter(DropTargetEvent e) 
			{
				if (e.detail == DND.DROP_DEFAULT)
					e.detail = DND.DROP_COPY;
			}

			public void dragOperationChanged(DropTargetEvent e) {}

			public void drop(DropTargetEvent e) 
			{
				String[] inputFilePath = (String[]) e.data;
				
				if (inputFilePath != null && inputFilePath.length > 0) 
				{
					inputAESFilePath = inputFilePath[0];
					String[] tokens = inputAESFilePath.split("\\.(?=[^\\.]+$)");
					if (tokens.length < 2) return;
					tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					
					if (tokens[1].equals("aes")) 
					{
						btnEncrypt.setEnabled(false);
						btnAESDecrypt.setEnabled(true);
						tbOutputText.setText(tokens[0] + ".txt");
						lbOutput.setText("Output (.txt)");
					}
					else
					{
						btnEncrypt.setEnabled(true);
						btnAESDecrypt.setEnabled(false);
						tbOutputText.setText(tokens[0] + ".aes");
						lbOutput.setText("Output (.aes)");
					}
				}
			}
			
		});
		
		btnEncrypt.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (inputAESFilePath == null) return;
				
				String outputFilePath = tbOutputText.getText();
				
				new Thread()
				{
					@Override
					public void run() 
					{	
						FileEncryptor fileEncryptor = new FileEncryptor();
						try 
						{
							fileEncryptor.encryptFileAES(inputAESFilePath, outputFilePath);	
						}
						catch(RuntimeException e)
						{
							Display.getDefault().syncExec(new Runnable()
							{
								@Override
								public void run() 
								{
									tbOutputText.setText("Not a support file type, Access denied");
									tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
								}
							});
						} 
						catch (IOException e)
						{
							Display.getDefault().syncExec(new Runnable()
							{
								@Override
								public void run() 
								{
									tbOutputText.setText("File not found, Access denied");
									tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
								}
							});	
						}
						catch (Exception e)
						{
							e.printStackTrace();
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
				if (inputAESFilePath == null) return;
				
				String outputFilePath = tbOutputText.getText();
				
				new Thread() 
				{
					@Override
					public void run() 
					{	
						FileEncryptor fileEncryptor = new FileEncryptor();
						try 
						{
							fileEncryptor.decryptFileAES(inputAESFilePath, outputFilePath);
						} 
						catch (RuntimeException e) 
						{
							Display.getDefault().syncExec(new Runnable()
							{
								@Override
								public void run() 
								{
									tbOutputText.setText("Not a support file type, Access denied");
									tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
								}
							});	
						} 
						catch (IOException e)
						{
							Display.getDefault().syncExec(new Runnable()
							{
								@Override
								public void run() 
								{
									tbOutputText.setText("File not found, Access denied");
									tbOutputText.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
								}
							});	
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						} 
					}
				}.start();
			}
			
		});
	
	}
	
	@Override
	protected void checkSubclass() 
	{}
}
