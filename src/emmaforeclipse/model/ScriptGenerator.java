package emmaforeclipse.model;

public abstract class ScriptGenerator {
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
        this.emmaPath = emmaPath.trim();
        this.androidDir = androidDir.trim();
        this.antDir = antDir.trim();
        this.javaHomeDir = javaHomeDir.trim();
    }
    
    public abstract void createShellScriptFile();
    public abstract String generateShellScriptString(); 
}
