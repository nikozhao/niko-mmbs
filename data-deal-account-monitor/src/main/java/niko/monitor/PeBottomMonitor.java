package niko.monitor;

import com.google.common.base.Strings;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @Date: Create in 2019/3/19
 */
@Service
@Slf4j
public class PeBottomMonitor extends BaseMonitor {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockDayRepository stockDayRepository;
    @Override
    protected List<Stock> getMonitorStock(JSONObject o) {
        String stocks = o.getString("stocks");
        if(Strings.isNullOrEmpty(stocks)){
           // return stockRepository.findByStockNo("600792");
            return stockRepository.findAll();
        }
        return null;
    }

    @Override
    protected List<StockDay> getMonitorStockDay(String stockNo) {
        return stockDayRepository.findByStockNoAndLimit(stockNo,100);
    }

    @Override
    protected Boolean filter(List<StockDay> stockDays) {
        Integer count =0;
        StockDay today = stockDays.remove(0);
        if(today.getPe().doubleValue()<0){
            return false;
        }
        for(StockDay stockDay:stockDays){
            if(stockDay.getPe().doubleValue()<today.getPe().doubleValue()){
                count = count+1;
            }
            if(count>3){
                return false;
            }
        }
        return true;
    }

    @Override
    protected void consumer(Set<String> set) {
        String s = set.stream().collect(Collectors.joining(","));
        log.info(s);
    }
}
