package emmaforeclipse.views;


import java.io.IOException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class PluginView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "emmaforeclipse.views.PluginView";

	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;

	private Text projectDir;
	private Text testDir;
	private Button projectDirSelectButton;
	private Button testDirSelectButton;

	private Text command;
	private Button runButton;

	Composite parent;
	

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		parent.setLayout(layout);

		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("Project Directory");
		projectDir = new Text(parent, SWT.BORDER);
		projectDirSelectButton = new Button(parent, SWT.PUSH);
		projectDirSelectButton.setText("Choose");
		projectDir.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		projectDirSelectButton.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false));
		addFileChooserListener(projectDirSelectButton, projectDir);

		new Label(parent, SWT.NONE).setText("Test Directory");
		testDir = new Text(parent, SWT.BORDER);
		testDirSelectButton = new Button(parent, SWT.PUSH);
		testDirSelectButton.setText("Choose");
		testDir.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		testDirSelectButton.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false)); 
		addFileChooserListener(testDirSelectButton, testDir);
		
		
		new Label(parent, SWT.NONE).setText("Command");
		command = new Text(parent, SWT.BORDER);
		runButton = new Button(parent, SWT.PUSH);
		runButton.setText("run");
		command.setText("open -t /Users/glcylily/Documents/workspace/EmmaForEclipse/plugin.xml");
		command.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
		runButton.setLayoutData(new GridData(GridData.END, SWT.BEGINNING, true, false)); 
		
		runButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				String cmd = command.getText();
				System.out.println(cmd);
				Runtime runtime = Runtime.getRuntime() ;
				try {
					Process pr = runtime.exec(cmd) ;
					pr.waitFor() ;
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		
	}

	private void addFileChooserListener(Button button, Text text) {
		
		FileSelectListener l = new FileSelectListener();
		l.text = text;
		button.addListener(SWT.Selection, l);
	}
//	
	class FileSelectListener implements Listener{
		Text text;
		@Override
		public void handleEvent(Event arg0) {			
			DirectoryDialog directoryDialog = new DirectoryDialog(parent.getShell());
			directoryDialog.setFilterPath("/Users/glcylily");
			directoryDialog.setMessage("Please select a directory and click OK");

			String dir = directoryDialog.open();
			if(dir != null) {
				text.setText(dir);
			}			
		}	
	}
	
	

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */

	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new String[] { "One", "Two", "Three" };
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public PluginView() {
	}



	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				PluginView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
				viewer.getControl().getShell(),
				"Sample View",
				message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//viewer.getControl().setFocus();
	}
}