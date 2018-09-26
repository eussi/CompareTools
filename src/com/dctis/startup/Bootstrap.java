package com.dctis.startup;

import java.util.Map;

import com.dctis.constants.Constants;
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
		} else {
			//��ȡʵ����
		}
	}
}
