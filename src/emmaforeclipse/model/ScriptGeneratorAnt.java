package emmaforeclipse.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ScriptGeneratorAnt extends ScriptGenerator{
    public ScriptGeneratorAnt(String projectDir, String testDir, String emmaPath,
            String androidDir, String antDir, String javaHomeDir) {
        super(projectDir, testDir, emmaPath, androidDir, antDir, javaHomeDir);
    }
    
    @Override
    public void createShellScriptFile() {
        String shellScript = generateShellScriptString();
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
    
    @Override
    public String generateShellScriptString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#!/bin/sh\n");
        sb.append("export CLASSPATH=$CLASSPATH:" + emmaPath + "\n");
        sb.append("export PATH=$PATH:" + androidDir + "\n");
        sb.append("export PATH=$PATH:" + androidDir + "tools\n");
        sb.append("cd " + projectDir + "\n");
        sb.append("cd ..\n");
        //String projectName = projectDir.substring(projectDir.lastIndexOf("/") + 1);
        sb.append("android update project -p " + projectDir + "\n");
        sb.append("cd " + testDir + "\n");
        sb.append("android update test-project -m "+projectDir+" -p .\n");
        sb.append("ant clean emma debug install test");
        return sb.toString();
    }
}
