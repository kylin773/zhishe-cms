package edu.fzu.zhishe.core.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

public class CmsClubsQuitParam {
    /**

     * clubId : 5000
     * reason : 没为什么
     */

    private int clubId;
    @ApiModelProperty(value = " 理由 ", required = true)
    @NotEmpty(message = " 理由不能为空 ")
    private String reason;



    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
