package com.dctis.compare.tar.commonflow;

import java.util.ArrayList;
import java.util.List;

import com.dctis.compare.tar.constants.TarConstants;
import com.dctis.compare.tar.utils.TarFileUtils;
import com.dctis.core.IValveContext;
import com.dctis.core.impl.ValveBase;
import com.dctis.utils.PrintUtils;

public class FileNumNameValve extends ValveBase{

	@Override
	public String getInfo() {
		return "�Ƚ�tar�����ļ����Ƿ�һ�£�����һ���ļ��г�";
	}

	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//��ȡtar�������е��ļ�
		    List<String> origFiles = TarFileUtils.getAllFileFromTar(orig);
		    List<String> destFiles = TarFileUtils.getAllFileFromTar(dest);
		    //�Ƚ�
		    List<String> origHas = compareList(origFiles, destFiles);
		    if(origHas.size()>0) {
		    	PrintUtils.print("[" + orig + "]�����[" + dest + "]�����ļ���");
		    	PrintUtils.printList(origHas);
		    }
		    PrintUtils.printNewLine();
		    
		    List<String> destHas = compareList(destFiles, origFiles);
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
