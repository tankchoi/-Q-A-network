/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ant.view.Model;

/**
 *
 * @author Admin
 */
import java.util.Date;
import java.text.SimpleDateFormat;
public class Post {
    private int postId;
    private int userId;
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    //Đức code
    private int displayedComments;
     private int demPost;

   public Post() {
        this.postId = 0;
        this.userId = 0;
        this.title = "";
        this.content = "";
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.displayedComments = 1;
        this.demPost = 0;
    }
    public Post(int postId, int userId, String title, String content, Date createdAt, Date updatedAt) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.displayedComments = 1;
        this.demPost = 0;
    }
    public Post(int userId, String title, String content, Date createdAt, Date updatedAt) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.displayedComments = 1;
        this.demPost = 0;
    }
    //ĐỨc code
    public int getDisplayedComments() {
        return displayedComments;
    }

    public void addDisplayedComment() {
        displayedComments += 3;
    }
    public int getDemPost() {
        return demPost;
    }
    public void addDemPost() {
         demPost++;
    }

    
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(updatedAt);
    }
    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(updatedAt);
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
