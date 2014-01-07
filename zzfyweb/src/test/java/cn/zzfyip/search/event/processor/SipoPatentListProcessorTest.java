package cn.zzfyip.search.event.processor;

import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zzfyip.search.base.BaseTest;
import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.dal.common.entity.AddPatentRecord;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.processor.IPatentListProcessor;
import cn.zzfyip.search.utils.DateUtils;

public class SipoPatentListProcessorTest extends BaseTest {
	@Autowired
	IPatentListProcessor patentListProcessor;
	@Autowired
	GlobalConstant globalConstant;

	@Test
	public void testProcessPatentList() {
		AddPatentRecord addPatentRecord = new AddPatentRecord();
		addPatentRecord.setPublicDate(DateUtils.convertDate("2010-01-06"));
		addPatentRecord.setPatentType(Short.valueOf("11"));
		addPatentRecord.setCurrentPage(new Long(1));

		List<PatentMain> list = Collections.emptyList();
		try {
			list = patentListProcessor.processPatentList(addPatentRecord);
		} catch (PatentNoLoadHttpWrongException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(list.size(), globalConstant.getNumPerPage().intValue());
	}
	
	@Test
	public void testCountPagePatentList() {
		Integer pageNum = patentListProcessor.countPagePatentList(DateUtils.convertDate("2010-01-06"), new Short("11"));
		Assert.assertEquals(54, pageNum.intValue());
	}

}
