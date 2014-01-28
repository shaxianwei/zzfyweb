package cn.zzfyip.search.event.engine;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.common.constant.PatentConstants;
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.processor.IPatentLawStatusProcessor;
import cn.zzfyip.search.event.engine.processor.SipoPatentLawStatusProcessor;
import cn.zzfyip.search.utils.JsonUtils;
import cn.zzfyip.search.utils.SpringContextUtils;
import cn.zzfyip.search.utils.ThreadSleepUtils;

public class PatentLawStatusLoader implements Runnable{
    
    private static final Logger logger = LoggerFactory.getLogger(PatentLawStatusLoader.class);

    private PatentMain patentMain;
    
    private ExecutorService patentLawStatusExecutor;
    
    private IPatentLawStatusProcessor patentLawStatusProcessor;
    
    private PatentDao patentDao;
    
    private GlobalConstant globalConstant;
    
	public PatentLawStatusLoader(PatentMain patentMain,ExecutorService patentLawStatusExecutor) {
        this.patentMain = patentMain;
        this.patentLawStatusExecutor = patentLawStatusExecutor;
        this.patentLawStatusProcessor = SpringContextUtils.getBean(SipoPatentLawStatusProcessor.class);
        this.patentDao = SpringContextUtils.getBean(PatentDao.class);
        this.globalConstant = SpringContextUtils.getBean(GlobalConstant.class);
    }


    @Override
	public void run() {
        logger.info("开始执行专利项法律状态检索线程，执行任务信息{}",JsonUtils.marshalToString(patentMain));
        String lawStatus = null;
        
        try {
        	lawStatus = patentLawStatusProcessor.processPatentLawStatus(patentMain.getPatentNo());
        	
        	if(StringUtils.isNotBlank(lawStatus)){
        		patentMain.setPatentStatus(lawStatus);
        		if(StringUtils.equals(PatentConstants.STATUS_05_CHEHUI, lawStatus)){
        			patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_04_NOSEARCH);
        		}
        	}
        	
        	patentMain.setPatentLawStatusSearchTime(new Date());
        	patentDao.updatePatentMain(patentMain);
        	
            ThreadSleepUtils.sleepMilliSeconds(globalConstant.getPatentLawStatusThreadDelayMilliSeconds());
            
        } catch (PatentNoLoadHttpWrongException e) {
        	logger.info("执行专利项法律状态线程出错，提升单线程检索等待时间为{}毫秒，结束线程池并睡眠120秒，参数{}",globalConstant.getPatentInfoThreadDelayMilliSeconds(),JsonUtils.marshalToString(patentMain));
            try {
            	patentLawStatusExecutor.awaitTermination(120, TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                logger.error("执行专利项法律状态检索线程出错，睡眠120秒被打断",e);
            }
        } catch (PatentPharseException e) {
			logger.error("执行专利项法律状态检索解析网页出错，参数"+JsonUtils.marshalToString(patentMain),e);
		} catch(Exception e){
            logger.error("执行专利项法律状态检索线程出错，参数"+JsonUtils.marshalToString(patentMain)+"，lawStatus="+JsonUtils.marshalToString(lawStatus),e);
        }
        
        logger.info("结束执行专利项法律状态检索线程，PatentMain={}，lawStatus={}",JsonUtils.marshalToString(patentMain),JsonUtils.marshalToString(lawStatus));
        		
	}

}
