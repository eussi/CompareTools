package com.dctis.compare.tar;

import com.dctis.core.IValve;
import com.dctis.core.IValveContext;

public class FlowFileNameValve  implements IValve{

	@Override
	public String getInfo() {
		return "�Ƚ�Flow�汾�����ļ����Ƿ�һ��";
	}

	@Override
	public void invoke(String orig, String dest, IValveContext context) {
		
		
		
		//ִ����һ��Valve
		context.invokeNext(orig, dest);
	}

}
