package com.dctis.compare;

import java.util.HashMap;
import java.util.Map;

import com.dctis.core.IPipeline;
import com.dctis.core.IValve;
import com.dctis.core.IValveContext;
/**
 * from tomcat
 * @author wangxueming 2019-09-26
 *
 */
public class SimplePipeline implements IPipeline {

  // the array of Valves
  protected IValve valves[] = new IValve[0];
  
  public void addValve(IValve IValve) {
      IValve results[] = new IValve[valves.length +1];
      System.arraycopy(valves, 0, results, 0, valves.length);
      results[valves.length] = IValve;
      valves = results;
  }

  public IValve[] getValves() {
    return valves;
  }

  public void invoke(String orig, String dest){
    // Invoke the first IValve in this pipeline for this request
    (new SimplePipelineValveContext()).invokeNext(orig, dest);
  }

  public void removeValve(IValve IValve) {
  }

  // this class is copied from org.apache.catalina.core.StandardPipeline class's
  // StandardPipelineValveContext inner class.
  protected class SimplePipelineValveContext implements IValveContext {

    protected int stage = 0;
    
    //用于存放需要储存的内容
    private Map<String, Object> temp = new HashMap<String, Object>();
    
    public String getInfo() {
      return null;
    }

    public void invokeNext(String orig, String dest){
      int subscript = stage;
      stage = stage + 1;
      if (subscript < valves.length) {
        valves[subscript].invoke(orig, dest, this);
      }
    }

	@Override
	public Object getTemp(String key) {
		return temp.get(key);
	}

	@Override
	public void setTemp(String key, Object value) {
		temp.put(key, value);
	}
    
  } // end of inner class

}