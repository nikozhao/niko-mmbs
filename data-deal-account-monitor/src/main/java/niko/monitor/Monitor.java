package niko.monitor;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2019/3/15
 */
public interface Monitor {
    public void run(JSONObject o);
}
