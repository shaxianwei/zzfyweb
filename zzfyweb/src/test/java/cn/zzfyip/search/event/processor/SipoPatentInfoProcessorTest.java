package cn.zzfyip.search.event.processor;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zzfyip.search.base.BaseTest;
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.dal.common.entity.PatentInfo;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.processor.IPatentInfoProcessor;

public class SipoPatentInfoProcessorTest extends BaseTest {
	@Autowired
	IPatentInfoProcessor patentInfoProcessor;

	@Test
	public void testProcessPatentList() {
		PatentMain patentMain = new PatentMain();
		patentMain.setPatentNo("200710300551");

		PatentInfo patentInfo = null;
        try {
            patentInfo = patentInfoProcessor.processPatentInfo(patentMain);
        } catch (PatentNoLoadHttpWrongException e) {
            e.printStackTrace();
        }
		Assert.assertEquals(patentInfo.getPatentName(), "苗移植机");
	}

}
