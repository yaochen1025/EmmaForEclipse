package emmaforeclipse.model;

import java.io.File;
import java.util.Date;

public abstract class ScriptGenerator {
    
    public static String scriptSaved;
    public static String testDirSaved;
    
	protected String projectDir;
    protected String testDir;
    protected String emmaPath;
    protected String androidDir;
    protected String antDir;
    protected String javaHomeDir;
    
    public ScriptGenerator(String projectDir, String testDir, String emmaPath,
            String androidDir, String antDir, String javaHomeDir) {
        this.projectDir = projectDir.trim();
        this.testDir = testDir.trim();
        testDirSaved = this.testDir;
        this.emmaPath = emmaPath.trim();
        this.androidDir = androidDir.trim();
        this.antDir = antDir.trim();
        this.javaHomeDir = javaHomeDir.trim();
    }
    
//    protected String createFolder() {
//        String reportFolderDir = this.testDir + "report/";
//        File reportFolder = new File(reportFolderDir);  
//        if (!reportFolder.exists()) {
//            reportFolder.mkdir();
//        }
//        String runNumber = new Date().toString();
//        String replaced = runNumber.replaceAll(" ", "_");
//        String dest = reportFolderDir + replaced;
//        HtmlPlacer.runNumber = replaced;
//        File coverageFolder = new File(dest);
//        coverageFolder.mkdir();
//        System.out.println("afwaeew\t" + dest);
//        return dest;
//    }
    
    public abstract void createShellScriptFile();
    public abstract String generateShellScriptString(); 
    public  abstract void setConfig();
}
