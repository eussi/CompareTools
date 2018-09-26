package com.dctis.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.dctis.domain.Entry;

/**
 * 
 * @author wangxueming 2018-09-26
 *
 */
public class FileUtils {
	/**
	 * 读取配置文件
	 * 
	 * @param ifd
	 */
	public static Map<String, Entry> readEntries(String path) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new File(path));
			List<Node> entryNodes = document.selectNodes("/entries/entry");
			Map<String, Entry> entries = new HashMap<String, Entry>();
			if (entryNodes != null) {
				for (Node entryNode : entryNodes) {
					Entry entry = new Entry();
					Node commandNode = entryNode.selectSingleNode("command");
					if(commandNode!=null) {
						entry.setCommand(commandNode.getText());
					}
					Node implementationNode = entryNode.selectSingleNode("implementation");
					if(implementationNode!=null) {
						entry.setImplementation(implementationNode.getText());
					}
					entries.put(entry.getCommand(), entry);
				}
			}
			return entries;
		} catch (Exception e) {
//			e.printStackTrace();
			PrintUtils.print("配置文件[entries.xml]解析错误：" + e.getMessage());
		}
		return null;
	}
}
