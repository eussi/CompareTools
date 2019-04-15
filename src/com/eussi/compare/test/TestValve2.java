package com.eussi.compare.test;

import com.eussi.core.IValveContext;
import com.eussi.core.impl.ValveBase;
import com.eussi.utils.PrintUtils;

public class TestValve2 extends ValveBase{

	@Override
	public String getInfo() {
		return "��������2";
	}

	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
		PrintUtils.print(getInfo());
		PrintUtils.print(orig);
		PrintUtils.print(dest);
		
	}
}
