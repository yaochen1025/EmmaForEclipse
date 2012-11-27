package emmaforeclipse.actions;

import org.eclipse.jface.wizard.Wizard;
import emmaforeclipse.model.*;

public class ConfigWizard extends Wizard {

	 protected ConfigWizardPageOne one;
	 protected ConfigWizardPageTwo two;

	  public  ConfigWizard() {
	    super();
	    setNeedsProgressMonitor(true);
	  }

	  @Override
	  public void addPages() {
	    one = new ConfigWizardPageOne();
	    two = new ConfigWizardPageTwo();
	    addPage(one);
	    addPage(two);

	  }

	  @Override
	  public boolean performFinish() {
		  String projectDir = one.getProjectDir();
		  String testDir = one.getTestDir();
		  String emmaPath = one.getEmmaJar();
		  String androidDir = one.getAndroidPah();
          ScriptGenerator scriptGenerator = new ScriptGeneratorAnt(projectDir, testDir, emmaPath, androidDir,
                 "", "");
          String runScript = scriptGenerator.generateShellScriptString();
          System.out.println(runScript);
	    return true;
	  }

}
