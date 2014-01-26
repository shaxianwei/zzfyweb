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
import cn.zzfyip.search.common.exception.PatentPharseException;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;
import cn.zzfyip.search.utils.DateUtils;
import cn.zzfyip.search.utils.HttpClientUtils;
import cn.zzfyip.search.utils.PatternUtils;

@Component
public class SipoPatentNoticeFawenProcessor implements IPatentNoticeFawenProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SipoPatentNoticeFawenProcessor.class);

	private static String SIPO_NOTICE_FAWEN_ADDRESS = "http://app.sipo.gov.cn:8080/sipoaid/jsp/notice/searchnotice_result.jsp";
	private static String SIPO_NOTICE_FAWEN_INDEX_ADDRESS = "http://app.sipo.gov.cn:8080/sipoaid/jsp/notice/searchnotice.jsp";

	public List<PatentNoticeFawen> processPatentNoticeFawen(PatentMain patentMain) throws PatentNoLoadHttpWrongException, PatentPharseException {
	    Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("sqh", patentMain.getPatentNo());

        int tryTime = 0;
        String response = HttpClientUtils.post(SIPO_NOTICE_FAWEN_ADDRESS, paramMap,"GBK");
        while(StringUtils.isBlank(response)&&tryTime<3){
        	response = HttpClientUtils.post(SIPO_NOTICE_FAWEN_ADDRESS, paramMap,"GBK");
        	tryTime++;
        }
        if(StringUtils.isBlank(response)){
        	throw new PatentNoLoadHttpWrongException();
        }
        
        //      logger.info("response = {}", response);
        List<PatentNoticeFawen> patentList = new ArrayList<PatentNoticeFawen>();
        
        //如果该文还没有通知书
        if(StringUtils.contains(response, "暂时没有该申请号的通知书发文信息")){
        	return patentList;
        }
        
        try {
			String[] responseArrays = response.split("<tr onMouseOver=");
			int sequnceNo = 0;
			for(String responsePart:responseArrays){
			    String pattenString = "(.*)<td height=\"27\" class=\"dixian1\" align=\"center\">　(.*?)</td>\r\n          <td class=\"dixian1\" align=\"center\">　(.*?)</td>\r\n          <td class=\"dixian1\" align=\"center\">　(.*?)</td>\r\n          <td class=\"dixian1\" align=\"center\">　(.*?)</td>\r\n          <td class=\"dixian1\" align=\"center\">　(.*?)</td>\r\n          <td class=\"dixian1\" align=\"center\">　(.*?)</td>(.*)";
			    String noticeDateString = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 2));
			    if(StringUtils.isBlank(noticeDateString)){
			    	continue;
			    }
			    String registerNo = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 4));
			    String noticeCode = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 5));
			    String receiver = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 6));
			    String address = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 7));
			    
			    PatentNoticeFawen patentNoticeFawen = new PatentNoticeFawen();
			    patentNoticeFawen.setPatentNo(patentMain.getPatentNo());
			    patentNoticeFawen.setAddDate(new Date());
			    if(StringUtils.isNotBlank(noticeDateString)){
			    	patentNoticeFawen.setNoticeDate(DateUtils.convertDate(noticeDateString));
			    }
			    patentNoticeFawen.setRegisterNo(registerNo);
			    patentNoticeFawen.setNoticeCode(noticeCode);
			    patentNoticeFawen.setReceiver(receiver);
			    patentNoticeFawen.setAddress(address);
			    patentNoticeFawen.setSequnceNo(sequnceNo++);
			    patentList.add(patentNoticeFawen);
			}
		} catch (Exception e) {
			throw new PatentPharseException(e);
		}
        if(patentList.size()==0){
            throw new PatentPharseException();
        }
        
        return patentList;
	}
	
	public Date processNoticeFawenUpdateDate() throws PatentNoLoadHttpWrongException, PatentPharseException{
		Map<String, String> paramMap = new HashMap<String, String>();
		String response = HttpClientUtils.post(SIPO_NOTICE_FAWEN_INDEX_ADDRESS, paramMap,"GBK");
		if(StringUtils.isBlank(response)){
            throw new PatentNoLoadHttpWrongException();
        }
		String updateDateString = StringUtils.trimToNull(PatternUtils.getMatchString("(.*)最新更新日期：(.{0,20})</font></p>(.*)", response, 2));
		if(StringUtils.isBlank(updateDateString)){
            throw new PatentPharseException();
        }
		Date updateDate = DateUtils.convertDate(updateDateString);
		return updateDate;
	}

}
