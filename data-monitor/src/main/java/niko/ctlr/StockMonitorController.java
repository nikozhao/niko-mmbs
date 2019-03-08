package niko.ctlr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import niko.monitor.DealAccountMonitor;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2019/2/28
 */
@RestController
public class StockMonitorController {
    @Autowired
    private DealAccountMonitor dealAccountMonitor;

    @RequestMapping("/stock/monitor")
    public Object test() {
        try {
            dealAccountMonitor.run();
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
