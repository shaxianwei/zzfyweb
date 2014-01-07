package cn.zzfyip.search.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionUtils {

	public static void main(String[] args){
		Pattern pattern = Pattern.compile("aaa");
		String s = "aaa";
		Matcher matcher = pattern.matcher(s);
		System.out.println(matcher.groupCount());
		
		if (matcher.find()){
			System.out.println(matcher.group());
		}else{
			System.out.println("no find");
		}
	}
	
}
