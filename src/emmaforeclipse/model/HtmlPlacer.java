package emmaforeclipse.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;

import emmaforeclipse.actions.ConfigWizardPageOne;


public class HtmlPlacer {

	public static void main(String[] args){
//
//		Properties prop = new Properties();
//
//		try {
//			FileInputStream x = new FileInputStream("x");
//			prop.load(x);
//			x.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		prop.setProperty("one", "1");
//		prop.setProperty("two", "2");
//
//		try {
//			FileOutputStream f = new FileOutputStream(new File("config.properties"));
//			prop.store(f, "");
//			f.flush();
//			f.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}




		//    		String s = "/home/ccfish/workspace/TestAndroidHelloWorld/";
		//    		HtmlPlacer.runNumber = "Wed-Dec-12-03:46:16-EST-2012";
		//    		HtmlPlacer h = new HtmlPlacer(s);
		//    		boolean b = h.finishUp();
		//    		System.out.println(b);
		//    		if (b) {
		//    			h.updateIndexHtml();
		//    		}
	}

	private static final String header = "<HTML><HEAD><META CONTENT=\"text/html; charset=UTF-8\" HTTP-EQUIV=\"Content-Type\"/><TITLE>EMMA Coverage Report (generated Wed Dec 05 11:56:48 EST 2012)</TITLE><STYLE TYPE=\"text/css\"> TABLE,TD,TH {border-style:solid; border-color:black;} TD,TH {background:white;margin:0;line-height:100%;padding-left:0.5em;padding-right:0.5em;} TD {border-width:0 1px 0 0;} TH {border-width:1px 1px 1px 0;} TR TD.h {color:red;} TABLE {border-spacing:0; border-collapse:collapse;border-width:0 0 1px 1px;} P,H1,H2,H3,TH {font-family:verdana,arial,sans-serif;font-size:10pt;} TD {font-family:courier,monospace;font-size:10pt;} TABLE.hdft {border-spacing:0;border-collapse:collapse;border-style:none;} TABLE.hdft TH,TABLE.hdft TD {border-style:none;line-height:normal;} TABLE.hdft TH.tl,TABLE.hdft TD.tl {background:#6699CC;color:white;} TABLE.hdft TD.nv {background:#6633DD;color:white;} .nv A:link {color:white;} .nv A:visited {color:white;} .nv A:active {color:yellow;} TABLE.hdft A:link {color:white;} TABLE.hdft A:visited {color:white;} TABLE.hdft A:active {color:yellow;} .in {color:#356085;} TABLE.s TD {padding-left:0.25em;padding-right:0.25em;} TABLE.s TD.l {padding-left:0.25em;padding-right:0.25em;text-align:right;background:#F0F0F0;} TABLE.s TR.z TD {background:#FF9999;} TABLE.s TR.p TD {background:#FFFF88;} TABLE.s TR.c TD {background:#CCFFCC;} A:link {color:#0000EE;text-decoration:none;} A:visited {color:#0000EE;text-decoration:none;} A:hover {color:#0000EE;text-decoration:underline;} TABLE.cn {border-width:0 0 1px 0;} TABLE.s {border-width:1px 0 1px 1px;} TD.h {color:red;border-width:0 1px 0 0;} TD.f {border-width:0 1px 0 1px;} TD.hf {color:red;border-width:0 1px 0 1px;} TH.f {border-width:1px 1px 1px 1px;} TR.cis TD {background:#F0F0F0;} TR.cis TD {border-width:1px 1px 1px 0;} TR.cis TD.h {color:red;border-width:1px 1px 1px 0;} TR.cis TD.f {border-width:1px 1px 1px 1px;} TR.cis TD.hf {color:red;border-width:1px 1px 1px 1px;} TD.b {border-style:none;background:transparent;line-height:50%;}  TD.bt {border-width:1px 0 0 0;background:transparent;line-height:50%;} TR.o TD {background:#F0F0F0;}TABLE.it {border-style:none;}TABLE.it TD,TABLE.it TH {border-style:none;}</STYLE></HEAD><BODY>";

	public void doSomething() {
		if (finishUp()) {
			updateIndexHtml();
		}
	}

