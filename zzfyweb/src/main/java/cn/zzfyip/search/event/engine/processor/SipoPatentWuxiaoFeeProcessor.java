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

@Component
public class SipoPatentWuxiaoFeeProcessor implements IPatentWuxiaoFeeProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SipoPatentWuxiaoFeeProcessor.class);

	private static String SIPO_WUXIAO_FEE_ADDRESS = "http://app.sipo.gov.cn:8080/searchfee/searchfee_action.jsp";


	@Override
	public Boolean processPatentHasWuxiaoFee(String patentNo) throws PatentNoLoadHttpWrongException, PatentPharseException {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sqh", patentNo);
		String response = HttpClientUtils.post(SIPO_WUXIAO_FEE_ADDRESS, paramMap, "GBK");
		
		if(StringUtils.isBlank(response)){
            throw new PatentNoLoadHttpWrongException();
        }
		
		logger.info(response);
		
		if(StringUtils.contains(response, "专利权无效宣告请求费")){
			return true;
		}else{
			return false;
		}
	}
	

	public static void main(String[] args) throws PatentNoLoadHttpWrongException, PatentPharseException{
		System.out.println(new SipoPatentWuxiaoFeeProcessor().processPatentHasWuxiaoFee("200910069535.3"));
	}

}
