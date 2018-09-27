package com.dcits.compare.test;

import com.dcits.core.IValveContext;
import com.dcits.core.impl.ValveBase;
import com.dcits.utils.PrintUtils;

public class TestValve extends ValveBase{

	@Override
	public String getInfo() {
		return "≤‚ ‘¡˜≥Ã";
	}

	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
		PrintUtils.print(getInfo());
		PrintUtils.print(orig);
		PrintUtils.print(dest);
		
	}
}
