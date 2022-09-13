package raon.encryption.ui;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import raon.encryption.Aes256Codec;
import raon.encryption.HashGenerator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FillLayout;

public class Test {
	private Text text;
	/**
	 * @wbp.parser.entryPoint
	 */
	public void openNewShell(Display display) {
		Shell shell = new Shell(display);
		shell.setMinimumSize(new Point(100, 250));
        
        // ========================     shell set up =================================================================
        shell.setText("File Integrity Check test");
        shell.setImage(new Image(display, "resource/logo.png"));
        shell.setLocation(display.getBounds().width*1/3, display.getBounds().height*1/3);
        
        // ========================  Main  Tab folder setup ==========================================================
        TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
        tabFolder.setBounds(10,10, 776 ,380); // just for Window Builder Editor show
        
        // ========================   TabItem 1 setup =================================================================
        TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
        tbtmNewItem.setText("New Item");
        
        Composite composite = new Composite(tabFolder, SWT.FILL);
        tbtmNewItem.setControl(composite);
        System.out.println(composite.getSize());
		// ========================     shell set up =================================================================
        
		// ========================     shell set up =================================================================
//        shell.addControlListener(new ControlAdapter() {
//        	@Override
//        	public void controlResized(ControlEvent e) {
//        		tabFolder.setBounds(10, 10, shell.getSize().x-30, shell.getSize().y-50);
//        	}
//        });
        
        shell.open();
        
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
	}
}
