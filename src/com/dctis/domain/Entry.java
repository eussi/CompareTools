package com.dctis.domain;

import java.util.List;

/**
 * 
 * @author wangxueming
 *
 */
public class Entry {
	private String command;
	private List<String> flows;
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public List<String> getFlows() {
		return flows;
	}
	public void setFlows(List<String> flows) {
		this.flows = flows;
	}
	
	
}
