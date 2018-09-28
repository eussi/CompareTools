package com.dcits.compare.tar.commonflow;

import java.util.ArrayList;
import java.util.List;

import com.dcits.compare.tar.constants.TarConstants;
import com.dcits.compare.tar.utils.FileUtils;
import com.dcits.compare.tar.utils.TarFileUtils;
import com.dcits.core.IValveContext;
import com.dcits.core.impl.ValveBase;
import com.dcits.utils.PrintUtils;

public class FileNumNameValve extends ValveBase{

	@Override
	public String getInfo() {
		return "�Ƚ�tar�����ļ����Ƿ�һ�£�����һ���ļ��г�";
	}

	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			boolean isExtract = (boolean) context.getTemp(TarConstants.IS_EXTRACT);
			List<String> origFiles = null;
		    List<String> destFiles = null;
		    List<String> origHas = null;
		    List<String> destHas = null;
			if(isExtract) {
				origFiles = new ArrayList<String>();
				destFiles = new ArrayList<String>();
				String origExtract = (String) context.getTemp(TarConstants.ORIG_EXTRACT);
				String destExtract = (String) context.getTemp(TarConstants.DEST_EXTRACT);
				FileUtils.getAllFiles(origFiles, origExtract);
				FileUtils.getAllFiles(destFiles, destExtract);
				origHas = compareAbosoluteList(origFiles, destFiles, origExtract, destExtract);
				destHas = compareAbosoluteList(destFiles, origFiles, origExtract, destExtract);
			} else {
				//��ȡtar�������е��ļ�
				origFiles = TarFileUtils.getAllFileFromTar(orig);
			    destFiles = TarFileUtils.getAllFileFromTar(dest);
			    origHas = compareList(origFiles, destFiles);
			    destHas = compareList(destFiles, origFiles);
			}
			
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
	/**
	 * �Ƚϵ�һ������ȵڶ�����������Ԫ��,�����а�������ȫ·��
	 * @param orig
	 * @param dest
	 * @param destExtract 
	 * @param origExtract 
	 * @return ���ض����Ԫ��
	 */
	private List<String> compareAbosoluteList(List<String> orig, List<String> dest, String origExtract, String destExtract) {
		List<String> list = new ArrayList<String>();
		for(String s : orig) {
			boolean ok = true;
			for(String s1 : dest) {
				String s2=s.replace(origExtract, "");
				s2 = s2.replace(destExtract, "");
				String s3=s1.replace(origExtract, "");
				s3 = s3.replace(destExtract, "");
				if(s2.equals(s3)) {
					ok = false;
				}
				
			}
			if(ok)
				list.add(s);
		}
		return list;
	}

}
