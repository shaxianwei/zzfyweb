package cn.zzfyip.search.event.engine.processor;

import java.util.List;

import cn.zzfyip.search.dal.common.entity.PatentFee;
import cn.zzfyip.search.dal.common.entity.PatentMain;

public interface IPatentFeeProcessor {

	public List<PatentFee> processPatentInfo(PatentMain patentMain);
}
