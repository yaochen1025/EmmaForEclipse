package emmaforeclipse.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.wizard.Wizard;
import emmaforeclipse.model.*;

public class ConfigWizard extends Wizard {

	protected WizardPageWelcome zero;
	protected ConfigWizardPageOne one;
	protected ConfigWizardPageTwo two;
	protected ConfigWizardPageThree three;

	public ConfigWizard() {
		super();
		setWindowTitle("Emma For Eclipse"); 
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		zero = new WizardPageWelcome();
		one = new ConfigWizardPageOne();
	//	two = new ConfigWizardPageTwo();
		addPage(zero);
		addPage(one);
	//	addPage(two);
	}
	
	@Override
	public boolean performFinish() {
		String projectDir = one.getProjectDir();
		String testDir = one.getTestDir();
		String emmaPath = one.getEmmaPath();
		String androidDir = one.getAndroidPath();

		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		prop.setProperty("PROJECT", projectDir);
		prop.setProperty("TESTPROJECT", testDir);
		prop.setProperty("EMMA", emmaPath);
		prop.setProperty("ANDROID_SDK", androidDir);

		try {
			FileOutputStream f = new FileOutputStream("config.properties");
			prop.store(f, "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ScriptGenerator scriptGenerator = new ScriptGeneratorAnt(projectDir, testDir, emmaPath, androidDir,
				"", "");
		scriptGenerator.setConfig();
		return true;
	}

}
