package emmaforeclipse.actions;

import java.util.ArrayList;

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

	protected ConfigWizardPageThree(String selectedPackage, String testDirSelected) {
		super("Class");
		setTitle("Class - using adb");
		setDescription("choose classes to run");
		this.selectedPackage = selectedPackage;
		this.testDirSelected = testDirSelected;
		
	}

	private String selectedPackage;
	private String testDirSelected;

	public Composite container = null;

	@Override
	public void createControl(Composite parent) {
		
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
		

			Button[] checkBoxs = new Button[length];
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
			System.out.println("I checked " + checkedBox.getText());
			setPageComplete(true);
		
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


}
