package com.dctis.compare.test;

import com.dctis.compare.ValveBase;
import com.dctis.core.IValve;
import com.dctis.core.IValveContext;
import com.dctis.utils.PrintUtils;

public class TestValve2 extends ValveBase{

	@Override
	public String getInfo() {
		return "≤‚ ‘¡˜≥Ã2";
	}

	@Override
	public void invokeHook(String orig, String dest) {
		PrintUtils.print(getInfo());
		PrintUtils.print(orig);
		PrintUtils.print(dest);
		
	}
}
