package emmaforeclipse.actions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.jface.wizard.Wizard;
import emmaforeclipse.model.*;

public class ConfigWizard extends Wizard {

	protected WizardPageWelcome zero;
	protected ConfigWizardPageOne one;
	protected ConfigWizardPageTwo two;
	protected ConfigWizardPageThree three;
	public static int runType = 0;

	public ConfigWizard() {
		super();
		setWindowTitle("Emma For Eclipse"); 
		setNeedsProgressMonitor(true);
	}

	public WizardPageWelcome getPageZero() {
		return this.zero;
	}
	@Override
	public void addPages() {
		zero = new WizardPageWelcome();
		one = new ConfigWizardPageOne();
		addPage(zero);
		addPage(one);
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
		
		if (runType == 1 && !ConfigWizardPageOne.isAdb) {
			ScriptGenerator scriptGenerator = new ScriptGeneratorAnt(projectDir, testDir, emmaPath, androidDir,
					"", "");
			scriptGenerator.setConfig();
			return true;
		} 


		if (runType == 1 && ConfigWizardPageOne.isAdb) {	
			System.out.println("adb run all");
			return true;
		}
		
		String packageSelected = ConfigWizardPageTwo.packageSelected;
		
		if (runType == 2 ) {	
			System.out.println( packageSelected + " adb run Package");
			return true;
		}
		
		if (runType == 3 ) {	
			ArrayList<String> testsSelected = ConfigWizardPageThree.getSelectedTests();
			System.out.println( packageSelected + " adb run test classes");
			for (String test : testsSelected) {
				System.out.println(test);
			}
		
			return true;
		}
		
		return true;

	}

}
