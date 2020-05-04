package edu.fzu.zhishe.core.web;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import edu.fzu.zhishe.common.api.AjaxResponse;
import edu.fzu.zhishe.core.constant.UpdatePasswordResultEnum;
import edu.fzu.zhishe.core.dto.*;
import edu.fzu.zhishe.core.param.SysUserQuestionParam;
import edu.fzu.zhishe.core.param.SysUserUpdateParam;
import edu.fzu.zhishe.core.service.SysUserService;
import edu.fzu.zhishe.cms.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 *
 * @author peng
 */

@RestController
@Api(tags = "UserController")
@RequestMapping("/users")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @ApiOperation(value = " 根据用户名获取密保问题 ")
    @GetMapping(value = "/question")
    public ResponseEntity<Object> question(@RequestBody SysUserQuestionParam info) {
        if (info.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        SysUser user = userService.getByUsername(info.getUsername());
        if (user == null || user.getLoginQuestion() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Map<String, String> myMap = new HashMap<>();
        myMap.put("loginProblem", user.getLoginQuestion());
        return ok(myMap);
    }

    @ApiOperation(value = " 校验密保问题回答是否正确 ")
    @PostMapping(value = "/answer")
    public ResponseEntity<Object> answer(@RequestBody SysUserUpdatePwdByAnswer param) {
        if (param.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        SysUser user = userService.getByUsername(param.getUsername());
        if (user != null && user.getLoginAnswer() != null && user.getLoginAnswer().equals(param.getAnswer())) {
            return noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @ApiOperation(value = " 用户修改个人信息 ")
    @PutMapping(value = "/info")
    public ResponseEntity<Object> info(@RequestBody SysUserUpdateParam updateParam) {
        userService.updateUserByParam(updateParam);
        return noContent().build();
    }

    @ApiOperation(value = " 忘记密码时通过回答保密问题修改密码 ")
    @PutMapping(value = "/password")
    public ResponseEntity<Object> password(@Validated @RequestBody SysUserUpdatePwdByAnswer param) {
        UpdatePasswordResultEnum result = userService.updateUserPasswordAfterAnswer(param);

        if (result != UpdatePasswordResultEnum.SUCCESS) {
            AjaxResponse response = new AjaxResponse();
            response.setMessage(result.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        return noContent().build();
    }
}
