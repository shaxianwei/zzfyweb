package cn.zzfyip.search.dal.common.dao;

import java.util.Date;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.zzfyip.search.dal.common.entity.AddPatentRecord;
import cn.zzfyip.search.dal.common.entity.AddPatentRecordExample;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.dal.common.mapper.AddPatentRecordMapper;
import cn.zzfyip.search.dal.common.mapper.PatentMainMapper;

@Repository
public class PatentDao {
	
	@Autowired
    public SqlSessionTemplate sqlSession;
	
	@Autowired
	AddPatentRecordMapper addPatentRecordMapper;
	
	@Autowired
	PatentMainMapper patentMainMapper;
	
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
	
	public PatentMain selectPatentMainByPatentNo(String patentNo){
		return patentMainMapper.selectByPrimaryKey(patentNo);
	}
}
