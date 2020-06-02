package com.raiden.util;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 22:35 2020/5/11
 * @Modified By:
 */
public final class DiscountFormatUtil {

    private static final String ZH_CN = "zh-CN";
    private static final String EN_US = "en-US";
    private static final String EMPTY = "";

    /**
     * 该方法只能处理 0-1之间的折扣 如果超出范围返回空
     * @param discount 比如 9.8 折就输入 0.98
     * @param language
     * @return
     */
   public static final String format(double discount,String language){
       if (discount > 0D && discount < 1D){
           if (ZH_CN.equals(language)){
               return String.format("%.1f", discount * 10);
           }else if (EN_US.equals(language)){
               return String.format("%.0f", discount * 100) + "%";
           }
       }
       return EMPTY;
   }
}
