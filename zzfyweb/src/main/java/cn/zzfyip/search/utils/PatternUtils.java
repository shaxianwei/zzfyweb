/*
 * Copyright 2013 zzfyip.cn All right reserved. This software is the confidential and proprietary information of
 * zzfyip.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with zzfyip.cn.
 */
package cn.zzfyip.search.utils;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

/**
 * 实现描述：perl5正则工具类
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-9-12 下午7:59:38
 */
public class PatternUtils {

    private static LoadingCache<String, Pattern> patterns = CacheBuilder.newBuilder().maximumSize(10000)
            .build(new CacheLoader<String, Pattern>() {
                @Override
                public Pattern load(String perl5RegExp) throws Exception {
                    Pattern compiledPattern;
                    Perl5Compiler compiler = new Perl5Compiler();
                    try {
                        compiledPattern = compiler.compile(perl5RegExp, Perl5Compiler.CASE_INSENSITIVE_MASK
                                | Perl5Compiler.READ_ONLY_MASK | Perl5Compiler.SINGLELINE_MASK);
                    } catch (MalformedPatternException mpe) {
                        throw new IllegalArgumentException("Malformed regular expression: " + perl5RegExp);
                    }
                    return compiledPattern;
                }
            });

    public static void clear() {
        PatternUtils.patterns.cleanUp();
    }

    public static Pattern compilePattern(String perl5RegExp) {
        Pattern compiledPattern = null;
        try {
            compiledPattern = PatternUtils.patterns.get(perl5RegExp);
        } catch (ExecutionException e) {
            throw new IllegalArgumentException("Malformed regular expression: " + perl5RegExp);
        }
        return compiledPattern;
    }

    public static String getMatchString(String perl5RegExp, String content, int groupIndex) {
        List<String> matchGroups = PatternUtils.listMatchGroups(perl5RegExp, content);
        if (groupIndex > 0 && groupIndex < matchGroups.size()) {
            return matchGroups.get(groupIndex);
        }
        return StringUtils.EMPTY;
    }
    
    public static boolean isMatch(String content, String perl5RegExp) {
        Pattern pattern = PatternUtils.compilePattern(perl5RegExp);
        PatternMatcher matcher = new Perl5Matcher();
        return matcher.matches(content, pattern);
    }

    public static List<String> listMatchGroups(String perl5RegExp, String content) {
        List<String> matchGroups = Lists.newArrayList();
        Pattern compiledPattern = PatternUtils.compilePattern(perl5RegExp);
        PatternMatcher matcher = new Perl5Matcher();
        if (matcher.matches(content, compiledPattern)) {
            for (int i = 0; i < matcher.getMatch().groups(); i++) {
                matchGroups.add(matcher.getMatch().group(i));
            }
        }
        return matchGroups;
    }
    
    public static void main(String[] args){
    	String patten = "(.*)共有(.*)条记录(.*)";
    	String response = "&nbsp;&nbsp;页次：1/54&nbsp;&nbsp;共有5343条记录 &nbsp;&nbsp;转到";
    	String result = PatternUtils.getMatchString(patten, response, 2);
    	System.out.println(result);
    }
}
