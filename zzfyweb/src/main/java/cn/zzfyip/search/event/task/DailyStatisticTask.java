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

@Service
public class DailyStatisticTask {

	@Autowired
	ReportService reportService;
	
	@Autowired
	PatentDao patentDao;
	
	private static final Logger logger = LoggerFactory.getLogger(DailyStatisticTask.class);

	public void runJob() {
		DailyStatisticTask.logger.info("DailyStatisticTask start!");
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
		DailyStatisticTask.logger.info("DailyStatisticTask finished!");
	}
}
