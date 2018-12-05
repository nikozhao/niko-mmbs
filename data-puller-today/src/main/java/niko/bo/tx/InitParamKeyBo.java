package niko.bo.tx;

import niko.bo.InitParamKey;
import lombok.Data;

/**
 * @Author: zhaozongqiang
 * @Date: Create in 2018/12/4
 */
@Data
public class InitParamKeyBo implements InitParamKey {
    private int key;
    private String preString;
}
