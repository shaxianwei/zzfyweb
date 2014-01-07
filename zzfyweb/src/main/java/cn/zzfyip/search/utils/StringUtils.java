package cn.zzfyip.search.utils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * 实现描述：org.apache.commons.lang.StringUtils 的扩展
 * 
 * @author zhangzixin
 * @version v1.0.0
 * @see
 * @since 13-10-9上午10:34
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

    public static String escape(String text) {
        return TextUtils.escapeNotNormalString(text);
    }

    public static String escapeQuote(String text) {
        if (isBlank(text)) {
            return EMPTY;
        }
        return replace(replace(text, "\\", "\\\\"), "\"", "\\\"");
    }

    public static String tidyAccurateDayOfWeek(String weekNum) {
        if (isNotEmpty(weekNum)) {
            final String[] days = { "一", "二", "三", "四", "五", "六", "日" };
            String week = "每周";
            List<Integer> weekNums = Lists.newArrayList(Iterables.transform(Splitter.on(",").trimResults()
                    .omitEmptyStrings().split(weekNum), new Function<String, Integer>() {
                public Integer apply(String input) {
                    int idx = Integer.parseInt(input);
                    if (idx >= 1 && idx <= 7)
                        return Integer.parseInt(input) - 1;
                    return null;
                }
            }));
            Collections.sort(weekNums);
            week += Joiner.on("、").skipNulls().join(Iterables.transform(weekNums, new Function<Integer, String>() {
                public String apply(Integer input) {
                    return days[input];
                }
            }));
            week += "可使用";
            return week;
        }
        return EMPTY;
    }

    /**
     * 分类模式产品，accurate_day_of_week字段存储的的形式是3,2,1，需要转换为周三，二，一可使用
     * 
     * @param weekNum
     * @return 例2013-01-30至2013-10-30，每周三、二、四可使用
     */
    public static String tidyClassifyValidDateRange(Date displayBeginDate, Date displayEndDate, String weekNum) {
        String classifyValidDateRange = DateUtils.formatDate(displayBeginDate, DateUtils.DATE_PATTERN) + "至"
                + DateUtils.formatDate(displayEndDate, DateUtils.DATE_PATTERN);
        String accurateDayOfWeek = tidyAccurateDayOfWeek(weekNum);
        if (isNotEmpty(accurateDayOfWeek)) {
            classifyValidDateRange += "，" + accurateDayOfWeek;
        }
        return classifyValidDateRange;
    }

    /**
     * 手机号 中间5位为*
     * 
     * @param telephone
     * @return
     */
    public static String tidyTelephone(String telephone) {
        if (isBlank(telephone)) {
            return EMPTY;
        }
        return telephone.substring(0, 3) + "****" + telephone.substring(7, 11);
    }

    /**
     * 限制字符串长度，超出部分截断并填充字符
     * 
     * @param str
     * @param maxLength
     * @param trailPadding
     * @return
     */
    public static String limit(String str, int maxLength, String trailPadding) {
        if (isBlank(str)) {
            return EMPTY;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + trailPadding;
    }

}
