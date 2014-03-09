package cn.zzfyip.search.event.engine.processor;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;

@Deprecated
public interface IPatentLawStatusProcessor {

	public String processPatentLawStatus(String patentNo) throws PatentNoLoadHttpWrongException,PatentPharseException;
}
