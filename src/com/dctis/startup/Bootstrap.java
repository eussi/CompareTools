package com.dctis.startup;

import java.util.List;
import java.util.Map;

import com.dctis.constants.Constants;
import com.dctis.core.IValve;
import com.dctis.core.impl.CompareBase;
import com.dctis.domain.Entry;
import com.dctis.utils.FileUtils;
import com.dctis.utils.PrintUtils;

/**
 * �ȽϹ���������
 * @author wangxueming 2018-09-26
 *
 */
public class Bootstrap {
	
	public static void main(String[] args) {
		if(args.length!=3) {
			PrintUtils.print("Usage:need three args, command|compareFilePath1|compareFilePath2. ");
			System.exit(0);
		}
		//��ȡ������ʵ�����map
		Map<String, Entry> entryMap = FileUtils.readEntries(Constants.ENTRY_PATH);
		String command = args[0].trim();
		Entry entry = entryMap.get(command);
		if(entry==null) {
			PrintUtils.print("No command found. Please check and try again. ");
			System.exit(0);
		}
		
		//��ȡ��ʵ���ಢ���
		CompareBase cmb = new CompareBase();
		List<String> flows = entry.getFlows();
		if(flows.size()==0) {
			PrintUtils.print("The process is empty, Try again after configuration. ");
			System.exit(0);
		}
		for(String flow : flows) {
			try {
				Class<?> clazz = Class.forName(flow);
				IValve valve = (IValve) clazz.newInstance();
				cmb.addValve(valve);
			} catch (Exception e) {
//				e.printStackTrace();
				PrintUtils.print("Create instance exception: " + e.getMessage());
				PrintUtils.print("Program quit!!!");
				System.exit(0);
			} 
		}
		
		//���ε��ô������̿�ʼ�Ƚ�
		String orig = args[1].trim();
		String dest = args[2].trim();
		cmb.compare(orig, dest);
		
	}
}
