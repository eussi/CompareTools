package com.dcits.compare.tar.commonflow;

import java.util.List;

import com.dcits.compare.tar.constants.TarConstants;
import com.dcits.compare.tar.utils.TarFileUtils;
import com.dcits.core.IValveContext;
import com.dcits.core.impl.ValveBase;
import com.dcits.diff.FileDiff;
import com.dcits.utils.PrintUtils;

public class FileContentDiffShowValve extends ValveBase{

	@Override
	public String getInfo() {
		return "列出tar包中内容不同的文本文件内容差异";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//获取上一个valve所获取的内容
			List<String> diffFiles = (List<String>) context.getTemp(TarConstants.FILE_DIFF);
			for(String diffFile : diffFiles) {
				//不处理二进制文件，如*.jar,*.class
				if(diffFile.endsWith("jar") || diffFile.endsWith("class"))
					continue;
				//比较文件
				PrintUtils.print("比较文件：" + diffFile);
				String[] origStrs = TarFileUtils.getStrArrayContentFromTar(orig, diffFile);
				String[] destStrs = TarFileUtils.getStrArrayContentFromTar(dest, diffFile);
				//比较
				new FileDiff(origStrs, destStrs);
				PrintUtils.printNewLine();
			}
			
		    PrintUtils.printLine();
	}
	

}
