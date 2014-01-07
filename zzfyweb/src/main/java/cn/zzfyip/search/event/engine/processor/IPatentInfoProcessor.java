package cn.zzfyip.search.event.engine.processor;

import cn.zzfyip.search.dal.common.entity.PatentInfo;
import cn.zzfyip.search.dal.common.entity.PatentMain;

public interface IPatentInfoProcessor {

	public PatentInfo processPatentInfo(PatentMain patentMain);
}
