package encryption;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Main {
    public static boolean bInit = false;
    public static Text textBox1 = null;

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Test Monitor");
        shell.setLayout(new Layout() {

            @Override
            protected void layout(Composite arg0, boolean arg1) {
                // set window start size
                if (!bInit) {
                    arg0.setSize(800, 600);
                    bInit = true;
                }

                // change textbox size by win size 
                try {
                    if (textBox1 != null) {
                        textBox1.setBounds(10, 50, arg0.getSize().x - 40, arg0.getSize().y - 110);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected Point computeSize(Composite arg0, int arg1, int arg2, boolean arg3) {
                return null;
            }
        });

        // make button
        Button button1 = new Button(shell, 0);
        button1.setBounds(10, 10, 150, 30);
        button1.setText("Button Text");

        // make textbox
        textBox1 = new Text(shell, 2626);
        textBox1.setLayoutData(new GridData(1808));
        textBox1.setFont(new Font(display, "Arial", 20, 0));
        textBox1.setText("Test now");
        textBox1.setFocus();

        // make label
        Label label1 = new Label(shell, 0);
        label1.setBounds(200, 10, 150, 30);
        label1.setText("whats this");

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}