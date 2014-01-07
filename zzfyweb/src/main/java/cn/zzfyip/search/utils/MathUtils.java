package cn.zzfyip.search.utils;

import java.math.BigDecimal;

/**
 * 数学精度工具
 * 
 * @author qing.liu
 * @version v1.0 2013/03/08
 */
public class MathUtils {

    /** 默认除法运算精度 */
    private static final int DEFAULT_DIV_SCALE = 10;

    /**
     * 提供精确的加法运算
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @return 两个参数的和，以字符串格式返回
     */
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 当取完2位精度后，value在数字上小于、等于或大于baseValue时，返回-1、0或1
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static int compareTo(double value, double baseValue) {
        return MathUtils.compareTo(value, baseValue, 2);
    }

    /**
     * 当取完精度后，value在数字上小于、等于或大于baseValue时，返回-1、0或1
     * 
     * @param value
     * @param baseValue
     * @param scale
     * @return
     */
    public static int compareTo(double value, double baseValue, int scale) {
        BigDecimal v1 = new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP);
        BigDecimal v2 = new BigDecimal(baseValue).setScale(scale, BigDecimal.ROUND_HALF_UP);
        return v1.compareTo(v2);
    }

    /**
     * 当取完2位精度后，value在数字上小于、等于或大于baseValue时，返回-1、0或1
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static int compareTo(String value, String baseValue) {
        return MathUtils.compareTo(value, baseValue, 2);
    }

    /**
     * 当取完精度后，value在数字上小于、等于或大于baseValue时，返回-1、0或1
     * 
     * @param value
     * @param baseValue
     * @param scale
     * @return
     */
    public static int compareTo(String value, String baseValue, int scale) {
        BigDecimal v1 = new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP);
        BigDecimal v2 = new BigDecimal(baseValue).setScale(scale, BigDecimal.ROUND_HALF_UP);
        return v1.compareTo(v2);
    }

    /**
     * 与0比较，相等返回true（2位精度处理）
     * 
     * @param value
     * @return
     */
    public static boolean compareWithZero(double value) {
        return MathUtils.isEqual(value, 0.00);
    }

    /**
     * 与0比较，相等返回true（2位精度处理）
     * 
     * @param value
     * @return
     */
    public static boolean compareWithZero(String value) {
        return MathUtils.isEqual(value, "0.00");
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后10位，以后的数字四舍五入，舍入模式采用ROUND_HALF_UP
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2) {
        return MathUtils.divide(v1, v2, MathUtils.DEFAULT_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入，舍入模式采用ROUND_HALF_UP
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @param scale 小数点后保留几位
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale) {
        return MathUtils.divide(v1, v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入，舍入模式采用用户指定舍入模式
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @param scale 小数点后保留几位
     * @param round_mode 指定的舍入模式
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale, int round_mode) {
        MathUtils.scaleCheck(scale);
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, round_mode).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，<br>
     * 精确到小数点以后10位，以后的数字四舍五入，舍入模式采用ROUND_HALF_UP
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2) {
        return MathUtils.divide(v1, v2, MathUtils.DEFAULT_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，<br>
     * 由scale参数指定精度，以后的数字四舍五入，舍入模式采用ROUND_HALF_UP
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @param scale 小数点后保留几位
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2, int scale) {
        return MathUtils.divide(v1, v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，<br>
     * 由scale参数指定精度，以后的数字四舍五入，舍入模式采用用户指定舍入模式
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @param scale 小数点后保留几位
     * @param round_mode 指定的舍入模式
     * @return 两个参数的商，以字符串格式返回
     */
    public static String divide(String v1, String v2, int scale, int round_mode) {
        MathUtils.scaleCheck(scale);
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, round_mode).toString();
    }

    /**
     * value是否等于baseValue（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isEqual(double value, double baseValue) {
        return MathUtils.compareTo(value, baseValue) == 0;
    }

    /**
     * value是否等于baseValue（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isEqual(String value, String baseValue) {
        return MathUtils.compareTo(value, baseValue) == 0;
    }

    /**
     * value是否小于等于baseValue（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isLessThan(double value, double baseValue) {
        return MathUtils.compareTo(value, baseValue) <= 0;
    }

    /**
     * value是否小于等于baseValue（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isLessThan(String value, String baseValue) {
        return MathUtils.compareTo(value, baseValue) <= 0;
    }

    /**
     * value是否小于等于0.00（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isLessThanZero(double value) {
        return MathUtils.isLessThan(value, 0.00);
    }

    /**
     * value是否小于等于0.00（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isLessThanZero(String value) {
        return MathUtils.isLessThan(value, "0.00");
    }

    /**
     * value是否大于等于baseValue（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isMoreThan(double value, double baseValue) {
        return MathUtils.compareTo(value, baseValue) >= 0;
    }

    /**
     * value是否大于等于baseValue（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isMoreThan(String value, String baseValue) {
        return MathUtils.compareTo(value, baseValue) >= 0;
    }

    /**
     * 是否大于等于1分钱（2位精度处理）
     * 
     * @param value
     * @return
     */
    public static boolean isMoreThanOne(double value) {
        return MathUtils.isMoreThan(value, 0.01);
    }

    /**
     * 是否大于等于1分钱（2位精度处理）
     * 
     * @param value
     * @return
     */
    public static boolean isMoreThanOne(String value) {
        return MathUtils.isMoreThan(value, "0.01");
    }

    /**
     * value是否不等于baseValue（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isNotEqual(double value, double baseValue) {
        return MathUtils.compareTo(value, baseValue) != 0;
    }

    /**
     * value是否不等于baseValue（2位精度处理）
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static boolean isNotEqual(String value, String baseValue) {
        return MathUtils.compareTo(value, baseValue) != 0;
    }

    /**
     * 提供精确的乘法运算
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @return 两个参数的积
     */
    public static double multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @return 两个参数的积，以字符串格式返回
     */
    public static String multiply(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).toString();
    }

    /**
     * 提供精确的小数位四舍五入处理，舍入模式采用ROUND_HALF_UP
     * 
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        return MathUtils.round(v, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理，舍入模式采用用户指定舍入模式
     * 
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @param round_mode 指定的舍入模式
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale, int round_mode) {
        MathUtils.scaleCheck(scale);
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, round_mode).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理，舍入模式采用ROUND_HALF_UP
     * 
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果，以字符串格式返回
     */
    public static String round(String v, int scale) {
        return MathUtils.round(v, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理，舍入模式采用用户指定舍入模式
     * 
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @param round_mode 指定的舍入模式
     * @return 四舍五入后的结果，以字符串格式返回
     */
    public static String round(String v, int scale, int round_mode) {
        if (v == null) {
            return null;
        }
        MathUtils.scaleCheck(scale);
        BigDecimal b = new BigDecimal(v);
        return b.setScale(scale, round_mode).toString();
    }

    /**
     * @param scale
     */
    private static void scaleCheck(int scale) {
        if (scale < 0) {
            String msg = "The scale must be a positive integer or zero";
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 原始值比较（未进行精度处理），value在数字上小于、等于或大于baseValue时，返回-1、0或1
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static int simpleCompareTo(double value, double baseValue) {
        BigDecimal v1 = new BigDecimal(value);
        BigDecimal v2 = new BigDecimal(baseValue);
        return v1.compareTo(v2);
    }

    /**
     * 原始值比较（未进行精度处理），value在数字上小于、等于或大于baseValue时，返回-1、0或1
     * 
     * @param value
     * @param baseValue
     * @return
     */
    public static int simpleCompareTo(String value, String baseValue) {
        BigDecimal v1 = new BigDecimal(value);
        BigDecimal v2 = new BigDecimal(baseValue);
        return v1.compareTo(v2);
    }

    /**
     * 提供精确的减法运算
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @return 两个参数的差
     */
    public static double subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算
     * 
     * @param v1 参数1
     * @param v2 参数2
     * @return 两个参数的差，以字符串格式返回
     */
    public static String subtract(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }
}