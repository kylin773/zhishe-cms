package edu.fzu.zhishe.core.dao;

import edu.fzu.zhishe.cms.model.CmsClub;
import edu.fzu.zhishe.cms.model.SysUser;

import java.util.List;

import edu.fzu.zhishe.core.dto.*;
import org.apache.ibatis.annotations.Param;

/**
 * @author PSF(52260506 @ qq.com)
 * @
 */
public interface CmsClubDAO {
    /*
     * 通过社团id获取社团的成员对象列表
     */
    List<SysUser> listClubMember(Integer id);

    /**
     * 获取人气推荐社团
     */
    List<CmsClubReturnData1> listHotClub(@Param("offset") Integer offset, @Param("limit") Integer limit,
                                         @Param("sort") String sort, @Param("order") String order);

    /**
     * 模糊搜索社团
     */
    List<CmsClubReturnData1> listClub(@Param("sort") String sort, @Param("order") String order,
                                      @Param("keyword") String keyword);


    /**
     * 加入社团列表
     */
    List<CmsClubReturnData1> listJoinedClub(@Param("offset") Integer offset, @Param("limit") Integer limit,
                                      @Param("sort") String sort, @Param("order") String order,
                                      @Param("userId") Integer userId);

    /**
     * 管理社团列表
     */
    List<CmsClubReturnData1> listManagedClub(@Param("offset") Integer offset, @Param("limit") Integer limit,
                                   @Param("sort") String sort, @Param("order") String order,
                                   @Param("userId") Integer userId);

    /**
     * 加入社团申请列表
     */
    List<CmsClubReturnData3> listJoinClubApply(@Param("offset") Integer offset, @Param("limit") Integer limit,
                                               @Param("sort") String sort, @Param("order") String order,
                                               @Param("userId") Integer userId);

    /**
     * 创建社团申请列表
     */
    List<CmsClubReturnData4> listCreateClubApply(@Param("offset") Integer offset, @Param("limit") Integer limit,
                                                 @Param("sort") String sort, @Param("order") String order,
                                                 @Param("userId") Integer userId);

    /**
     * 查看社员列表
     */
    List<CmsClub> listClubMember(@Param("offset") Integer offset, @Param("limit") Integer limit,
                                      @Param("sort") String sort, @Param("order") String order,
                                      @Param("clubId") Integer clubId);

    /**
     * 查看社员详情
     */
    CmsClubReturnData6 showClubMemberInfo(@Param("clubId") Integer clubId, @Param("userId") Integer userId);


    /**
     * 添加社员
     */
    List<CmsClub> addClubMember(Integer clubId, Integer userId);

}
