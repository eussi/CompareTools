package com.dctis.compare.tar.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import com.dctis.compare.tar.constants.TarConstants;
import com.dctis.utils.FileUtils;
import com.dctis.utils.PrintUtils;

/**
 * tar文件的各项操作
 * @author wangxueming
 *
 */
public class TarFileUtils {
	/**
	 * 获取tar文件中所有的文件全路径名，不包括文件夹
	 * @param path tar文件路径
	 * @return
	 */
	public static List<String> getAllFileFromTar(String path) {
		List<String> files = new ArrayList<String>();
		File tarFile = new File(path);
	    FileInputStream fileIn = null;
	    BufferedInputStream bufIn = null;
	    TarArchiveInputStream taris = null;
	    try {
	        fileIn = new FileInputStream(tarFile);
	        bufIn = new BufferedInputStream(fileIn);
	        taris = new TarArchiveInputStream(bufIn);
	        TarArchiveEntry entry = null;
	        while ((entry = taris.getNextTarEntry()) != null) {
	            if (entry.isDirectory()) {
	            	continue;
	            }
	            files.add(entry.getName());
	        }
	        return files;
	    } catch (Exception e) {
//			e.printStackTrace();
	    	PrintUtils.print("tar文件处理错误！");
		} finally {
	        try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
//				e.printStackTrace();
				PrintUtils.print("数据流关闭失败！");
			}
	    }
	    return null;
	}
	
	/**
	 * 根据tar包中文件全路径名，获取文件内容
	 * @param tarPath
	 * @param filePath
	 * @return
	 */
	public static byte[] getFileContentFromTar(String tarPath, String filePath) {
		File tarFile = new File(tarPath);
	    FileInputStream fileIn = null;
	    BufferedInputStream bufIn = null;
	    TarArchiveInputStream taris = null;
	    try {
	        fileIn = new FileInputStream(tarFile);
	        bufIn = new BufferedInputStream(fileIn);
	        taris = new TarArchiveInputStream(bufIn);
	        TarArchiveEntry entry = null;
	        while ((entry = taris.getNextTarEntry()) != null) {
	            if (entry.isDirectory()) {
	            	continue;
	            }
	            if(entry.getName().equals(filePath)){
	                byte[] b = new byte[(int) entry.getSize()];
	                taris.read(b, 0, (int) entry.getSize());
	                return b;
	            }
	        }
	    } catch (Exception e) {
//			e.printStackTrace();
	    	PrintUtils.print("tar文件读取内容处理错误！");
		} finally {
	        try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
//				e.printStackTrace();
				PrintUtils.print("数据流关闭失败！");
			}
	    }
	    return null;
	}
	
	/**
	 * 根据tar包中文件全路径名，获取文件内容数组
	 * @param tarPath
	 * @param filePath
	 * @return
	 */
	public static String[] getStrArrayContentFromTar(String tarPath, String filePath) {
		File tarFile = new File(tarPath);
	    FileInputStream fileIn = null;
	    BufferedInputStream bufIn = null;
	    TarArchiveInputStream taris = null;
	    try {
	        fileIn = new FileInputStream(tarFile);
	        bufIn = new BufferedInputStream(fileIn);
	        taris = new TarArchiveInputStream(bufIn);
	        TarArchiveEntry entry = null;
	        while ((entry = taris.getNextTarEntry()) != null) {
	            if (entry.isDirectory()) {
	            	continue;
	            }
	            if(entry.getName().equals(filePath)){
	                byte[] b = new byte[(int) entry.getSize()];
	                taris.read(b, 0, (int) entry.getSize());
	                String content = new String(b, TarConstants.ENCODING);
	                if(FileUtils.isUnixFile(content)) {
	                	return content.split(TarConstants.UNIX_NEWLINE);
	                } else {
	                	return content.split(TarConstants.WIN_NEWLINE);
	                }
	            }
	        }
	    } catch (Exception e) {
//			e.printStackTrace();
	    	PrintUtils.print("tar文件读取内容处理错误！");
		} finally {
	        try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
//				e.printStackTrace();
				PrintUtils.print("数据流关闭失败！");
			}
	    }
	    return null;
	}
}
