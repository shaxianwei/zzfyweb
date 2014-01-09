package cn.zzfyip.search.event.task;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.service.search.PatentInfoLoadService;
import cn.zzfyip.search.service.search.PatentNoLoadService;
import cn.zzfyip.search.service.search.PatentNoticeFawenLoadService;

@Service
public class SystemStartJob implements InitializingBean {

	@Autowired
	PatentNoLoadService patentNoLoadService;
	
	@Autowired
	PatentInfoLoadService patentInfoLoadService;
	
	@Autowired
	PatentNoticeFawenLoadService patentNoticeFawenLoadService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		patentInfoLoadService.searchPatentInfoJob();
		patentNoticeFawenLoadService.searchPatentNoticeFawenJob();
	}

}
