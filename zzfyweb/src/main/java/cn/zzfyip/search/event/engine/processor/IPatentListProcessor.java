package cn.zzfyip.search.event.engine.processor;

import java.util.Date;
import java.util.List;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.dal.common.entity.AddPatentRecord;
import cn.zzfyip.search.dal.common.entity.PatentMain;

public interface IPatentListProcessor {

	public List<PatentMain> processPatentList(AddPatentRecord addPatentRecord) throws PatentNoLoadHttpWrongException;
	public Integer countPagePatentList(Date publicDay,Short patentType);
}
