package cn.zzfyip.search.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述: 身份证号码校验 version 1.0
 */
public class IDCardCheck {

    private static int[] ai = new int[18];
    public static final String v = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";

    // verify digit
    private static final int[] vi = { 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 };

    // wi=2(n-1)(mod 11)
    private static final int[] wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };

    /**
     * 
     * 描述: 身份证15to18的完整算法 param sfzh return
     */
    public static String get15To18(String sfzh) {
        if (sfzh == null || sfzh.length() != 15) {
            return sfzh;
        }
        // 校验码
        char[] sVC = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
        // 加权因子
        int[] sEQ = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
        String strTemp;
        int intTemp = 0;
        strTemp = sfzh.substring(0, 6) + "19" + sfzh.substring(6);
        try {
            for (int i = 0; i < strTemp.length(); i++) {
                intTemp += Integer.parseInt(strTemp.substring(i, i + 1)) * sEQ[i];
            }
        } catch (Exception e) {
            return sfzh;
        }
        intTemp = intTemp % 11;
        return strTemp + sVC[intTemp];
    }

    /**
     * 
     * 描述: 获得身份证最后一位校验位 param eightcardid return
     */
    public static String getVerify(String eightcardid) {
        int remaining = 0;
        if (eightcardid.length() == 18) {
            eightcardid = eightcardid.substring(0, 17);
        }
        if (eightcardid.length() == 17) {
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                String k = eightcardid.substring(i, i + 1);
                IDCardCheck.ai[i] = Integer.parseInt(k);
            }
            for (int i = 0; i < 17; i++) {
                sum = sum + IDCardCheck.wi[i] * IDCardCheck.ai[i];
            }
            remaining = sum % 11;
        }
        return remaining == 2 ? "X" : String.valueOf(IDCardCheck.vi[remaining]);
    }

    /**
     * 
     * 描述: 15位身份证到升级到18位的算法 param fifteencardid return
     */
    public static String uptoeighteen(String fifteencardid) {
        String eighteencardid = fifteencardid.substring(0, 6);
        eighteencardid = eighteencardid + "19";
        eighteencardid = eighteencardid + fifteencardid.substring(6, 15);
        eighteencardid = eighteencardid + IDCardCheck.getVerify(eighteencardid);
        return eighteencardid;
    }

    /**
     * 
     * 描述: 身份证校验 param idcard return
     */
    public static boolean Verify(String idcard) {
        if (idcard.length() == 15) {
            idcard = IDCardCheck.uptoeighteen(idcard);
        }
        if (idcard.length() != 18) {
            return false;
        }
        if (!IDCardCheck.verifyDate(idcard)) {
            return false;
        }
        String verify = idcard.substring(17, 18);
        if (verify.toUpperCase().equals(IDCardCheck.getVerify(idcard).toUpperCase())) {
            return true;
        }
        return false;
    }

    /**
     * 
     * 描述:验证18位身份证中的日期是否合法,接受日期格式：2012-02-21 param eighteencardid return
     */
    public static boolean verifyDate(String eighteencardid) {
        String date = eighteencardid.substring(6, 10);
        date += "-";
        date += eighteencardid.substring(10, 12);
        date += "-";
        date += eighteencardid.substring(12, 14);
        // 判断年月日的正则表达式，接受输入格式为2010-12-24，可接受平年闰年的日期
        Pattern p = Pattern.compile(IDCardCheck.v);
        Matcher m = p.matcher(date);
        return m.matches();
    }

    public IDCardCheck() {

    }
}
