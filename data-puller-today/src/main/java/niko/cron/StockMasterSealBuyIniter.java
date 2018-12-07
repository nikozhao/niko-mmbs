package niko.cron;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import niko.bo.InitParam;
import niko.bo.InitParamKey;
import niko.bo.tx.InitParamBo;
import niko.bo.tx.InitParamKeyBo;
import niko.callback.StockMasterCallBack;
import niko.callback.TodayCallBack;
import niko.dao.repository.StockMasterRepository;
import niko.server.StockService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import util.StockUtil;

/**
 * @Author: Niko Zhao
 * @Date: Create in 04/11/18
 * @Email:
 */

/**
 * the timer to init the StockDay
 */
@Configuration
@EnableScheduling
@Slf4j
public class StockMasterSealBuyIniter {

    @Autowired
    private StockService stockService;
    @Autowired
    private StockMasterCallBack stockMasterCallBack;

    //time cron
    @Scheduled(cron = "* */2 * * * ?")
    public void init() {
        Boolean flag = check();
        //flag=true;
        if (flag) {
            InitParamKey szKey= initParam("sz");
            pull(szKey);
            InitParamKey shKey= initParam("sh");
            pull(shKey);
        }
    }

    private void pull(InitParamKey initParamKey) {
        while (stockService.hasNext(initParamKey)){
            List<InitParam> initParams = stockService.getInitParam(initParamKey);
            StringBuffer sb = new StringBuffer();
            Optional.of(stockService.getInitParam(initParamKey)).orElseGet(()->Lists.newArrayList())
                    .forEach(initParam->{
                        if (sb.length() > 0) {
                            sb.append(",");
                        }
                        String stockNo = getStock(initParam);
                        sb.append("ff_");
                        sb.append(StockUtil.getSinaStockNo(stockNo));
                    });
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(getUrl(sb.toString())).build();
            boolean flag = true;
            try {
                Response response = client.newCall(request).execute();
                flag = stockMasterCallBack.onResponse(response,initParamKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!flag){
                log.info("stop to pull data ,the url==> {}",getUrl(sb.toString()));
                break;
            }
            sb.delete(0, sb.length());
            initParamKey = stockService.refushKey(initParamKey);
        }
    }

    private InitParamKey initParam(String pre) {
        InitParamKeyBo initParamKeyBo = new InitParamKeyBo();
        initParamKeyBo.setKey(1);
        initParamKeyBo.setPreString(pre);
        return initParamKeyBo;
    }

    private String getStock(InitParam initParam) {
        InitParamBo initParamBo = (InitParamBo)initParam;
        return initParamBo.getStockNo();
    }

    private String getUrl(String stockNo) {
        return StockUtil.url + stockNo;
    }

    //check time .hour need gt 15. as the stock transaction in 9:15 - 15:00
    private Boolean check() {
        boolean f = false;
        Calendar cl = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String[] dateStrings = sdf.format(cl.getTime()).split(":");
        if (Integer.parseInt(dateStrings[0]) >= 15) {
            f = true;
        }
        return f;
    }
}
