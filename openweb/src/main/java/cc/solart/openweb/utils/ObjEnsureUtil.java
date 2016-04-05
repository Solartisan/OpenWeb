package cc.solart.openweb.utils;

import java.util.List;

/**
 * Created by imilk on 15/6/9.
 */
public class ObjEnsureUtil {

    /**
     * 检测Object数组中是否有值为空
     * @param arrayOfObject
     * @return
     */
    public static boolean ensureNonNull(Object[] arrayOfObject){
        if (arrayOfObject == null) {
            return false;
        }

        for(Object obj:arrayOfObject){
            if(obj==null){
                return  false;
            }
        }
       return true;
    }

}
