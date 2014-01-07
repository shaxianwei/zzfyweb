package cn.zzfyip.search.utils;

/**
 * 实现描述：校验用户信息
 * 
 * @author zixin.zhang
 * @version v1.0.0
 * @see
 * @since 2013-8-6 下午17:35:39
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class ValidateUtils {
    public final static int MAX_EMAIL_LENGTH = 50;
    public final static int MAX_ID_LENGTH = 50;
    public final static int MAX_MOBILE_LENGTH = 11;
    public final static int MAX_NAME_LENGTH = 30;
    public final static int MAX_PHONE_LENGTH = 20;

    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        String str = "^[a-zA-Z0-9.!#$%&amp;'*+\\-\\/=?\\^_`{|}~\\-]+@[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches() && email.length() < ValidateUtils.MAX_EMAIL_LENGTH;
    }

    public static boolean isIDCard(String IDCard) {
        if (StringUtils.isBlank(IDCard)) {
            return false;
        }
        return IDCardCheck.Verify(IDCard);
    }

    public static boolean isMobileNo(String mobiles) {
        if (StringUtils.isBlank(mobiles)) {
            return false;
        }
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isPhoneNo(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        Pattern p = Pattern.compile("^[[0-9]-()]*$");
        Matcher m = p.matcher(phone);
        return m.matches() && phone.length() <= ValidateUtils.MAX_PHONE_LENGTH;
    }

    public static boolean isValidUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return false;
        }
        return userName.length() <= ValidateUtils.MAX_NAME_LENGTH;
    }
}
