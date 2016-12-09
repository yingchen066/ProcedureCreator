package cn.com.egova.swing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.com.egova.util.Constant;

public class MainFrame {
	public static ProcedureCreatorFrame mainFrame = null;
	public static boolean isShoewMainFrame = false;

	public static void main(String[] args) {
		mainFrame = new ProcedureCreatorFrame();
		File file = new File(Constant.DIR + "/dbConfig.txt");
		if (!file.exists()) {
			SettingFrame settingFrame = new SettingFrame();
			settingFrame.setVisible(true);
		} else {
			initConfig(file);

			mainFrame.setVisible(true);
			isShoewMainFrame = true;
		}

	}

	public static void initConfig(File file) {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			char[] ch = new char[1024];
			fr.read(ch);
			String config = new String(ch).trim();
			HashMap<String, String> map = new HashMap<String, String>();
			String[] split = config.split(";");
			for (String str : split) {
				String[] split2 = str.split(":");
				map.put(split2[0].trim(), split2[1].trim());
			}
			Constant.DB_URL = map.get("url");
			Constant.DB_PORT = map.get("port");
			Constant.DB_NAME = map.get("dbname");
			Constant.DB_USERNAME = map.get("username");
			Constant.DB_PASSWORD = map.get("password");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void saveConfigsToFile() {
		FileWriter fw = null;
		try {
			File file = new File(Constant.DIR + "/dbConfig.txt");
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			fw = new FileWriter(file);
			fw.write("url:" + Constant.DB_URL + ";");
			fw.write("port:" + Constant.DB_PORT + ";");
			fw.write("dbname:" + Constant.DB_NAME + ";");
			fw.write("username:" + Constant.DB_USERNAME + ";");
			fw.write("password:" + Constant.DB_PASSWORD);
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static boolean checkNull(String... args) {
		for (String str : args) {
			if (str == null || "".equals(str)) {
				return false;
			}
		}
		return true;
	}

}
