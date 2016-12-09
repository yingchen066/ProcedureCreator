package cn.com.egova.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
	public static boolean save(String str, String pathname, boolean isAppend) {
		BufferedWriter bufferWritter = null;
		try {

			File file = new File(pathname);
			createFile(file);
			FileWriter fileWritter = new FileWriter(file.getPath(), isAppend);
			bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(str);
			bufferWritter.flush();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (bufferWritter != null) {
					bufferWritter.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	private static void createFile(File file) throws IOException {
		if (file.exists()) {
			return;
		}
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		file.createNewFile();
	}

}
