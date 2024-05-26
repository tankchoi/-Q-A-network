/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ant.view.Dao;

/**
 *
 * @author Admin
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ant.view.Model.Post;


public class PostDaoImpl implements PostDao {

    private static final String URL = "jdbc:mysql://localhost:3306/java";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final int PAGE_SIZE = 10;
    private static final String SELECT_PAGED_QUERY = "SELECT * FROM posts ORDER BY PostID DESC LIMIT ?, ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM posts";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM posts WHERE PostID = ?";
    private static final String INSERT_QUERY = "INSERT INTO posts (UserID, Title, Content, CreatedAt, UpdatedAt) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE posts SET UserID = ?, Title = ?, Content = ?, UpdatedAt = ? WHERE PostID = ?";

    private static final String GET_USERNAME_QUERRY = "SELECT Username FROM users WHERE UserID = ?";

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SELECT_ALL_QUERY);
            while (resultSet.next()) {
                Post post = extractPostFromResultSet(resultSet);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    @Override
    public Post getPostById(int postId) {
        Post post = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(SELECT_BY_ID_QUERY);
            preparedStatement.setInt(1, postId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                post = extractPostFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return post;
    }

    @Override
    public int createPost(Post post) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        int postId = -1; // Khởi tạo postId với giá trị mặc định -1
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, post.getUserId());
            preparedStatement.setString(2, post.getTitle());
            preparedStatement.setString(3, post.getContent());
            preparedStatement.setTimestamp(4, new Timestamp(post.getCreatedAt().getTime()));
            preparedStatement.setTimestamp(5, new Timestamp(post.getUpdatedAt().getTime()));
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                postId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return postId;
    }

    @Override
    public void updatePost(Post post) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setInt(1, post.getUserId());
            preparedStatement.setString(2, post.getTitle());
            preparedStatement.setString(3, post.getContent());
            preparedStatement.setTimestamp(4, new Timestamp(post.getUpdatedAt().getTime()));
            preparedStatement.setInt(5, post.getPostId());
            preparedStatement.executeUpdate();
           
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deletePost(int postId) {

        Connection Con = null;

        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");

            String deleteCommentLikesQuery = "DELETE FROM commentlikes WHERE CommentID IN (SELECT CommentID FROM comments WHERE PostID = ?)";
            try (PreparedStatement deleteCommentLikesStmt = Con.prepareStatement(deleteCommentLikesQuery)) {
                deleteCommentLikesStmt.setInt(1, postId);
                deleteCommentLikesStmt.executeUpdate();
            }

            String deleteCommentsQuery = "DELETE FROM comments WHERE PostID = ?";
            try (PreparedStatement deleteCommentsStmt = Con.prepareStatement(deleteCommentsQuery)) {
                deleteCommentsStmt.setInt(1, postId);
                deleteCommentsStmt.executeUpdate();
            }

            String deletePostLikesQuery = "DELETE FROM postlikes WHERE PostID = ?";
            try (PreparedStatement deletePostLikesStmt = Con.prepareStatement(deletePostLikesQuery)) {
                deletePostLikesStmt.setInt(1, postId);
                deletePostLikesStmt.executeUpdate();
            }

            String deletePostCategoryQuery = "DELETE FROM post_cate WHERE PostID = ?";
            try (PreparedStatement deletePostCategoryStmt = Con.prepareStatement(deletePostCategoryQuery)) {
                deletePostCategoryStmt.setInt(1, postId);
                deletePostCategoryStmt.executeUpdate();
            }

            String deletePostQuery = "DELETE FROM posts WHERE PostID = ?";
            try (PreparedStatement deletePostStmt = Con.prepareStatement(deletePostQuery)) {
                deletePostStmt.setInt(1, postId);
                deletePostStmt.executeUpdate();
            }

            Con.close();
        } catch (Exception e) {

        }

    }

    private Post extractPostFromResultSet(ResultSet resultSet) throws SQLException {
        int postId = resultSet.getInt("PostID");
        int userId = resultSet.getInt("UserID");
        String title = resultSet.getString("Title");
        String content = resultSet.getString("Content");
        java.util.Date createdAt = resultSet.getTimestamp("CreatedAt");
        java.util.Date updatedAt = resultSet.getTimestamp("UpdatedAt");
        return new Post(postId, userId, title, content, createdAt, updatedAt);
    }

    public void addCategoryToPost(int postId, String categoryName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement("INSERT INTO post_cate(postID, cateID) VALUES(?, (SELECT cateID FROM categories WHERE CateName = ?))");
            preparedStatement.setInt(1, postId);
            preparedStatement.setString(2, categoryName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTotalPages() {
        int totalPosts = getTotalPostsCount();
        return (int) Math.ceil((double) totalPosts / PAGE_SIZE);
    }

    // Phương thức để lấy tổng số bài viết
    private int getTotalPostsCount() {
        int count = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) AS total FROM posts");
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    @Override
    public List<Post> getPostsByPage(int pageNumber) {
        List<Post> posts = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(SELECT_PAGED_QUERY);
            int offset = (pageNumber - 1) * PAGE_SIZE;
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, PAGE_SIZE);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Post post = extractPostFromResultSet(resultSet);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    @Override
    public String getUsername(int id) {
        String username = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement(GET_USERNAME_QUERRY);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                username = resultSet.getString("Username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return username;
    }

    @Override
    public void likePost(int postId, int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            boolean alreadyLiked = checkIfUserLikedPost(postId, userId);

            if (!alreadyLiked) {

                preparedStatement = connection.prepareStatement("INSERT INTO postlikes (PostID, UserID) VALUES (?, ?)");
                preparedStatement.setInt(1, postId);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();

            } else {
                preparedStatement = connection.prepareStatement("DELETE FROM postlikes WHERE postId = ? AND userId = ?");
                preparedStatement.setInt(1, postId);
                preparedStatement.setInt(2, userId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean checkIfUserLikedPost(int postId, int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM postlikes WHERE PostID = ? AND UserID = ?");
            preparedStatement.setInt(1, postId);
            preparedStatement.setInt(2, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int countLikePost(int postId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM postlikes WHERE PostID = ?");
            preparedStatement.setInt(1, postId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public List<Post> getPostsByPageByUserId(int pageNumber, int userId) {
        List<Post> posts = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT * FROM posts where UserId = ? ORDER BY PostID DESC LIMIT ?, ?");
            int offset = (pageNumber - 1) * PAGE_SIZE;
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, PAGE_SIZE);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Post post = extractPostFromResultSet(resultSet);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng các tài nguyên
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }

    @Override
    public void removeAllCategoriesFromPost(int postId) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String deleteQuery = "DELETE FROM post_cate WHERE PostID = ?";
            try (PreparedStatement deleteStmt = con.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, postId);
                deleteStmt.executeUpdate();
            }

            System.out.println("All categories removed from post with ID: " + postId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error occurred while removing categories from post: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
 
}
