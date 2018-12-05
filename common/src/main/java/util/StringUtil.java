package util;

import com.google.common.base.Strings;

import java.math.BigDecimal;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2018/12/5
 */
public class StringUtil {

    public static BigDecimal cover(String value){
        if(Strings.isNullOrEmpty(value)){
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(Double.parseDouble(value));
    }
}
