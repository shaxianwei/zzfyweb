package cn.zzfyip.search.event.engine.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;
import cn.zzfyip.search.utils.HttpClientUtils;
import cn.zzfyip.search.utils.PatternUtils;

@Component
public class SipoPatentNoticeFawenProcessor implements IPatentNoticeFawenProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SipoPatentNoticeFawenProcessor.class);

	private static String SIPO_NOTICE_FAWEN_ADDRESS = "http://app.sipo.gov.cn:8080/sipoaid/jsp/notice/searchnotice.jsp";

	public List<PatentNoticeFawen> processPatentNoticeFawen(PatentMain patentMain) throws PatentNoLoadHttpWrongException {
	    Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("flag3", "1");

        String response = HttpClientUtils.post(SIPO_NOTICE_FAWEN_ADDRESS, paramMap,"GBK");
        if(StringUtils.isBlank(response)){
            throw new PatentNoLoadHttpWrongException();
        }
        //      logger.info("response = {}", response);
        List<PatentNoticeFawen> patentList = new ArrayList<PatentNoticeFawen>();
        
        String[] responseArrays = response.split("<tr onMouseOver=");
        for(String responsePart:responseArrays){
            String patentNo = StringUtils.trimToNull(PatternUtils.getMatchString("(.*)?recid=CN(.*)&leixin=(.*)", responsePart, 2));
            if(StringUtils.isNotBlank(patentNo)){
                PatentNoticeFawen patentNoticeFawen = new PatentNoticeFawen();
                patentNoticeFawen.setAddDate(new Date());
                
                patentList.add(patentNoticeFawen);
            }
        }
        if(patentList.size()==0){
            throw new PatentNoLoadHttpWrongException();
        }
        
        return patentList;
	}

}
