package com.dcits.compare.tar.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dcits.utils.PrintUtils;

public class FileUtils {
	/**
	 * 获取某目录下所有文件，相对于extract路径的相对路径
	 * 
	 * @param fileNames
	 * @param path
	 * @param extract 
	 */
	public static void getAllFiles(List<String> files, String path, String extract) {
		File file = new File(path);
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			for (File f : fs) {
				getAllFiles(files, f.getAbsolutePath(), extract);
			}
		} else {
			files.add(file.getAbsolutePath().replace(extract, ""));
		}
	}

	@SuppressWarnings("resource")
	public static byte[] getFileContent(String extract, String origFile) {
		FileInputStream fis = null;
		ByteArrayOutputStream bao = null;
		try {
			fis = new FileInputStream(new File(extract + origFile));
			byte[] b = new byte[1024];
			bao = new ByteArrayOutputStream();
			int byteLength = -1;
			while ((byteLength = fis.read(b)) != -1) {
				bao.write(b, 0, byteLength);
			}
			return bao.toByteArray(); // 文件数据全部读入
		} catch (Exception e) {
//			e.printStackTrace();
			PrintUtils.print("读取文件异常: " + e.getMessage());
		} finally {
			try {
				bao.close();
				fis.close();
			} catch (IOException e) {
//				e.printStackTrace();
				PrintUtils.print("流关闭异常: " + e.getMessage());
			}
		}
		return null;
	}

	public static String[] getContentFromFile(String extract, String filepath) {
		FileReader fr = null;	
		BufferedReader br = null;
		try {
				List<String> strs = new ArrayList<String>();
				File f = new File(extract + filepath);
				fr = new FileReader(f);
				br = new BufferedReader(fr);
				String line = null;
				while ((line = br.readLine()) != null) {
					strs.add(line);
				}
				return strs.toArray(new String[strs.size()]);
			} catch (Exception e) {
//				e.printStackTrace();
				PrintUtils.print("读取文件异常: " + e.getMessage());
			} finally {
				try {
					br.close();
					fr.close();
				} catch (IOException e) {
//					e.printStackTrace();
					PrintUtils.print("流关闭异常: " + e.getMessage());
				}
			}
		return null;
	}
	
	/**
	 * 删除文件夹以及所有子文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists())
			return flag;
		if (!file.isDirectory())
			return flag;
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + File.separator + tempList[i]);
				delFolder(path + File.separator + tempList[i]);
				flag = true;
			}
		}
		if(flag) {
			file.delete();
		}
		return flag;
	}
	
	private static void delFolder(String folderPath) {
		delAllFile(folderPath);
		String filePath = folderPath;
		File file = new File(filePath);
		file.delete();
	}
}
