package emmaforeclipse.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ScriptGeneratorAdb extends ScriptGenerator{
    private String projectPackageName;
    private String testPackageName;
    private String testRunnerName;
    
    public ScriptGeneratorAdb(String projectDir, String testDir, String emmaPath,
            String androidDir, String antDir, String javaHomeDir, String projectPackageName,
            String testPackageName) {
        super(projectDir, testDir, emmaPath, androidDir, antDir, javaHomeDir);
        this.projectPackageName = projectPackageName.trim();
        this.testPackageName = testPackageName.trim();
        this.testRunnerName = "android.test.InstrumentationTestRunner";
    }
    
    public ScriptGeneratorAdb(String projectDir, String testDir, String emmaPath,
            String androidDir, String antDir, String javaHomeDir, String projectPackageName,
            String testPackageName, String testRunnerName) {
        this(projectDir, testDir, emmaPath, androidDir, antDir, javaHomeDir, projectPackageName,
                testPackageName);
        this.testRunnerName = testRunnerName.trim();
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
        /// install apk???
        sb.append("java -cp " + emmaPath + " emma instr -m overwrite -ip " + projectDir 
                + "bin/\n");
        sb.append("adb shell am instrument -w -e coverage true " + testPackageName + "/" 
                + testRunnerName + "\n");
        sb.append("adb pull /data/data/" + projectPackageName + "/files/coverage.ec .\n");
        sb.append("java -cp " + emmaPath + " emma report -r html -in coverage.ec,coverage.em -sp " + projectDir + "src/\n");
        return sb.toString();
    }
}
