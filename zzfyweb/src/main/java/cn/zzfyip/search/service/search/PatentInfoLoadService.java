package cn.zzfyip.search.service.search;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.dal.common.dao.PatentDao;
import cn.zzfyip.search.event.engine.processor.IPatentListProcessor;

@Service
public class PatentInfoLoadService implements InitializingBean{

	private static final Logger logger = LoggerFactory.getLogger(PatentInfoLoadService.class);

	@Autowired
	private IPatentListProcessor sipoPatentListProcessor;
	
	@Autowired
	private GlobalConstant globalConstant;
	
	@Autowired
	private PatentDao patentDao;
	
	private ExecutorService loadPatentInfoExecutor;

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
