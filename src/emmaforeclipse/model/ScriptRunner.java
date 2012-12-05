package emmaforeclipse.model;

import java.io.*;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ScriptRunner extends Job implements Runnable{
	private Display display;
	private String testDir;
	private String outputFile;

	public ScriptRunner(String testDir, Display display, String outputFile) {
		super("run script");
		this.display = display;
		this.testDir = testDir;
		this.outputFile = outputFile;
	}
	
	@Override
	public void run() {
		 
		Runtime runtime = Runtime.getRuntime() ;
		Process pr;
		try {
			pr = runtime.exec("sh " + testDir.trim() + "src/temp.sh");

			
			pr.waitFor();
			InputStream inputStream = pr.getInputStream();
			writeFile(inputStream);
			inputStream.close();

		} catch (IOException e){
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	
		Shell s = new Shell(display);
		s.setLayout(new FillLayout());
		Browser browser = new Browser(s, SWT.None);
		browser.setUrl(testDir.trim() + "bin/coverage.html");
		s.open();

	}
	
	private void writeFile(InputStream inputStream) throws FileNotFoundException, IOException{
	    OutputStream outputStream = new FileOutputStream(new File(outputFile));
	    byte buf[] = new byte[1024];
	    int len;
	    while ((len = inputStream.read(buf)) > 0) {
	        outputStream.write(buf, 0, len);
	    }
	    outputStream.close();
	}

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		run();
		return Status.OK_STATUS;
	}

}
