package raon.encryption.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class Test2 {

  public static void main(String[] args) {
    final Display display = new Display();
    Shell shell = new Shell(display);
    final ProgressBar bar = new ProgressBar(shell, SWT.SMOOTH);
    bar.setBounds(10, 10, 800, 32);
    shell.open();
    final int maximum = bar.getMaximum();
    
    
    
    new Thread() {
    	
      public void run() {
        for (final int[] i = new int[1]; i[0] <= maximum; i[0]++) {
          try {
            Thread.sleep(20);
          } catch (Throwable th) {
          }
          
          
          System.out.println(i[0]);
          display.asyncExec(new Runnable() {
            public void run() {
              if (bar.isDisposed())
                return;
              bar.setSelection(i[0]);
            }
          });
          
          
          
          
        }
      }
    }.start();
    
    
    
    
    
    
    
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
}