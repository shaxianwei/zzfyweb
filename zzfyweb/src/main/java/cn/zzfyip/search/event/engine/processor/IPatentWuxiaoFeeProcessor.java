package cn.zzfyip.search.event.engine.processor;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;

public interface IPatentWuxiaoFeeProcessor {

	public Boolean processPatentHasWuxiaoFee(String patentNo) throws PatentNoLoadHttpWrongException,PatentPharseException;
}
