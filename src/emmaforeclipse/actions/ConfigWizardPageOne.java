package emmaforeclipse.actions;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ConfigWizardPageOne extends WizardPage {
	
	private Composite parent;
	Text projectDirBox;
	Text testDirBox;
	Text emmaBox;
	Text androidBox;

	Button projectDirSelectButton;
	Button testDirSelectButton;
	Button emmaSelectButton;
	Button androidSelectButton;
	Button runButton;	

	public ConfigWizardPageOne() {
		super("Environment Setting");
		setTitle("Environment Setting");
		setDescription("Please set the environment for Emma");
	}

	@Override
	public void createControl(Composite parentC) {
		parent = new Composite(parentC, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		parent.setLayout(layout);

		new Label(parent, SWT.NONE).setText("Project Directory");
		projectDirBox = new Text(parent, SWT.BORDER);
		projectDirSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(projectDirBox, projectDirSelectButton);

		new Label(parent, SWT.NONE).setText("Test Directory");
		testDirBox = new Text(parent, SWT.BORDER);
		testDirSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(testDirBox , testDirSelectButton);


		new Label(parent, SWT.NONE).setText("Emma Path");
		emmaBox = new Text(parent, SWT.BORDER);
		emmaSelectButton = new Button(parent, SWT.PUSH);
		emmaSelectButton.setText("Choose");
		emmaBox.setText("");
		emmaBox.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		emmaSelectButton.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false)); 
		FileSelectListener ls = new FileSelectListener();
		ls.shell = parent.getShell();
		ls.text = emmaBox;
		ls.page =this;
		emmaSelectButton.addListener(SWT.Selection, ls);


		new Label(parent, SWT.NONE).setText("Android Directory");
		androidBox = new Text(parent, SWT.BORDER);
		androidSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(androidBox, androidSelectButton);
		
		getPathProperties();
		
		// Required to avoid an error in the system
		setControl(parent);
		setPageComplete(false);
		
		if (!projectDirBox.getText().isEmpty() && !testDirBox.getText().isEmpty() 
				&& !emmaBox.getText().isEmpty() && !androidBox.getText().isEmpty()) {
			setPageComplete(true);
			ConfigWizardPageTwo pageTwo = (ConfigWizardPageTwo) this.getNextPage();
			pageTwo.testDirSelected = testDirBox.getText();
			
		}

	}
	private void buildSelectionRow (Text text, Button button) {
		button.setText("Choose");
		text.setText("");
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		button.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false)); 
		addDirectoryChooserListener(button, text);	
		NextListener l = new NextListener();
		l.page = this;
		text.addKeyListener(l);
	}

	private void addDirectoryChooserListener(Button button, Text text) {
		DirectorySelectListener l = new DirectorySelectListener();
		l.page = this;
		l.text = text;
		l.shell = parent.getShell();
		button.addListener(SWT.Selection, l);	
	}
	
	private void getPathProperties() {
		Properties prop = new Properties();
    	 
    	try {
               //load a properties file

    		prop.load(new FileInputStream("config.properties"));
 
            String projectDirectory = prop.getProperty("PROJECT");
            this.projectDirBox.setText(projectDirectory == null? "" : projectDirectory);
            
            String testProjectDirectory = prop.getProperty("TESTPROJECT");
            this.testDirBox.setText(testProjectDirectory == null? "" : testProjectDirectory);
            
            String emmaDirectory = prop.getProperty("EMMA");
            this.emmaBox.setText(emmaDirectory == null? "" : emmaDirectory);
            
            String androidDirectory = prop.getProperty("ANDROID_SDK");
            this.androidBox.setText(androidDirectory == null? "" : androidDirectory);
            
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
	}
	
	class NextListener implements KeyListener{
		ConfigWizardPageOne page;
		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			if (projectDirBox.getText().isEmpty() || testDirBox.getText().isEmpty() 
					|| emmaBox.getText().isEmpty() || androidBox.getText().isEmpty()) {
				return;
			}
			setPageComplete(true);
			ConfigWizardPageTwo pageTwo = (ConfigWizardPageTwo)page.getNextPage();
			pageTwo.testDirSelected = testDirBox.getText();
		}
	}

	class DirectorySelectListener implements Listener{
		Text text;
		Shell shell;
		ConfigWizardPageOne page;

		@Override
		public void handleEvent(Event arg0) {		
			DirectoryDialog dialog = new DirectoryDialog(shell);
			text.setText(dialog.open());
			if (projectDirBox.getText().isEmpty() || testDirBox.getText().isEmpty() 
					|| emmaBox.getText().isEmpty() || androidBox.getText().isEmpty()) {
				return;
			}
			setPageComplete(true);
			ConfigWizardPageTwo pageTwo = (ConfigWizardPageTwo)page.getNextPage();
			pageTwo.testDirSelected = testDirBox.getText();
					
			
		}	
	}


	class FileSelectListener implements Listener{
		Text text;
		Shell shell;
		ConfigWizardPageOne page;
		@Override
		public void handleEvent(Event arg0) {
			FileDialog dialog = new FileDialog(shell);
			text.setText(dialog.open());
			if (projectDirBox.getText().isEmpty() || testDirBox.getText().isEmpty() 
					|| emmaBox.getText().isEmpty() || androidBox.getText().isEmpty()) {
				return;
			}
			setPageComplete(true);
			ConfigWizardPageTwo pageTwo = (ConfigWizardPageTwo)page.getNextPage();
			pageTwo.testDirSelected = testDirBox.getText();
					
		}

	}
	
	public String getProjectDir() {
		return this.projectDirBox.getText();
	}
	
	public String getTestDir() {
		return this.testDirBox.getText();
	}
	
	public String getEmmaJar() {
		return this.emmaBox.getText();
	}
	public String getAndroidPah() {
		return this.androidBox.getText();
	}
}
