package cn.zzfyip.search.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadSleepUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(ThreadSleepUtils.class);
	
	public static void sleepMilliSeconds(long milliSeconds){
		try {
			TimeUnit.MILLISECONDS.sleep(milliSeconds);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
	}

	public static void sleepSeconds(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
	}

	public static void sleepMinutes(long minutes){
		try {
			TimeUnit.MINUTES.sleep(minutes);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
	}
	public static void sleepHours(long hours){
		try {
			TimeUnit.HOURS.sleep(hours);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
		}
	}
}
