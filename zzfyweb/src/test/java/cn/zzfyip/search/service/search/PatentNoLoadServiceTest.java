package cn.zzfyip.search.service.search;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zzfyip.search.base.BaseTest;

public class PatentNoLoadServiceTest extends BaseTest{

	@Autowired
	PatentNoLoadService patentNoLoadService;
	
	@Test
	public void testCreateAddPatentRecord(){
		patentNoLoadService.createAddPatentRecord();
	}
}
