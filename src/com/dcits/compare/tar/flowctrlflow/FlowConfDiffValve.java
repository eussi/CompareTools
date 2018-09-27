package com.dcits.compare.tar.flowctrlflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dcits.compare.tar.constants.TarConstants;
import com.dcits.compare.tar.utils.TarFileUtils;
import com.dcits.core.IValveContext;
import com.dcits.core.impl.ValveBase;
import com.dcits.utils.PrintUtils;

public class FlowConfDiffValve extends ValveBase{

	@Override
	public String getInfo() {
		return "比较tar包中flow_conf与flow_conf1中存在差异的文件，将差异文件列出";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//获取上一个valve所获取的内容
			List<String> origGetFiles = (List<String>) context.getTemp(TarConstants.ORIG_FILES);
			List<String> destGetFiles = (List<String>) context.getTemp(TarConstants.DEST_FILES);
			
			//筛选包含flow_conf的文件
			List<String> origFiles = new ArrayList<String>();   //假设包含flow_conf，不包含flow_conf1
			for(String s : origGetFiles) {
				if(s.contains(TarConstants.PATH_FLOW_FLAG))
					origFiles.add(s);
			}
			List<String> destFiles = new ArrayList<String>();
			for(String s : destGetFiles) {
				if(s.contains(TarConstants.PATH_FLOW1_FLAG))
					destFiles.add(s);
			}
			
			//比较
			List<String> origHas = compareList(origFiles, destFiles);
			List<String> destHas = compareList(destFiles, origFiles);
		    if(origHas.size()>0) {
		    	PrintUtils.print("[" + orig + "]flow_conf相对于[" + dest + "]flow_conf1多余文件：");
		    	PrintUtils.printList(origHas);
		    }
		    PrintUtils.printNewLine();
		    
		    if(destHas.size()>0) {
		    	PrintUtils.print("[" + dest + "]flow_conf1相对于[" + orig + "]flow_conf多余文件：");
		    	PrintUtils.printList(destHas);
		    }
			
			int count=0;
			Map<String, String> confButDiff = new HashMap<String, String>();
			PrintUtils.print("开始比较文件");
			for(String origFile : origFiles) {
				if(!listHas(origHas, origFile) && !confButDiff.containsKey(origFile)) {
					byte[] origContent = TarFileUtils.getFileContentFromTar(orig, origFile);
					
					String orig1File = origFile.replace(TarConstants.PATH_FLOW_FLAG, TarConstants.PATH_FLOW1_FLAG);
					byte[] destContent = TarFileUtils.getFileContentFromTar(dest, orig1File);
					if(!Arrays.equals(origContent, destContent)) {
						confButDiff.put(origFile, orig1File);
					}
					if(++count%TarConstants.DEALFILE_PRINT==0 && count>0) {
						PrintUtils.print("已经处理文件数：" + count);
					}
				}
			}
			for(String destFile : destFiles) {
				if(!listHas(destHas, destFile) && confButDiff.containsValue(destFile)) {
					String dest1File = destFile.replace(TarConstants.PATH_FLOW1_FLAG, TarConstants.PATH_FLOW_FLAG);
					byte[] origContent = TarFileUtils.getFileContentFromTar(orig, dest1File);
					byte[] destContent = TarFileUtils.getFileContentFromTar(dest, destFile);
					if(!Arrays.equals(origContent, destContent)) {
						confButDiff.put(dest1File, destFile);
					}
					if(++count%TarConstants.DEALFILE_PRINT==0 && count>0) {
						PrintUtils.print("已经处理文件数：" + count);
					}
				}
			}
			PrintUtils.print("处理完成，处理文件数：" + count);
			PrintUtils.print("[" + orig + "]与[" + dest + "]差异文件：");
			PrintUtils.printMap(confButDiff);
			
		    PrintUtils.printLine();
		    
		    //存放结果传递给下一个valve
		    context.setTemp(TarConstants.CONF_FILE_DIFF, confButDiff);
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
	
	/**
	 * 比较第一个数组比第二个数组多余的元素
	 * @param orig
	 * @param dest
	 * @return 返回多余的元素
	 */
	private List<String> compareList(List<String> orig, List<String> dest) {
		List<String> list = new ArrayList<String>();
		for(String s : orig) {
			boolean ok = true;
			for(String s1 : dest) {
				String s2 = s.replace(TarConstants.PATH_FLOW_FLAG, TarConstants.PATH_FLOW1_FLAG);
				String s3 = s1.replace(TarConstants.PATH_FLOW_FLAG, TarConstants.PATH_FLOW1_FLAG);
				if(s2.equals(s3)) {
					ok = false;
				}
			}
			if(ok)
				list.add(s);
		}
		return list;
	}
}
