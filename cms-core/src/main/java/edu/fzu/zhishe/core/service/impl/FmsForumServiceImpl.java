package edu.fzu.zhishe.core.service.impl;

import com.github.pagehelper.PageHelper;
import edu.fzu.zhishe.cms.mapper.FmsPostMapper;
import edu.fzu.zhishe.cms.mapper.FmsPostRemarkMapper;
import edu.fzu.zhishe.cms.model.FmsPost;
import edu.fzu.zhishe.cms.model.FmsPostRemark;
import edu.fzu.zhishe.cms.model.SysUser;
import edu.fzu.zhishe.common.exception.Asserts;
import edu.fzu.zhishe.core.constant.DeleteStateEnum;
import edu.fzu.zhishe.core.constant.PostTypeEnum;
import edu.fzu.zhishe.core.dao.FmsPostDAO;
import edu.fzu.zhishe.core.dao.FmsRemarkDAO;
import edu.fzu.zhishe.core.dto.FmsPostDTO;
import edu.fzu.zhishe.core.param.FmsPostParam;
import edu.fzu.zhishe.core.dto.FmsRemarkDTO;
import edu.fzu.zhishe.core.param.FmsPostQuery;
import edu.fzu.zhishe.core.param.FmsRemarkParam;
import edu.fzu.zhishe.core.param.PaginationParam;
import edu.fzu.zhishe.core.service.FmsForumService;
import edu.fzu.zhishe.core.service.FmsUserLikeService;
import edu.fzu.zhishe.core.service.SysUserService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liang on 4/25/2020.
 * @version 1.0
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class FmsForumServiceImpl implements FmsForumService {

    @Autowired
    private FmsPostDAO postDAO;

    @Autowired
    private FmsPostMapper postMapper;

    @Autowired
    private FmsRemarkDAO remarkDAO;

    @Autowired
    private FmsPostRemarkMapper remarkMapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    FmsUserLikeService userLikeService;

    private FmsPostDTO queryLikeCount(FmsPostDTO post) {
        if (post == null) {
            return null;
        }
        post.setLikeCount(userLikeService.getPostLikeCount(post.getId()));
        return post;
    }

    private List<FmsPostDTO> queryLikeCount(List<FmsPostDTO> postList) {
        postList.forEach(p -> {
            p.setLikeCount(userLikeService.getPostLikeCount(p.getId()));
        });
        return postList;
    }

    @Override
    public List<FmsPostDTO> listPersonalPost(Integer clubId, PaginationParam paginationParam, FmsPostQuery postQuery) {
        PageHelper.startPage(paginationParam.getPage(), paginationParam.getLimit());
        return queryLikeCount(postDAO.listPersonalPost(clubId, postQuery));
    }

    @Override
    public FmsPostDTO getPersonalPostById(Integer id) {
        return queryLikeCount(postDAO.getPersonalPostById(id));
    }

    @Override
    public List<FmsPostDTO> listActivityPost(Integer clubId, PaginationParam paginationParam, FmsPostQuery postQuery) {
        PageHelper.startPage(paginationParam.getPage(), paginationParam.getLimit());
        return queryLikeCount(postDAO.listActivityPost(clubId, postQuery));
    }

    @Override
    public FmsPostDTO getActivityPostById(Integer id) {
        return queryLikeCount(postDAO.getActivityPostById(id));
    }

    @Override
    public int savePost(FmsPostParam postParam) {
        SysUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            Asserts.unAuthorized();
        }
        FmsPost post = new FmsPost();
        post.setPosterId(currentUser.getId());
        post.setType(PostTypeEnum.PERSONAL.getValue());
        post.setTitle(postParam.getTitle());
        post.setContent(postParam.getContent());
        post.setImgUrl(postParam.getImgUrl());
        post.setCreateAt(new Date());
        post.setDeleteState(DeleteStateEnum.Existence.getValue());
        return postMapper.insertSelective(post);
    }

    @Override
    public int updatePost(Long id, FmsPostParam postParam) {
        FmsPost oldPost = postMapper.selectByPrimaryKey(id);
        Asserts.notFound(oldPost == null || oldPost.getDeleteState() == 1);

        if (oldPost.getType().equals(PostTypeEnum.ACTIVITY.getValue())) {
            Asserts.fail("can't update activity post");
        }
        SysUser currentUser = userService.getCurrentUser();
        if (!oldPost.getPosterId().equals(currentUser.getId())) {
            Asserts.forbidden();
        }

        FmsPost post = new FmsPost();
        post.setId(id);
        post.setTitle(postParam.getTitle());
        post.setContent(postParam.getContent());
        post.setImgUrl(postParam.getImgUrl());
        return postMapper.updateByPrimaryKeySelective(post);
    }

    @Override
    public int deletePost(Long id) {
        SysUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            Asserts.unAuthorized();
        }

        FmsPost post = postMapper.selectByPrimaryKey(id);
        Asserts.notFound(post == null || post.getDeleteState() == 1);

        if (post.getType().equals(PostTypeEnum.ACTIVITY.getValue())) {
            Asserts.fail("can't delete activity post");
        }
        if (!currentUser.getId().equals(post.getPosterId())) {
            Asserts.forbidden("你不是该贴的发帖人");
        }

        FmsPost newPost = new FmsPost();
        newPost.setId(id);
        newPost.setDeleteState(1);
        return postMapper.updateByPrimaryKeySelective(newPost);
    }

    @Override
    public int saveRemark(FmsRemarkParam remarkParam) {
        SysUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            Asserts.unAuthorized();
        }

        Long postId = remarkParam.getPostId();
        FmsPost post = postMapper.selectByPrimaryKey(postId);
        if (post == null || post.getDeleteState() == DeleteStateEnum.Deleted.getValue()) {
            Asserts.notFound();
        }

        SysUser user = userService.getCurrentUser();
        FmsPostRemark remark = new FmsPostRemark();
        remark.setUserId(user.getId());
        remark.setPostId(remarkParam.getPostId().intValue());
        remark.setContent(remarkParam.getContent());
        remark.setCreateAt(new Date());
        remark.setUpdateAt(null);
        return remarkMapper.insertSelective(remark);
    }

    @Override
    public int deleteRemark(Long id) {
        FmsPostRemark remark = remarkMapper.selectByPrimaryKey(id);
        Asserts.notNull(remark);

        SysUser currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            Asserts.unAuthorized();
        }

        Integer userId = currentUser.getId();
        if (!remark.getUserId().equals(userId)) {
            Asserts.unAuthorized();
        }
        return remarkMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<FmsRemarkDTO> listRemarkByPostId(Long postId, PaginationParam paginationParam) {
        PageHelper.startPage(paginationParam.getPage(), paginationParam.getLimit());
        return remarkDAO.listRemarkByPostId(postId);
    }
}
