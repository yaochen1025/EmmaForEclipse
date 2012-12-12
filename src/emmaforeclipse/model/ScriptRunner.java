package emmaforeclipse.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ScriptRunner extends Job implements Runnable {

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

		HtmlPlacer hp = new HtmlPlacer(testDir.trim());
		hp.updateIndexHtml();
		
		
//		final Shell shell = new Shell(display);		
//		Browser browser = new Browser(shell);
//		browser.setUrl(testDir.trim() + "report/index.html");
//		shell.open();

	}


	public IStatus run(IProgressMonitor monitor) {
		run();
		return Status.OK_STATUS;
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
}
