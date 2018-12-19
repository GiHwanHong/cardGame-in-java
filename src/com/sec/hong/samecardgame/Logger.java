package com.sec.hong.samecardgame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static Logger instance;
	private String logFileName;
	private String ClassName;
	private long start;
	//File f = new File("game_samecard/");
	File f=null;
	FileWriter fw = null;
	Date today = new Date();
	SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

	public Logger() {
		try {
			isDir();
			fw = new FileWriter(f, true);
			start = System.currentTimeMillis();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Logger(String logfile) {
		this.logFileName = logfile;
		try {
			isDir();
			
			start = System.currentTimeMillis();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Logger getInstance() {
		if(instance == null) {
			instance = new Logger(); 
		}
		return instance;
	}
	public void writelog(String ClassName, String message) {
		try {
			SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			fw.write("["+date1.format(today)+"]       "+ ClassName +"         "+ message +"\n");
			fw.flush();
		} catch (IOException e) {
			System.out.println("IOException : "+ e.getMessage());
		}
	}
	public void close() {
		try {
			fw.close();
		} catch (IOException e) {
			System.out.println("IOException : "+ e.getMessage());
		}
	}
	public void isDir() throws IOException {
		logFileName = date.format(today);
		String dir = System.getProperty("user.dir")+"/log/";
		f = new File(dir);
		if(!f.exists()) {
			f.mkdirs();
		}else {
			if(!f.isDirectory()) {
				System.out.println("디렉토리가 존재하지 않습니다.");
			}
		}
		f = new File(dir,logFileName+".log");
		f.createNewFile();
	}
}
