package cn.zzfyip.search.event.engine.processor;

import java.util.Date;
import java.util.List;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;

public interface IPatentNoticeFawenProcessor {

	public List<PatentNoticeFawen> processPatentNoticeFawen(PatentMain patentMain) throws PatentNoLoadHttpWrongException, PatentPharseException;

	public Date processNoticeFawenUpdateDate() throws PatentNoLoadHttpWrongException, PatentPharseException;
}
