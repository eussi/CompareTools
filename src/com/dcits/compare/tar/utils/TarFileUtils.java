package com.dcits.compare.tar.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;

import com.dcits.compare.tar.constants.TarConstants;
import com.dcits.constants.Constants;
import com.dcits.utils.FileUtils;
import com.dcits.utils.PrintUtils;

/**
 * tar文件的各项操作
 * 
 * @author wangxueming
 *
 */
public class TarFileUtils {
	/**
	 * 获取tar文件中所有的文件全路径名，不包括文件夹
	 * 
	 * @param path
	 *            tar文件路径
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
			// e.printStackTrace();
			PrintUtils.print("tar文件处理错误！");
		} finally {
			try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
				// e.printStackTrace();
				PrintUtils.print("数据流关闭失败！");
			}
		}
		return null;
	}
	/**
	 * 获取tar文件中所有的文件全路径名，不包括文件夹
	 * 
	 * @param path
	 *            tar文件路径
	 * @return
	 */
	public static List<String> getAllFileFromTar(String path, String filter) {
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
				if(entry.getName().contains(filter))
					files.add(entry.getName());
			}
			return files;
		} catch (Exception e) {
			// e.printStackTrace();
			PrintUtils.print("tar文件处理错误！");
		} finally {
			try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
				// e.printStackTrace();
				PrintUtils.print("数据流关闭失败！");
			}
		}
		return null;
	}

	/**
	 * 根据tar包中文件全路径名，获取文件内容
	 * 
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
				if (entry.getName().equals(filePath)) {
					byte[] b = new byte[(int) entry.getSize()];
					taris.read(b, 0, (int) entry.getSize());
					return b;
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			PrintUtils.print("tar文件读取内容处理错误！");
		} finally {
			try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
				// e.printStackTrace();
				PrintUtils.print("数据流关闭失败！");
			}
		}
		return null;
	}

	/**
	 * 根据tar包中文件全路径名，获取文件内容数组
	 * 
	 * @param tarPath
	 * @param filePath
	 * @return
	 */
	public static String[] getStrArrayContentFromTar(String tarPath,
			String filePath) {
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
				if (entry.getName().equals(filePath)) {
					byte[] b = new byte[(int) entry.getSize()];
					taris.read(b, 0, (int) entry.getSize());
					String content = new String(b, TarConstants.ENCODING);
					if (FileUtils.isUnixFile(content)) {
						return content.split(TarConstants.UNIX_NEWLINE);
					} else {
						return content.split(TarConstants.WIN_NEWLINE);
					}
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			PrintUtils.print("tar文件读取内容处理错误！");
		} finally {
			try {
				taris.close();
				bufIn.close();
				fileIn.close();
			} catch (IOException e) {
				// e.printStackTrace();
				PrintUtils.print("数据流关闭失败！");
			}
		}
		return null;
	}

	/**
	 * .TAR格式文件解压
	 * 
	 * @param path tar文件路径
	 * @param basePath 解压路径
	 */
	public static void deCompressTARFile(String path, String basePath) {
		File file = new File(path);
		int buffersize = 2048;
		TarArchiveInputStream is = null;
		try {
			is = new TarArchiveInputStream(new FileInputStream(file));
			while (true) {
				TarArchiveEntry entry = is.getNextTarEntry();
				if (entry == null) {
					break;
				}
				if (entry.isDirectory()) {// 这里貌似不会运行到，跟ZipEntry有点不一样
					new File(basePath+ File.separator + entry.getName()).mkdirs();
				} else {
					FileOutputStream os = null;
					try {
						File f = new File(basePath + entry.getName());
						System.out.println(f.getAbsolutePath());
						if (!f.getParentFile().exists()) {
							f.getParentFile().mkdirs();
						}
						if (!f.exists()) {
							f.createNewFile();
						}
						os = new FileOutputStream(f);
						byte[] bs = new byte[buffersize];
						int len = -1;
						while ((len = is.read(bs)) != -1) {
							os.write(bs, 0, len);
						}
						os.flush();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						os.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
//				file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
