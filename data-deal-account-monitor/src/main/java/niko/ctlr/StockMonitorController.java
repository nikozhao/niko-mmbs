package niko.ctlr;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import niko.monitor.DealAccountMonitor;
import niko.monitor.Monitor;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2019/2/28
 */
@RestController
public class StockMonitorController {
    @Autowired
    private Monitor dealAccountMonitor;
    @Autowired
    private Monitor peBottomMonitor;

    @RequestMapping("/stock/monitor/dealAmount")
    public Object dealAmount() {
        try {
            dealAccountMonitor.run(null);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/stock/monitor/peBottom")
    public Object peBottom() {
        try {
            peBottomMonitor.run(new JSONObject());
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
