package cn.zzfyip.search.event.engine.processor;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;

public interface IPatentPaperStatusProcessor {

	public String processPatentPaperStatus(String patentNo) throws PatentNoLoadHttpWrongException, PatentPharseException;
}
