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
import cn.zzfyip.search.event.engine.processor.IPatentPaperStatusProcessor;
import cn.zzfyip.search.event.engine.processor.SipoPatentPaperStatusProcessor;
import cn.zzfyip.search.utils.JsonUtils;
import cn.zzfyip.search.utils.SpringContextUtils;
import cn.zzfyip.search.utils.ThreadSleepUtils;

public class PatentPaperStatusLoader implements Runnable{
    
    private static final Logger logger = LoggerFactory.getLogger(PatentPaperStatusLoader.class);

    private PatentMain patentMain;
    
    private ExecutorService patentPaperStatusExecutor;
    
    private IPatentPaperStatusProcessor patentPaperStatusProcessor;
    
    private PatentDao patentDao;
    
    private GlobalConstant globalConstant;
    
	public PatentPaperStatusLoader(PatentMain patentMain, ExecutorService patentPaperStatusExecutor) {
        this.patentMain = patentMain;
        this.patentPaperStatusExecutor = patentPaperStatusExecutor;
        this.patentPaperStatusProcessor = SpringContextUtils.getBean(SipoPatentPaperStatusProcessor.class);
        this.patentPaperStatusProcessor = SpringContextUtils.getBean(SipoPatentPaperStatusProcessor.class);
        this.patentDao = SpringContextUtils.getBean(PatentDao.class);
        this.globalConstant = SpringContextUtils.getBean(GlobalConstant.class);
    }


    @Override
	public void run() {
        logger.info("开始执行专利案卷状态检索线程，执行任务信息{}",JsonUtils.marshalToString(patentMain));
        String patentPaperStatus = null;
        
        try {
        	//更新 案卷状态
        	patentPaperStatus = patentPaperStatusProcessor.processPatentPaperStatus(patentMain.getPatentNo());
            patentMain.setPatentPaperStatus(patentPaperStatus);
            patentMain.setPatentPaperStatusSearchTime(new Date());
            
            if(StringUtils.contains(patentPaperStatus, "失效")||StringUtils.contains(patentPaperStatus, "放弃专利权")){
            	if(!StringUtils.equals(patentMain.getPatentFawenSearchType(), PatentConstants.FAWEN_STATUS_04_NOSEARCH)){
            		patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_04_NOSEARCH);
            	}
            }
            
            patentDao.updatePatentMain(patentMain);
            
            //如果案卷状态包括了 驳回 ，则进行发文检索
            if(StringUtils.contains(patentPaperStatus, "驳回等复审请求")){
            	PatentNoticeFawenLoader patentNoticeFawenLoader = new PatentNoticeFawenLoader(patentMain,patentPaperStatusExecutor,new Date());
                patentPaperStatusExecutor.execute(patentNoticeFawenLoader);
            }
            
            ThreadSleepUtils.sleepMilliSeconds(globalConstant.getPatentPaperStatusThreadDelayMilliSeconds());
            
        } catch (PatentNoLoadHttpWrongException e) {
        	logger.info("执行专利案卷状态检索线程出错，提升单线程检索等待时间为{}毫秒，结束线程池并睡眠120秒，参数{}",globalConstant.getPatentPaperStatusThreadDelayMilliSeconds(),JsonUtils.marshalToString(patentMain));
            try {
                patentPaperStatusExecutor.awaitTermination(120, TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                logger.error("执行专利案卷状态检索线程出错，睡眠120秒被打断",e);
            }
            
        } catch (PatentPharseException e) {
			logger.error("执行专利案卷状态检索解析网页出错，参数"+JsonUtils.marshalToString(patentMain),e);
		} catch(Exception e){
            logger.error("执行专利案卷状态检索线程出错，参数"+JsonUtils.marshalToString(patentMain)+"，patentInfo="+JsonUtils.marshalToString(patentPaperStatus),e);
        }
        
        logger.info("结束执行专利案卷状态检索线程，PatentMain={}，PatentInfo={}",JsonUtils.marshalToString(patentMain),JsonUtils.marshalToString(patentPaperStatus));
        		
	}

}
