package emmaforeclipse.views;


import java.io.IOException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class PluginView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "emmaforeclipse.views.PluginView";

	private Text projectDir;
	private Text testDir;
	private Button projectDirSelectButton;
	private Button testDirSelectButton;

	private Text command;
	private Button runButton;


	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {


		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		parent.setLayout(layout);

		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("Project Directory");
		projectDir = new Text(parent, SWT.BORDER);
		projectDirSelectButton = new Button(parent, SWT.PUSH);
		projectDirSelectButton.setText("Choose");
		projectDir.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		projectDirSelectButton.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false));
		addFileChooserListener(parent, projectDirSelectButton, projectDir);

		new Label(parent, SWT.NONE).setText("Test Directory");
		testDir = new Text(parent, SWT.BORDER);
		testDirSelectButton = new Button(parent, SWT.PUSH);
		testDirSelectButton.setText("Choose");
		testDir.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		testDirSelectButton.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false)); 
		addFileChooserListener(parent, testDirSelectButton, testDir);


		new Label(parent, SWT.NONE).setText("Command");
		command = new Text(parent, SWT.BORDER);
		runButton = new Button(parent, SWT.PUSH);
		runButton.setText("run");
		command.setText("open -t /Users/glcylily/Documents/workspace/EmmaForEclipse/plugin.xml");
		command.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		runButton.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false)); 

		runButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				String cmd = command.getText();
				System.out.println(cmd);
				Runtime runtime = Runtime.getRuntime() ;
				try {
					Process pr = runtime.exec(cmd) ;
					pr.waitFor() ;
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

	}

	private void addFileChooserListener(Composite parent, Button button, Text text) {

		FileSelectListener l = new FileSelectListener();
		l.text = text;
		l.shell = parent.getShell();
		button.addListener(SWT.Selection, l);
	}

	class FileSelectListener implements Listener{
		Text text;
		Shell shell;
		@Override
		public void handleEvent(Event arg0) {		
			
			ContainerSelectionDialog dialog = new ContainerSelectionDialog(shell, null, true, "Select a directory");
			dialog.setTitle("Container Selection");
			dialog.open();
			Object[] result = dialog.getResult();
			for(Object o : result) {
				text.setText(o.toString());
			}
		
			//			DirectoryDialog directoryDialog = new DirectoryDialog(shell, SWT.NULL);
//			directoryDialog.setFilterPath("/Users/glcylily/Documents/workspace/");
//			directoryDialog.setMessage("Please select a directory and click OK");
//			String dir = directoryDialog.open();
//			if(dir != null) {
//				text.setText(dir);
//			}		
//			Display display = shell.getDisplay();
//			while (!shell.isDisposed()) {
//				if (!display.readAndDispatch())
//					display.sleep();
//			}
//			display.dispose();
		}	
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		//viewer.getControl().setFocus();
	}
}