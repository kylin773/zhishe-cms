package edu.fzu.zhishe.core.web;

import edu.fzu.zhishe.core.param.SysAdminNewUsersQuery;
import edu.fzu.zhishe.core.service.SysAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author PSF(52260506 @ qq.com)
 * @
 */

@RestController
@Api(tags = "AdminHomeController")
@RequestMapping("/admin")
public class AdminHomeController {

    @Autowired
    private SysAdminService adminService;

    @ApiOperation(" 待审核事项 ")
    @GetMapping("/unaudited")
    public ResponseEntity<Object> unaudited() {
        return ResponseEntity.ok(adminService.unaudited());
    }

    @ApiOperation(" 注册人数 ")
    @GetMapping("/newusers")
    public ResponseEntity<Object> newUsers(SysAdminNewUsersQuery query) {
        return ResponseEntity.ok(adminService.newUsers(query));
    }

    @ApiOperation(" 社团类别数 ")
    @GetMapping("/clubspecies")
    public ResponseEntity<Object> clubSpecies() {
        return ResponseEntity.ok(adminService.clubSpecies());
    }

}
