package cn.zzfyip.search.service.search;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.common.constant.PatentConstants;
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.common.thread.NamedThreadFactory;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.AddPatentRecord;
import cn.zzfyip.search.event.engine.PatentNoLoader;
import cn.zzfyip.search.event.engine.processor.IPatentListProcessor;
import cn.zzfyip.search.utils.DateUtils;
import cn.zzfyip.search.utils.ThreadSleepUtils;

/**
 * 专利号加载服务
 * 
 * @author changsure
 * 
 */
@Service
public class PatentNoLoadService implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(PatentNoLoadService.class);

	@Autowired
	private IPatentListProcessor sipoPatentListProcessor;

	@Autowired
	private GlobalConstant globalConstant;

	@Autowired
	private PatentDao patentDao;

	private ExecutorService addPatentExecutor;

	@Override
	public void afterPropertiesSet() throws Exception {
		addPatentExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("add-patent-dispatcher", true));
	}

	/**
	 * 添加专利项搜索的记录条目
	 * @throws PatentPharseException 
	 * @throws PatentNoLoadHttpWrongException 
	 */
	public void createAddPatentRecord() {
		logger.info("---------------------开始执行专利项添加任务服务-------------------");
		try {
			Date today = new Date();
			Date addEndDay = DateUtils.addDay(today, -30);
			// 查询数据库，最近的一天，如果没有，则以配置的起始天为准
			Date fromDate = DateUtils.convertDate(globalConstant.getPatentNoFromDate());
			Date maxRecentRecordDay = patentDao.selectMaxPublicDateInPatentMain();
			if (maxRecentRecordDay != null && maxRecentRecordDay.after(fromDate)) {
				fromDate = maxRecentRecordDay;
			}
			Date publicDate = DateUtils.addDay(fromDate, 1);

			while (publicDate.before(addEndDay)) {
				addPatentTypePatentRecordList(today, publicDate, PatentConstants.TYPE_01_FAMING);
				addPatentTypePatentRecordList(today, publicDate, PatentConstants.TYPE_02_SHIYONGXINXING);
				addPatentTypePatentRecordList(today, publicDate, PatentConstants.TYPE_03_WAIGUANSHEJI);
				publicDate = DateUtils.addDay(publicDate, 1);
			}
		} catch (PatentNoLoadHttpWrongException e) {
			logger.error("添加专利项添加任务读取网页出错",e);
		} catch (PatentPharseException e) {
			logger.error("添加专利项添加任务解析出错",e);
		}
		logger.info("---------------------结束执行专利项添加任务服务-------------------");
	}

	/**
	 * 添加专利项搜索的记录条目
	 * @throws PatentPharseException 
	 * @throws PatentNoLoadHttpWrongException 
	 * @throws  
	 */
	public void createAddPatentRecordByBeginDateAndEndDate(Date beginDate, Date endDate) throws PatentNoLoadHttpWrongException, PatentPharseException {
		Date publicDate = beginDate;
		Date today = new Date();

		while (publicDate.before(endDate)) {
			addPatentTypePatentRecordList(today, publicDate, new Short("11"));
			ThreadSleepUtils.sleepSeconds(2);
			addPatentTypePatentRecordList(today, publicDate, new Short("22"));
			ThreadSleepUtils.sleepSeconds(2);
			addPatentTypePatentRecordList(today, publicDate, new Short("33"));
			ThreadSleepUtils.sleepSeconds(2);
			publicDate = DateUtils.addDay(publicDate, 1);
		}

	}

	private void addPatentTypePatentRecordList(Date today, Date publicDate, Short patentType) throws PatentNoLoadHttpWrongException, PatentPharseException {
		logger.info("开始添加专利项读取任务记录，发布日为：{}，专利类型为{} 。", publicDate, patentType);
		Integer pageNumCount;
		try {
			pageNumCount = sipoPatentListProcessor.countPagePatentList(publicDate, patentType);
		} catch (PatentNoLoadHttpWrongException e) {
			throw e;
		} catch (PatentPharseException e) {
			throw e;
		}
		AddPatentRecord record = new AddPatentRecord();
		record.setAddDate(today);
		record.setLoadStatus(new Short("0"));
		record.setPatentType(patentType);
		record.setPerPageNum(globalConstant.getPatentNoNumPerPage().longValue());
		record.setPublicDate(publicDate);
		record.setTotalPage(pageNumCount.longValue());

		// 如果没有记录，也增加一条已经完成的load记录
		if (pageNumCount == 0) {
			record.setCurrentPage(0L);
			record.setId(null);
			record.setLoadStatus(new Short("1"));
			patentDao.insertAddPatentRecord(record);
		} else {
			Integer pageNum = 1;
			while (pageNum <= pageNumCount) {
				record.setCurrentPage(pageNum.longValue());
				record.setId(null);
				patentDao.insertAddPatentRecord(record);
				pageNum++;
			}
		}
		logger.info("结束添加专利项读取任务记录，发布日为：{}，专利类型为{} 。", publicDate, patentType);
	}

	/**
	 * 将未载入的专利项，添加到专利主表
	 */
	public void addUnsearchedPatentRecordToPatentMain() {
		logger.info("---------------------开始执行专利项主表添加检索服务-------------------");
		List<AddPatentRecord> list = patentDao.selectUnfinishedAddPatentRecord();

		for (AddPatentRecord record : list) {
			addPatentExecutor.execute(new PatentNoLoader(record, addPatentExecutor));
		}
		logger.info("---------------------结束执行专利项主表添加检索服务-------------------");
	}

	/**
	 * 将未载入的专利项，添加到专利主表
	 */
	public void addPatentRecordJob() {
		// 延迟一分钟执行
		ThreadSleepUtils.sleepMinutes(1);
		
		while (true) {
			try {
				this.createAddPatentRecord();
			} catch (Exception e) {
				logger.error("执行专利项任务添加 JOB 失败", e);
			}
			
			ThreadSleepUtils.sleepHours(2);
		}
	}
	/**
	 * 将未载入的专利项，添加到专利主表
	 */
	public void addUnsearchedPatentRecordToPatentMainJob() {
		// 延迟一分钟执行
		ThreadSleepUtils.sleepMinutes(5);
		
		while (true) {
			try {
				this.addUnsearchedPatentRecordToPatentMain();
			} catch (Exception e) {
				logger.error("执行专利项List检索添加MAIN表服务 JOB 失败", e);
			}
			
			ThreadSleepUtils.sleepHours(2);
		}
	}
}
