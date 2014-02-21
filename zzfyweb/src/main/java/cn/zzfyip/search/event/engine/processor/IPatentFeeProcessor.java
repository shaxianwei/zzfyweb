package cn.zzfyip.search.event.engine.processor;

import java.util.List;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.dal.common.entity.PatentFee;

public interface IPatentFeeProcessor {

	public List<PatentFee> processPatentFee(String patentNo) throws PatentNoLoadHttpWrongException,PatentPharseException;
}
