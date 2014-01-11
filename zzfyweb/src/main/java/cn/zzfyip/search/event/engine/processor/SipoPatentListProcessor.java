package cn.zzfyip.search.event.engine.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.zzfyip.search.common.constant.GlobalConstant;
import cn.zzfyip.search.common.constant.PatentConstants;
import cn.zzfyip.search.common.exception.PatentNoLoadHttpWrongException;
import cn.zzfyip.search.dal.common.entity.AddPatentRecord;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.utils.DateUtils;
import cn.zzfyip.search.utils.HttpClientUtils;
import cn.zzfyip.search.utils.PatternUtils;

@Component
public class SipoPatentListProcessor implements IPatentListProcessor{

	private static final Logger logger = LoggerFactory
			.getLogger(SipoPatentListProcessor.class);

	private static String SIPO_SEARCH_ADDRESS = "http://211.157.104.87:8080/sipo/zljs/hyjs-jieguo.jsp";

	@Autowired
	private GlobalConstant globalConstant;

	@Override
	public List<PatentMain> processPatentList(AddPatentRecord addPatentRecord) throws PatentNoLoadHttpWrongException {

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("recshu", globalConstant.getPatentNoNumPerPage()+"");
		String searchWord = "公开（公告）日=("+ DateUtils.formatDateToPointDate(addPatentRecord.getPublicDate()) + ")";
		
		paramMap.put("searchword", searchWord);
		paramMap.put("pg", addPatentRecord.getCurrentPage()+"");
		paramMap.put("flag3", "1");
		paramMap.put("selectbase", addPatentRecord.getPatentType() + "");

		String response = HttpClientUtils.post(SIPO_SEARCH_ADDRESS, paramMap,"GBK");
		if(StringUtils.isBlank(response)){
			throw new PatentNoLoadHttpWrongException();
		}
		//		logger.info("response = {}", response);
		List<PatentMain> patentList = new ArrayList<PatentMain>();
		String[] responseArrays = response.split("<tr onMouseOver=");
		for(String responsePart:responseArrays){
			String patentNo = StringUtils.trimToNull(PatternUtils.getMatchString("(.*)?recid=CN(.*)&leixin=(.*)", responsePart, 2));
			String patentName = StringUtils.trimToNull(PatternUtils.getMatchString("(.*)&title=(.*)&ipc=(.*)", responsePart, 2));
//			String ipc = StringUtils.trimToNull(PatternUtils.getMatchString("(.*)&ipc=(.*)\" target=(.*)", responsePart, 2));
			if(StringUtils.isNotBlank(patentNo)){
				PatentMain patentMain = new PatentMain();
				patentMain.setAddDate(DateUtils.getCurrentTimestamp());
				patentMain.setPatentName(patentName);
				patentMain.setPublicDate(addPatentRecord.getPublicDate());
				patentMain.setPatentNo(patentNo);
				patentMain.setPatentStatus(PatentConstants.STATUS_01_INIT);
				patentMain.setPatentType(addPatentRecord.getPatentType());
				patentList.add(patentMain);
			}
		}
		if(patentList.size()==0){
			throw new PatentNoLoadHttpWrongException();
		}
		
		return patentList;
	}

	@Override
	public Integer countPagePatentList(Date publicDay, Short patentType) {
		Map<String, String> paramMap = new HashMap<String, String>();
		
		paramMap.put("recshu", "1");
		String searchWord = "公开（公告）日=("+ DateUtils.formatDateToPointDate(publicDay) + ")";
		
		paramMap.put("searchword", searchWord);
		paramMap.put("pg", "1");
		paramMap.put("flag3", "1");
		paramMap.put("selectbase", patentType + "");

		String response = HttpClientUtils.post(SIPO_SEARCH_ADDRESS, paramMap,"GBK");
//		logger.info("response = {}", response);

		// count pageNum of the totol
		Integer totol = 0;
		String totolString = StringUtils.trimToNull(PatternUtils.getMatchString("(.*)共有(.*)条记录(.*)", response, 2));
		if(NumberUtils.isDigits(totolString)){
			totol = NumberUtils.createInteger(totolString);
		}else{
			return 0;
		}
		
		Integer pageNum = Math.round(totol / globalConstant.getPatentNoNumPerPage())+1;
		return pageNum;
	}
}
