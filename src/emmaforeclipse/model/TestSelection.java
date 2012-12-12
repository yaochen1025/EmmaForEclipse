package emmaforeclipse.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class TestSelection {
	private static String DEFAULT_PACKAGE_NAME =  "(Default) - not supported by adb";
	private Hashtable<String, ArrayList<String>> testPackageMap;
	private ArrayList<String> testClassList;


	public ArrayList<String> getTestPackageName() {
		ArrayList<String> testPackageNameList = new ArrayList<String>();
		Iterator<String> iter = testPackageMap.keySet().iterator();
		while (iter.hasNext()) {
			String str = iter.next();
			if (str.length() == 0) {
				str = DEFAULT_PACKAGE_NAME;
			}
			testPackageNameList.add(str);
		}
		return testPackageNameList;
	}

	public ArrayList<String> getTestClasses(String packageName) {
		String key = packageName;
		if (key == null) {
			return new ArrayList<String>();
		} else if (key.equals(DEFAULT_PACKAGE_NAME)) {
			key = "";
		}
		if (testPackageMap.containsKey(key)) {
			return testPackageMap.get(key);
		} else {
			return new ArrayList<String>();
		}
	}

//	public ArrayList<String> getTestClassList() {
//		return testClassList;
//	}

	public TestSelection(String testProjectPath) {
		setUp(testProjectPath);
	}

	public void setPath(String testProjectPath) {
		setUp(testProjectPath);
	}

	private void setUp(String testProjectPath) {
		testPackageMap = new Hashtable<String, ArrayList<String>>();
		testClassList = new ArrayList<String>();
		if (testProjectPath != null) {
			init(testProjectPath);
		}
	}

	private void init(String testProjectPath) {
		// Read test file list of the path
		ArrayList<String> testFileList = getTestFilePath(testProjectPath);

		// Construct testPackageMap by test file list
		for (String testFile : testFileList) {
			String testPackageName = getPackageName(testFile);
			String testClassName = getClassName(testFile);
			if (testPackageMap.containsKey(testPackageName)) {
				testPackageMap.get(testPackageName).add(testClassName);
			} else {
				ArrayList<String> newClass = new ArrayList<String>();
				newClass.add(testClassName);
				testPackageMap.put(testPackageName, newClass);
			}
		}

		// Construct testClassList by testPackageMap
		for (Map.Entry<String, ArrayList<String>> entry : testPackageMap.entrySet()) {
			for (String className : entry.getValue()) {
				if (entry.getKey() == "") {
					testClassList.add(className);
				} else {
					testClassList.add(entry.getKey() + "." + className);
				}
			}
		}
	}

	private ArrayList<String> getTestFilePath(String testProjectPath) {
		// Assume that source codes are in src directory
		String testSrcPath = testProjectPath + "src/";
		ArrayList<String> fileList = getAbsoluteTestFilePath(testSrcPath);
		for (int i = 0; i < fileList.size(); i++) {
			fileList.set(i, fileList.get(i).substring(testSrcPath.length()));
		}
		return fileList;
	}

	private ArrayList<String> getAbsoluteTestFilePath(String path) {
		File root = new File(path);
		ArrayList<String> fileList = new ArrayList<String>();
		if (!root.exists()) {
		    return fileList;
		}
		File[] list = root.listFiles();
		for (File file : list) {
			if (file.isDirectory()) {
				ArrayList<String> subFileList = getAbsoluteTestFilePath(file.getAbsolutePath());
				for (String subFile : subFileList) {
					fileList.add(subFile);
				}
			} else if (file.isFile()) {
				String fileName = file.getAbsolutePath();
				if (fileName.endsWith(".java")) {
					fileList.add(fileName);
				}
			}
		}
		return fileList;
	}

	private String getPackageName(String testFileName) {
		int lastSlash = testFileName.lastIndexOf('/');
		// check if the java file is using default package
		if (lastSlash < 0) {
			return "";
		}
		return testFileName.substring(0, lastSlash).replace('/', '.');
	}

	private String getClassName(String testFileName) {
		int lastSlash = testFileName.lastIndexOf('/');
		// check if the java file is using default package
		if (lastSlash > 0) {
			testFileName = testFileName.substring(lastSlash + 1);
		}
		int lastDot = testFileName.lastIndexOf('.');
		if (lastDot < 0) {
			return null;
		}
		return testFileName.substring(0, lastDot);
	}




	//    // SKIP FOLLOWING CODE!!
	//    public static void main(String[] args) {
	//        test();
	//    }
	//    
	//    public static void test() {
	//        String path = "/home/ccfish/workspace/TestAndroidHelloWorld/";
	//        TestSelection testSelection = new TestSelection(path);
	//        ArrayList<String> result = testSelection.getTestClassList();
	//        for (String str : result) {
	//            System.out.println(str);
	//        }
	//        System.out.println();
	//        for (String str : testSelection.getTestPackageName()) {
	//            System.out.println(str);
	//        }
	//        System.out.println();
	//        for (String str : testSelection.getTestClasses("(Default)")) {
	//            System.out.println(str);
	//        }
	//        System.out.println();
	//        if (testSelection.getTestClasses("") == null) {
	//            System.out.println("YES!");
	//        }
	//        System.out.println();
	//        for (String str : testSelection.getTestClasses("com.example.androidhelloworld.test")) {
	//            System.out.println(str);
	//        }
	//    }
}
