package edu.fzu.zhishe.core.annotation;

import edu.fzu.zhishe.core.constant.UserRoleEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被该注解标记的方法会检查
 *      1. 社团是否存在
 *      2. 当前用户是不是社长
 *
 * @author xjliang
 * @date 2020-05-03
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckClubAuth {

    /**
     * 允许访问的角色 id
     */
    UserRoleEnum value() default UserRoleEnum.NORMAL;

    /**
     * 多个角色
     */
    UserRoleEnum[] values() default {UserRoleEnum.NORMAL};
}