	public void updateIndexHtml() {

		System.out.println("xxxxxxxxxxx" + testProjectDir);

		String indexFileDir = testProjectDir + "report/index.html";
		System.out.println(indexFileDir);

		String coverageFile;

		if (!ConfigWizardPageOne.isAdb) {
			coverageFile = testProjectDir + "report/" + runNumber +"/coverage.html";
		} else {
			coverageFile = testProjectDir + "report/" + runNumber +"/index.html";
		}
		System.out.println(coverageFile);

		try {

			File cFile = new File(coverageFile);
			if (!cFile.exists()) return;
			String newCoverageFileString = readFile(coverageFile);


			File indexFile = new File(indexFileDir); 

			if (!indexFile.exists()) {
				indexFile.createNewFile();
				PrintWriter pw = new PrintWriter(indexFile);
				String temp = changePathForHref(newCoverageFileString);
				pw.write(temp);
				pw.flush();
				pw.close();

			} else {

				String indexFileString = readFile(indexFile);
				int start = header.length();
				int end = newCoverageFileString.indexOf("</BODY></HTML>");
				PrintWriter pw = new PrintWriter(indexFile);
				if (start < end ) {
					System.out.println(start + "lllllllllllllllllll " + end);
					String coverageStatsNeeded = newCoverageFileString.substring(start, end);
					pw.write(header);
					String temp = changePathForHref(coverageStatsNeeded);
					pw.write(temp);
					pw.write("<div style='height:20px;'></div>");
					pw.write(indexFileString.substring(start));
				}
				pw.flush();
				pw.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String changePathForHref(String s) {
		if (s == null) return "";
		//		System.out.println(s);
		String s1 = s.replaceAll("HREF=\"_files/", "HREF=\""+ this.runNumber +"/_files/");
		//		System.out.println(s1);
		String s2 = s1.replaceAll("HREF=\"coverage.html", "HREF=\"" + this.runNumber + "/coverage.html");
		return s2;
	}

	private String readFile(String fileDir) {
		File file = new File(fileDir);
		return readFile(file);
	}

	private String readFile(File file) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			int read = 0;
			while (true) {
				read = br.read();
				if (read == -1) break;
				else sb.append( (char) read);
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println(sb.toString());
		return sb.toString();
	}


	private String testProjectDir;
	private String runNumber;

	public HtmlPlacer(String testProjectDirectory1) {
		if (testProjectDirectory1.endsWith("/")) {
			this.testProjectDir = testProjectDirectory1;
		} else {
			this.testProjectDir = testProjectDirectory1 + "/";
		}
	}

	public boolean finishUp() {
		if (validatePath()) {
			return move();
		}
		return false;
	}

	private boolean validatePath(){
		String reportFolderDir = testProjectDir+"report/";
		File reportFolder = new File(reportFolderDir);  
		if (!reportFolder.exists()) {
			if (!reportFolder.mkdir()) {
				return false;
			}
		}
		setInfo();
		File coverageFolder = new File(reportFolderDir + this.runNumber);
		return coverageFolder.mkdir();
	}

	private boolean move() {
		if (ConfigWizardPageOne.isAdb) {
			boolean b0 = moveFile("index.html");
			boolean b2 = moveFile("_files");
			return b0 && b2;
		} 
		boolean b1 = moveFile("coverage.html");
		boolean b2 = moveFile("_files");
		//optional move
		moveFile("coverage.xml");
		moveFile("coverage.txt");
		return b1 && b2; 
	}

	private boolean moveFile(String sourceFile) {
		File src;
		if (ConfigWizardPageOne.isAdb) {
			src = new File(testProjectDir + "coverage/" + sourceFile);
		} else {
			src  = new File(testProjectDir + "bin/" + sourceFile); 
		}
		File dest = new File(testProjectDir + "report/" + this.runNumber + "/" + sourceFile);
		if (src == null || !src.exists()) return false;
		return src.renameTo(dest);
	}

	//should be override, info should be extracted from html generated
	private void setInfo() {
		runNumber = new Date().toString().replaceAll(" ", "_");
	}








}
