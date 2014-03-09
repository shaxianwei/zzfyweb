package cn.zzfyip.search.event.engine.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.zzfyip.search.common.constant.PatentConstants;
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.utils.HttpClientUtils;

@Deprecated
@Component
public class SipoPatentLawStatusProcessor implements IPatentLawStatusProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SipoPatentLawStatusProcessor.class);

	private static String SIPO_LAW_STATUS_ADDRESS = "http://search.sipo.gov.cn/zljs/FlztResult.jsp";

	@Override
	public String processPatentLawStatus(String patentNo) throws PatentNoLoadHttpWrongException, PatentPharseException {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("searchword", "申请号=" + patentNo + "%");
		String response = HttpClientUtils.post(SIPO_LAW_STATUS_ADDRESS, paramMap, "GBK");

		if (StringUtils.isBlank(response)) {
			throw new PatentNoLoadHttpWrongException();
		}

//		logger.info(response);

		String patentStatus = "";

		try {
			if (StringUtils.contains(response, "视为撤回") || StringUtils.contains(response, "驳回")|| StringUtils.contains(response, "放弃专利权")|| StringUtils.contains(response, "视为放弃")|| StringUtils.contains(response, "专利权的终止")|| StringUtils.contains(response, "专利权全部无效")) {
				patentStatus = PatentConstants.STATUS_05_CHEHUI;
			}
		} catch (Exception e) {
			throw new PatentPharseException(e);
		}

		return patentStatus;
	}
	
	public static void main(String[] args) throws PatentNoLoadHttpWrongException, PatentPharseException{
		System.out.println(new SipoPatentLawStatusProcessor().processPatentLawStatus("200910069535.3"));
	}

}
