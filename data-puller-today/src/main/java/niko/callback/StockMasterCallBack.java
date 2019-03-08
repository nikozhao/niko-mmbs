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

import lombok.extern.slf4j.Slf4j;
import niko.bo.InitParamKey;
import niko.bo.tx.InitParamKeyBo;
import niko.dao.entry.Stock;
import niko.dao.entry.StockDay;
import niko.dao.entry.StockMaster;
import niko.dao.entry.StockWorkday;
import niko.dao.repository.StockDayRepository;
import niko.dao.repository.StockMasterRepository;
import niko.dao.repository.StockRepository;
import niko.dao.repository.StockWorkdayRepository;
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
public class StockMasterCallBack {
    @Autowired
    StockMasterRepository stockMasterRepository;
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
                String stockNo = stocks[0].replaceAll("sz","")
                        .replaceAll("sh","").replaceAll("\"","");
                List<Stock> tmp = stockRepository.findByStockNo(stockNo);
                //if the stock don't exit.then insert into db
                if(ObjectUtils.isEmpty(tmp) ){
                    Stock stock = new Stock();
                    stock.setStockNo(stockNo);
                    stock.setStockName(stocks[1]);
                    stockRepository.save(stock);
                }

                List<StockMaster> s = stockMasterRepository.findByStockNoAndDay(stockNo,getToday());
                if(!ObjectUtils.isEmpty(s)){
                    continue;
                }
                //save the stockDay info
                StockMaster stockMaster= new StockMaster();
                stockMaster.setStockNo(stockNo);
                stockMaster.setBigIn(StringUtil.cover(stocks[1]));
                stockMaster.setBigOut(StringUtil.cover(stocks[2]));
                stockMaster.setBig(StringUtil.cover(stocks[3]));
                stockMaster.setSmallIn(StringUtil.cover(stocks[5]));
                stockMaster.setSmallOut(StringUtil.cover(stocks[6]));
                stockMaster.setSmall(StringUtil.cover(stocks[7]));
                stockMaster.setDay(new Timestamp(getToday().getTime()));
                stockMasterRepository.save(stockMaster);
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
   /* public static void main(String[] args){
        String context = "v_ff_sz000725=\"sz000725~146314.59~109976.49~36338.10~7.81~318671.89~302517.24~16154.64~3.92~8854800000.00~1385057.44~960563.34~京东方Ａ~20190219~20190218^285045.38^206042.95~20190215^280451.66^153922.39~20190214^438225.16^369494.99~20190213^235020.66^121126.52~687355400.00~1663098200.00~20190219150006\";";
        String[] strs = context.split("=");
        int index=0;
        for (int i = 1; i < strs.length; i++) {
            String[] stocks = strs[i].split("~");
            String stockNo = stocks[0].replaceAll("sz","")
                    .replaceAll("sh","").replaceAll("\"","");
            System.out.println(stockNo);
        }
    }*/
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
