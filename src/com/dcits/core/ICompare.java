package com.dcits.core;
/**
 * 
 * @author wangxueming
 *
 */
public interface ICompare {
	/**
	 * 比较两文件是否一致
	 * @param orig 比较文件1
	 * @param dest 比较文件2
	 * @return 是否一致
	 */
	public void compare(String orig, String dest);
}
