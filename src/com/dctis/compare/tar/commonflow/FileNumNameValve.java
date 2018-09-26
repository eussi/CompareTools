package com.dctis.compare.tar.commonflow;

import java.util.ArrayList;
import java.util.List;

import com.dctis.compare.tar.constants.TarConstants;
import com.dctis.compare.tar.utils.TarFileUtils;
import com.dctis.core.IValveContext;
import com.dctis.core.impl.ValveBase;
import com.dctis.utils.PrintUtils;

public class FileNumNameValve extends ValveBase{

	@Override
	public String getInfo() {
		return "比较tar包中文件名是否一致，将不一致文件列出";
	}

	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//获取tar包中所有的文件
		    List<String> origFiles = TarFileUtils.getAllFileFromTar(orig);
		    List<String> destFiles = TarFileUtils.getAllFileFromTar(dest);
		    //比较
		    List<String> origHas = compareList(origFiles, destFiles);
		    if(origHas.size()>0) {
		    	PrintUtils.print("[" + orig + "]相对于[" + dest + "]多余文件：");
		    	PrintUtils.printList(origHas);
		    }
		    PrintUtils.printNewLine();
		    
		    List<String> destHas = compareList(destFiles, origFiles);
		    if(destHas.size()>0) {
		    	PrintUtils.print("[" + dest + "]相对于[" + orig + "]多余文件：");
		    	PrintUtils.printList(destHas);
		    }
		    PrintUtils.printLine();
		    
		    //存放结果传递给下一个valve
		    context.setTemp(TarConstants.ORIG_FILES, origFiles);
		    context.setTemp(TarConstants.DEST_FILES, destFiles);
		    context.setTemp(TarConstants.ORIG_HAS, origHas);
		    context.setTemp(TarConstants.DEST_HAS, destHas);
		    
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
