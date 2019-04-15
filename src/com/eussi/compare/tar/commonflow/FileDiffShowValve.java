package com.eussi.compare.tar.commonflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.eussi.compare.tar.constants.TarConstants;
import com.eussi.compare.tar.utils.FileUtils;
import com.eussi.compare.tar.utils.TarFileUtils;
import com.eussi.core.IValveContext;
import com.eussi.core.impl.ValveBase;
import com.eussi.utils.PrintUtils;

public class FileDiffShowValve extends ValveBase{

	@Override
	public String getInfo() {
		return "�Ƚ�tar���й�ͬ�����ļ������Ƿ�һ�£�����һ�µ��ļ����г�";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//��ȡ��һ��valve����ȡ������
			List<String> origFiles = (List<String>) context.getTemp(TarConstants.ORIG_FILES);
			List<String> destFiles = (List<String>) context.getTemp(TarConstants.DEST_FILES);
			List<String> origHas = (List<String>) context.getTemp(TarConstants.ORIG_HAS);
			List<String> destHas = (List<String>) context.getTemp(TarConstants.DEST_HAS);
			
			Boolean isExtract = (Boolean) context.getTemp(TarConstants.IS_EXTRACT);
			String origExtract = (String) context.getTemp(TarConstants.ORIG_EXTRACT);
			String destExtract = (String) context.getTemp(TarConstants.DEST_EXTRACT);
			
			//�Ա��ļ������Ƿ�һ�£���һ�µ��г���
			int count=0;
			List<String> commonButDiff = new ArrayList<String>();
			PrintUtils.print("��ʼ�Ƚ��ļ�");
			for(String origFile : origFiles) {
				if(!listHas(origHas, origFile) && !listHas(commonButDiff, origFile)) {
					byte[] origContent = null;
					byte[] destContent = null;
					if(isExtract!=null && isExtract) {
						origContent = FileUtils.getFileContent(origExtract, origFile);
						destContent = FileUtils.getFileContent(destExtract, origFile);
					} else {
						origContent = TarFileUtils.getFileContentFromTar(orig, origFile);
						destContent = TarFileUtils.getFileContentFromTar(dest, origFile);
					}
					if(!Arrays.equals(origContent, destContent)) {
						commonButDiff.add(origFile);
					}
					if(++count%TarConstants.DEALFILE_PRINT==0 && count>0) {
						PrintUtils.print("�Ѿ������ļ�����" + count);
					}
				}
			}
			for(String destFile : destFiles) {
				if(!listHas(destHas, destFile) && !listHas(commonButDiff, destFile)) {
					byte[] origContent = null;
					byte[] destContent = null;
					if(isExtract!=null && isExtract) {
						origContent = FileUtils.getFileContent(origExtract, destFile);
						destContent = FileUtils.getFileContent(destExtract, destFile);
					} else {
						origContent = TarFileUtils.getFileContentFromTar(orig, destFile);
						destContent = TarFileUtils.getFileContentFromTar(dest, destFile);
					}
					
					if(!Arrays.equals(origContent, destContent)) {
						commonButDiff.add(destFile);
					}
					if(++count%TarConstants.DEALFILE_PRINT==0 && count>0) {
						PrintUtils.print("�Ѿ������ļ�����" + count);
					}
				}
			}
			PrintUtils.print("������ɣ������ļ�����" + count);
			PrintUtils.print("[" + orig + "]��[" + dest + "]�����ļ���");
			PrintUtils.printList(commonButDiff);
			
		    PrintUtils.printLine();
		    
		    //��Ž�����ݸ���һ��valve
		    context.setTemp(TarConstants.FILE_DIFF, commonButDiff);
	}
	
	/**
	 * �ж��������Ƿ�����ַ���
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
}
