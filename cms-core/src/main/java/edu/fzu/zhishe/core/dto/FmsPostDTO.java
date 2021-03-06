package edu.fzu.zhishe.core.dto;

/**
 * @author liang on 4/26/2020.
 */
public class FmsPostDTO {

    /**
     * id : 1
     * title : 活动1
     * content : 这是内容
     * posterName : 文学社
     * createAt : 2018-04-19 18:14:12
     * imgUrl : 131231241241.jpg
     * avatarUrl : e312312312312.jpg
     * likeCount: 4
     * commentCount: 5
     */

    private Long id;
    private String title;
    private String content;
    private String posterName;
    private String createAt;
    private String imgUrl;
    private String avatarUrl;
    private Integer likeCount;
    private Integer commentCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String clubName) {
        this.posterName = clubName;
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

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "FmsPostDTO{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", posterName='" + posterName + '\'' +
            ", createAt='" + createAt + '\'' +
            ", imgUrl='" + imgUrl + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            ", likeCount=" + likeCount +
            ", commentCount=" + commentCount +
            '}';
    }
}
