package cn.zzfyip.search.service.search;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.common.thread.NamedThreadFactory;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.PatentLawStatusLoader;
import cn.zzfyip.search.event.engine.PatentPaperStatusLoader;
import cn.zzfyip.search.utils.ThreadSleepUtils;

@Service
public class PatentPaperStatusLoadService implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(PatentPaperStatusLoadService.class);

	@Autowired
	private GlobalConstant globalConstant;

	@Autowired
	private PatentDao patentDao;

	private ExecutorService loadPatentPaperStatusExecutor;

	@Override
	public void afterPropertiesSet() throws Exception {
		loadPatentPaperStatusExecutor = Executors.newFixedThreadPool(globalConstant.getPatentPaperStatusSearchThreadNum(), new NamedThreadFactory("patent-paperStatus-dispatcher", true));
	}

	public void searchPatentPaperStatus() {

		logger.info("---------------------开始执行专利项案卷状态检索服务-------------------");
		List<PatentMain> list = patentDao.selectFirst100RecordPatentPaperStatusSearchPatentMain();

		while (list.size() != 0) {
			logger.info("执行专利项案卷状态检索服务，本分页执行开始。 ");
			for (PatentMain patentMain : list) {
				PatentPaperStatusLoader patentPaperStatusLoader = new PatentPaperStatusLoader(patentMain, loadPatentPaperStatusExecutor);
				loadPatentPaperStatusExecutor.execute(patentPaperStatusLoader);
			}
			while (((ThreadPoolExecutor) loadPatentPaperStatusExecutor).getQueue().size() > 0) {
				logger.info("执行专利项案卷状态检索服务，活跃线程数{}，队列中线程数{}，线程结束等待时间{}毫秒，主线程睡眠10秒。 ", ((ThreadPoolExecutor) loadPatentPaperStatusExecutor).getActiveCount(),
						((java.util.concurrent.ThreadPoolExecutor) loadPatentPaperStatusExecutor).getQueue().size(),globalConstant.getPatentInfoThreadDelayMilliSeconds());
				ThreadSleepUtils.sleepSeconds(10);
			}
			logger.info("执行专利项案卷状态检索服务，本分页执行完成。 ");
			list = patentDao.selectFirst100RecordPatentLawStatusSearchPatentMain();
		}

		logger.info("---------------------完成执行专利项案卷状态检索服务-------------------");

	}

	public void searchPatentPaperStatusStatusJob() {

		// 延迟一分钟执行
		ThreadSleepUtils.sleepSeconds(30);

		while (true) {
			try {
				this.searchPatentPaperStatus();
			} catch (Exception e) {
				logger.error("执行专利项案卷状态检索服务 JOB 失败", e);
			}

			//下次任务检测2小时候进行
			ThreadSleepUtils.sleepHours(2);
		}
	}

}
