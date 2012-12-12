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


public class ConfigWizardPageThree extends WizardPage {

	
	int numOfCheckedBox = 0;
	protected ConfigWizardPageThree(String selectedPackage, String testDirSelected) {
		super("Class");
		setTitle("Class - using adb");
		setDescription("choose classes to run");
		this.selectedPackage = selectedPackage;
		this.testDirSelected = testDirSelected;
		
	}

	private String selectedPackage;
	private String testDirSelected;
	private static Button[] checkBoxs;

	public Composite container = null;

	@Override
	public void createControl(Composite parent) {
		ConfigWizard.runType = 3;
		container = new Composite(parent, SWT.DOUBLE_BUFFERED);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
	
		layout.numColumns = 1;
		update();
	}

	public void update() {
		if (container != null) {

			Label info = new Label(container, SWT.NULL);
			info.setText("Choose classes to test:");

			ArrayList<String> classNames = new TestSelection(this.testDirSelected).getTestClasses(this.selectedPackage);
			int length = classNames.size();
		

			checkBoxs = new Button[length];
			for (int i = 0; i < length; i++) {
				checkBoxs[i] = new Button(container, SWT.CHECK);
				checkBoxs[i].setText(classNames.get(i));
				checkListener cl = new checkListener ();
				cl.checkedBox = checkBoxs[i];
				checkBoxs[i].addSelectionListener(cl);
				checkBoxs[i].pack();
			}
			setControl(container); // Required to avoid an error in the system
			setPageComplete(false);	
		}
	}


	class checkListener implements SelectionListener  {

		Button checkedBox;

		public void handleEvent(SelectionEvent arg0) {
			boolean isSelected = checkedBox.getSelection();
			if (isSelected) {
				numOfCheckedBox ++;
			} else {
				numOfCheckedBox --;
			}
			if (numOfCheckedBox > 0) setPageComplete(true);
			else setPageComplete(false);
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
	public IWizardPage getPreviousPage() {
		return null;
	}

	public static ArrayList<String> getSelectedTests() {
		ArrayList<String> testsSelected = new ArrayList<String>();
		for (Button button : checkBoxs) {
			if (button.getSelection())  testsSelected.add(button.getText());
		}
		return testsSelected;
	}

}
