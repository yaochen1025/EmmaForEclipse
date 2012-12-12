package emmaforeclipse.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ScriptGeneratorAdb extends ScriptGenerator{
    private String projectPackageName;
    private String testPackageName;
    private String testRunnerName;
    private ArrayList<String> testClassNameList;
    
    // run all classes within the test package with default test runner
    public ScriptGeneratorAdb(String projectDir, String testDir, String emmaPath,
            String androidDir, String antDir, String javaHomeDir, String projectPackageName,
            String testPackageName) {
        super(projectDir, testDir, emmaPath, androidDir, antDir, javaHomeDir);
        this.projectPackageName = projectPackageName.trim();
        this.testPackageName = testPackageName.trim();
        this.testRunnerName = "android.test.InstrumentationTestRunner";
        this.testClassNameList = null;
    }
    
    // run all classes within the test package with user-defined test runner
    public ScriptGeneratorAdb(String projectDir, String testDir, String emmaPath,
            String androidDir, String antDir, String javaHomeDir, String projectPackageName,
            String testPackageName, String testRunnerName) {
        this(projectDir, testDir, emmaPath, androidDir, antDir, javaHomeDir, projectPackageName,
                testPackageName);
        this.testRunnerName = testRunnerName.trim();
    }
    
    // run user-defined classes within the test package with default test runner
    public ScriptGeneratorAdb(String projectDir, String testDir, String emmaPath,
            String androidDir, String antDir, String javaHomeDir, String projectPackageName,
            String testPackageName, ArrayList<String> testClassNameList) {
        this(projectDir, testDir, emmaPath, androidDir, antDir, javaHomeDir, projectPackageName,
                testPackageName);
        this.testClassNameList = testClassNameList;
    }
    
    // run user-defined classes within the test package with user-defined test runner
    public ScriptGeneratorAdb(String projectDir, String testDir, String emmaPath,
            String androidDir, String antDir, String javaHomeDir, String projectPackageName,
            String testPackageName, String testRunnerName, ArrayList<String> testClassNameList) {
        this(projectDir, testDir, emmaPath, androidDir, antDir, javaHomeDir, projectPackageName,
                testPackageName);
        this.testRunnerName = testRunnerName.trim();
        this.testClassNameList = testClassNameList;
    }
    
    @Override
    public void createShellScriptFile() {
        String shellScript = generateShellScriptString();
        try {
            File file = new File(testDir + "src/temp.sh");
            if (!file.exists()) file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(shellScript);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println("File IO error");
        }
    }

    @Override
    public String generateShellScriptString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#!/bin/sh\n");
        sb.append("adb remount\n");
        sb.append("adb push " + emmaPath + " /system/framework\n");
        sb.append("cd " + testDir + "\n");
        /// install apk
        String testDirName = testDir;
        if (testDirName.endsWith("/")) {
            testDirName = testDirName.substring(0, testDirName.length() - 1);
        }
        int index = testDirName.lastIndexOf('/');
        testDirName = testDirName.substring(index + 1);
        sb.append("adb install -r bin/" + testDirName + ".apk\n");
        String projectDirName = projectDir;
        if (projectDirName.endsWith("/")) {
            projectDirName = projectDirName.substring(0, projectDirName.length() - 1);
        }
        int index2 = projectDirName.lastIndexOf('/');
        projectDirName = projectDirName.substring(index2 + 1);
        sb.append("adb install -r ../" + projectDirName + "/bin/" + projectDirName + ".apk\n");
        
        sb.append("java -cp " + emmaPath + " emma instr -m overwrite -ip " + projectDir 
                + "bin/\n");
        sb.append("adb shell am instrument -w -e coverage true ");
        if (testClassNameList != null && testClassNameList.size() != 0) {
            sb.append("-e class ");
            for (String className : testClassNameList) {
                sb.append(testPackageName + "." + className + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(' ');
        }
        sb.append(testPackageName + "/" + testRunnerName + "\n");

        sb.append("adb pull /data/data/" + projectPackageName + "/files/coverage.ec .\n");
        sb.append("java -cp " + emmaPath + " emma report -r html -in coverage.ec,coverage.em -sp " + projectDir + "src/\n");
        
        String dest = createFolder();
        sb.append("mv coverage/index.html " + dest + "\n");
        sb.append("cp -r coverage/_files " + dest + "\n");

        
        return sb.toString();
    }

	@Override
	public void setConfig() {
	      Properties prop = new Properties();

	        try {
	            //load a properties file
//	          File file = new File("run.properties");
//	          if(!file.exists()) file.createNewFile();
	            prop.load(new FileInputStream("run.properties"));
	            String script = this.generateShellScriptString();
	            prop.setProperty("NEXTRUN", script);
	            System.out.println(script);
	            prop.setProperty("SCRIPTCONTAINER", this.testDir);
	            FileOutputStream f = new FileOutputStream("run.properties");
	            prop.store(f, "");
	            
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		
	}
}
