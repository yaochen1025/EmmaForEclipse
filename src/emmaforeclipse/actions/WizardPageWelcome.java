package emmaforeclipse.actions;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class WizardPageWelcome extends WizardPage {

	protected WizardPageWelcome() {
		super("Welcome");
		setTitle("Welcome to Emma For Eclipse!");
		setDescription("First time to launch.");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);

		Label info = new Label(container, SWT.NULL);
		info.setText("By now you should have your android installed.\n" +
				"Emma.jar should be packaged in the tool/libs in your android directory.\n" +
				"Also, you need either ant or adb ready to run the test.\n" +
				"Make sure you have opened an emulator.\n" +
				"Wish you a good experience using Emma For Eclipse.\n\n\n" +
				"Click next to continue.");

		// Required to avoid an error in the system
		setControl(container);
		this.setPageComplete(false);
		
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return true;
	}
}
