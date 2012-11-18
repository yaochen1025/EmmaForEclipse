package emma.views;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ScriptRunner extends Thread{
	private Display display;
	private String testDir;

	public ScriptRunner(String testDir, Display display) {
		this.display = display;
		this.testDir = testDir;	
	}
	
	@Override
	public void run() {
		Runtime runtime = Runtime.getRuntime() ;
		Process pr;
		try {
			pr = runtime.exec("sh " + testDir.trim() + "src/temp.sh");
			pr.waitFor() ;	
		} catch (IOException e){
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Shell s = new Shell(display);
		s.setLayout(new FillLayout());
		Browser browser = new Browser(s, SWT.None);
		browser.setUrl(testDir + "bin/coverage.html");
		s.open();
	}
}
