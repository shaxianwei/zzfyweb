package cn.zzfyip.search.service.search;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.common.thread.NamedThreadFactory;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.PatentFeeLoader;
import cn.zzfyip.search.event.engine.processor.IPatentFeeProcessor;
import cn.zzfyip.search.utils.ThreadSleepUtils;

@Service
public class PatentFeeLoadService implements InitializingBean{

	private static final Logger logger = LoggerFactory.getLogger(PatentFeeLoadService.class);

	@Autowired
	private IPatentFeeProcessor patentFeeProcessor;
	
	@Autowired
	private GlobalConstant globalConstant;
	
	@Autowired
	private PatentDao patentDao;
	
	private ExecutorService patentFeeExecutor;

	@Override
	public void afterPropertiesSet() throws Exception {
	    patentFeeExecutor = Executors.newFixedThreadPool(globalConstant.getPatentWuxiaoFeeSearchThreadNum(), new NamedThreadFactory(
                "patent-fee-dispatcher", true));
	}
	
	public void searchPatentFee(){
		logger.info("---------------------开始执行专利项收费信息检索服务-------------------");
	    List<PatentMain> list = patentDao.selectFirst100RecordPatentFeeSearchPatentMain();
	    
	    while(list.size()!=0){
	        logger.info("执行专利项收费信息检索服务，本分页执行开始。 ");
	        for(PatentMain patentMain:list){
	            PatentFeeLoader patentFeeLoader = new PatentFeeLoader(patentMain,patentFeeExecutor);
	            patentFeeExecutor.execute(patentFeeLoader);
	        }
	        while(((java.util.concurrent.ThreadPoolExecutor)patentFeeExecutor).getQueue().size()>0){
	        	logger.info("执行专利项收费信息检索服务，活跃线程数{}，队列中线程数{}，线程结束等待时间{}毫秒，主线程睡眠10秒。 ",((ThreadPoolExecutor)patentFeeExecutor).getActiveCount(), ((java.util.concurrent.ThreadPoolExecutor)patentFeeExecutor).getQueue().size(),globalConstant.getPatentWuxiaoFeeThreadDelayMilliSeconds());
				ThreadSleepUtils.sleepSeconds(10);
	        }
	        logger.info("执行专利项收费信息检索服务，本分页执行完成。 ");
	        list = patentDao.selectFirst100RecordPatentFeeSearchPatentMain();
	    }
	    
	    logger.info("---------------------完成执行专利项收费信息检索服务-------------------");
	    	    
	}
	
	public void searchPatentFeeJob() {
		
		// 延迟一分钟执行
		ThreadSleepUtils.sleepMinutes(0);
		
		while (true) {
			try {
				this.searchPatentFee();
			} catch (Exception e) {
				logger.error("执行专利项收费信息检索服务 JOB 失败", e);
			}

			//下次任务检测4小时候进行
			ThreadSleepUtils.sleepHours(4);
		}
	}
	
}
