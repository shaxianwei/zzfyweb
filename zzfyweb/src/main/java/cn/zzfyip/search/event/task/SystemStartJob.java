package cn.zzfyip.search.event.task;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.common.thread.NamedThreadFactory;
import cn.zzfyip.search.service.search.PatentInfoLoadService;
import cn.zzfyip.search.service.search.PatentNoLoadService;
import cn.zzfyip.search.service.search.PatentNoticeFawenLoadService;

@Service
public class SystemStartJob implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(SystemStartJob.class);

	@Autowired
	PatentNoLoadService patentNoLoadService;
	
	@Autowired
	PatentInfoLoadService patentInfoLoadService;
	
	@Autowired
	PatentNoticeFawenLoadService patentNoticeFawenLoadService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//root application context
		if(event.getApplicationContext().getParent() == null){
			
	         //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。 安全起见，延迟两分钟启动。
			logger.info("Spring容器启动完毕，加载系统检索线程");
			ExecutorService jobExecutor = Executors.newFixedThreadPool(3, new NamedThreadFactory("patent-JOB-dispatcher", true));
			
			jobExecutor.execute(new Runnable() {
				@Override
				public void run() {
					patentNoLoadService.addUnsearchedPatentRecordToPatentMainJob();
				}
			});
			
			jobExecutor.execute(new Runnable() {
				@Override
				public void run() {
					patentInfoLoadService.searchPatentInfoJob();
				}
			});
			
			jobExecutor.execute(new Runnable() {
				@Override
				public void run() {
					patentNoticeFawenLoadService.searchPatentNoticeFawenJob();
				}
			});
	    } 
	}
}
