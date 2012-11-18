package emmaforeclipse.views;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.browser.*;
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

	Text projectDirBox;
	Text testDirBox;
	Text antBox;
	Text emmaBox;
	Text javaHomeBox;
	Text androidBox;

	Button projectDirSelectButton;
	Button testDirSelectButton;
	Button antSelectButton;
	Button emmaSelectButton;
	Button javaHomeSelectButton;
	Button androidSelectButton;
	Button runButton;	


	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {


		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		parent.setLayout(layout);

		new Label(parent, SWT.NONE).setText("Project Directory");
		projectDirBox = new Text(parent, SWT.BORDER);
		projectDirSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(parent, projectDirBox, projectDirSelectButton);

		new Label(parent, SWT.NONE).setText("Test Directory");
		testDirBox = new Text(parent, SWT.BORDER);
		testDirSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(parent, testDirBox , testDirSelectButton);

		new Label(parent, SWT.NONE).setText("Ant Directory");
		antBox = new Text(parent, SWT.BORDER);
		antSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(parent, antBox, antSelectButton);

		new Label(parent, SWT.NONE).setText("Emma Path");
		emmaBox = new Text(parent, SWT.BORDER);
		emmaSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(parent, emmaBox, emmaSelectButton);

		new Label(parent, SWT.NONE).setText("Java Home");
		javaHomeBox = new Text(parent, SWT.BORDER);
		javaHomeSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(parent, javaHomeBox, javaHomeSelectButton);

		new Label(parent, SWT.NONE).setText("Android Directory");
		androidBox = new Text(parent, SWT.BORDER);
		androidSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(parent, androidBox, androidSelectButton);


		new Label(parent, SWT.NONE).setText("");
		new Label(parent, SWT.NONE).setText("");
		runButton = new Button(parent, SWT.PUSH);
		runButton.setText("runTest");
		runButton.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false)); 
		RunListener rl = new RunListener();
		rl.parent = parent;
		runButton.addListener(SWT.Selection, rl);
	}


	private void buildSelectionRow (Composite parent, Text text, Button button) {
		button.setText("Choose");
		projectDirBox.setText("");
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		button.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false)); 
		addFileChooserListener(parent, button, text);		
	}

	private void addFileChooserListener(Composite parent, Button button, Text text) {
		FileSelectListener l = new FileSelectListener();
		l.text = text;
		l.shell = parent.getShell();
		button.addListener(SWT.Selection, l);	
	}

	static void generateShellScript(String projectDir, String testDir, String emmaPath,
			String androidDir, String antDir, String javaHomeDir) {
		StringBuilder sb = new StringBuilder();
		sb.append("#!/bin/sh\n");
		sb.append("export CLASSPATH=$CLASSPATH:" + emmaPath.trim() + "\n");
		sb.append("export PATH=$PATH:" + androidDir.trim() + "\n");
		sb.append("export PATH=$PATH:" + androidDir.trim() + "tools\n");
		sb.append("cd " + projectDir + "\n");
		sb.append("cd ..\n");
		String projectName = projectDir.trim().substring(projectDir.trim().lastIndexOf("/") + 1);
		sb.append("android update project -p " + projectName + "\n");
		sb.append("cd " + testDir + "\n");
		sb.append("android update test-project -m "+projectDir+" -p .\n");
		sb.append("ant clean emma debug install test");	
		try {
			File file = new File(testDir.trim() + "src/temp.sh");
			if (!file.exists()) file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sb.toString());
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("File IO error");
		}

	}

	class RunListener implements Listener{
		Composite parent;
		@Override
		public void handleEvent(Event arg0) {


	
				//				String projectDir = projectDirBox.getText();
				//				String testDir = testDirBox.getText();
				//				String emmaPath = emmaBox.getText();
				//				String androidDir = androidBox.getText();
				//				String antDir = antBox.getText();
				//				String javaHomeDir = javaHomeBox.getText();
				//				
				String projectDir = "/Users/glcylily/Documents/workspace/Snake/";
				String testDir = "/Users/glcylily/Documents/workspace/SnakeTest/";
				String emmaPath = "~/Downloads/android-sdk-macosx/tools/lib/emma.jar";
				String androidDir = "~/Downloads/android-sdk-macosx/";
				String antDir = projectDirBox.getText();
				String javaHomeDir = "";
				Runtime runtime = Runtime.getRuntime() ;
				generateShellScript(projectDir, testDir, emmaPath, androidDir, antDir, javaHomeDir);
				Process pr;
				try {
					pr = runtime.exec("sh " + testDir.trim() + "src/temp.sh");
					pr.waitFor() ;
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				Shell s = new Shell(parent.getDisplay());
				s.setLayout(new FillLayout());
				Browser browser = new Browser(s, SWT.None);
				browser.setUrl(testDir + "bin/coverage.html");
				s.open();
				
//				ScriptRunner scriptRunner = new ScriptRunner(testDir, parent.getDisplay());
//				scriptRunner.start();
//				try {
//					scriptRunner.join();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		}

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