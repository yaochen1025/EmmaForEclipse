package emmaforeclipse.actions;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
	Text androidBox;
	Text emmaBox;
	ConfigWizard wizard;
	ConfigWizardPageTwo nextPage;
	Button runUseAnt;
	Button runUseAdb;
	boolean methodChosen = false;
	public static boolean isAdb = false;

	public String getProjectDir() {
		return this.projectDirBox.getText();
	}

	public String getTestDir() {
		return this.testDirBox.getText();
	}

	public String getEmmaPath() {
		return this.emmaBox.getText();
	}
	public String getAndroidPath() {
		return this.androidBox.getText();
	}

	private Button projectDirSelectButton;
	private Button testDirSelectButton;
	private Button androidSelectButton;
	private Button emmaSelectButton;

	public ConfigWizardPageOne() {
		super("Environment Setting");
		setTitle("Environment Setting");
		setDescription("Please set the environment for Emma");
	}

	@Override
	public void createControl(Composite parentC) {
		wizard = (ConfigWizard) this.getWizard();
		parent = new Composite(parentC, SWT.NULL);
		ConfigWizard.runType = 1;
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		parent.setLayout(layout);
		
		

		runUseAnt = new Button(parent, SWT.RADIO);
		runUseAnt.setText("Run Use Ant");
		runUseAnt.addSelectionListener(new SelectionListener() {

			public void handleEvent(SelectionEvent arg0) {
				methodChosen = true;
				isAdb = false;
				if (isComplete()) {
					wizard.getPageZero().setPageComplete(true);
					setPageComplete(true);
				}else setPageComplete(false);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				handleEvent (arg0) ;

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				handleEvent(arg0) ;

			}

		});
		runUseAdb = new Button(parent, SWT.RADIO);
		runUseAdb.setText("Run Use Adb");
		runUseAdb.addSelectionListener(new SelectionListener() {

			public void handleEvent(SelectionEvent arg0) {
				isAdb = true;
				methodChosen = true;
				if (isComplete()) {
					wizard.getPageZero().setPageComplete(true);
					setPageComplete(true);
				}
				else setPageComplete(false);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				handleEvent (arg0) ;

			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				handleEvent(arg0) ;

			}

		});
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE).setText("Project Directory");
		projectDirBox = new Text(parent, SWT.BORDER);
		projectDirSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(projectDirBox, projectDirSelectButton);

		new Label(parent, SWT.NONE).setText("Test Directory");
		testDirBox = new Text(parent, SWT.BORDER);
		testDirSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(testDirBox , testDirSelectButton);

		new Label(parent, SWT.NONE).setText("Android Directory");
		androidBox = new Text(parent, SWT.BORDER);
		androidSelectButton = new Button(parent, SWT.PUSH);
		buildSelectionRow(androidBox, androidSelectButton);

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
		NextListener l = new NextListener();
		l.page = this;
		emmaSelectButton.addListener(SWT.Selection, ls);
		emmaBox.addKeyListener(l);


		getPathProperties();
		setControl(parent);
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

/*		    File file = new File("workspace/EmmaForEclipse/config.properties");
		    System.out.println(file.getAbsolutePath());
		    
		    FileInputStream f = new FileInputStream(file);
		    System.out.println("good");
		    prop.load(f);
		    System.out.println("better");
*/		    
			prop.load(new FileInputStream(new File("config.properties")));

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
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	class NextListener implements KeyListener{
		ConfigWizardPageOne page;
		@Override
		public void keyPressed(KeyEvent arg0) { 
			//
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			if (isComplete()) setPageComplete(true);
			else setPageComplete(false);
		}
	}


	void doComplete() {
		addSlash(projectDirBox);
		addSlash(testDirBox);				
		addSlash(androidBox);
		if (!this.methodChosen) return;
		nextPage = new ConfigWizardPageTwo(testDirBox.getText());
		wizard.addPage(nextPage);
		wizard.getPageZero().setPageComplete(true);
	}


	static void addSlash(Text box) {
		String s = box.getText();
		if (!s.endsWith("/")) {
			box.setText(s+"/");
		}

	}

	boolean isComplete() {
		return !(projectDirBox.getText().isEmpty() || testDirBox.getText().isEmpty() 
				|| emmaBox.getText().isEmpty() || androidBox.getText().isEmpty());
	}

	class DirectorySelectListener implements Listener{

		Text text;
		Shell shell;
		ConfigWizardPageOne page;

		@Override
		public void handleEvent(Event arg0) {

			DirectoryDialog dialog = new DirectoryDialog(shell);
			text.setText(dialog.open());
			if (isComplete()) {
				setPageComplete(true);
			}else setPageComplete(false);
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
			if (isComplete()) {
				setPageComplete(true);
			}else setPageComplete(false);
		}

	}

	@Override
	public IWizardPage getNextPage() {
		doComplete();
		return this.nextPage;
	}
	@Override
	public IWizardPage getPreviousPage() {
		return null;
	}
	@Override
	public boolean canFlipToNextPage() {
		return isAdb && isComplete();
	}
	
	
}
