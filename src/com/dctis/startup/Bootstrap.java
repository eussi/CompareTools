package com.dctis.startup;

import java.util.List;
import java.util.Map;

import com.dctis.compare.CompareBase;
import com.dctis.constants.Constants;
import com.dctis.core.IValve;
import com.dctis.domain.Entry;
import com.dctis.utils.FileUtils;
import com.dctis.utils.PrintUtils;

/**
 * 比较工具启动类
 * @author wangxueming 2018-09-26
 *
 */
public class Bootstrap {
	
	public static void main(String[] args) {
		if(args.length!=3) {
			PrintUtils.print("Usage:need three args, command|compareFilePath1|compareFilePath2. ");
			System.exit(0);
		}
		//获取命令与实现类的map
		Map<String, Entry> entryMap = FileUtils.readEntries(Constants.ENTRY_PATH);
		String command = args[0].trim();
		Entry entry = entryMap.get(command);
		if(entry==null) {
			PrintUtils.print("No command found. Please check and try again. ");
			System.exit(0);
		}
		
		//获取阀实现类并添加
		CompareBase cmb = new CompareBase();
		List<String> flows = entry.getFlows();
		if(flows.size()==0) {
			PrintUtils.print("The process is empty and retry after the configuration is cleared. ");
			System.exit(0);
		}
		for(String flow : flows) {
			try {
				Class clazz = Class.forName(flow);
				IValve valve = (IValve) clazz.newInstance();
				cmb.addValve(valve);
			} catch (Exception e) {
//				e.printStackTrace();
				PrintUtils.print("Create instance exception: " + e.getMessage());
			} 
		}
		
		//依次调用处理流程开始比较
		String orig = args[1].trim();
		String dest = args[2].trim();
		cmb.compare(orig, dest);
		
	}
}
