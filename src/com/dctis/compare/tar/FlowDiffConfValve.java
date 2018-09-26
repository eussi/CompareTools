package com.dctis.compare.tar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dctis.compare.ValveBase;
import com.dctis.compare.tar.constants.TarConstants;
import com.dctis.compare.tar.utils.TarFileUtils;
import com.dctis.core.IValveContext;
import com.dctis.utils.PrintUtils;

public class FlowDiffConfValve extends ValveBase{

	@Override
	public String getInfo() {
		return "�Ƚ�Flow�汾����flow_conf��flow_conf1";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
			PrintUtils.print(getInfo());
			PrintUtils.printLine();
			//��ȡ��һ��valve����ȡ������
			List<String> origGetFiles = (List<String>) context.getTemp(TarConstants.ORIG_FILES);
			List<String> destGetFiles = (List<String>) context.getTemp(TarConstants.DEST_FILES);
			
			//ɸѡ����flow_conf���ļ�
			List<String> origFiles = new ArrayList<String>();   //�������flow_conf��������flow_conf1
			for(String s : origGetFiles) {
				if(s.contains(TarConstants.PATH_FLOW_FLAG))
					origFiles.add(s);
			}
			List<String> destFiles = new ArrayList<String>();
			for(String s : destGetFiles) {
				if(s.contains(TarConstants.PATH_FLOW1_FLAG))
					destFiles.add(s);
			}
			List<String> origHas = compareList(origFiles, destFiles);
			List<String> destHas = compareList(destFiles, origFiles);
			
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
			PrintUtils.print("[" + dest + "]��[" + orig + "]�����ļ���");
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
