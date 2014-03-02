package cn.zzfyip.search.dal.common.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.zzfyip.search.common.constant.PatentConstants;
import cn.zzfyip.search.dal.common.entity.AddPatentRecord;
import cn.zzfyip.search.dal.common.entity.AddPatentRecordExample;
import cn.zzfyip.search.dal.common.entity.PatentFee;
import cn.zzfyip.search.dal.common.entity.PatentFeeExample;
import cn.zzfyip.search.dal.common.entity.PatentInfo;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.dal.common.entity.PatentMainExample;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawenExample;
import cn.zzfyip.search.dal.common.entity.PatentStatisticJob;
import cn.zzfyip.search.dal.common.entity.PatentStatisticJobExample;
import cn.zzfyip.search.dal.common.mapper.AddPatentRecordMapper;
import cn.zzfyip.search.dal.common.mapper.PatentFeeMapper;
import cn.zzfyip.search.dal.common.mapper.PatentInfoMapper;
import cn.zzfyip.search.dal.common.mapper.PatentMainMapper;
import cn.zzfyip.search.dal.common.mapper.PatentNoticeFawenMapper;
import cn.zzfyip.search.dal.common.mapper.PatentStatisticJobMapper;
import cn.zzfyip.search.dal.common.vo.PatentStatisticVo;
import cn.zzfyip.search.utils.DateUtils;

@Repository
public class PatentDao {
	
	@Autowired
    public SqlSessionTemplate sqlSession;
	
	@Autowired
	AddPatentRecordMapper addPatentRecordMapper;
	
	@Autowired
	PatentMainMapper patentMainMapper;
	
	@Autowired
	PatentInfoMapper patentInfoMapper;
	
	@Autowired
	PatentNoticeFawenMapper patentNoticeFawenMapper;
	
	@Autowired
	PatentFeeMapper patentFeeMapper;
	
	@Autowired
	PatentStatisticJobMapper patentStatisticJobMapper;
	
	public Date selectMaxPublicDateInPatentMain(){
		Date maxPublicDate = sqlSession.selectOne("cn.zzfyip.search.dal.common.dao.PatentDao.selectMaxPublicDateInPatentMain");
		return maxPublicDate;
	}
	
	public Date selectMaxFawenUpdateDateInPatentMain(){
		Date maxFawenUPdateDate = sqlSession.selectOne("cn.zzfyip.search.dal.common.dao.PatentDao.selectMaxFawenUpdateDate");
		return maxFawenUPdateDate;
	}
	
	public void insertAddPatentRecord(AddPatentRecord record){
		addPatentRecordMapper.insertSelective(record);
	}
	
	public void updateAddPatentRecord(AddPatentRecord record){
		addPatentRecordMapper.updateByPrimaryKeySelective(record);
	}
	
	public List<AddPatentRecord> selectUnfinishedAddPatentRecord(){
		AddPatentRecordExample example = new AddPatentRecordExample();
		example.createCriteria().andLoadStatusEqualTo(new Short("0"));
		example.setOrderByClause("patent_type,public_date");
		return addPatentRecordMapper.selectByExample(example);
	}
	
	public void insertPatentMain(PatentMain record){
		patentMainMapper.insertSelective(record);
	}
	
	public void updatePatentMain(PatentMain record){
	    patentMainMapper.updateByPrimaryKeySelective(record);
	}
	
	public PatentMain selectPatentMainByPatentNo(String patentNo){
		return patentMainMapper.selectByPrimaryKey(patentNo);
	}
	
	public Integer selectCountNotFinishedPatentMainByUpdateDate(Date fawenUpdateDate){
		PatentMainExample example = new PatentMainExample();
		example.createCriteria().andPatentFawenSearchTypeEqualTo(PatentConstants.FAWEN_STATUS_01_NORMAL).andPatentFawenSearchTimeNotEqualTo(fawenUpdateDate);
		return patentMainMapper.countByExample(example);
	}
	
	public List<PatentMain> selectFirst100RecordPatentInfoSearchPatentMain(){
	    return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectFirst100RecordPatentInfoSearchPatentMain");
	}
	
	public void insertPatentInfo(PatentInfo record){
	    patentInfoMapper.insertSelective(record);
    }
	
