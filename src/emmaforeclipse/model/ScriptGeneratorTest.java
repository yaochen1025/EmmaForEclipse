package emmaforeclipse.model;

import java.util.ArrayList;

public class ScriptGeneratorTest {
    public static void main(String[] args) {
        ScriptGeneratorTest sgt = new ScriptGeneratorTest();
        sgt.testAdb();
    }
    
    public void testAdb() {
        String projectDir = "/home/ccfish/workspace/AndroidHelloWorld/";
        String testDir = "/home/ccfish/workspace/TestAndroidHelloWorld/";
        String emmaPath = "/home/ccfish/android-sdk-linux/tools/lib/emma.jar";
        String androidDir = "/home/ccfish/android-sdk-linux/";
        String antDir = "";
        String javaHomeDir = "/home/ccfish/workspace/";
        String projectPackage = "com.example.androidhelloworld";
        String testPackage = "com.example.androidhelloworld.test";

        // running all test classes in the test package
//        ScriptGenerator sg = new ScriptGeneratorAdb(projectDir, testDir, emmaPath, androidDir, 
//                antDir, javaHomeDir, null, projectPackage, testPackage, null);
//        sg.createShellScriptFile();
        
        // running specific class/method
        ArrayList<String> testClasses = new ArrayList<String>();
        testClasses.add("Testa");
        testClasses.add("Testb");
        testClasses.add("Testc");
        ScriptGenerator sg = new ScriptGeneratorAdb(projectDir, testDir, emmaPath, androidDir, 
                antDir, javaHomeDir, projectPackage, testPackage, testClasses);
        sg.createShellScriptFile();
    }
    public void testAnt() {
        String projectDir = "/home/ccfish/workspace/AndroidHelloWorld/";
        String testDir = "/home/ccfish/workspace/TestAndroidHelloWorld/";
        String emmaPath = "/home/ccfish/android-sdk-linux/tools/lib/emma.jar";
        String androidDir = "/home/ccfish/android-sdk-linux/";
        String antDir = "";
        String javaHomeDir = "/home/ccfish/workspace/";
        ScriptGenerator sg = new ScriptGeneratorAnt(projectDir, testDir, emmaPath, androidDir, 
                antDir, javaHomeDir);
        sg.createShellScriptFile();
    }
    
}
