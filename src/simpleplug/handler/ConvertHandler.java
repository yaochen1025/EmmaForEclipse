package simpleplug.handler;

import org.eclipse.core.internal.resources.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class ConvertHandler extends AbstractHandler {
  private QualifiedName path = new QualifiedName("html", "path");

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Shell shell = HandlerUtil.getActiveShell(event);
    ISelection sel = HandlerUtil.getActiveMenuSelection(event);
    IStructuredSelection selection = (IStructuredSelection) sel;
    Object firstElement = selection.getFirstElement();
    if (firstElement instanceof org.eclipse.core.internal.resources.File) {
    	firstElement = (org.eclipse.core.internal.resources.File) firstElement;
    	try {
			write(((org.eclipse.core.internal.resources.File) firstElement).getContents());
		} catch (CoreException e) {
	
			e.printStackTrace();
		}
    } else {
      MessageDialog.openInformation(shell, "Info",
          "Please select a Java source file");
    }
   
    return null;
  }


  private void write(InputStream cu) {
    try {
     
      // Need
      String htmlFile = "/Users/glcylily/Documents/runtime-EclipseApplication/Test/test.html";
      FileWriter output = new FileWriter(htmlFile);
      BufferedWriter writer = new BufferedWriter(output);
      writer.write("<html>");
      writer.write("<head>");
      writer.write("</head>");
      writer.write("<body>");
      writer.write("<pre>");
      int i;
      while ((i = cu.read()) != -1) {
    	  writer.write((char) i);
      }
      writer.write("</pre>");
      writer.write("</body>");
      writer.write("</html>");
      writer.flush();
    }  catch (IOException e) {
      e.printStackTrace();
    }

  }
} 