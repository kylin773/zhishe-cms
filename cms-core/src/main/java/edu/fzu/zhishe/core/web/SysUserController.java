package edu.fzu.zhishe.core.web;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import cn.hutool.json.JSONObject;
import edu.fzu.zhishe.common.api.AjaxResponse;
import edu.fzu.zhishe.common.exception.Asserts;
import edu.fzu.zhishe.core.config.StorageProperties;
import edu.fzu.zhishe.core.constant.UpdatePasswordResultEnum;
import edu.fzu.zhishe.core.dto.*;
import edu.fzu.zhishe.core.param.SysUserUpdateParam;
import edu.fzu.zhishe.core.service.FmsLikeCacheService;
import edu.fzu.zhishe.core.service.FmsUserLikeService;
import edu.fzu.zhishe.core.service.StorageService;
import edu.fzu.zhishe.core.service.SysUserService;
import edu.fzu.zhishe.cms.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户管理
 *
 * @author peng
 */
@Slf4j
@RestController
@Api(tags = "UserController")
@RequestMapping("/users")
public class SysUserController {

    @Autowired
    SysUserService userService;

    @Autowired
    FmsLikeCacheService likeCacheService;

    @Autowired
    FmsUserLikeService userLikeService;

    @Autowired
    StorageService storageService;

    private final Path imageRootLocation;

    @Autowired
    public SysUserController(StorageProperties storageProperties) {
        this.imageRootLocation = Paths.get(storageProperties.getImageLocation());
    }

    @ApiOperation(value = " 根据用户名获取密保问题 ")
    @GetMapping(value = "/question")
    public ResponseEntity<Object> question(String username) {
        if (username == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        SysUser user = userService.getByUsername(username);
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

    @ApiOperation(value = " 用户修改头像 ")
    @PostMapping(value = "/avatar")
    public ResponseEntity<Object> avatar(@RequestParam("image") MultipartFile image) {

        SysUser currentUser = userService.getCurrentUser();
        String avatarUrl = currentUser.getAvatarUrl();
        // FIXME: hard code here
        String rootLocation = "http://101.200.193.180:9520/files/images";
        // delete if avatar is uploaded to server before
        int index = avatarUrl.lastIndexOf('/');
        if (rootLocation.equals(avatarUrl.substring(0, index))) {
            String filename = avatarUrl.substring(index);
            Path oldAvatarPath = Paths.get(imageRootLocation.toAbsolutePath() + filename);
            storageService.deleteFile(oldAvatarPath);
        }

        // 1. upload avatar
        String url = storageService.store(image, imageRootLocation);
        log.info("You successfully uploaded " + image.getOriginalFilename() + "!");

        // 2. update user info
        SysUser user = new SysUser() {{
            setId(currentUser.getId());
            setAvatarUrl(url);
        }};
        if (userService.updateUserSelective(user) == 0) {
            Asserts.fail("update avatar failed");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("avatarUrl", url);
        return ok().body(jsonObject);
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

    @ApiOperation(value = " 点赞 ")
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "likedPostId", value = " 被点赞的帖子 id"),
    })
    public ResponseEntity<Object> like(@RequestParam("likedPostId") Long likedPostId) {

        SysUser currentUser = userService.getCurrentUser();
        if (likeCacheService.hasLiked(currentUser.getId(), likedPostId)) {
            Asserts.fail("你已经点过赞了");
        }
        // 先存到 Redis 里面，再定时写到数据库里
        likeCacheService.saveLiked2Redis(currentUser.getId(), likedPostId);
        likeCacheService.incrLikedCount(likedPostId);
        return noContent().build();
    }

    @ApiOperation(value = " 取消点赞 ")
    @RequestMapping(value = "/unlike", method = RequestMethod.POST)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "likedPostId", value = " 被取消点赞的帖子 id"),
    })
    public ResponseEntity<Object> unlike(@RequestParam("likedPostId") Long likedPostId) {

        SysUser currentUser = userService.getCurrentUser();
        if (likeCacheService.hasUnLiked(currentUser.getId(), likedPostId)) {
            Asserts.fail("你还没有赞过呢");
        }
        // 取消点赞，先存到 Redis 里面，再定时写到数据库里
        likeCacheService.unlikeFromRedis(currentUser.getId(), likedPostId);
        likeCacheService.decrLikedCount(likedPostId);
        return noContent().build();
    }
}
