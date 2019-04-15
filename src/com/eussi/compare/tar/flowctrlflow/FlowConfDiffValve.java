package com.eussi.compare.tar.flowctrlflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eussi.compare.tar.constants.TarConstants;
import com.eussi.compare.tar.utils.TarFileUtils;
import com.eussi.core.IValveContext;
import com.eussi.core.impl.ValveBase;
import com.eussi.utils.PrintUtils;

public class FlowConfDiffValve extends ValveBase{

	@Override
	public String getInfo() {
		return "�Ƚ�tar����flow_conf��flow_conf1�д��ڲ�����ļ����������ļ��г�";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			
			//ɸѡ����flow_conf���ļ�
			List<String> origFiles = TarFileUtils.getAllFileFromTar(orig, TarConstants.PATH_FLOW_FLAG);
			List<String> destFiles = TarFileUtils.getAllFileFromTar(dest, TarConstants.PATH_FLOW1_FLAG);
			
			//�Ƚ�
			List<String> origHas = compareList(origFiles, destFiles);
			List<String> destHas = compareList(destFiles, origFiles);
		    if(origHas.size()>0) {
		    	PrintUtils.print("[" + orig + "]flow_conf�����[" + dest + "]flow_conf1�����ļ���");
		    	PrintUtils.printList(origHas);
		    }
		    PrintUtils.printNewLine();
		    
		    if(destHas.size()>0) {
		    	PrintUtils.print("[" + dest + "]flow_conf1�����[" + orig + "]flow_conf�����ļ���");
		    	PrintUtils.printList(destHas);
		    }
			
			int count=0;
			Map<String, String> confButDiff = new HashMap<String, String>();
			PrintUtils.print("��ʼ�Ƚ��ļ�");
			for(String origFile : origFiles) {
				if(!listHas(origHas, origFile) && !confButDiff.containsKey(origFile)) {
					byte[] origContent = TarFileUtils.getFileContentFromTar(orig, origFile);
					
					String orig1File = origFile.replace(TarConstants.PATH_FLOW_FLAG, TarConstants.PATH_FLOW1_FLAG);
					byte[] destContent = TarFileUtils.getFileContentFromTar(dest, orig1File);
					if(!Arrays.equals(origContent, destContent)) {
						confButDiff.put(origFile, orig1File);
					}
					if(++count%TarConstants.DEALFILE_PRINT==0 && count>0) {
						PrintUtils.print("�Ѿ������ļ�����" + count);
					}
				}
			}
			for(String destFile : destFiles) {
				if(!listHas(destHas, destFile) && confButDiff.containsValue(destFile)) {
					String dest1File = destFile.replace(TarConstants.PATH_FLOW1_FLAG, TarConstants.PATH_FLOW_FLAG);
					byte[] origContent = TarFileUtils.getFileContentFromTar(orig, dest1File);
					byte[] destContent = TarFileUtils.getFileContentFromTar(dest, destFile);
					if(!Arrays.equals(origContent, destContent)) {
						confButDiff.put(dest1File, destFile);
					}
					if(++count%TarConstants.DEALFILE_PRINT==0 && count>0) {
						PrintUtils.print("�Ѿ������ļ�����" + count);
					}
				}
			}
			PrintUtils.print("������ɣ������ļ�����" + count);
			PrintUtils.print("[" + orig + "]��[" + dest + "]�����ļ���");
			PrintUtils.printMap(confButDiff);
			
		    PrintUtils.printLine();
		    
		    //��Ž�����ݸ���һ��valve
		    context.setTemp(TarConstants.CONF_FILE_DIFF, confButDiff);
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
				String s2 = s.replace(TarConstants.PATH_FLOW_FLAG, TarConstants.PATH_FLOW1_FLAG);
				String s3 = s1.replace(TarConstants.PATH_FLOW_FLAG, TarConstants.PATH_FLOW1_FLAG);
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
