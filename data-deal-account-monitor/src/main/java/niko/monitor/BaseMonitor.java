package niko.monitor;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import niko.dao.entry.Stock;
import niko.dao.entry.StockDay;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2019/3/15
 */
@Slf4j
public abstract class BaseMonitor implements Monitor {
    volatile CountDownLatch countDownLatch;
    @Autowired
    ExecutorService threadPoolExecutor;

    public void run(JSONObject o) {
        final Set<String> set = new HashSet<>();
        List<Stock> stocks = getMonitorStock(o);
        countDownLatch = new CountDownLatch(stocks.size());
        for (Stock stock : stocks) {
            try {
                threadPoolExecutor.submit(() -> {
                    try {
                        List<StockDay> stockDays = getMonitorStockDay(stock.getStockNo());
                        if (CollectionUtils.isEmpty(stockDays)) {
                            log.info("stockday is empty.stock no {}", stock.getStockNo());
                            return;
                        }
                        Boolean flag = filter(stockDays);
                        if (flag) {
                            set.add(stock.getStockNo());
                        }
                    } catch (Exception e) {
                        log.info("monitor error", e);
                    } finally {
                        log.info("process stock no {},countDownLatch {}", stock.getStockNo(), countDownLatch.getCount());
                        countDownLatch.countDown();
                    }
                });
            } catch (Exception e) {
                countDownLatch.countDown();
                log.error("stock no :{} ,selector error!", stock.getStockNo());
            }
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        consumer(set);
    }


    protected abstract List<Stock> getMonitorStock(JSONObject o);

    protected abstract List<StockDay> getMonitorStockDay(String stockNo);

    protected abstract Boolean filter(List<StockDay> stockDays);

    protected  void consumer(Set<String> set){
        String s = set.stream().collect(Collectors.joining(","));
        log.info(s);
    }


}
