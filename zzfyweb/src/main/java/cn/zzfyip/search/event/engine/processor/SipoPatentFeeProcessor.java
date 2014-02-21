package cn.zzfyip.search.event.engine.processor;

import java.math.BigDecimal;
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
import cn.zzfyip.search.dal.common.entity.PatentFee;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;
import cn.zzfyip.search.utils.DateUtils;
import cn.zzfyip.search.utils.HttpClientUtils;
import cn.zzfyip.search.utils.PatternUtils;

@Component
public class SipoPatentFeeProcessor implements IPatentFeeProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SipoPatentFeeProcessor.class);

	private static String SIPO_WUXIAO_FEE_ADDRESS = "http://app.sipo.gov.cn:8080/searchfee/searchfee_action.jsp";

	@Override
	public List<PatentFee> processPatentFee(String patentNo) throws PatentNoLoadHttpWrongException, PatentPharseException {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sqh", patentNo);
		String response = HttpClientUtils.post(SIPO_WUXIAO_FEE_ADDRESS, paramMap, "GBK");

		if (StringUtils.isBlank(response)) {
			throw new PatentNoLoadHttpWrongException();
		}

//		logger.info("response = {}", response);

		List<PatentFee> patentFeeList = new ArrayList<PatentFee>();
		try {
			String[] responseArrays = response.split("<tr onMouseOver=");
			int sequnceNo = 0;
			for (String responsePart : responseArrays) {
				String pattenString = "(.*)	  <td height=\"27\" class=\"dixian1\" align=\"center\">　(.*)</td>\r\n          <td height=\"27\" class=\"dixian1\" align=\"center\">　(.*)</td>\r\n          <td height=\"27\" class=\"dixian1\" align=\"center\">　(.*)</td>\r\n          <td height=\"27\" class=\"dixian1\" align=\"center\">　(.*)</td>\r\n          <td height=\"27\" class=\"dixian1\" align=\"center\">　(.*)</td>\r\n          <td height=\"27\" class=\"dixian1\" align=\"center\">　(.*)</td>\r\n          <td height=\"27\" class=\"dixian1\" align=\"center\">　(.*)</td>\r\n          <td height=\"27\" class=\"dixian1\" align=\"center\">　(.*)</td>(.*)";
				String feeDateString = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 9));
				if (StringUtils.isBlank(feeDateString)) {
					continue;
				}
				String feeAmount = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 3));
				String feeType = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 4));
				String registerNo = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 5));
				String receiptNo = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 6));
				String feePerson = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 7));
				String status = StringUtils.trimToNull(PatternUtils.getMatchString(pattenString, responsePart, 8));

				PatentFee patentFee = new PatentFee();
				patentFee.setAddDate(new Date());
				patentFee.setPatentNo(patentNo);
				if(StringUtils.isNotBlank(feeAmount)){
					patentFee.setFeeAmount(new BigDecimal(feeAmount));
				}
				patentFee.setFeeType(feeType);
				patentFee.setRegisterNo(registerNo);
				patentFee.setReceiptNo(receiptNo);
				patentFee.setFeePerson(feePerson);
				patentFee.setStatus(status);
				patentFee.setFeeDate(DateUtils.convertDate(feeDateString, DateUtils.DATE_POINT_PATTERN));
				patentFeeList.add(patentFee);
			}
		} catch (Exception e) {
			throw new PatentPharseException(e);
		}
		if (patentFeeList.size() == 0) {
			throw new PatentPharseException();
		}

		return patentFeeList;
	}

	public static void main(String[] args) throws PatentNoLoadHttpWrongException, PatentPharseException {
		System.out.println(new SipoPatentFeeProcessor().processPatentFee("201330255477.0"));
	}

}
