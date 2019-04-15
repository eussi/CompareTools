package com.eussi.core.impl;

import com.eussi.core.IValve;
import com.eussi.core.IValveContext;

public abstract class ValveBase implements IValve{
		
	@Override
	public abstract String getInfo();

	@Override
	public void invoke(String orig, String dest, IValveContext context) {
		//��������
		invokeHook(orig, dest, context);
		//ִ����һ��Valve
		context.invokeNext(orig, dest);
	}
	
	public abstract void invokeHook(String orig, String dest, IValveContext context);

}
