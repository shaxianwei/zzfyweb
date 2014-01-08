package cn.zzfyip.search.event.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentInfo;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.processor.IPatentInfoProcessor;
import cn.zzfyip.search.event.engine.processor.SipoPatentInfoProcessor;
import cn.zzfyip.search.utils.JsonUtils;
import cn.zzfyip.search.utils.SpringContextUtils;

public class PatentInfoLoader implements Runnable{
    
    private static final Logger logger = LoggerFactory.getLogger(PatentInfoLoader.class);

    private PatentMain patentMain;
    
    private ExecutorService patentInfoExecutor;
    
    private IPatentInfoProcessor patentInfoProcessor;
    
    private PatentDao patentDao;
    
    
	public PatentInfoLoader(PatentMain patentMain, ExecutorService addPatentExecutor) {
        this.patentMain = patentMain;
        this.patentInfoExecutor = addPatentExecutor;
        this.patentInfoProcessor = SpringContextUtils.getBean(SipoPatentInfoProcessor.class);
        this.patentDao = SpringContextUtils.getBean(PatentDao.class);
    }


    @Override
	public void run() {
        logger.info("开始执行专利项信息检索线程，执行任务信息{}",JsonUtils.marshalToString(patentMain));
        PatentInfo patentInfo = null;
        
        try {
            patentInfo = patentDao.selectPatentInfoByPatentNo(patentMain.getPatentNo());
            
            if(patentInfo == null){
                patentInfo = patentInfoProcessor.processPatentInfo(patentMain);
                patentDao.insertPatentInfo(patentInfo);
            }
        } catch (PatentNoLoadHttpWrongException e) {
            logger.info("执行专利项信息检索线程出错，结束线程池并睡眠120秒，参数"+JsonUtils.marshalToString(patentMain));
            try {
                patentInfoExecutor.awaitTermination(120, TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                logger.error("执行专利项信息检索线程出错，睡眠120秒被打断",e);
            }
        } catch(Exception e){
            logger.info("执行专利项信息检索线程出错，参数"+JsonUtils.marshalToString(patentMain));
        }
        
        logger.info("结束执行专利项信息检索线程，PatentMain={}，PatentInfo={}",JsonUtils.marshalToString(patentMain),JsonUtils.marshalToString(patentInfo));
        		
	}

}
