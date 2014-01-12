package cn.zzfyip.search.event.engine.processor;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.dal.common.entity.PatentInfo;
import cn.zzfyip.search.dal.common.entity.PatentMain;

public interface IPatentInfoProcessor {

	public PatentInfo processPatentInfo(PatentMain patentMain) throws PatentNoLoadHttpWrongException, PatentPharseException;
}
