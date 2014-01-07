package cn.zzfyip.search.utils;

import java.security.MessageDigest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 实现描述：MD5加密工具，主要用于HMAC签名
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-8-28 上午10:56:27
 */
public class Md5Utils {

    private static final String HEX_DIGITS[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f" };

    private final static Logger logger = LoggerFactory.getLogger(Md5Utils.class);

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (byte element : b) {
            resultSb.append(Md5Utils.byteToHexString(element));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return Md5Utils.HEX_DIGITS[d1] + Md5Utils.HEX_DIGITS[d2];
    }

    public static String md5Encode(String origin) {
        return Md5Utils.md5Encode(origin, Consts.UTF_8.name());
    }

    public static String md5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (StringUtils.isBlank(charsetname)) {
                resultString = Md5Utils.byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = Md5Utils.byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception e) {
            Md5Utils.logger.error("ERROR ## md5 encode happend error, the trace is ", e);
        }
        return resultString;
    }

}
