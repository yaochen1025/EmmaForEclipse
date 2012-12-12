package emmaforeclipse.actions;

import java.util.ArrayList;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import emmaforeclipse.model.TestSelection;


public class ConfigWizardPageTwo extends WizardPage {

	public String testDirSelected;
	ConfigWizard wizard;
	ConfigWizardPageThree nextPage;
	public static String packageSelected = "";
	public ConfigWizardPageTwo(String testDirSelected) {
		super("Package");
		setTitle("Packages - using adb");
		setDescription("choose one package to run");
		this.testDirSelected = testDirSelected;
	}

	@Override
	public void createControl(Composite parent) {
		ConfigWizard.runType = 2;
		wizard = (ConfigWizard) this.getWizard();
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;

		Label info = new Label(container, SWT.NULL);
		info.setText("Choose a package to test:");

		SelectListener listener = new SelectListener();
		listener.page = this;

		ArrayList<String> packageNames = new TestSelection(testDirSelected).getTestPackageName();
		int length = packageNames.size();
		Button[] radioButtons = new Button[length];
		for (int i = 0; i < length; i++) {
			radioButtons[i] = new Button(container, SWT.RADIO);
			radioButtons[i].setText(packageNames.get(i));
			radioButtons[i].addSelectionListener(listener);
			radioButtons[i].pack();
		}

		setControl(container); // Required to avoid an error in the system
		setPageComplete(false);

	}

	class SelectListener implements SelectionListener  {

		ConfigWizardPageTwo page;

		public void handleEvent(SelectionEvent arg0) {
			Button button = (Button) arg0.widget;
			if (button.getSelection()) {
				packageSelected = button.getText();
				setPageComplete(true);

			}
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			handleEvent(arg0);
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			handleEvent(arg0);
		}
	}

	@Override
	public IWizardPage getNextPage() {
		nextPage = new ConfigWizardPageThree(packageSelected ,testDirSelected );
		wizard.addPage(nextPage);
		return this.nextPage;
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		return null;
	}

}
