package com.dctis.core.impl;

import com.dctis.core.IValve;
import com.dctis.core.IValveContext;

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
