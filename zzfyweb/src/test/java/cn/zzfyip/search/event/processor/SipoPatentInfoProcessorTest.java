package cn.zzfyip.search.event.processor;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.zzfyip.search.base.BaseTest;
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.dal.common.entity.PatentInfo;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.event.engine.processor.IPatentInfoProcessor;

public class SipoPatentInfoProcessorTest extends BaseTest {
	@Autowired
	IPatentInfoProcessor patentInfoProcessor;

	@Test
	public void testProcessPatentList() {
		PatentMain patentMain = new PatentMain();
//		patentMain.setPatentNo("200880124001.X");
//		patentMain.setPatentName("电力线通信系统中用于获取包括空间和时间再用的资源共享的方法、系统、集成电路、通信模块、和计算机可读介质");
		patentMain.setPatentNo("201010281407.8");
		patentMain.setPatentName("粉煤灰脱硅碱渣或提取Al<sub>2</sub>O<sub>3</sub>工艺后的碱性赤泥的碳分洗涤脱钠方法");

		PatentInfo patentInfo = null;
        try {
            patentInfo = patentInfoProcessor.processPatentInfo(patentMain);
        } catch (PatentNoLoadHttpWrongException e) {
            e.printStackTrace();
        } catch (PatentPharseException e) {
			e.printStackTrace();
		}
		Assert.assertEquals(patentInfo.getPatentName(), patentMain.getPatentName());
	}

}
