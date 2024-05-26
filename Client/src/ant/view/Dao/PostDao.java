/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ant.view.Dao;

/**
 *
 * @author Admin
 */
import java.util.List;
import ant.view.Model.Post;


public interface PostDao {
    List<Post> getAllPosts();
    Post getPostById(int postId);
    int createPost(Post post);
    void updatePost(Post post);
    void deletePost(int postId);
    void addCategoryToPost(int postId, String categoryName);
    List<Post> getPostsByPage(int pageNumber);
    String getUsername(int postID);
    void likePost(int postID, int userID);
    boolean checkIfUserLikedPost(int postId, int userId);
    int countLikePost(int postId);
    List<Post> getPostsByPageByUserId(int pageNumber, int userId);
    void removeAllCategoriesFromPost(int postId);
    
}
