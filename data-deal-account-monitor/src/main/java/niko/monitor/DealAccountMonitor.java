package niko.monitor;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import niko.dao.entry.Stock;
import niko.dao.entry.StockDay;
import niko.dao.repository.StockDayRepository;
import niko.dao.repository.StockRepository;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2019/2/19
 */
@Service
@Slf4j
public class DealAccountMonitor extends BaseMonitor implements Monitor{

    @Autowired
    StockDayRepository stockDayRepository;
    @Autowired
    StockRepository stockRepository;


    @Override
    protected List<Stock> getMonitorStock(JSONObject o) {
        return stockRepository.findAll();
    }

    @Override
    protected List<StockDay> getMonitorStockDay(String stockNo) {
        return stockDayRepository.findByStockNoAndLimit(stockNo,5);
    }

    @Override
    protected Boolean filter(List<StockDay> stockDays) {
        StockDay stockDay = stockDays.remove(0);
        BigDecimal rs = new BigDecimal(stockDay.getRiseRatio().doubleValue());
        boolean flag = false;
        for (StockDay stockDay1 : stockDays) {
            if (ObjectUtils.isEmpty(stockDay.getDealAccount()) || ObjectUtils.isEmpty(stockDay1.getDealAccount())) {
                continue;
            }
            rs = rs.add(stockDay1.getRiseRatio());
            if (stockDay.getDealAccount().intValue() > stockDay1.getDealAccount().intValue() * 5
                    && stockDay1.getRiseRatio().doubleValue() < 9.5
                    && stockDay1.getDealAccount().intValue() != 0) {
                flag = true;
            }
        }
        if(flag && rs.intValue()>15){
            flag = false;
        }
        return flag;
    }

    @Override
    protected void consumer(Set<String> set) {
        String s = set.stream().collect(Collectors.joining(","));
        log.info(s);
    }
}
