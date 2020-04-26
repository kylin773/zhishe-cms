package edu.fzu.zhishe.core.dto;

/**
 * @author liang on 4/26/2020.
 */
public class CmsActivityDTO {

    /**
     * id : 1
     * title : 活动1
     * content : 这是内容
     * clubName : 文学社
     * createAt : 2018-04-19 18:14:12
     * imgUrl : 131231241241.jpg
     * avatarUrl : e312312312312.jpg
     */

    private Integer id;
    private String title;
    private String content;
    private String clubName;
    private String createAt;
    private String imgUrl;
    private String avatarUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
