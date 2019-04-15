package com.eussi.compare.tar.commonflow;

import java.util.ArrayList;
import java.util.List;

import com.eussi.compare.tar.constants.TarConstants;
import com.eussi.compare.tar.utils.FileUtils;
import com.eussi.compare.tar.utils.TarFileUtils;
import com.eussi.core.IValveContext;
import com.eussi.core.impl.ValveBase;
import com.eussi.utils.PrintUtils;

public class FileNumNameValve extends ValveBase{

	@Override
	public String getInfo() {
		return "�Ƚ�tar�����ļ����Ƿ�һ�£�����һ���ļ��г�";
	}

	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			Boolean isExtract = (Boolean) context.getTemp(TarConstants.IS_EXTRACT);
			List<String> origFiles = null;
		    List<String> destFiles = null;
			if(isExtract!=null && isExtract) {
				origFiles = new ArrayList<String>();
				destFiles = new ArrayList<String>();
				String origExtract = (String) context.getTemp(TarConstants.ORIG_EXTRACT); //��ȡ��ѹ�ļ�·��
				String destExtract = (String) context.getTemp(TarConstants.DEST_EXTRACT);
				FileUtils.getAllFiles(origFiles, origExtract, origExtract);//��ȡ��ѹ·���������ļ�
				FileUtils.getAllFiles(destFiles, destExtract, destExtract);
			} else {
				//��ȡtar�������е��ļ�
				origFiles = TarFileUtils.getAllFileFromTar(orig);
			    destFiles = TarFileUtils.getAllFileFromTar(dest);
			}
			
			List<String> origHas = compareList(origFiles, destFiles);
			List<String> destHas = compareList(destFiles, origFiles);
		    //�Ƚ�
		    if(origHas.size()>0) {
		    	PrintUtils.print("[" + orig + "]�����[" + dest + "]�����ļ���");
		    	PrintUtils.printList(origHas);
		    }
		    PrintUtils.printNewLine();
		    
		    if(destHas.size()>0) {
		    	PrintUtils.print("[" + dest + "]�����[" + orig + "]�����ļ���");
		    	PrintUtils.printList(destHas);
		    }
		    PrintUtils.printLine();
		    
		    //��Ž�����ݸ���һ��valve
		    context.setTemp(TarConstants.ORIG_FILES, origFiles);
		    context.setTemp(TarConstants.DEST_FILES, destFiles);
		    context.setTemp(TarConstants.ORIG_HAS, origHas);
		    context.setTemp(TarConstants.DEST_HAS, destHas);
		    
	}
	/**
	 * �Ƚϵ�һ������ȵڶ�����������Ԫ��
	 * @param orig
	 * @param dest
	 * @return ���ض����Ԫ��
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
