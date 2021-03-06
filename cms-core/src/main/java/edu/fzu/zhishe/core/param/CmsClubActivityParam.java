package edu.fzu.zhishe.core.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author PSF(52260506 @ qq.com)
 * @
 */
@Data
public class CmsClubActivityParam {

    @ApiModelProperty(value = " 社团编号 ", required = true)
    @NotNull(message = " 社团不能为空 ")
    private Integer clubId;

    @ApiModelProperty(value = " 活动名称 ", required = true)
    @NotEmpty(message = " 活动名不能为空 ")
    private String name;

    @ApiModelProperty(value = " 活动人数 ", required = true)
    @Min(1)
    private Integer memberCount = 1;

    @ApiModelProperty(value = " 活动标题 ")
    private String title;

    @ApiModelProperty(value = " 活动内容 ")
    private String content;

    @ApiModelProperty(value = " 活动开始时间 ", required = true)
    @Future
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @ApiModelProperty(value = " 活动结束时间 ", required = true)
    @Future
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @ApiModelProperty(value = " 活动地点 ")
    private String location;
}
