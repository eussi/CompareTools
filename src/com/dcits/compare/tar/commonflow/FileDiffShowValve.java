package com.dcits.compare.tar.commonflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dcits.compare.tar.constants.TarConstants;
import com.dcits.compare.tar.utils.FileUtils;
import com.dcits.compare.tar.utils.TarFileUtils;
import com.dcits.core.IValveContext;
import com.dcits.core.impl.ValveBase;
import com.dcits.utils.PrintUtils;

public class FileDiffShowValve extends ValveBase{

	@Override
	public String getInfo() {
		return "比较tar包中共同存在文件内容是否一致，将不一致的文件名列出";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//获取上一个valve所获取的内容
			List<String> origFiles = (List<String>) context.getTemp(TarConstants.ORIG_FILES);
			List<String> destFiles = (List<String>) context.getTemp(TarConstants.DEST_FILES);
			List<String> origHas = (List<String>) context.getTemp(TarConstants.ORIG_HAS);
			List<String> destHas = (List<String>) context.getTemp(TarConstants.DEST_HAS);
			
			Boolean isExtract = (Boolean) context.getTemp(TarConstants.IS_EXTRACT);
			String origExtract = (String) context.getTemp(TarConstants.ORIG_EXTRACT);
			String destExtract = (String) context.getTemp(TarConstants.DEST_EXTRACT);
			
			//对比文件内容是否一致，不一致的列出来
			int count=0;
			List<String> commonButDiff = new ArrayList<String>();
			PrintUtils.print("开始比较文件");
			for(String origFile : origFiles) {
				if(!listHas(origHas, origFile) && !listHas(commonButDiff, origFile)) {
					byte[] origContent = null;
					byte[] destContent = null;
					if(isExtract!=null && isExtract) {
						origContent = FileUtils.getFileContent(origExtract, origFile);
						destContent = FileUtils.getFileContent(destExtract, origFile);
					} else {
						origContent = TarFileUtils.getFileContentFromTar(orig, origFile);
						destContent = TarFileUtils.getFileContentFromTar(dest, origFile);
					}
					if(!Arrays.equals(origContent, destContent)) {
						commonButDiff.add(origFile);
					}
					if(++count%TarConstants.DEALFILE_PRINT==0 && count>0) {
						PrintUtils.print("已经处理文件数：" + count);
					}
				}
			}
			for(String destFile : destFiles) {
				if(!listHas(destHas, destFile) && !listHas(commonButDiff, destFile)) {
					byte[] origContent = null;
					byte[] destContent = null;
					if(isExtract!=null && isExtract) {
						origContent = FileUtils.getFileContent(origExtract, destFile);
						destContent = FileUtils.getFileContent(destExtract, destFile);
					} else {
						origContent = TarFileUtils.getFileContentFromTar(orig, destFile);
						destContent = TarFileUtils.getFileContentFromTar(dest, destFile);
					}
					
					if(!Arrays.equals(origContent, destContent)) {
						commonButDiff.add(destFile);
					}
					if(++count%TarConstants.DEALFILE_PRINT==0 && count>0) {
						PrintUtils.print("已经处理文件数：" + count);
					}
				}
			}
			PrintUtils.print("处理完成，处理文件数：" + count);
			PrintUtils.print("[" + orig + "]与[" + dest + "]差异文件：");
			PrintUtils.printList(commonButDiff);
			
		    PrintUtils.printLine();
		    
		    //存放结果传递给下一个valve
		    context.setTemp(TarConstants.FILE_DIFF, commonButDiff);
	}
	
	/**
	 * 判断数组中是否包含字符串
	 * @param list
	 * @param str
	 * @return
	 */
	private boolean listHas(List<String> list, String str) {
		for(String s : list) {
			if(str.equals(s))
				return true;
		}
		return false;
	}
}
