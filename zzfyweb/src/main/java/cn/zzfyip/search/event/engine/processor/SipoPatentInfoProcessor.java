package cn.zzfyip.search.event.engine.processor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.zzfyip.search.dal.common.entity.PatentInfo;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.utils.DateUtils;
import cn.zzfyip.search.utils.HttpClientUtils;
import cn.zzfyip.search.utils.PatternUtils;

@Component
public class SipoPatentInfoProcessor implements IPatentInfoProcessor {

	private static final Logger logger = LoggerFactory.getLogger(SipoPatentInfoProcessor.class);

	private static String SIPO_INFO_ADDRESS = "http://211.157.104.87:8080/sipo/zljs/hyjs-yx-new.jsp";

	@Override
	public PatentInfo processPatentInfo(PatentMain patentMain) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("recid", patentMain.getPatentNo());
		String response = HttpClientUtils.post(SIPO_INFO_ADDRESS, paramMap, "GBK");
		
		String patentName = StringUtils.trimToNull(PatternUtils.getMatchString(".*名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称： </td>\r\n    <td colspan=\"3\" class=\"kuang2\">&nbsp;(.{0,50})</td>.*", response, 1));
		String applyDateString = StringUtils.trimToNull(PatternUtils.getMatchString(".*申&nbsp; &nbsp;请&nbsp;&nbsp; 日：\r\n    </td>\r\n    <td class=\"kuang2\">&nbsp;(.{0,20})</td>.*", response, 1));
		String publicNo = StringUtils.trimToNull(PatternUtils.getMatchString(".*公&nbsp;开&nbsp;\\(公告\\)&nbsp;号：</td>\r\n    <td class=\"kuang2\">&nbsp;(.{0,20})</td>.*", response, 1));
		String mainCategoryNo = StringUtils.trimToNull(PatternUtils.getMatchString(".*主 &nbsp;分 &nbsp;类 &nbsp;号： </td>\r\n    <td class=\"kuang2\">&nbsp;(.{0,30})</td>.*", response, 1));
		String preApplyNo = StringUtils.trimToNull(PatternUtils.getMatchString(".*分案原申请号：</td>\r\n    <td class=\"kuang2\">&nbsp;(.{0,20})</td>.*", response, 1));
		String secCategoryNo = StringUtils.trimToNull(PatternUtils.getMatchString(".*分&nbsp;&nbsp;&nbsp; 类&nbsp; &nbsp;&nbsp;号： </td>\r\n    <td colspan=\"3\" class=\"kuang2\">&nbsp;(.{0,30})</td>.*", response, 1));
		String certificateDateString = StringUtils.trimToNull(PatternUtils.getMatchString(".*颁&nbsp;&nbsp;&nbsp;证&nbsp; &nbsp;&nbsp;&nbsp;日： </td>\r\n    <td class=\"kuang2\">(.{0,20})</td>.*", response, 1));
		String preRight = StringUtils.trimToNull(PatternUtils.getMatchString(".*优&nbsp;&nbsp;&nbsp;先&nbsp;&nbsp;&nbsp;权：</td>\r\n    <td class=\"kuang2\">&nbsp;(.{0,30})</td>.*", response, 1));
		String applier = StringUtils.trimToNull(PatternUtils.getMatchString(".*申请\\(专利权\\)人： </td>\r\n    <td colspan=\"3\" class=\"kuang2\">&nbsp;(.{0,50})</td>.*", response, 1));
		String address = StringUtils.trimToNull(PatternUtils.getMatchString(".*地 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</td>\r\n    <td colspan=\"3\" class=\"kuang2\">&nbsp;(.{0,50})</td>.*", response, 1));
		String inventor = StringUtils.trimToNull(PatternUtils.getMatchString(".*发&nbsp;明&nbsp;\\(设计\\)人：</td>\r\n    <td class=\"kuang2\">&nbsp;(.{0,50})</td>.*", response, 1));
		String globalPatent = StringUtils.trimToNull(PatternUtils.getMatchString(".*国 &nbsp;际 申 请：</td>\r\n    <td class=\"kuang2\">&nbsp;(.{0,20})</td>.*", response, 1));
		String globalPublic = StringUtils.trimToNull(PatternUtils.getMatchString(".*国&nbsp;&nbsp;际&nbsp;&nbsp;公&nbsp;&nbsp;布：</td>\r\n    <td class=\"kuang2\">&nbsp;(.{0,20})</td>.*", response, 1));
		String entryCountryDateString = StringUtils.trimToNull(PatternUtils.getMatchString(".*进入国家日期：</td>\r\n    <td class=\"kuang2\">&nbsp;(.{0,20})</td>.*", response, 1));
		String agency = StringUtils.trimToNull(PatternUtils.getMatchString(".*专利&nbsp;代理&nbsp;机构： </td>\r\n    <td class=\"kuang3\">&nbsp;(.{0,30})</td>.*", response, 1));
		String agent = StringUtils.trimToNull(PatternUtils.getMatchString(".*代&nbsp;&nbsp;&nbsp;理&nbsp;&nbsp;&nbsp;人：</td>\r\n    <td class=\"kuang3\">&nbsp;(.{0,20})</td>.*", response, 1));
		String summary = StringUtils.trimToNull(PatternUtils.getMatchString(".*<td align=\"left\" class=\"zi_zw\">&nbsp;(.*)</td>.*", response, 1));
		
		PatentInfo patentInfo = new PatentInfo();
		patentInfo.setPublicDate(patentMain.getPublicDate());
		patentInfo.setPatentNo(patentMain.getPatentNo());
		patentInfo.setAddDate(new Date());
		patentInfo.setAddress(address);
		patentInfo.setAgency(agency);
		patentInfo.setAgent(agent);
		if(StringUtils.isNotBlank(applyDateString)){
			patentInfo.setApplyDate(DateUtils.convertDate(applyDateString, DateUtils.DATE_POINT_PATTERN));
		}
		if(StringUtils.isNotBlank(certificateDateString)){
			patentInfo.setCertificateDate(DateUtils.convertDate(certificateDateString, DateUtils.DATE_POINT_PATTERN));
		}
		if(StringUtils.isNotBlank(entryCountryDateString)){
			patentInfo.setEntryCountryDate(DateUtils.convertDate(entryCountryDateString, DateUtils.DATE_POINT_PATTERN));
		}
		patentInfo.setGlobalPublic(globalPublic);
		patentInfo.setGlobalPatent(globalPatent);
		patentInfo.setInventor(inventor);
		patentInfo.setApplier(applier);
		patentInfo.setMainCategoryNo(mainCategoryNo);
		patentInfo.setPatentName(patentName);
		patentInfo.setPreApplyNo(preApplyNo);
		patentInfo.setPreRight(preRight);
		patentInfo.setPublicNo(publicNo);
		patentInfo.setSecCategoryNo(secCategoryNo);
		patentInfo.setSummary(summary);
		
		return patentInfo;
	}

}
