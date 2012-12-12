package emmaforeclipse.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.handlers.HandlerUtil;

import emmaforeclipse.model.ScriptRunner;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class RunAction extends AbstractHandler implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
	public RunAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	@Override
	public void run(IAction action) {
		performAction();
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		//
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	@Override
	public void dispose() {
		//
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	@Override
	public void init(IWorkbenchWindow iwindow) {
		this.window = iwindow;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow windowL = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		this.window = windowL;
		performAction();
		return null;
	}

	private void performAction() {
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream("run.properties"));
			String script = prop.getProperty("NEXTRUN");
			String testDir = prop.getProperty("SCRIPTCONTAINER");
			System.out.println(script + "\n" + testDir);
			if (script == null) {
				WizardDialog wizardDialog = new WizardDialog(window.getShell(), new  ConfigWizard());
				wizardDialog.open();
			}
			createShellScriptFile(script, testDir);
			//ScriptRunner scriptRunner = new ScriptRunner(testDir, window.getShell().getDisplay(), testDir + "src/scriptOutput.txt");
			//  scriptRunner.run();
			//            scriptRunner.setPriority(Job.SHORT);
			//            scriptRunner.setUser(true);
			//            scriptRunner.schedule(); // start as soon as possible

			//           Display.getDefault().asyncExec(scriptRunner);



			ScriptRunner job = new ScriptRunner(testDir, window.getShell().getDisplay(), testDir + "src/scriptLog.txt");
			job.setUser(true);
			//job.schedule(); // start as soon as possible
			job.run();
			

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public void createShellScriptFile(String shellScript, String testDir) {

		try {
			File file = new File(testDir + "src/temp.sh");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(shellScript);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}