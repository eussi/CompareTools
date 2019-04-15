package com.eussi.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.eussi.domain.Entry;

/**
 * 
 * @author wangxueming 2018-09-26
 *
 */
public class FileUtils {
	/**
	 * ��ȡ�����ļ�
	 * 
	 * @param ifd
	 */
	@SuppressWarnings("unchecked")
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
					List<String> flows = new ArrayList<String>();
					List<Node> flowNodes = entryNode.selectNodes("flows/flow");
					if (flowNodes != null) {
						for (Node flowNode : flowNodes) {
							flows.add(flowNode.getText());
						}
					}
					entry.setFlows(flows);
					entries.put(entry.getCommand(), entry);
				}
			}
			return entries;
		} catch (Exception e) {
//			e.printStackTrace();
			PrintUtils.print("�����ļ�[entries.xml]��������" + e.getMessage());
		}
		return null;
	}
	
	public static String[] read(String fileName) {
		try {
			List<String> contents = new ArrayList<String>();
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = br.readLine()) != null) {
				contents.add(line);
			}
			return (String[]) contents.toArray(new String[]{});
		} catch (Exception e) {
			System.err.println("error reading " + fileName + ": " + e);
			System.exit(1);
		}
		return null;
	}
	
	/**
	 * ��֤�ļ��Ƿ�ΪUNIX��ʽ�ļ� UNIX���з�\n OS���з�\r Window���з� \r\n \r 13, \n 10
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static boolean isUnixFile(String content) {
		int index = content.indexOf(10); // "\n"�ַ�
		if (index == -1)
			return false;
		int nextIndex = index - 1;
		if(nextIndex == -1)         //2018-04-14 ����÷����ж�Unix�ļ���һ��Ϊ���б���bug
			return true;
		char nextChar = content.charAt(nextIndex);
		if (nextChar == 13) // "\r"�ַ�
			return false;
		return true;
	}
}
