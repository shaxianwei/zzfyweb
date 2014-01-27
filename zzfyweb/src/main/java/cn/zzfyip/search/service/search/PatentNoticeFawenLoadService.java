package cn.zzfyip.search.service.search;

import java.util.Date;
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
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.common.thread.NamedThreadFactory;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.PatentNoticeFawenLoader;
import cn.zzfyip.search.event.engine.processor.IPatentNoticeFawenProcessor;
import cn.zzfyip.search.utils.ThreadSleepUtils;

@Service
public class PatentNoticeFawenLoadService implements InitializingBean{

	private static final Logger logger = LoggerFactory.getLogger(PatentNoticeFawenLoadService.class);

	@Autowired
	private IPatentNoticeFawenProcessor patentNoticeFawenProcessor;
	@Autowired
	private IPatentNoticeFawenProcessor sipoPatentNoticeFawenProcessor;
	
	@Autowired
	private GlobalConstant globalConstant;
	
	@Autowired
	private PatentDao patentDao;
	
	private ExecutorService patentNoticeFawenExecutor;

	@Override
	public void afterPropertiesSet() throws Exception {
	    patentNoticeFawenExecutor = Executors.newFixedThreadPool(globalConstant.getPatentFawenSearchTreadNum(), new NamedThreadFactory(
                "patent-noticefawen-dispatcher", true));
	}
	
	public void searchPatentNoticeFawen(){
		logger.info("---------------------开始执行专利项发文通知检索服务-------------------");
	    Date fawenUpdateDate = null;
		while (fawenUpdateDate == null){
	    	try {
				fawenUpdateDate = sipoPatentNoticeFawenProcessor.processNoticeFawenUpdateDate();
			} catch (PatentNoLoadHttpWrongException e) {
				logger.info("获取专利项发文通知更新时间失败，十分钟后再次获取。 ");
				// 延迟十分钟执行
				ThreadSleepUtils.sleepMinutes(10);
			} catch (PatentPharseException e) {
				logger.error("获取专利项发文通知解析更新时间失败。 ",e);
				return;
			}
	    }
	    
	    List<PatentMain> list = patentDao.selectFirst100RecordPatentNoticeFawenSearchPatentMain(fawenUpdateDate);
	    
	    while(list.size()!=0){
	        logger.info("执行专利项发文通知检索服务，本分页执行开始。 ");
	        for(PatentMain patentMain:list){
	            PatentNoticeFawenLoader patentNoticeFawenLoader = new PatentNoticeFawenLoader(patentMain,patentNoticeFawenExecutor,fawenUpdateDate);
	            patentNoticeFawenExecutor.execute(patentNoticeFawenLoader);
	        }
	        while(((java.util.concurrent.ThreadPoolExecutor)patentNoticeFawenExecutor).getQueue().size()>0){
	        	logger.info("执行专利项发文通知检索服务，活跃线程数{}，队列中线程数{}，线程结束等待时间{}毫秒，主线程睡眠10秒。 ",((ThreadPoolExecutor)patentNoticeFawenExecutor).getActiveCount(), ((java.util.concurrent.ThreadPoolExecutor)patentNoticeFawenExecutor).getQueue().size(),globalConstant.getPatentFawenThreadDelayMilliSeconds());
				ThreadSleepUtils.sleepSeconds(10);
	        }
	        logger.info("执行专利项发文通知检索服务，本分页执行完成。 ");
	        list = patentDao.selectFirst100RecordPatentNoticeFawenSearchPatentMain(fawenUpdateDate);
	    }
	    logger.info("---------------------完成执行专利项发文通知检索服务-------------------");
	    	    
	}
	
	public void searchPatentNoticeFawenJob() {
		
		// 延迟一分钟执行
		ThreadSleepUtils.sleepMinutes(1);
		
		while (true) {
			try {
				this.searchPatentNoticeFawen();
			} catch (Exception e) {
				logger.error("执行专利项发文通知检索服务 JOB 失败", e);
			}

			//下次任务检测2小时候进行
			ThreadSleepUtils.sleepHours(2);
		}
	}
	
}
