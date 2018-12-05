package niko.server;

import java.util.List;

import niko.bo.InitParam;
import niko.bo.InitParamKey;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2018/12/4
 */
public interface StockService {
    boolean hasNext(InitParamKey key);

    List<InitParam> getInitParam(InitParamKey key);

    InitParamKey refushKey(InitParamKey initParamKey);
}
