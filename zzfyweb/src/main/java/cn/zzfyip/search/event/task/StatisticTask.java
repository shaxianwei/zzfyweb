package cn.zzfyip.search.event.task;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentStatisticJob;
import cn.zzfyip.search.service.ReportService;
import cn.zzfyip.search.utils.DateUtils;

@Service
public class StatisticTask {

	@Autowired
	ReportService reportService;
	
	@Autowired
	PatentDao patentDao;
	
	private static final Logger logger = LoggerFactory.getLogger(StatisticTask.class);

	public void runFawenJob() {
		StatisticTask.logger.info("DailyStatisticTask start!");
		try {
			
			List<PatentStatisticJob> jobList = patentDao.selectInitFawenPatentStatisticJobList();
			Date maxFawenUpdateDate = patentDao.selectMaxFawenUpdateDateInPatentMain();
			for(PatentStatisticJob job:jobList){
				if(maxFawenUpdateDate.after(job.getFawenUpdateDate())){
					reportService.generateFawenReport(job);
				}else if(maxFawenUpdateDate.equals(job.getFawenUpdateDate())){
					Integer notFinishCount = patentDao.selectCountNotFinishedPatentMainByUpdateDate(job.getFawenUpdateDate());
					if(notFinishCount==0){
						reportService.generateFawenReport(job);
					}
				}
			}
		} catch (Exception e) {
			logger.error("任务执行失败",e);
		}
		StatisticTask.logger.info("DailyStatisticTask finished!");
	}
	
	public void runFeeJob() {
		StatisticTask.logger.info("Fee StatisticTask start!");
		try {
			Date today = DateUtils.today();
			Date monthEarlyDate = DateUtils.addMonth(today, -1);
			reportService.generateFeeReport(monthEarlyDate,today);
		} catch (Exception e) {
			logger.error("任务执行失败",e);
		}
		StatisticTask.logger.info("Fee StatisticTask finished!");
	}
	
	public void runBohuiJob() {
		StatisticTask.logger.info("Bohui StatisticTask start!");
		try {
			Date today = DateUtils.addDay(new Date(), 1);
			Date monthEarlyDate = DateUtils.addMonth(today, -1);
			reportService.generateBohuiReport(monthEarlyDate,today);
		} catch (Exception e) {
			logger.error("任务执行失败",e);
		}
		StatisticTask.logger.info("Bohui StatisticTask finished!");
	}
}
