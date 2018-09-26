package com.dctis.compare.tar;

import java.util.ArrayList;
import java.util.List;

import com.dctis.compare.ValveBase;
import com.dctis.compare.tar.constants.TarConstants;
import com.dctis.compare.tar.utils.TarFileUtils;
import com.dctis.core.IValveContext;
import com.dctis.utils.PrintUtils;

public class FlowFileContentValve extends ValveBase{

	@Override
	public String getInfo() {
		return "比较Flow版本包中文件内容是否一致";
	}

	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//获取tar包中所有的文件
			PrintUtils.print(TarFileUtils.getFileContentFromTar(orig, "SmartESB/configs/flow_conf1/log4j.properties"));
			PrintUtils.printList((List<String>) context.getTemp(TarConstants.DEST_FILES));
			
//		    List<String> origFiles = TarFileUtils.getAllFileFromTar(orig);
//		    List<String> destFiles = TarFileUtils.getAllFileFromTar(dest);
//		    //比较
//		    List<String> origHas = compareList(origFiles, destFiles);
//		    if(origHas.size()>0) {
//		    	PrintUtils.print(orig + "相对于" + dest + "多余文件：");
//		    	PrintUtils.printList(origHas);
//		    }
//		    
//		    List<String> destHas = compareList(destFiles, origFiles);
//		    if(destHas.size()>0) {
//		    	PrintUtils.print(orig + "相对于" + dest + "多余文件：");
//		    	PrintUtils.printList(destHas);
//		    }
		    PrintUtils.printLine();
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
				if(s.equals(s1)) {
					ok = false;
				}
			}
			if(ok)
				list.add(s);
		}
		return list;
	}

}
