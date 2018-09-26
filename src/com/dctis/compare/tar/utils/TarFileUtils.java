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
 * tar�ļ��ĸ������
 * @author wangxueming
 *
 */
public class TarFileUtils {
	/**
	 * ��ȡtar�ļ������е��ļ�ȫ·�������������ļ���
	 * @param path tar�ļ�·��
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
	    	PrintUtils.print("tar�ļ��������");
		} finally {
	        try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
//				e.printStackTrace();
				PrintUtils.print("�������ر�ʧ�ܣ�");
			}
	    }
	    return null;
	}
	
	/**
	 * ����tar�����ļ�ȫ·��������ȡ�ļ�����
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
	    	PrintUtils.print("tar�ļ���ȡ���ݴ������");
		} finally {
	        try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
//				e.printStackTrace();
				PrintUtils.print("�������ر�ʧ�ܣ�");
			}
	    }
	    return null;
	}
	
	/**
	 * ����tar�����ļ�ȫ·��������ȡ�ļ���������
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
	    	PrintUtils.print("tar�ļ���ȡ���ݴ������");
		} finally {
	        try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
//				e.printStackTrace();
				PrintUtils.print("�������ر�ʧ�ܣ�");
			}
	    }
	    return null;
	}
}
