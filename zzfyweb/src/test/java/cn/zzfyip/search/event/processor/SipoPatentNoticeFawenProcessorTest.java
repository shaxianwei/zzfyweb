package cn.zzfyip.search.event.processor;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zzfyip.search.base.BaseTest;
import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;
import cn.zzfyip.search.event.engine.processor.IPatentNoticeFawenProcessor;

public class SipoPatentNoticeFawenProcessorTest extends BaseTest {
	@Autowired
	IPatentNoticeFawenProcessor patentNoticeFawenProcessor;
	@Autowired
	GlobalConstant globalConstant;

	@Test
	public void testProcessPatentNoticeFawen() {
		PatentMain patentMain = new PatentMain();
		patentMain.setPatentNo("200530006229.8");

		List<PatentNoticeFawen> list = Collections.emptyList();
		try {
			list = patentNoticeFawenProcessor.processPatentNoticeFawen(patentMain);
		} catch (PatentNoLoadHttpWrongException e) {
			e.printStackTrace();
		} catch (PatentPharseException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(list.size(), 4);
	}
	
	@Test
	public void processNoticeFawenUpdateDate() {
		Date updateDate = null;
		try {
			updateDate = patentNoticeFawenProcessor.processNoticeFawenUpdateDate();
		} catch (PatentNoLoadHttpWrongException e) {
			e.printStackTrace();
		} catch (PatentPharseException e) {
			e.printStackTrace();
		}
		Assert.assertNotNull(updateDate);
	}
	
}
