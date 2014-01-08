package cn.zzfyip.search.event.engine;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;
import cn.zzfyip.search.event.engine.processor.IPatentNoticeFawenProcessor;
import cn.zzfyip.search.event.engine.processor.SipoPatentNoticeFawenProcessor;
import cn.zzfyip.search.utils.JsonUtils;
import cn.zzfyip.search.utils.SpringContextUtils;

public class PatentNoticeFawenLoader implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(PatentNoticeFawenLoader.class);

    private PatentMain patentMain;
    
    private ExecutorService patentNoticeFawenExecutor;
    
    private Date fawenUpdateDate; 
    
    private IPatentNoticeFawenProcessor patentNoticeFawenProcessor;
    
    private PatentDao patentDao;
    
    
    public PatentNoticeFawenLoader(PatentMain patentMain, ExecutorService patentNoticeFawenExecutor,Date fawenUpdateDate) {
        this.patentMain = patentMain;
        this.patentNoticeFawenExecutor = patentNoticeFawenExecutor;
        this.fawenUpdateDate = fawenUpdateDate;
        this.patentNoticeFawenProcessor = SpringContextUtils.getBean(SipoPatentNoticeFawenProcessor.class);
        this.patentDao = SpringContextUtils.getBean(PatentDao.class);
    }


    @Override
    public void run() {
        logger.info("开始执行专利项发文通知检索线程，执行任务信息{}",JsonUtils.marshalToString(patentMain));
        List<PatentNoticeFawen> list = null;
        try {
            list = patentNoticeFawenProcessor.processPatentNoticeFawen(patentMain);
            patentDao.refreshPatentNoticeFawenByPatentNo(patentMain.getPatentNo(),list);
            
        } catch (PatentNoLoadHttpWrongException e) {
            logger.info("执行专利项发文通知检索线程出错，结束线程池并睡眠120秒，参数"+JsonUtils.marshalToString(patentMain));
            try {
                patentNoticeFawenExecutor.awaitTermination(120, TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                logger.error("执行专利项发文通知检索线程出错，睡眠120秒被打断",e);
            }
        } catch(Exception e){
            logger.info("执行专利项发文通知检索线程出错，参数"+JsonUtils.marshalToString(patentMain));
        }
        
        logger.info("结束执行专利项发文通知检索线程，PatentMain={}，PatentInfo={}",JsonUtils.marshalToString(patentMain),JsonUtils.marshalToString(list));
                
    }
    
    /**
     * 通过Info的信息更新 patentMain，为其他检索提供配置信息
     * @param patentMain
     * @param patentInfo
     */
    private void updatePatentMainFawenStatus(PatentMain patentMain,List<PatentNoticeFawen> fawenList){
        
        patentMain.setPatentFawenSearchTime(fawenUpdateDate);
        //TODO:根据规则进行发文情况的检查，对符合规则的产品，将其主patent的发文状态置成成功。针对专利权终止的，进行patent发文状态改成不检索。
        
        patentDao.updatePatentMain(patentMain);
    }


}
