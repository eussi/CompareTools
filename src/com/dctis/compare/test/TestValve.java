package com.dctis.compare.test;

import com.dctis.core.IValveContext;
import com.dctis.core.impl.ValveBase;
import com.dctis.utils.PrintUtils;

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
