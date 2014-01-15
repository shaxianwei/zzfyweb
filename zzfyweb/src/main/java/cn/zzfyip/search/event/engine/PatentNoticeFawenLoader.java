package cn.zzfyip.search.event.engine;

import java.util.Date;
import java.util.List;
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
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;
import cn.zzfyip.search.event.engine.processor.IPatentNoticeFawenProcessor;
import cn.zzfyip.search.event.engine.processor.SipoPatentNoticeFawenProcessor;
import cn.zzfyip.search.utils.JsonUtils;
import cn.zzfyip.search.utils.SpringContextUtils;
import cn.zzfyip.search.utils.ThreadSleepUtils;

public class PatentNoticeFawenLoader implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(PatentNoticeFawenLoader.class);

	private PatentMain patentMain;

	private ExecutorService patentNoticeFawenExecutor;

	private Date fawenUpdateDate;

	private IPatentNoticeFawenProcessor patentNoticeFawenProcessor;

	private PatentDao patentDao;
	
	private GlobalConstant globalConstant;

	public PatentNoticeFawenLoader(PatentMain patentMain, ExecutorService patentNoticeFawenExecutor, Date fawenUpdateDate) {
		this.patentMain = patentMain;
		this.patentNoticeFawenExecutor = patentNoticeFawenExecutor;
		this.fawenUpdateDate = fawenUpdateDate;
		this.patentNoticeFawenProcessor = SpringContextUtils.getBean(SipoPatentNoticeFawenProcessor.class);
		this.patentDao = SpringContextUtils.getBean(PatentDao.class);
		this.globalConstant = SpringContextUtils.getBean(GlobalConstant.class);
	}

	@Override
	public void run() {
		logger.info("开始执行专利项发文通知检索线程，执行任务信息{}", JsonUtils.marshalToString(patentMain));
		List<PatentNoticeFawen> list = null;
		try {
			list = patentNoticeFawenProcessor.processPatentNoticeFawen(patentMain);
			patentDao.refreshPatentNoticeFawenByPatentNo(patentMain.getPatentNo(), list);
			this.updatePatentMainFawenStatus(patentMain, list);
			
			ThreadSleepUtils.sleepMilliSeconds(globalConstant.getPatentFawenThreadDelayMilliSeconds());
            
		} catch (PatentNoLoadHttpWrongException e) {
//			globalConstant.setPatentFawenThreadDelayMilliSeconds(globalConstant.getPatentFawenThreadDelayMilliSeconds()+100);
			logger.info("执行专利项发文通知检索线程出错，提升单线程检索等待时间为{}毫秒，结束线程池并睡眠300秒，参数{}" ,globalConstant.getPatentFawenThreadDelayMilliSeconds(),JsonUtils.marshalToString(patentMain));
			try {
				patentNoticeFawenExecutor.awaitTermination(300, TimeUnit.SECONDS);
			} catch (InterruptedException e1) {
				logger.error("执行专利项发文通知检索线程出错，睡眠300秒被打断", e);
			}
		} catch (PatentPharseException e) {
			logger.error("执行专利项发文通知检索解析网页出错，参数"+JsonUtils.marshalToString(patentMain),e);
		} catch (Exception e) {
			logger.error("执行专利项发文通知检索线程出错，参数" + JsonUtils.marshalToString(patentMain),e);
		}

		logger.info("结束执行专利项发文通知检索线程，PatentMain={}，PatentInfo={}", JsonUtils.marshalToString(patentMain), JsonUtils.marshalToString(list));

	}

	/**
	 * 通过Info的信息更新 patentMain，为其他检索提供配置信息
	 * 
	 * @param patentMain
	 * @param patentInfo
	 */
	private void updatePatentMainFawenStatus(PatentMain patentMain, List<PatentNoticeFawen> fawenList) {

		patentMain.setPatentFawenSearchTime(fawenUpdateDate);
		if(isChehui(fawenList)){
			patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_04_NOSEARCH);
			patentMain.setPatentStatus(PatentConstants.STATUS_05_CHEHUI);
		}else if(isShizhi(fawenList)){
			patentMain.setPatentStatus(PatentConstants.STATUS_03_SHENCHA);
			if(isNoticeDabian(fawenList)){
				patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_02_DABIAN);
			}
		}
		
		if(isBohui(fawenList)){
			patentMain.setPatentFawenSearchType(PatentConstants.FAWEN_STATUS_03_BOHUI);
		}

		patentDao.updatePatentMain(patentMain);
	}

	/**
	 * 通过发文识别，专利是否撤回
	 * 
	 * @param fawenList
	 * @return
	 */
	private Boolean isChehui(List<PatentNoticeFawen> fawenList){
    	for(PatentNoticeFawen fawen:fawenList){
    		if("20108".equals(fawen.getNoticeCode())||"20109".equals(fawen.getNoticeCode())||"21230".equals(fawen.getNoticeCode())||"21249".equals(fawen.getNoticeCode())||"21105".equals(fawen.getNoticeCode())||"200022".equals(fawen.getNoticeCode())||"200602".equals(fawen.getNoticeCode())){
    			return true;
    		}
    	}
    	
    	return false;
    }
	
	/**
	 * 通过发文识别，专利是否进入实质审查
	 * 
	 * @param fawenList
	 * @return
	 */
	private Boolean isShizhi(List<PatentNoticeFawen> fawenList){
		for(PatentNoticeFawen fawen:fawenList){
			if("21231".equals(fawen.getNoticeCode())||"21229".equals(fawen.getNoticeCode())){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 通过发文识别，专利有答辩通知的发文<br>
	 * 规则： 在20108，20109发文通知之后，有一条空白的发文情况的，该条属于答辩意见通知书
	 * 
	 * @param fawenList
	 * @return
	 */
	private Boolean isNoticeDabian(List<PatentNoticeFawen> fawenList){
		int sequenceNo = -1;
		for(PatentNoticeFawen fawen:fawenList){
			if("20108".equals(fawen.getNoticeCode())||"20109".equals(fawen.getNoticeCode())||"21230".equals(fawen.getNoticeCode())||"21249".equals(fawen.getNoticeCode())||"21105".equals(fawen.getNoticeCode())){
				sequenceNo = fawen.getSequnceNo();
			}
		}
		
		if(sequenceNo!=-1){
			for(PatentNoticeFawen fawen:fawenList){
				if((sequenceNo+1) == fawen.getSequnceNo()&&StringUtils.isBlank(fawen.getNoticeCode())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 通过发文识别，专利是否进入已有驳回通知书
	 * 
	 * @param fawenList
	 * @return
	 */
	private Boolean isBohui(List<PatentNoticeFawen> fawenList){
		for(PatentNoticeFawen fawen:fawenList){
			if("210407".equals(fawen.getNoticeCode())){
				return true;
			}
		}
		
		return false;
	}
	
	
}