	public PatentInfo selectPatentInfoByPatentNo(String patentNo){
	    return patentInfoMapper.selectByPrimaryKey(patentNo);
	}
	
//	@Transactional
	public void refreshPatentNoticeFawenByPatentNo(String patentNo,List<PatentNoticeFawen> list){
	    PatentNoticeFawenExample example = new PatentNoticeFawenExample();
	    example.createCriteria().andPatentNoEqualTo(patentNo);
	    Integer patentFawenCount = patentNoticeFawenMapper.countByExample(example);
	    if(patentFawenCount.intValue()==list.size()){
	    	return;
	    }
	    patentNoticeFawenMapper.deleteByExample(example);
	    for(PatentNoticeFawen record:list){
	        patentNoticeFawenMapper.insertSelective(record);
	    }
	}
	
//	@Transactional
	public void refreshPatentFeeByPatentNo(String patentNo,List<PatentFee> list){
		PatentFeeExample example = new PatentFeeExample();
		example.createCriteria().andPatentNoEqualTo(patentNo);
		Integer patentFeeCount = patentFeeMapper.countByExample(example);
		if(patentFeeCount.intValue()==list.size()){
			return;
		}
		patentFeeMapper.deleteByExample(example);
		for(PatentFee record:list){
			patentFeeMapper.insertSelective(record);
		}
	}
	
	public List<PatentMain> selectFirst100RecordPatentNoticeFawenSearchPatentMain(Date fawenUpdateDate){
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("fawenUpdateDate", DateUtils.formatDate(fawenUpdateDate));
	    
	    return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectFirst100RecordPatentNoticeFawenSearchPatentMain",paramMap);
    }
	
	public List<PatentMain> selectFirst100RecordPatentFeeSearchPatentMain(){
		return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectFirst100RecordPatentFeeSearchPatentMain");
	}
	
	public List<PatentMain> selectFirst100RecordPatentLawStatusSearchPatentMain(){
		return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectFirst100RecordPatentLawStatusSearchPatentMain");
	}
	
	public List<PatentMain> selectFirst100RecordPatentPaperStatusSearchPatentMain(){
		return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectFirst100RecordPatentPaperStatusSearchPatentMain");
	}
	
	public void insertPatentStatisticJobRecord(PatentStatisticJob record){
		patentStatisticJobMapper.insertSelective(record);
	}
	public void updatePatentStatisticJobRecord(PatentStatisticJob record){
		patentStatisticJobMapper.updateByPrimaryKeySelective(record);
	}
	
	public Integer selectCountPatentStatisticJobByFawenUpdateDate(Date updateDate){
		PatentStatisticJobExample example = new PatentStatisticJobExample();
		example.createCriteria().andFawenUpdateDateEqualTo(updateDate);
		return patentStatisticJobMapper.countByExample(example);
	}
	
	public List<PatentStatisticJob> selectInitFawenPatentStatisticJobList(){
		PatentStatisticJobExample example = new PatentStatisticJobExample();
		example.createCriteria().andJobStatusEqualTo("INIT").andJobTypeEqualTo("FAWEN");
		return patentStatisticJobMapper.selectByExample(example);
	}
	
	public List<PatentStatisticVo> selectPatentFawenStatisticVoListByFawenUpdateDate(Date fawenUpdateDate){
		Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("fawenUpdateDate", DateUtils.formatDate(fawenUpdateDate));
	    
	    return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectFawenStatisticData",paramMap);
	}
	public List<PatentStatisticVo> selectPatentFeeStatisticVoListByFromDateAndEndDate(Date fromDate,Date endDate){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("fromDate", DateUtils.formatDate(fromDate));
		paramMap.put("endDate", DateUtils.formatDate(endDate));
		
		return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectFeeStatisticData",paramMap);
	}
	public List<PatentStatisticVo> selectPatentBohuiStatisticVoListByFromDateAndEndDate(Date fromDate,Date endDate){
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("fromDate", DateUtils.formatDate(fromDate));
		paramMap.put("endDate", DateUtils.formatDate(endDate));
		
		return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectBohuiStatisticData",paramMap);
	}
}
