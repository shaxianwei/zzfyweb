package cn.zzfyip.search.dal.common.dao;

import java.util.Date;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.zzfyip.search.dal.common.entity.AddPatentRecord;
import cn.zzfyip.search.dal.common.entity.AddPatentRecordExample;
import cn.zzfyip.search.dal.common.entity.PatentInfo;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawenExample;
import cn.zzfyip.search.dal.common.mapper.AddPatentRecordMapper;
import cn.zzfyip.search.dal.common.mapper.PatentInfoMapper;
import cn.zzfyip.search.dal.common.mapper.PatentMainMapper;
import cn.zzfyip.search.dal.common.mapper.PatentNoticeFawenMapper;

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
	
	public Date selectMaxPublicDateInPatentMain(){
		Date maxPublicDate = sqlSession.selectOne("cn.zzfyip.search.dal.common.dao.PatentDao.selectMaxPublicDateInPatentMain");
		return maxPublicDate;
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
	
	public List<PatentMain> selectFirst100RecordPatentInfoSearchPatentMain(){
	    return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectFirst100RecordPatentInfoSearchPatentMain");
	}
	
	public void insertPatentInfo(PatentInfo record){
	    patentInfoMapper.insertSelective(record);
    }
	
	public PatentInfo selectPatentInfoByPatentNo(String patentNo){
	    return patentInfoMapper.selectByPrimaryKey(patentNo);
	}
	
	@Transactional
	public void refreshPatentNoticeFawenByPatentNo(String patentNo,List<PatentNoticeFawen> list){
	    PatentNoticeFawenExample example = new PatentNoticeFawenExample();
	    example.createCriteria().andPatentNoEqualTo(patentNo);
	    patentNoticeFawenMapper.deleteByExample(example);
	    
	    for(PatentNoticeFawen record:list){
	        patentNoticeFawenMapper.insertSelective(record);
	    }
	}
	
	public List<PatentMain> selectFirst100RecordPatentNoticeFawenSearchPatentMain(){
        return sqlSession.selectList("cn.zzfyip.search.dal.common.dao.PatentDao.selectFirst100RecordPatentNoticeFawenSearchPatentMain");
    }
	
}
