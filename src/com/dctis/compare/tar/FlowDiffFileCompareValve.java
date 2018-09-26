package com.dctis.compare.tar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dctis.compare.ValveBase;
import com.dctis.compare.tar.constants.TarConstants;
import com.dctis.compare.tar.utils.TarFileUtils;
import com.dctis.core.IValveContext;
import com.dctis.diff.FileDiff;
import com.dctis.utils.PrintUtils;

public class FlowDiffFileCompareValve extends ValveBase{

	@Override
	public String getInfo() {
		return "�г�Flow�汾���в�ͬ�ı��ļ����ݲ���֮��";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//��ȡ��һ��valve����ȡ������
			List<String> diffFiles = (List<String>) context.getTemp(TarConstants.FILE_DIFF);
			for(String diffFile : diffFiles) {
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
