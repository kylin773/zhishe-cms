package edu.fzu.zhishe.core.service;

import edu.fzu.zhishe.core.constant.UpdatePasswordResultEnum;
import edu.fzu.zhishe.core.dto.*;
import edu.fzu.zhishe.cms.model.SysUser;
//import org.springframework.security.core.userdetails.UserDetails;
import edu.fzu.zhishe.core.param.SysRegisterParam;
import edu.fzu.zhishe.core.param.SysUserRegisterParam;
import edu.fzu.zhishe.core.param.SysUserUpdateParam;
import edu.fzu.zhishe.core.param.UpdateUserPasswordParam;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户管理
 * @author liang
 */
public interface SysUserService {

    /**
     * 根据用户名获取用户
     */
    SysUser getByUsername(String username);

    /**
     * 根据用户编号获取用户
     */
    SysUser getById(Integer id);

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Throwable.class)
    int register(SysRegisterParam registerParam);

    /**
     * 生成验证码
     */
    void generateAuthCode(String email);

    /**
     * 修改密码
     */
    @Transactional
    UpdatePasswordResultEnum updatePassword(UpdateUserPasswordParam updateUserPasswordParam);

    /**
     * 获取当前登录
     */
    SysUser getCurrentUser();

    /**
     * 根据用户 id 修改用户积分
     */
    //void updateIntegration(Integer id, Integer integration);


    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 登录后获取 token
     */
    String login(String username, String password);

    /**
     * 获取所有用户
     */
    List<SysUser> users();

    /**
     * 修改用户信息
     */
    @Transactional
    void updateUserByParam(SysUserUpdateParam updateParam);

    String updateAvatar(MultipartFile avatarImg);

    /**
     * 选择性更新非空字段
     */
    int updateUserSelective(SysUser user);

    /**
     * 验证密保通过后修改密码
     */
    @Transactional
    UpdatePasswordResultEnum updateUserPasswordAfterAnswer(SysUserUpdatePwdByAnswer param);
}
