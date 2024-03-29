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
import cn.zzfyip.search.dal.common.entity.PatentInfo;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.processor.IPatentInfoProcessor;
import cn.zzfyip.search.event.engine.processor.SipoPatentInfoProcessor;
import cn.zzfyip.search.utils.DateUtils;
import cn.zzfyip.search.utils.JsonUtils;
import cn.zzfyip.search.utils.SpringContextUtils;
import cn.zzfyip.search.utils.ThreadSleepUtils;

public class PatentInfoLoader implements Runnable{
    
    private static final Logger logger = LoggerFactory.getLogger(PatentInfoLoader.class);

    private PatentMain patentMain;
    
    private ExecutorService patentInfoExecutor;
    
    private IPatentInfoProcessor patentInfoProcessor;
    
    private PatentDao patentDao;
    
    private GlobalConstant globalConstant;
    
	public PatentInfoLoader(PatentMain patentMain, ExecutorService addPatentExecutor) {
        this.patentMain = patentMain;
        this.patentInfoExecutor = addPatentExecutor;
        this.patentInfoProcessor = SpringContextUtils.getBean(SipoPatentInfoProcessor.class);
        this.patentDao = SpringContextUtils.getBean(PatentDao.class);
        this.globalConstant = SpringContextUtils.getBean(GlobalConstant.class);
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
            updatePatentMainByPatentInfo(patentMain,patentInfo);
            
            ThreadSleepUtils.sleepMilliSeconds(globalConstant.getPatentInfoThreadDelayMilliSeconds());
            
        } catch (PatentNoLoadHttpWrongException e) {
        	logger.info("执行专利项信息检索线程出错，提升单线程检索等待时间为{}毫秒，结束线程池并睡眠120秒，参数{}",globalConstant.getPatentInfoThreadDelayMilliSeconds(),JsonUtils.marshalToString(patentMain));
            try {
                patentInfoExecutor.awaitTermination(120, TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                logger.error("执行专利项信息检索线程出错，睡眠120秒被打断",e);
            }
//            globalConstant.setPatentInfoThreadDelayMilliSeconds(globalConstant.getPatentInfoThreadDelayMilliSeconds()+100);
            
        } catch (PatentPharseException e) {
			logger.error("执行专利项信息检索解析网页出错，参数"+JsonUtils.marshalToString(patentMain),e);
		} catch(Exception e){
            logger.error("执行专利项信息检索线程出错，参数"+JsonUtils.marshalToString(patentMain)+"，patentInfo="+JsonUtils.marshalToString(patentInfo),e);
        }
        
        
        logger.info("结束执行专利项信息检索线程，PatentMain={}，PatentInfo={}",JsonUtils.marshalToString(patentMain),JsonUtils.marshalToString(patentInfo));
        		
	}
    
    /**
     * 通过Info的信息更新 patentMain，为其他检索提供配置信息
     * @param patentMain
     * @param patentInfo
     */
    private void updatePatentMainByPatentInfo(PatentMain patentMain,PatentInfo patentInfo){
        patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_01_NORMAL);
        
        Date twoYearsEarlyDate = DateUtils.addMonth(DateUtils.today(), -24);
		if(null!=patentMain.getPublicDate() && twoYearsEarlyDate.after(patentMain.getPublicDate())){
			patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_04_NOSEARCH);
		}else if(!PatentConstants.TYPE_01_FAMING.equals(patentMain.getPatentType())){
            patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_04_NOSEARCH);
        }else if(StringUtils.isNotBlank(patentInfo.getAgency())){
            patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_04_NOSEARCH);
        }else if(containsFilterWords(patentInfo.getApplier())||containsFilterWords(patentInfo.getAddress())){
            patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_04_NOSEARCH);
        }
        patentDao.updatePatentMain(patentMain);
    }
    
    /**
     * 过滤词检索
     * @param text
     * @return
     */
    private Boolean containsFilterWords(String text){
        if(StringUtils.isBlank(globalConstant.getPatentFawenInfoFilterWords())){
            return false;
        }
        
        String[] filterWords = globalConstant.getPatentFawenInfoFilterWords().split(",");
        for(String filterWord:filterWords){
            if(StringUtils.contains(text, filterWord)){
                return true;
            }
        }
        
        return false;
    }

}
