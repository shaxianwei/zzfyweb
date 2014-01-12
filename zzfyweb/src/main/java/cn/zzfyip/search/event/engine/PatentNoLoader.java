package cn.zzfyip.search.event.engine;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.AddPatentRecord;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.processor.IPatentListProcessor;
import cn.zzfyip.search.event.engine.processor.SipoPatentListProcessor;
import cn.zzfyip.search.utils.JsonUtils;
import cn.zzfyip.search.utils.SpringContextUtils;

public class PatentNoLoader implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(PatentNoLoader.class);

	private AddPatentRecord addPatentRecord;
	
	private IPatentListProcessor patentListProcessor;
	
	private PatentDao patentDao;
	
	private ExecutorService addPatentExecutor;
	
	public PatentNoLoader(AddPatentRecord addPatentRecord,ExecutorService addPatentExecutor) {
		this.addPatentRecord = addPatentRecord;
		this.addPatentExecutor = addPatentExecutor;
		this.patentListProcessor = SpringContextUtils.getBean(SipoPatentListProcessor.class);
		this.patentDao = SpringContextUtils.getBean(PatentDao.class);
	}


	@Override
	public void run() {
		logger.info("开始执行专利项检索插入线程，执行任务信息{}",JsonUtils.marshalToString(addPatentRecord));
		try {
			List<PatentMain> list = patentListProcessor.processPatentList(addPatentRecord);
			for(PatentMain patentMain:list){
				PatentMain old = patentDao.selectPatentMainByPatentNo(patentMain.getPatentNo());
				if(null==old){
					patentDao.insertPatentMain(patentMain);
				}
			}
			
			addPatentRecord.setLoadStatus(new Short("1"));
			patentDao.updateAddPatentRecord(addPatentRecord);
		} catch (PatentNoLoadHttpWrongException e) {
			logger.info("执行专利项检索插入线程抓取网页出错，结束线程池并睡眠120秒，参数"+JsonUtils.marshalToString(addPatentRecord));
			try {
				addPatentExecutor.awaitTermination(120, TimeUnit.SECONDS);
			} catch (InterruptedException e1) {
				logger.error("执行专利项检索插入线程检索出错，睡眠120秒被打断",e);
			}
			
		} catch (PatentPharseException e) {
			logger.error("执行专利项检索插入线程解析网页出错，参数"+JsonUtils.marshalToString(addPatentRecord),e);
		} catch (Exception e) {
			logger.error("执行专利项检索插入线程出错，参数"+JsonUtils.marshalToString(addPatentRecord),e);
		}
		logger.info("结束执行专利项检索插入线程，执行任务信息{}",JsonUtils.marshalToString(addPatentRecord));
	}

}
