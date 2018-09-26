package com.dctis.compare.tar.commonflow;

import java.util.List;

import com.dctis.compare.tar.constants.TarConstants;
import com.dctis.compare.tar.utils.TarFileUtils;
import com.dctis.core.IValveContext;
import com.dctis.core.impl.ValveBase;
import com.dctis.diff.FileDiff;
import com.dctis.utils.PrintUtils;

public class FileContentDiffShowValve extends ValveBase{

	@Override
	public String getInfo() {
		return "�г�tar�������ݲ�ͬ���ı��ļ����ݲ���";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//��ȡ��һ��valve����ȡ������
			List<String> diffFiles = (List<String>) context.getTemp(TarConstants.FILE_DIFF);
			for(String diffFile : diffFiles) {
				//������������ļ�����*.jar,*.class
				if(diffFile.endsWith("jar") || diffFile.endsWith("class"))
					continue;
				//�Ƚ��ļ�
				PrintUtils.print("�Ƚ��ļ���" + diffFile);
				String[] origStrs = TarFileUtils.getStrArrayContentFromTar(orig, diffFile);
				String[] destStrs = TarFileUtils.getStrArrayContentFromTar(dest, diffFile);
				//�Ƚ�
				new FileDiff(origStrs, destStrs);
				PrintUtils.printNewLine();
			}
			
		    PrintUtils.printLine();
	}
	

}
