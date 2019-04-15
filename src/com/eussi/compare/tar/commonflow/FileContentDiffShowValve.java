package com.eussi.compare.tar.commonflow;

import java.io.File;
import java.util.List;

import com.eussi.compare.tar.constants.TarConstants;
import com.eussi.compare.tar.utils.FileUtils;
import com.eussi.compare.tar.utils.TarFileUtils;
import com.eussi.core.IValveContext;
import com.eussi.core.impl.ValveBase;
import com.eussi.diff.FileDiff;
import com.eussi.utils.PrintUtils;

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
			
			Boolean isExtract = (Boolean) context.getTemp(TarConstants.IS_EXTRACT);
			String origExtract = (String) context.getTemp(TarConstants.ORIG_EXTRACT);
			String destExtract = (String) context.getTemp(TarConstants.DEST_EXTRACT);
			
			for(String diffFile : diffFiles) {
				//������������ļ�����*.jar,*.class
				if(diffFile.endsWith("jar") || diffFile.endsWith("class"))
					continue;
				//�Ƚ��ļ�
				PrintUtils.print("�Ƚ��ļ���" + diffFile);
				String[] origStrs = null;
				String[] destStrs = null;
				if(isExtract!=null && isExtract) {
					origStrs = FileUtils.getContentFromFile(origExtract, diffFile);
					destStrs = FileUtils.getContentFromFile(destExtract, diffFile);
				} else {
					origStrs = TarFileUtils.getStrArrayContentFromTar(orig, diffFile);
					destStrs = TarFileUtils.getStrArrayContentFromTar(dest, diffFile);
				}
				//�Ƚ�
				new FileDiff(origStrs, destStrs);
				PrintUtils.printNewLine();
			}
			
		    PrintUtils.printLine();
		    
		    //ɾ����ѹ����ʱ�ļ�
		    String newDir = System.getProperty("user.dir") + File.separator + ".tmp";
		    FileUtils.delAllFile(newDir);
	}
	

}
