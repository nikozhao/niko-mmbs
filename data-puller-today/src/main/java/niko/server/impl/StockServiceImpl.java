package niko.server.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import niko.bo.InitParam;
import niko.bo.InitParamKey;
import niko.bo.tx.InitParamBo;
import niko.bo.tx.InitParamKeyBo;
import niko.server.StockService;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2018/12/4
 */
@Service
public class StockServiceImpl implements StockService {

    @Value("${requst.size:50}")
    private int size;

    @Override
    public boolean hasNext(InitParamKey key) {
        return true;
    }

    @Override
    public List<InitParam> getInitParam(InitParamKey key) {
        InitParamKeyBo initParamKeyBo = (InitParamKeyBo)key;
        List<InitParam> initParams = new ArrayList<>();
        for(int i=initParamKeyBo.getKey();i<=initParamKeyBo.getKey()+size;i++){
            InitParam initParam = buildInitParam(i,initParamKeyBo.getPreString());
            initParams.add(initParam);
        }
        return initParams;
    }

    private InitParam buildInitParam(int i, String preString) {
        InitParamBo initParamBo = new InitParamBo();
        initParamBo.setStockNo(buildStockNo(i,preString));
        return initParamBo;
    }

    private String buildStockNo(int index,String preString) {
        StringBuilder sb = new StringBuilder();
        if("sz".equalsIgnoreCase(preString)){
            sb.append("00");
        }else {
            sb.append("60");
        }
        for(int i =0;i<4-String.valueOf(index).length();i++){
            sb.append("0");
        }
        sb.append(index);
        return sb.toString();
    }

    @Override
    public InitParamKey refushKey(InitParamKey initParamKey) {
        InitParamKeyBo initParamKeyBo = (InitParamKeyBo)initParamKey;
        initParamKeyBo.setKey(initParamKeyBo.getKey()+size);
        return initParamKeyBo;
    }
}
