package cn.zzfyip.search.service.search;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.common.thread.NamedThreadFactory;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.PatentInfoLoader;
import cn.zzfyip.search.event.engine.processor.IPatentListProcessor;
import cn.zzfyip.search.event.engine.processor.IPatentNoticeFawenProcessor;

@Service
public class PatentNoticeFawenLoadService implements InitializingBean{

	private static final Logger logger = LoggerFactory.getLogger(PatentNoticeFawenLoadService.class);

	@Autowired
	private IPatentNoticeFawenProcessor patentNoticeFawenProcessor;
	
	@Autowired
	private GlobalConstant globalConstant;
	
	@Autowired
	private PatentDao patentDao;
	
	private ExecutorService patentNoticeFawenExecutor;

	@Override
	public void afterPropertiesSet() throws Exception {
	    patentNoticeFawenExecutor = Executors.newFixedThreadPool(globalConstant.getPatentNoticeFawenThreadNum(), new NamedThreadFactory(
                "patent-noticefawen-dispatcher", true));
	}
	
	public void searchPatentNoticeFawen(){
	    logger.info("---------------------开始执行专利项发文通知检索服务-------------------");
	    List<PatentMain> list = patentDao.selectFirst100RecordPatentNoticeFawenSearchPatentMain();
	    
	    while(list.size()!=0){
	        logger.info("执行专利项发文通知检索服务，本分页执行开始。 ");
	        for(PatentMain patentMain:list){
	            PatentInfoLoader patentInfoLoader = new PatentInfoLoader(patentMain,patentNoticeFawenExecutor);
	            patentNoticeFawenExecutor.execute(patentInfoLoader);
	        }
	        while(((ThreadPoolExecutor)patentNoticeFawenExecutor).getActiveCount()>0){
	            try {
	                logger.info("执行专利项发文通知检索服务，活跃线程数{}，队列中线程数{}，主线程睡眠10秒。 ",((ThreadPoolExecutor)patentNoticeFawenExecutor).getActiveCount(), ((java.util.concurrent.ThreadPoolExecutor)patentNoticeFawenExecutor).getQueue().size());
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    logger.error("InterruptedException",e);
                }
	        }
	        logger.info("执行专利项发文通知检索服务，本分页执行完成。 ");
	        list = patentDao.selectFirst100RecordPatentNoticeFawenSearchPatentMain();
	    }
	    logger.info("---------------------完成执行专利项发文通知检索服务-------------------");
	    	    
	}
	
}