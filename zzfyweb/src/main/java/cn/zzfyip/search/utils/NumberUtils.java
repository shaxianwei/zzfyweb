package cn.zzfyip.search.utils;

import java.text.DecimalFormat;

/**
 * 实现描述：
 * 
 * @author qiaoyun.lv
 * @version v1.0.0
 * @see
 * @since 下午2:13
 */
public class NumberUtils {

    private final static DecimalFormat decimalFormat = new DecimalFormat("###,###.##");

    /**
     * 将数字格式化为###,###的形式
     * 
     * @param amount
     * @return
     */
    public static String formatMoney(double amount) {
        try {
            return NumberUtils.decimalFormat.format(amount);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将数字格式化为###,###的形式
     * 
     * @param amount
     * @return
     */
    public static String formatMoney(long amount) {
        try {
            return NumberUtils.decimalFormat.format(amount);
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        String s = NumberUtils.decimalFormat.format(123);
        System.out.print(s);
    }
}
