package com.eussi.compare.tar.commonflow;

import java.io.File;

import com.eussi.compare.tar.constants.TarConstants;
import com.eussi.compare.tar.utils.FileUtils;
import com.eussi.compare.tar.utils.TarFileUtils;
import com.eussi.core.IValveContext;
import com.eussi.core.impl.ValveBase;
import com.eussi.utils.PrintUtils;

public class ExtractTarValve extends ValveBase{

	@Override
	public String getInfo() {
		return "��ѹtar�ļ�";
	}

	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
		PrintUtils.print(getInfo());
		PrintUtils.printLine();
		
		//ɾ��old��ѹ����ʱ�ļ�
	    String oldDir = System.getProperty("user.dir") + File.separator + ".tmp";
	    FileUtils.delAllFile(oldDir);
		String extractOrigDir = System.getProperty("user.dir") + File.separator + ".tmp" + File.separator + "orig" + File.separator;
		String extractDestDir = System.getProperty("user.dir") + File.separator + ".tmp" + File.separator + "dest" + File.separator;
		TarFileUtils.deCompressTARFile(orig, extractOrigDir);
		TarFileUtils.deCompressTARFile(dest, extractDestDir);
		
		PrintUtils.printLine();
		
		//��Ž�����ݸ���һ��valve
	    context.setTemp(TarConstants.ORIG_EXTRACT, extractOrigDir);
	    context.setTemp(TarConstants.DEST_EXTRACT, extractDestDir);
	    context.setTemp(TarConstants.IS_EXTRACT, true);
	}

}
