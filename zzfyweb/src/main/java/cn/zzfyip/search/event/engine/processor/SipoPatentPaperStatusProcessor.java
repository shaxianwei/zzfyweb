package cn.zzfyip.search.event.engine.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.utils.HttpClientUtils;
import cn.zzfyip.search.utils.PatternUtils;

@Component
public class SipoPatentPaperStatusProcessor implements IPatentPaperStatusProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SipoPatentPaperStatusProcessor.class);

	private static String SIPO_PAPER_STATUS_ADDRESS = "http://publicquery.sipo.gov.cn/search/info/txn801510.do";

	@Override
	public String processPatentPaperStatus(String patentNo) throws PatentNoLoadHttpWrongException, PatentPharseException {
		String patentPaperStatus = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("select-key:shenqingh", StringUtils.remove(patentNo, "."));
		String response = HttpClientUtils.post(SIPO_PAPER_STATUS_ADDRESS, paramMap, "GBK");
		
		if(StringUtils.isBlank(response)){
            throw new PatentNoLoadHttpWrongException();
        }
		
		patentPaperStatus = StringUtils.trimToNull(PatternUtils.getMatchString(".*案件状态：</th>\n                <td>(.*?)</td>.*", response, 1));
		return patentPaperStatus;
	}

	public static void main(String[] args) throws PatentNoLoadHttpWrongException, PatentPharseException{
		SipoPatentPaperStatusProcessor processor = new SipoPatentPaperStatusProcessor();
		System.out.println(processor.processPatentPaperStatus("2011100245950"));
	}
}
