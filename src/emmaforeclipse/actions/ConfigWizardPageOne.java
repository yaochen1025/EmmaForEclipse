package emmaforeclipse.actions;


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
		emmaSelectButton.addListener(SWT.Selection, ls);


		new Label(parent, SWT.NONE).setText("Android Directory");
		androidBox = new Text(parent, SWT.BORDER);
		androidSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(androidBox, androidSelectButton);


		// Required to avoid an error in the system
		setControl(parent);
		setPageComplete(false);

	}
	private void buildSelectionRow (Text text, Button button) {
		button.setText("Choose");
		text.setText("");
		text.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		button.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false)); 
		addDirectoryChooserListener(button, text);	
		NextListener l = new NextListener();
		text.addKeyListener(l);
	}

	private void addDirectoryChooserListener(Button button, Text text) {
		DirectorySelectListener l = new DirectorySelectListener();
		l.text = text;
		l.shell = parent.getShell();
		button.addListener(SWT.Selection, l);	
	}
	
	class NextListener implements KeyListener{
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
		}
	}

	class DirectorySelectListener implements Listener{
		Text text;
		Shell shell;

		@Override
		public void handleEvent(Event arg0) {		
			DirectoryDialog dialog = new DirectoryDialog(shell);
			text.setText(dialog.open());
			if (projectDirBox.getText().isEmpty() || testDirBox.getText().isEmpty() 
					|| emmaBox.getText().isEmpty() || androidBox.getText().isEmpty()) {
				return;
			}
			setPageComplete(true);
		}	
	}


	class FileSelectListener implements Listener{
		Text text;
		Shell shell;
		@Override
		public void handleEvent(Event arg0) {
			FileDialog dialog = new FileDialog(shell);
			text.setText(dialog.open());
			if (projectDirBox.getText().isEmpty() || testDirBox.getText().isEmpty() 
					|| emmaBox.getText().isEmpty() || androidBox.getText().isEmpty()) {
				return;
			}
			setPageComplete(true);
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
