package cn.zzfyip.search.event.engine;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentFee;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.processor.IPatentFeeProcessor;
import cn.zzfyip.search.event.engine.processor.SipoPatentFeeProcessor;
import cn.zzfyip.search.utils.JsonUtils;
import cn.zzfyip.search.utils.SpringContextUtils;
import cn.zzfyip.search.utils.ThreadSleepUtils;

public class PatentFeeLoader implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(PatentFeeLoader.class);

	private PatentMain patentMain;

	private ExecutorService patentFeeExecutor;

	private IPatentFeeProcessor patentFeeProcessor;

	private PatentDao patentDao;
	
	private GlobalConstant globalConstant;

	public PatentFeeLoader(PatentMain patentMain, ExecutorService patentFeeExecutor) {
		this.patentMain = patentMain;
		this.patentFeeExecutor = patentFeeExecutor;
		this.patentFeeProcessor = SpringContextUtils.getBean(SipoPatentFeeProcessor.class);
		this.patentDao = SpringContextUtils.getBean(PatentDao.class);
		this.globalConstant = SpringContextUtils.getBean(GlobalConstant.class);
	}

	@Override
	public void run() {
		logger.info("开始执行专利项费用信息检索线程，执行任务信息{}", JsonUtils.marshalToString(patentMain));
		List<PatentFee> list = null;
		try {
			list = patentFeeProcessor.processPatentFee(patentMain.getPatentNo());
			patentDao.refreshPatentFeeByPatentNo(patentMain.getPatentNo(), list);
			patentMain.setPatentFeeWuxiaoSearchTime(new Date());
			patentDao.updatePatentMain(patentMain);
			
			ThreadSleepUtils.sleepMilliSeconds(globalConstant.getPatentWuxiaoFeeThreadDelayMilliSeconds());
            
		} catch (PatentNoLoadHttpWrongException e) {
//			globalConstant.setPatentFawenThreadDelayMilliSeconds(globalConstant.getPatentFawenThreadDelayMilliSeconds()+100);
			logger.info("执行专利项费用信息检索线程出错，提升单线程检索等待时间为{}毫秒，结束线程池并睡眠300秒，参数{}" ,globalConstant.getPatentFawenThreadDelayMilliSeconds(),JsonUtils.marshalToString(patentMain));
			try {
				patentFeeExecutor.awaitTermination(300, TimeUnit.SECONDS);
			} catch (InterruptedException e1) {
				logger.error("执行专利项费用信息检索线程出错，睡眠300秒被打断", e);
			}
		} catch (PatentPharseException e) {
			logger.error("执行专利项费用信息检索解析网页出错，参数"+JsonUtils.marshalToString(patentMain),e);
		} catch (Exception e) {
			logger.error("执行专利项费用信息检索线程出错，参数" + JsonUtils.marshalToString(patentMain),e);
		}

		logger.info("结束执行专利项费用信息检索线程，PatentMain={}，PatentInfo={}", JsonUtils.marshalToString(patentMain), JsonUtils.marshalToString(list));
	}

}
