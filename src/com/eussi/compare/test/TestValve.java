package com.eussi.compare.test;

import com.eussi.core.IValveContext;
import com.eussi.core.impl.ValveBase;
import com.eussi.utils.PrintUtils;

public class TestValve extends ValveBase{

	@Override
	public String getInfo() {
		return "��������";
	}

	@Override
	public void invokeHook(String orig, String dest, IValveContext context) {
		PrintUtils.print(getInfo());
		PrintUtils.print(orig);
		PrintUtils.print(dest);
		
	}
}
