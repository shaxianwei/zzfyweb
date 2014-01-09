package cn.zzfyip.search.dal.common.mapper;

import cn.zzfyip.search.common.annotation.Mapper;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawen;
import cn.zzfyip.search.dal.common.entity.PatentNoticeFawenExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Mapper
public interface PatentNoticeFawenMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    int countByExample(PatentNoticeFawenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    int deleteByExample(PatentNoticeFawenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    int insert(PatentNoticeFawen record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    int insertSelective(PatentNoticeFawen record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    List<PatentNoticeFawen> selectByExample(PatentNoticeFawenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    PatentNoticeFawen selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    int updateByExampleSelective(@Param("record") PatentNoticeFawen record, @Param("example") PatentNoticeFawenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    int updateByExample(@Param("record") PatentNoticeFawen record, @Param("example") PatentNoticeFawenExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    int updateByPrimaryKeySelective(PatentNoticeFawen record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_notice_fawen
     *
     * @mbggenerated Thu Jan 09 21:11:26 CST 2014
     */
    int updateByPrimaryKey(PatentNoticeFawen record);
}