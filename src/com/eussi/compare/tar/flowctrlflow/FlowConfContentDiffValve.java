package com.eussi.compare.tar.flowctrlflow;

import java.util.Map;

import com.eussi.compare.tar.constants.TarConstants;
import com.eussi.compare.tar.utils.TarFileUtils;
import com.eussi.core.IValveContext;
import com.eussi.core.impl.ValveBase;
import com.eussi.diff.FileDiff;
import com.eussi.utils.PrintUtils;

public class FlowConfContentDiffValve extends ValveBase{

	@Override
	public String getInfo() {
		return "�Ƚ�tar����flow_conf��flow_conf1�д��ڲ�����ļ����������ļ������г�";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
		PrintUtils.print(getInfo());
		PrintUtils.printLine();
		//��ȡ��һ��valve����ȡ������
		Map<String, String> diffFiles = (Map<String, String>) context.getTemp(TarConstants.CONF_FILE_DIFF);
		for(String keyFile : diffFiles.keySet()) {
			//������������ļ�����*.jar,*.class
			if(keyFile.endsWith("jar") || keyFile.endsWith("class"))
				continue;
			//�Ƚ��ļ�
			PrintUtils.print("�Ƚ��ļ���" + keyFile + "," + diffFiles.get(keyFile));
			String[] origStrs = TarFileUtils.getStrArrayContentFromTar(orig, keyFile);
			String[] destStrs = TarFileUtils.getStrArrayContentFromTar(dest, diffFiles.get(keyFile));
			//�Ƚ�
			new FileDiff(origStrs, destStrs);
			PrintUtils.printNewLine();
		}
		
	    PrintUtils.printLine();
	}
	
}
