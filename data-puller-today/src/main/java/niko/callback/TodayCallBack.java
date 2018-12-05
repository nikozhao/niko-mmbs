package niko.callback;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import niko.bo.InitParamKey;
import niko.bo.tx.InitParamKeyBo;
import niko.dao.entry.Stock;
import niko.dao.entry.StockDay;
import niko.dao.entry.StockWorkday;
import niko.dao.repository.StockDayRepository;
import niko.dao.repository.StockRepository;
import niko.dao.repository.StockWorkdayRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import util.StringUtil;

/**
 * @Author: Niko Zhao
 * @Date: Create in 04/11/18
 * @Email:
 */

/**
 * the callback after send http request to get the stock's price info
 */
@Component
@Slf4j
public class TodayCallBack{
    @Autowired
    StockDayRepository stockDayRepository;
    @Autowired
    StockRepository stockRepository;
    @Autowired
    StockWorkdayRepository stockWorkdayRepository;

    public boolean onResponse(Response repsonse, InitParamKey initParamKey) throws IOException {
        String context = repsonse.body().string().trim();
        InitParamKeyBo initParamKeyBo = (InitParamKeyBo)initParamKey;
        /**
         * 没有结果，结束循环
         */
        if(context.startsWith("v_pv_none_match") && initParamKeyBo.getKey()>5000){
            log.info("end to pull data ==>{}",context);
            return false;
        }
        String[] strs = context.split("=");
        int index=0;
        for (int i = 1; i < strs.length; i++) {
            String[] stocks = strs[i].split("~");
            if(stocks.length<2){
                log.info("get the today stock exit,->{}",strs[i]);
                continue;
            }
            if (!ObjectUtils.isEmpty(stocks) && stocks.length > 0) {
                String stockNo = stocks[2];
                List<Stock> tmp = stockRepository.findByStockNo(stockNo);
                //if the stock don't exit.then insert into db
                if(ObjectUtils.isEmpty(tmp) ){
                    Stock stock = new Stock();
                    stock.setStockNo(stockNo);
                    stock.setStockName(stocks[1]);
                    stockRepository.save(stock);
                }

                List<StockDay> s = stockDayRepository.findByStockNoAndDay(stockNo,getToday());
                if(!ObjectUtils.isEmpty(s)){
                    continue;
                }
                //save the stockDay info
                StockDay stockDay= new StockDay();
                stockDay.setStockNo(stockNo);
                stockDay.setMin(StringUtil.cover(stocks[33]));
                stockDay.setMax(StringUtil.cover(stocks[34]));
                stockDay.setDay(new Timestamp(getToday().getTime()));
                stockDay.setPe(StringUtil.cover(stocks[39]));
                stockDay.setPb(StringUtil.cover(stocks[46]));
                stockDay.setDealAccount(StringUtil.cover(stocks[37]));
                stockDay.setTotalAccount(StringUtil.cover(stocks[45]));
                stockDay.setTotalTradableAccount(StringUtil.cover(stocks[44]));
                stockDay.setRiseRatio(StringUtil.cover(stocks[32]));
                //stockDay.setNumberRatio(BigDecimal.ZERO);
                stockDay.setOutDeal(StringUtil.cover(stocks[7]));
                stockDay.setInDeal(StringUtil.cover(stocks[8]));
                stockDay.setYPrice(StringUtil.cover(stocks[4]));
                stockDay.setTPriceStart(StringUtil.cover(stocks[5]));
                stockDay.setTPriceEnd(StringUtil.cover(stocks[3]));
                stockDay.setTPricePre(getPre(stocks[35]));
                stockDay.setMaxMinPrice(StringUtil.cover(stocks[43]));
                stockDay.setSellBuyReatio(StringUtil.cover(stocks[38]));
                stockDayRepository.save(stockDay);
            }
            Date date = getToday();
            List<StockWorkday> stockWorkdays= stockWorkdayRepository.findByDay(getToday());
            if(ObjectUtils.isEmpty(stockWorkdays)){
                StockWorkday stockWorkday = new StockWorkday();
                stockWorkday.setDay(new Timestamp(date.getTime()));
                stockWorkdayRepository.save(stockWorkday);
            }
        }
        return true;
    }

    private BigDecimal getPre(String str) {
        if(Strings.isNotBlank(str)){
            return BigDecimal.ZERO;
        }
        String [] strs = str.split("/");
        Double pre =Double.parseDouble(strs[2])/Double.parseDouble(strs[1]);
        return BigDecimal.valueOf(pre);
    }

    private Date getToday() {
        Calendar cl = Calendar.getInstance();
        int hour = cl.get(Calendar.HOUR_OF_DAY);
        if(hour<9){
            cl.add(Calendar.DATE, -1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        try {
            date =sdf.parse(sdf.format(cl.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


}
