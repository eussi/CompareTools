package com.dctis.utils;

import java.util.List;

/**
 * 
 * @author wangxueming
 *
 */
public class PrintUtils {
	//´òÓ¡×Ö·û´®Êý×é
	public static void printList(List<String> list) {
		for(String s : list) {
			print("[" + s + "]");
		}
	}
	
	public static void printStep(String s) {
		print("===================================" + s + "===================================");
	}

	public static void printLine() {
		print("------------------------------------------------------------------------------------");
	}
	
	public static void printMsg(String s) {
		printAndLine(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + s+ "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	
	//
	public static void print(String s) {
		System.out.println(s);
	}
	public static void printAndLine(String s) {
		System.out.println(s);
		System.out.println();
	}
	public static void printNewLine() {
		System.out.println();
	}
	
}
