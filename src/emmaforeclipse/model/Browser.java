package emmaforeclipse.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


public class Browser {
	
	private org.eclipse.swt.browser.Browser browser;
	private Shell shell;

	public Browser(Shell shell) {
		setShellForBrowser(shell);
		setUpBrowserContent();
	}

	private void setShellForBrowser(Shell shell) {
		this.shell = shell;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		this.shell.setLayout(gridLayout);
		this.shell.setSize(1024, 768);
	}
	
	public void setUpBrowserContent(){
		
		ToolBar toolbar = new ToolBar(shell, SWT.NONE);
		ToolItem itemBack = new ToolItem(toolbar, SWT.PUSH);
		itemBack.setText("Back");
		ToolItem itemForward = new ToolItem(toolbar, SWT.PUSH);
		itemForward.setText("Forward");
		ToolItem itemStop = new ToolItem(toolbar, SWT.PUSH);
		itemStop.setText("Stop");
		ToolItem itemRefresh = new ToolItem(toolbar, SWT.PUSH);
		itemRefresh.setText("Refresh");
		ToolItem itemGo = new ToolItem(toolbar, SWT.PUSH);
		itemGo.setText("Go");

		GridData data = new GridData();
		data.horizontalSpan = 1;
		toolbar.setLayoutData(data);

		final Text location = new Text(shell, SWT.BORDER);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		data.grabExcessHorizontalSpace = true;
		location.setLayoutData(data);

		try {
			browser = new org.eclipse.swt.browser.Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			shell.getDisplay().dispose();
			return;
		}
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.horizontalSpan = 4;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		browser.setLayoutData(data);


		/* event handling */
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				ToolItem item = (ToolItem)event.widget;
				String string = item.getText();
				if (string.equals("Back")) browser.back(); 
				else if (string.equals("Forward")) browser.forward();
				else if (string.equals("Stop")) browser.stop();
				else if (string.equals("Refresh")) browser.refresh();
				else if (string.equals("Go")) browser.setUrl(location.getText());
			}
		};

		itemBack.addListener(SWT.Selection, listener);
		itemForward.addListener(SWT.Selection, listener);
		itemStop.addListener(SWT.Selection, listener);
		itemRefresh.addListener(SWT.Selection, listener);
		itemGo.addListener(SWT.Selection, listener);
		location.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				browser.setUrl(location.getText());
			}
		});	
	}
	
	public void setUrl(String url) {
		browser.setUrl(url);
	}

}
