package com.dctis.compare.tar;

import com.dctis.core.IValve;
import com.dctis.core.IValveContext;

public class FlowFileNameValve  implements IValve{

	@Override
	public String getInfo() {
		return "比较Flow版本包中文件数是否一致";
	}

	@Override
	public void invoke(String orig, String dest, IValveContext context) {
		
		
		
		//执行下一个Valve
		context.invokeNext(orig, dest);
	}

}
