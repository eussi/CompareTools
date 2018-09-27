package com.dcits.compare.tar.flowctrlflow;

import java.util.Map;

import com.dcits.compare.tar.constants.TarConstants;
import com.dcits.compare.tar.utils.TarFileUtils;
import com.dcits.core.IValveContext;
import com.dcits.core.impl.ValveBase;
import com.dcits.diff.FileDiff;
import com.dcits.utils.PrintUtils;

public class FlowConfContentDiffValve extends ValveBase{

	@Override
	public String getInfo() {
		return "比较tar包中flow_conf与flow_conf1中存在差异的文件，将差异文件内容列出";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
		PrintUtils.print(getInfo());
		PrintUtils.printLine();
		//获取上一个valve所获取的内容
		Map<String, String> diffFiles = (Map<String, String>) context.getTemp(TarConstants.CONF_FILE_DIFF);
		for(String keyFile : diffFiles.keySet()) {
			//不处理二进制文件，如*.jar,*.class
			if(keyFile.endsWith("jar") || keyFile.endsWith("class"))
				continue;
			//比较文件
			PrintUtils.print("比较文件：" + keyFile + "," + diffFiles.get(keyFile));
			String[] origStrs = TarFileUtils.getStrArrayContentFromTar(orig, keyFile);
			String[] destStrs = TarFileUtils.getStrArrayContentFromTar(dest, diffFiles.get(keyFile));
			//比较
			new FileDiff(origStrs, destStrs);
			PrintUtils.printNewLine();
		}
		
	    PrintUtils.printLine();
	}
	
}
