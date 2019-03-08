package niko.monitor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import lombok.extern.slf4j.Slf4j;
import niko.dao.entry.Stock;
import niko.dao.entry.StockDay;
import niko.dao.repository.StockDayRepository;
import niko.dao.repository.StockRepository;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2019/2/19
 */
@Configuration
//@EnableScheduling
@Slf4j
public class DealAccountMonitor implements InitializingBean {

    @Autowired
    StockDayRepository stockDayRepository;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    ExecutorService threadPoolExecutor;
    volatile CountDownLatch countDownLatch;

    //@Scheduled(cron = "* */2 * * * ?")
    public void run() {
        List<Stock> stocks = stockRepository.findAll();
        // stocks = CollectionUtils.isEmpty(stocks)?Collections.EMPTY_LIST:stocks.subList(0,5);
        countDownLatch = new CountDownLatch(stocks.size());
        final Set<StockDay> set = new HashSet<>();
        Map<String, BigDecimal> rsMap = new HashMap<>();
        //countDownLatch = new CountDownLatch(stocks.size());
        //stocks.stream().forEach(stock -> {
        for (Stock stock : stocks) {
            try {
                threadPoolExecutor.submit(() -> {
                    try {
                        List<StockDay> stockDays = stockDayRepository.findLastFiveDataByStockNo(stock.getStockNo());
                        if (CollectionUtils.isEmpty(stockDays)) {
                            log.info("stockday is empty.stock no {}", stock.getStockNo());
                            return;
                        }
                        StockDay stockDay = stockDays.remove(0);
                        BigDecimal rs = new BigDecimal(stockDay.getRiseRatio().doubleValue());
                        for (StockDay stockDay1 : stockDays) {
                            if (ObjectUtils.isEmpty(stockDay.getDealAccount()) || ObjectUtils.isEmpty(stockDay1.getDealAccount())) {
                                continue;
                            }
                            rs = rs.add(stockDay1.getRiseRatio());
                            if (stockDay.getDealAccount().intValue() > stockDay1.getDealAccount().intValue() * 5
                                    && stockDay1.getRiseRatio().doubleValue() < 9.5
                                    && stockDay1.getDealAccount().intValue() != 0) {
                                set.add(stockDay);
                            }
                        }
                        rsMap.put(stock.getStockNo(), rs);
                    } catch (Exception e) {
                        log.info("monitor error", e);
                    } finally {
                        log.info("process stock no {},countDownLatch {}", stock.getStockNo(), countDownLatch.getCount());
                        countDownLatch.countDown();
                    }
                });
                //countDownLatch.countDown();
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
        StringBuffer sb = new StringBuffer();

        CountDownLatch countDownLatch1 = new CountDownLatch(set.size());
        set.stream().forEach(stockDay -> {
            try {
                if (rsMap.get(stockDay.getStockNo()).intValue() < 15) {
                    sb.append(stockDay.getStockNo()).append(",");
                }
                log.info("MVP stock {} {} {}", stockDay.getStockName(), stockDay.getStockNo(), stockDay.getDealAccount());
            } catch (Exception e) {
                log.info("totel error!", e);
            } finally {

                countDownLatch1.countDown();
            }
        });
        try {
            countDownLatch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info(sb.toString());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(7.12);
        BigDecimal b = new BigDecimal(9.99);
        BigDecimal c = new BigDecimal(-10);
        BigDecimal d = new BigDecimal(9.99);
        BigDecimal e = new BigDecimal(9.98);
        System.out.println(a.add(b).add(c).add(d).add(e).intValue());
    }

    /*class Selector implements Callable<String> {
        public Selector(String stockNo, Set set) {
            this.stockNo = stockNo;
            this.set = set;
        }

        private String stockNo;
        private Set<StockDay> set;
        @Override
        public String call() throws Exception {
            log.info("process stock no {},countDownLatch {}",stockNo,countDownLatch.getCount());
            List<StockDay> stockDays = stockDayRepository.findLastFiveDataByStockNo(stockNo);
            StockDay stockDay = stockDays.get(0);
            for (StockDay stockDay1 : stockDays) {
                if (stockDay.getDealAccount().divide(stockDay1.getDealAccount()).intValue() >= 5) {
                    set.add(stockDay);
                    countDownLatch.countDown();
                    return stockNo;
                }
            }
            countDownLatch.countDown();
            return "";
        }
    }*/

}
