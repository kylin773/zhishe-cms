package edu.fzu.zhishe.cms.mapper;

import edu.fzu.zhishe.cms.model.CmsClub;
import edu.fzu.zhishe.cms.model.CmsClubExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CmsClubMapper {
    long countByExample(CmsClubExample example);

    int deleteByExample(CmsClubExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CmsClub record);

    int insertSelective(CmsClub record);

    List<CmsClub> selectByExample(CmsClubExample example);

    CmsClub selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CmsClub record, @Param("example") CmsClubExample example);

    int updateByExample(@Param("record") CmsClub record, @Param("example") CmsClubExample example);

    int updateByPrimaryKeySelective(CmsClub record);

    int updateByPrimaryKey(CmsClub record);
}