package cn.zzfyip.search.dal.common.mapper;

import cn.zzfyip.search.common.annotation.Mapper;
import cn.zzfyip.search.dal.common.entity.PatentMain;
import cn.zzfyip.search.dal.common.entity.PatentMainExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PatentMainMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    int countByExample(PatentMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    int deleteByExample(PatentMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    int deleteByPrimaryKey(String patentNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    int insert(PatentMain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    int insertSelective(PatentMain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    List<PatentMain> selectByExample(PatentMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    PatentMain selectByPrimaryKey(String patentNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    int updateByExampleSelective(@Param("record") PatentMain record, @Param("example") PatentMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    int updateByExample(@Param("record") PatentMain record, @Param("example") PatentMainExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    int updateByPrimaryKeySelective(PatentMain record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table public.patent_main
     *
     * @mbggenerated Sat Mar 01 15:58:04 CST 2014
     */
    int updateByPrimaryKey(PatentMain record);
}