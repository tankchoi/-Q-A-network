/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ant.view;

import javax.swing.*;
import java.awt.*;
import ant.view.Dao.PostDaoImpl;
import ant.view.Dao.PostDao;
import ant.view.Model.Post;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;
import java.awt.event.*;

/**
 *
 * @author DELL
 */
public class HomePage extends javax.swing.JFrame {

    private int Role;
    private int UserID;
    String content1;
    int pComment1 = -1;
    List<Post> posts = new PostDaoImpl().getPostsByPage(1);
    Connection conn = null;
    String divide = "all";

    /**
     * Creates new form HomePage
     */
    public HomePage(int Role, int UserID) {
        this.Role = Role;
        this.UserID = UserID;
        initComponents();
        connectDB();
        getCate();
        displayPosts(this.posts);
        update();
        ItemListener checkBoxListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {

                    List<String> selectedCategories = new ArrayList<>();
                    for (Component component : CateP.getComponents()) {
                        if (component instanceof JCheckBox) {
                            JCheckBox checkBox = (JCheckBox) component;
                            if (checkBox.isSelected()) {
                                selectedCategories.add(checkBox.getText());
                            }
                        }
                    }

                    displayPostsByCategories(selectedCategories);
                    update();

                }
            }
        };

        for (Component component : CateP.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.addItemListener(checkBoxListener);
            }
        }

        //đức
        displayUserName();

    }

    private void getCate() {
        Connection Con = null;
        PreparedStatement pst = null;
        ResultSet Rs = null;
        Statement St = null;
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery("select * from categories where UserID is not NULL");

            CateP.removeAll();

            while (Rs.next()) {
                String categoryName = Rs.getString("CateName");
                JCheckBox checkBox = new JCheckBox(categoryName);
                CateP.add(checkBox);
            }

            CateP.revalidate();
            CateP.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (Rs != null) {
                    Rs.close();
                }
                if (St != null) {
                    St.close();
                }
                if (Con != null) {
                    Con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectDB() {
        try {
            String url = "jdbc:mysql://localhost:3306/java";
            String username = "root";
            String password = "";
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    //Đức code
    private void displayUserName() {
        try {
            String sql = "SELECT * FROM users WHERE UserID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, UserID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                jLabel3.setText(resultSet.getString("Username"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //hàm xóa like để xóa được comment
    private void deleteListLike(int commentID) {
        try {
            String sql = "DELETE FROM commentlikes WHERE CommentID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, commentID);
            statement.executeUpdate(); // Sử dụng executeUpdate() thay vì executeQuery()
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Hàm đệ quy để xóa tất cả các comment con của một comment
    private void deleteChildrenComments(int parentCommentID) {
        try {
            String sql = "SELECT CommentID FROM comments WHERE PComment = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, parentCommentID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int commentID = resultSet.getInt("CommentID");
                // Đệ quy xóa tất cả các comment con của comment này
                deleteChildrenComments(commentID);
                // Sau đó xóa comment này
                deleteComment(commentID);
            }

            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Hàm xóa một comment dựa trên CommentID
    private void deleteComment(int commentID) {
        try {
            // Trước tiên, xóa tất cả các like tương ứng với comment này
            deleteListLike(commentID);

            // Tiếp theo, xóa comment
            String sql = "DELETE FROM comments WHERE CommentID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, commentID);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private int demComment(int postID) {
        int dem = 0;
        try {
            String query = "SELECT * FROM comments WHERE PostID = " + postID;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                dem++;
            }
        } catch (SQLException e2) {
            System.out.println("Error displaying comments: " + e2.getMessage());
        }
        return dem;
    }

    private int demCommentLike(int commentID) {
        int dem = 0;
        try {
            String query = "SELECT * FROM commentlikes WHERE CommentID = " + commentID;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                dem++;
            }
        } catch (SQLException e2) {
            System.out.println("Error count like comments: " + e2.getMessage());
        }
        return dem;
    }

    //like comment
    public void likeComment(int userId, int commentId) {
        try {
            if (!hasLiked(userId, commentId)) {
                String sql = "INSERT INTO commentlikes (UserID, CommentID) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setInt(2, commentId);
                stmt.executeUpdate();
                System.out.println("Liked comment with ID " + commentId);
            } else {
                unlikeComment(userId, commentId);
            }
        } catch (SQLException e) {
            System.out.println("Error liking comment: " + e.getMessage());
        }
    }

    //xóa like comment
    public void unlikeComment(int userId, int commentId) {
        try {
            String sql = "DELETE FROM commentlikes WHERE UserID = ? AND CommentID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, commentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Unliked comment with ID " + commentId);
            } else {
                System.out.println("User did not like comment with ID " + commentId);
            }
        } catch (SQLException e) {
            System.out.println("Error unliking comment: " + e.getMessage());
        }
    }

    //kiểm tra đã like chưa
    private boolean hasLiked(int userId, int commentId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM commentlikes WHERE UserID = ? AND CommentID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, userId);
        stmt.setInt(2, commentId);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        return count > 0;
    }

    private void displayComments(int postID, JTextArea newTxtParentContent1, JLabel newLbParentName1, JTextArea newTxtContent1,
            int displayedDataCount, int demPost, boolean[] isFirstClick) {
        try {
            String query = "SELECT * FROM comments WHERE PostID = " + postID + " ORDER BY CommentID DESC LIMIT " + displayedDataCount;
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String content = resultSet.getString("Content");
                // Tạo một panel mới
                JPanel newPnComment = new JPanel();

                JPanel newPnleft = new JPanel();
                int commentID = resultSet.getInt("CommentID");
                int userID = resultSet.getInt("UserID");
                String query3 = "SELECT * FROM users Where UserID = " + userID;
                Statement statement3 = conn.createStatement();
                ResultSet resultSet3 = statement3.executeQuery(query3);
                String userName = "";
                while (resultSet3.next()) {
                    userName = resultSet3.getString("Username");
                }
                JLabel newLbName = new JLabel(userName);

                newLbName.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                newLbName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-30.png")));

                newPnleft.setBackground(new java.awt.Color(182, 221, 218));
                javax.swing.GroupLayout PnLeftLayout = new javax.swing.GroupLayout(newPnleft);
                newPnleft.setLayout(PnLeftLayout);
                PnLeftLayout.setHorizontalGroup(
                        PnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(PnLeftLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(newLbName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addContainerGap())
                );
                PnLeftLayout.setVerticalGroup(
                        PnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(PnLeftLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(newLbName)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                JPanel newPnTop = new JPanel();
                JScrollPane scrTop = new JScrollPane();
                int pComment = resultSet.getInt("PComment");
                String query2 = "SELECT * FROM comments WHERE CommentID = " + pComment;
                Statement statement2 = conn.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(query2);
                String conTentTextTop = "";
                int userIDParent = 0;
                while (resultSet2.next()) {
                    conTentTextTop = resultSet2.getString("Content");
                    userIDParent = resultSet2.getInt("UserID");
                }
                String query4 = "SELECT * FROM users WHERE UserID = " + userIDParent;
                Statement statement4 = conn.createStatement();
                ResultSet resultSet4 = statement4.executeQuery(query4);
                String userNameParent = "";
                while (resultSet4.next()) {
                    userNameParent = resultSet4.getString("Username") + " said:";
                }
                JLabel newLbParentName = new JLabel(userNameParent);
                JTextArea newTxtParentContent = new JTextArea(conTentTextTop);

                newLbParentName.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                newLbParentName.setForeground(new java.awt.Color(255, 176, 169));

                newTxtParentContent.setEditable(false);
                newTxtParentContent.setBackground(new java.awt.Color(1, 135, 126));
                newTxtParentContent.setColumns(20);
                newTxtParentContent.setLineWrap(true);
                newTxtParentContent.setRows(1);
                newTxtParentContent.setWrapStyleWord(true);
                newTxtParentContent.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
                scrTop.setViewportView(newTxtParentContent);

                //
                newPnTop.setBackground(new java.awt.Color(1, 135, 126));

                javax.swing.GroupLayout PnTopLayout = new javax.swing.GroupLayout(newPnTop);
                newPnTop.setLayout(PnTopLayout);
                PnTopLayout.setHorizontalGroup(
                        PnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnTopLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(PnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(newLbParentName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(PnTopLayout.createSequentialGroup()
                                                        .addGap(0, 79, Short.MAX_VALUE)
                                                        .addComponent(scrTop, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(42, 42, 42))
                );
                PnTopLayout.setVerticalGroup(
                        PnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(PnTopLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(newLbParentName, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrTop, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                        .addContainerGap())
                );

                String conTentTextBottom = resultSet.getString("Content");
                JScrollPane scrBot = new JScrollPane();
                JTextArea newTxtContent = new JTextArea(conTentTextBottom);

                newTxtContent.setEditable(false);
                newTxtContent.setColumns(20);
                newTxtContent.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
                newTxtContent.setLineWrap(true);
                newTxtContent.setRows(1);
                newTxtContent.setWrapStyleWord(true);
                scrBot.setViewportView(newTxtContent);

                newPnComment.setBackground(new java.awt.Color(182, 221, 218));
                newPnComment.setForeground(new java.awt.Color(207, 207, 207));

                if (UserID == userID) {
                    //like
                    JLabel newCountLike = new JLabel();
                    newCountLike.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png")));
                    int commentLikeCount = demCommentLike(commentID);
                    String commentLikeCountString = String.valueOf(commentLikeCount);
                    newCountLike.setText(commentLikeCountString);

                    JToggleButton newBtnLike = new JToggleButton();
                    //newBtnLike.setBackground(new java.awt.Color(255, 176, 169));
                    if (hasLiked(UserID, commentID)) {
                        newBtnLike.setBackground(new java.awt.Color(255, 176, 169));
                    } else {
                        newBtnLike.setBackground(Color.GRAY);
                    }
                    newBtnLike.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                    newBtnLike.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png"))); // NOI18N
                    newBtnLike.setText("Like");
                    newBtnLike.setBorder(null);
                    newBtnLike.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            likeComment(UserID, commentID);
                            displayPosts(posts);
                        }
                    });

                    //rep
                    JButton newBtnRep = new JButton();
                    newBtnRep.setBackground(new java.awt.Color(1, 135, 126));
                    newBtnRep.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                    newBtnRep.setForeground(new java.awt.Color(255, 255, 255));
                    newBtnRep.setText("Rep");
                    newBtnRep.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            newTxtParentContent1.setText(content);
                            newLbParentName1.setText(newLbName.getText() + " said:");
                            pComment1 = commentID;
                        }
                    });
                    //xóa
                    JButton newBtnDel = new JButton();
                    newBtnDel.setBackground(new java.awt.Color(1, 135, 126));
                    newBtnDel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                    newBtnDel.setForeground(new java.awt.Color(255, 255, 255));
                    newBtnDel.setText("Delete");
                    newBtnDel.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Hiển thị hộp thoại xác nhận
                            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Confirmation", JOptionPane.YES_NO_OPTION);

                            // Nếu người dùng chọn Yes
                            if (option == JOptionPane.YES_OPTION) {
                                listComments.removeAll();
                                //xóa listlike trước
//                                deleteListLike(commentID);
                                // Xóa tất cả các comment con của commentID đầu tiên
                                deleteChildrenComments(commentID);
                                // Sau đó xóa commentID
                                deleteComment(commentID);
                                JOptionPane.showMessageDialog(null, "Comment deleted successfully!");
                                newTxtContent1.setText("");
                                pComment1 = -1;

                                newTxtParentContent1.setText("");
                                newLbParentName1.setText("");

                                displayPosts(posts);
                            }
                        }
                    });

                    //sửa
                    JButton newBtnEdit = new JButton();
                    newBtnEdit.setBackground(new java.awt.Color(1, 135, 126));
                    newBtnEdit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                    newBtnEdit.setForeground(new java.awt.Color(255, 255, 255));
                    newBtnEdit.setText("Edit");
                    newBtnEdit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            isFirstClick[0] = false; 
                            newTxtParentContent1.setText(newTxtParentContent.getText());
                            newLbParentName1.setText(newLbParentName.getText());
                            newTxtContent1.setText(newTxtContent.getText());
                            pComment1 = commentID;
                        }
                    });

                    javax.swing.GroupLayout Comment_detailLayout = new javax.swing.GroupLayout(newPnComment);
                    newPnComment.setLayout(Comment_detailLayout);
                    Comment_detailLayout.setHorizontalGroup(
                            Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Comment_detailLayout.createSequentialGroup()
                                            .addComponent(newPnleft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(newPnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(Comment_detailLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(scrBot)
                                            .addContainerGap())
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Comment_detailLayout.createSequentialGroup()
                                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(newCountLike, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(newBtnLike, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE))
                                            .addGap(33, 33, 33)
                                            .addComponent(newBtnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(newBtnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(newBtnRep, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(39, 39, 39))
                    );
                    Comment_detailLayout.setVerticalGroup(
                            Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Comment_detailLayout.createSequentialGroup()
                                            .addGap(0, 0, 0)
                                            .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(newPnleft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(newPnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(scrBot, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(newCountLike, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(newBtnRep)
                                                    .addComponent(newBtnDel)
                                                    .addComponent(newBtnEdit)
                                                    .addComponent(newBtnLike, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addContainerGap())
                    );

                } else {
                    //like
                    JLabel newCountLike = new JLabel();
                    newCountLike.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png")));
                    int commentLikeCount = demCommentLike(commentID);
                    String commentLikeCountString = String.valueOf(commentLikeCount);
                    newCountLike.setText(commentLikeCountString);

                    JToggleButton newBtnLike = new JToggleButton();
                    //newBtnLike.setBackground(new java.awt.Color(255, 176, 169));
                    if (hasLiked(UserID, commentID)) {
                        newBtnLike.setBackground(new java.awt.Color(255, 176, 169));
                    } else {
                        newBtnLike.setBackground(Color.GRAY);
                    }
                    newBtnLike.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                    newBtnLike.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png"))); // NOI18N
                    newBtnLike.setText("Like");
                    newBtnLike.setBorder(null);
                    newBtnLike.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            likeComment(UserID, commentID);
                            displayPosts(posts);
                        }
                    });

                    //rep
                    JButton newBtnRep = new JButton();
                    newBtnRep.setBackground(new java.awt.Color(1, 135, 126));
                    newBtnRep.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                    newBtnRep.setForeground(new java.awt.Color(255, 255, 255));
                    newBtnRep.setText("Rep");

                    newBtnRep.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            newTxtParentContent1.setText(content);
                            newLbParentName1.setText(newLbName.getText() + " said:");
                            pComment1 = commentID;
                        }
                    });

                    javax.swing.GroupLayout Comment_detailLayout = new javax.swing.GroupLayout(newPnComment);
                    newPnComment.setLayout(Comment_detailLayout);
                    Comment_detailLayout.setHorizontalGroup(
                            Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Comment_detailLayout.createSequentialGroup()
                                            .addComponent(newPnleft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(newPnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(Comment_detailLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(scrBot)
                                            .addContainerGap())
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Comment_detailLayout.createSequentialGroup()
                                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(newCountLike, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(Comment_detailLayout.createSequentialGroup()
                                                            .addComponent(newBtnLike, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addGap(33, 33, 33)
                                                            .addComponent(newBtnRep, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGap(39, 39, 39))
                    );
                    Comment_detailLayout.setVerticalGroup(
                            Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(Comment_detailLayout.createSequentialGroup()
                                            .addGap(0, 0, 0)
                                            .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(newPnleft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(newPnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(scrBot, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(newCountLike, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(newBtnRep)
                                                    .addComponent(newBtnLike, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addContainerGap())
                    );
                }
                listComments.add(newPnComment);
                listComments.add(Box.createVerticalStrut(20));
            }

            // Đóng các tài nguyên ResultSet và Statement
            resultSet.close();
            statement.close();

            listComments.revalidate();
            listComments.repaint();
        } catch (SQLException e2) {
            System.out.println("Error displaying comments: " + e2.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ViewComment = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        Reply1 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jToggleButton3 = new javax.swing.JToggleButton();
        jButton4 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        Reply2 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jToggleButton4 = new javax.swing.JToggleButton();
        jButton5 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        DeleteWarning = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        Header = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CateP = new javax.swing.JPanel();
        SearchTb = new javax.swing.JTextField();
        SearchCb = new javax.swing.JComboBox<>();
        SearchBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        Menu = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        CreatePostBtn = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        PostScrPn = new javax.swing.JScrollPane();
        PostsPn = new javax.swing.JPanel();
        PostPn = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        CommentBtn = new javax.swing.JButton();
        UsernamePostLb = new javax.swing.JLabel();
        DateLb = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        ContentTa = new javax.swing.JTextArea();
        TimeLb = new javax.swing.JLabel();
        LikeCountLb = new javax.swing.JLabel();
        CommentCountLb = new javax.swing.JLabel();
        LikeBtn = new javax.swing.JToggleButton();
        TitleLb = new javax.swing.JLabel();
        EditPostBtn = new javax.swing.JButton();
        DeletePostBtn = new javax.swing.JButton();
        CatePostP = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        listComments = new javax.swing.JPanel();
        Comment_detail = new javax.swing.JPanel();
        PnLeft = new javax.swing.JPanel();
        lbNameLeft = new javax.swing.JLabel();
        PnTop = new javax.swing.JPanel();
        lbNameTop = new javax.swing.JLabel();
        scrTop = new javax.swing.JScrollPane();
        txtContentTop = new javax.swing.JTextArea();
        BtnDel = new javax.swing.JButton();
        BtnRep = new javax.swing.JButton();
        scrBot = new javax.swing.JScrollPane();
        txtContentBot = new javax.swing.JTextArea();
        BtnEdit = new javax.swing.JButton();
        PrePageBtn = new javax.swing.JButton();
        NextPageBtn = new javax.swing.JButton();
        CountPageTb = new javax.swing.JTextField();

        ViewComment.setTitle("View_Comment");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setMinimumSize(new java.awt.Dimension(800, 800));
        jPanel5.setPreferredSize(new java.awt.Dimension(800, 1200));

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jScrollPane6.setViewportView(jTextArea4);

        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png"))); // NOI18N
        jToggleButton3.setText("Like");

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-comment-30.png"))); // NOI18N
        jButton4.setText("Comment");

        jLabel19.setText("10");

        jLabel20.setText("1");

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-30.png"))); // NOI18N
        jLabel22.setText("Tuấn Anh");

        jLabel49.setText("Trả lời của ........");

        javax.swing.GroupLayout Reply1Layout = new javax.swing.GroupLayout(Reply1);
        Reply1.setLayout(Reply1Layout);
        Reply1Layout.setHorizontalGroup(
            Reply1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Reply1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel49)
                .addContainerGap(683, Short.MAX_VALUE))
            .addGroup(Reply1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Reply1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Reply1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Reply1Layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel22))
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(Reply1Layout.createSequentialGroup()
                            .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel19)
                            .addGap(43, 43, 43)
                            .addComponent(jButton4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel20)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        Reply1Layout.setVerticalGroup(
            Reply1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Reply1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel49)
                .addContainerGap(173, Short.MAX_VALUE))
            .addGroup(Reply1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Reply1Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addGroup(Reply1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jLabel22))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(Reply1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Reply1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Reply1Layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, Short.MAX_VALUE)
                                .addGap(6, 6, 6))
                            .addGroup(Reply1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19)
                                .addComponent(jLabel20)))
                        .addComponent(jToggleButton3))
                    .addGap(30, 30, 30)))
        );

        jTextArea5.setColumns(20);
        jTextArea5.setRows(5);
        jScrollPane7.setViewportView(jTextArea5);

        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png"))); // NOI18N
        jToggleButton4.setText("Like");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-comment-30.png"))); // NOI18N
        jButton5.setText("Comment");

        jLabel23.setText("10");

        jLabel24.setText("1");

        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-48.png"))); // NOI18N
        jLabel26.setText("Tuấn Anh");

        javax.swing.GroupLayout Reply2Layout = new javax.swing.GroupLayout(Reply2);
        Reply2.setLayout(Reply2Layout);
        Reply2Layout.setHorizontalGroup(
            Reply2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(Reply2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Reply2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(Reply2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Reply2Layout.createSequentialGroup()
                            .addComponent(jLabel25)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel26))
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(Reply2Layout.createSequentialGroup()
                            .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel23)
                            .addGap(43, 43, 43)
                            .addComponent(jButton5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel24)))
                    .addContainerGap(18, Short.MAX_VALUE)))
        );
        Reply2Layout.setVerticalGroup(
            Reply2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 242, Short.MAX_VALUE)
            .addGroup(Reply2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Reply2Layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addGroup(Reply2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(jLabel26))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(Reply2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jToggleButton4)
                        .addGroup(Reply2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24)))
                    .addContainerGap(18, Short.MAX_VALUE)))
        );

        jButton17.setText("Đóng");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton17)
                    .addComponent(Reply1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Reply2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton17)
                .addGap(11, 11, 11)
                .addComponent(Reply2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Reply1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(662, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel5);

        javax.swing.GroupLayout ViewCommentLayout = new javax.swing.GroupLayout(ViewComment.getContentPane());
        ViewComment.getContentPane().setLayout(ViewCommentLayout);
        ViewCommentLayout.setHorizontalGroup(
            ViewCommentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ViewCommentLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
                .addContainerGap())
        );
        ViewCommentLayout.setVerticalGroup(
            ViewCommentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ViewCommentLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 733, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Bạn có chắc chắn xóa bài không?");

        jButton8.setBackground(new java.awt.Color(51, 102, 0));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Có");

        jButton9.setBackground(new java.awt.Color(204, 51, 0));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Không");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(jButton8)
                        .addGap(61, 61, 61)
                        .addComponent(jButton9)))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel2)
                .addGap(73, 73, 73)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton9))
                .addContainerGap(116, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DeleteWarningLayout = new javax.swing.GroupLayout(DeleteWarning.getContentPane());
        DeleteWarning.getContentPane().setLayout(DeleteWarningLayout);
        DeleteWarningLayout.setHorizontalGroup(
            DeleteWarningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        DeleteWarningLayout.setVerticalGroup(
            DeleteWarningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(182, 221, 218));
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(720, 480));
        setPreferredSize(new java.awt.Dimension(1366, 768));
        setResizable(false);

        Header.setBackground(new java.awt.Color(182, 221, 218));
        Header.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 176, 169), 2, true));
        Header.setPreferredSize(new java.awt.Dimension(1280, 150));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-king-100.png"))); // NOI18N

        SearchTb.setBackground(new java.awt.Color(1, 135, 126));
        SearchTb.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SearchTb.setForeground(new java.awt.Color(255, 255, 255));
        SearchTb.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        SearchTb.setPreferredSize(new java.awt.Dimension(80, 40));
        SearchTb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchTbActionPerformed(evt);
            }
        });

        SearchCb.setBackground(new java.awt.Color(1, 135, 126));
        SearchCb.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        SearchCb.setForeground(new java.awt.Color(255, 255, 255));
        SearchCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Title", "Content", "Username" }));
        SearchCb.setPreferredSize(new java.awt.Dimension(100, 40));

        SearchBtn.setBackground(new java.awt.Color(1, 135, 126));
        SearchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-search-30.png"))); // NOI18N
        SearchBtn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        SearchBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        SearchBtn.setPreferredSize(new java.awt.Dimension(40, 40));
        SearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchBtnActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(249, 214, 120));
        jLabel5.setText("FAQs");

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel1)
                .addGap(57, 57, 57)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HeaderLayout.createSequentialGroup()
                        .addComponent(SearchCb, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(SearchTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HeaderLayout.createSequentialGroup()
                        .addComponent(CateP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(168, 168, 168))
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(CateP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HeaderLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SearchBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(SearchCb, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SearchTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeaderLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(37, 37, 37))))
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        Menu.setBackground(new java.awt.Color(182, 221, 218));
        Menu.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 176, 169), 2, true), "Menu", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 2, 14), new java.awt.Color(255, 255, 255))); // NOI18N
        Menu.setPreferredSize(new java.awt.Dimension(250, 570));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-48.png"))); // NOI18N
        jLabel3.setText("Phạm Phạm");

        CreatePostBtn.setBackground(new java.awt.Color(1, 135, 126));
        CreatePostBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CreatePostBtn.setForeground(new java.awt.Color(255, 255, 255));
        CreatePostBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-create-30.png"))); // NOI18N
        CreatePostBtn.setText("Tạo bài viết");
        CreatePostBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        CreatePostBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreatePostBtnActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(1, 135, 121));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-folder-30.png"))); // NOI18N
        jButton7.setText("Bài viết của bạn");
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(1, 135, 126));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-home-30.png"))); // NOI18N
        jButton11.setText("Trang chủ");
        jButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(1, 135, 126));
        jButton16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-30.png"))); // NOI18N
        jButton16.setText("Tài khoản");
        jButton16.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(1, 135, 126));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-logout-30.png"))); // NOI18N
        jButton10.setText("Đăng xuất");
        jButton10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu);
        Menu.setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(MenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(CreatePostBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CreatePostBtn)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addGap(18, 18, 18)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PostScrPn.setBorder(null);
        PostScrPn.setPreferredSize(new java.awt.Dimension(1080, 570));

        PostsPn.setPreferredSize(new java.awt.Dimension(1020, 20000));

        PostPn.setBackground(new java.awt.Color(182, 221, 218));
        PostPn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 176, 169), 2, true));
        PostPn.setPreferredSize(new java.awt.Dimension(1100, 800));

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-48.png"))); // NOI18N
        jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        CommentBtn.setBackground(new java.awt.Color(255, 176, 169));
        CommentBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CommentBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-comment-30.png"))); // NOI18N
        CommentBtn.setText("Comment");
        CommentBtn.setBorder(null);
        CommentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CommentBtnActionPerformed(evt);
            }
        });

        UsernamePostLb.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UsernamePostLb.setText("Phạm Phạm");

        DateLb.setText("08/02/2024");

        ContentTa.setEditable(false);
        ContentTa.setBackground(new java.awt.Color(182, 221, 218));
        ContentTa.setColumns(20);
        ContentTa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ContentTa.setLineWrap(true);
        ContentTa.setRows(5);
        ContentTa.setText("Lorem \n");
        ContentTa.setWrapStyleWord(true);
        ContentTa.setBorder(null);
        jScrollPane5.setViewportView(ContentTa);

        TimeLb.setText("09:41");

        LikeCountLb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png"))); // NOI18N
        LikeCountLb.setText("100");

        CommentCountLb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-comment-24.png"))); // NOI18N
        CommentCountLb.setText("150");

        LikeBtn.setBackground(new java.awt.Color(255, 176, 169));
        LikeBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LikeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png"))); // NOI18N
        LikeBtn.setText("Like");
        LikeBtn.setBorder(null);
        LikeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LikeBtnMouseClicked(evt);
            }
        });
        LikeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LikeBtnActionPerformed(evt);
            }
        });

        TitleLb.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        TitleLb.setText("Title");

        EditPostBtn.setBackground(new java.awt.Color(1, 135, 126));
        EditPostBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EditPostBtn.setForeground(new java.awt.Color(255, 255, 255));
        EditPostBtn.setText("Edit");

        DeletePostBtn.setBackground(new java.awt.Color(1, 135, 126));
        DeletePostBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        DeletePostBtn.setForeground(new java.awt.Color(255, 255, 255));
        DeletePostBtn.setText("Delete");

        CatePostP.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Cate");

        javax.swing.GroupLayout CatePostPLayout = new javax.swing.GroupLayout(CatePostP);
        CatePostP.setLayout(CatePostPLayout);
        CatePostPLayout.setHorizontalGroup(
            CatePostPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CatePostPLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        CatePostPLayout.setVerticalGroup(
            CatePostPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CatePostPLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        Comment_detail.setBackground(new java.awt.Color(182, 221, 218));
        Comment_detail.setForeground(new java.awt.Color(207, 207, 207));

        PnLeft.setBackground(new java.awt.Color(182, 221, 218));

        lbNameLeft.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbNameLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-30.png"))); // NOI18N
        lbNameLeft.setText("jLabel4");

        javax.swing.GroupLayout PnLeftLayout = new javax.swing.GroupLayout(PnLeft);
        PnLeft.setLayout(PnLeftLayout);
        PnLeftLayout.setHorizontalGroup(
            PnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbNameLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        PnLeftLayout.setVerticalGroup(
            PnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbNameLeft)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PnTop.setBackground(new java.awt.Color(1, 135, 126));

        lbNameTop.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbNameTop.setForeground(new java.awt.Color(255, 176, 169));
        lbNameTop.setText("jLabel5");

        txtContentTop.setBackground(new java.awt.Color(1, 135, 126));
        txtContentTop.setColumns(20);
        txtContentTop.setLineWrap(true);
        txtContentTop.setRows(1);
        txtContentTop.setWrapStyleWord(true);
        txtContentTop.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        scrTop.setViewportView(txtContentTop);

        javax.swing.GroupLayout PnTopLayout = new javax.swing.GroupLayout(PnTop);
        PnTop.setLayout(PnTopLayout);
        PnTopLayout.setHorizontalGroup(
            PnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnTopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbNameTop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PnTopLayout.createSequentialGroup()
                        .addGap(0, 79, Short.MAX_VALUE)
                        .addComponent(scrTop, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42))
        );
        PnTopLayout.setVerticalGroup(
            PnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PnTopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbNameTop, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrTop, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        BtnDel.setBackground(new java.awt.Color(1, 135, 126));
        BtnDel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnDel.setForeground(new java.awt.Color(255, 255, 255));
        BtnDel.setText("Delete");
        BtnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDelActionPerformed(evt);
            }
        });

        BtnRep.setBackground(new java.awt.Color(1, 135, 126));
        BtnRep.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnRep.setForeground(new java.awt.Color(255, 255, 255));
        BtnRep.setText("Rep");
        BtnRep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRepActionPerformed(evt);
            }
        });

        txtContentBot.setColumns(20);
        txtContentBot.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtContentBot.setLineWrap(true);
        txtContentBot.setRows(1);
        txtContentBot.setWrapStyleWord(true);
        scrBot.setViewportView(txtContentBot);

        BtnEdit.setBackground(new java.awt.Color(1, 135, 126));
        BtnEdit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnEdit.setForeground(new java.awt.Color(255, 255, 255));
        BtnEdit.setText("Edit");

        javax.swing.GroupLayout Comment_detailLayout = new javax.swing.GroupLayout(Comment_detail);
        Comment_detail.setLayout(Comment_detailLayout);
        Comment_detailLayout.setHorizontalGroup(
            Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Comment_detailLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(BtnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(BtnRep, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
            .addGroup(Comment_detailLayout.createSequentialGroup()
                .addComponent(PnLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(Comment_detailLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrBot)
                .addContainerGap())
        );
        Comment_detailLayout.setVerticalGroup(
            Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Comment_detailLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PnLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrBot, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnRep)
                    .addComponent(BtnDel)
                    .addComponent(BtnEdit))
                .addContainerGap())
        );

        javax.swing.GroupLayout listCommentsLayout = new javax.swing.GroupLayout(listComments);
        listComments.setLayout(listCommentsLayout);
        listCommentsLayout.setHorizontalGroup(
            listCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Comment_detail, javax.swing.GroupLayout.PREFERRED_SIZE, 933, Short.MAX_VALUE)
        );
        listCommentsLayout.setVerticalGroup(
            listCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listCommentsLayout.createSequentialGroup()
                .addComponent(Comment_detail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PostPnLayout = new javax.swing.GroupLayout(PostPn);
        PostPn.setLayout(PostPnLayout);
        PostPnLayout.setHorizontalGroup(
            PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PostPnLayout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PostPnLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UsernamePostLb)
                            .addGroup(PostPnLayout.createSequentialGroup()
                                .addComponent(TimeLb)
                                .addGap(18, 18, 18)
                                .addComponent(DateLb)))
                        .addGap(718, 718, 718))
                    .addComponent(CatePostP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TitleLb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PostPnLayout.createSequentialGroup()
                        .addComponent(EditPostBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(DeletePostBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PostPnLayout.createSequentialGroup()
                        .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LikeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LikeCountLb))
                        .addGap(18, 18, 18)
                        .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CommentCountLb)
                            .addComponent(CommentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(listComments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(134, 134, 134))
        );
        PostPnLayout.setVerticalGroup(
            PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PostPnLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PostPnLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CatePostP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PostPnLayout.createSequentialGroup()
                        .addComponent(UsernamePostLb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TimeLb)
                            .addComponent(DateLb)
                            .addComponent(DeletePostBtn)
                            .addComponent(EditPostBtn))))
                .addGap(5, 5, 5)
                .addComponent(TitleLb, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(LikeCountLb, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CommentCountLb)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LikeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CommentBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listComments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout PostsPnLayout = new javax.swing.GroupLayout(PostsPn);
        PostsPn.setLayout(PostsPnLayout);
        PostsPnLayout.setHorizontalGroup(
            PostsPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PostsPnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PostPn, javax.swing.GroupLayout.PREFERRED_SIZE, 1120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PostsPnLayout.setVerticalGroup(
            PostsPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PostsPnLayout.createSequentialGroup()
                .addComponent(PostPn, javax.swing.GroupLayout.PREFERRED_SIZE, 699, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19301, Short.MAX_VALUE))
        );

        PostScrPn.setViewportView(PostsPn);

        PrePageBtn.setText("Previous");
        PrePageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrePageBtnActionPerformed(evt);
            }
        });

        NextPageBtn.setText("Next");
        NextPageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextPageBtnActionPerformed(evt);
            }
        });

        CountPageTb.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CountPageTb.setText("1");
        CountPageTb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CountPageTbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(PrePageBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CountPageTb, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NextPageBtn)
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(PostScrPn, javax.swing.GroupLayout.PREFERRED_SIZE, 1146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, 1360, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PostScrPn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NextPageBtn)
                    .addComponent(CountPageTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PrePageBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CreatePostBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreatePostBtnActionPerformed
        new ant.function.Create(this.UserID).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_CreatePostBtnActionPerformed

    private void PrePageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrePageBtnActionPerformed
        int currentPage = Integer.parseInt(CountPageTb.getText());
        if (currentPage > 1) {
            int page = currentPage - 1;
            CountPageTb.setText(String.valueOf(page));
            update();
        }

    }//GEN-LAST:event_PrePageBtnActionPerformed

    private void NextPageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextPageBtnActionPerformed
        int currentPage = Integer.parseInt(CountPageTb.getText());
        if (currentPage < totalPages) {
            int page = currentPage + 1;
            CountPageTb.setText(String.valueOf(page));
            update();
        }

    }//GEN-LAST:event_NextPageBtnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        PostDao postDao = new PostDaoImpl();
        List<Post> posts = postDao.getPostsByPageByUserId(Integer.parseInt(CountPageTb.getText()), this.UserID);

        int yPosition = 0;
        if (posts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa đăng bài viết nào");
            CountPageTb.setText("1");
            divide = "all";
            displayPosts(this.posts);
            update();
        } else {
            this.posts = posts;
            displayPosts(this.posts);
            CountPageTb.setText("1");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        for (Component component : CateP.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setSelected(false);
            }
        }
        CountPageTb.setText("1");
        divide = "all";
        this.posts = new PostDaoImpl().getPostsByPage(1);
        totalPages = new PostDaoImpl().getTotalPages();
        update();
    }//GEN-LAST:event_jButton11ActionPerformed
    private void displayPostsByCategories(List<String> categories) {
        if (categories.isEmpty()) {
            CountPageTb.setText("1");
            divide = "all";
            this.posts = new PostDaoImpl().getPostsByPage(1);
            displayPosts(posts);
            totalPages = new PostDaoImpl().getTotalPages();
        } else {

            if (SearchTb.getText().isEmpty()) {
                if (!divide.equals("filter")) {
                    CountPageTb.setText("1");
                    divide = "filter";
                }
                Connection con = null;
                PreparedStatement pst = null;
                ResultSet rs = null;
                try {
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                    StringBuilder queryBuilder = new StringBuilder();

                    queryBuilder.append("SELECT COUNT(*) AS total FROM posts p ");
                    queryBuilder.append("INNER JOIN post_cate pc ON p.PostID = pc.PostID ");
                    queryBuilder.append("INNER JOIN categories c ON pc.CateID = c.CateID ");
                    queryBuilder.append("WHERE c.CateName IN (");
                    for (int i = 0; i < categories.size(); i++) {
                        queryBuilder.append("?");
                        if (i < categories.size() - 1) {
                            queryBuilder.append(", ");
                        }
                    }
                    queryBuilder.append(")");

                    pst = con.prepareStatement(queryBuilder.toString());
                    for (int i = 0; i < categories.size(); i++) {
                        pst.setString(i + 1, categories.get(i));
                    }
                    rs = pst.executeQuery();
                    int totalCount = 0;
                    if (rs.next()) {
                        totalCount = rs.getInt("total");
                    }

                    // Tính toán số trang
                    totalPages = (totalCount / 10) + ((totalCount % 10 != 0) ? 1 : 0);

                    // Tạo câu lệnh SQL để lấy bài viết dựa trên các danh mục
                    queryBuilder = new StringBuilder();
                    queryBuilder.append("SELECT p.* FROM posts p ");
                    queryBuilder.append("INNER JOIN post_cate pc ON p.PostID = pc.PostID ");
                    queryBuilder.append("INNER JOIN categories c ON pc.CateID = c.CateID ");
                    queryBuilder.append("WHERE c.CateName IN (");
                    for (int i = 0; i < categories.size(); i++) {
                        queryBuilder.append("?");
                        if (i < categories.size() - 1) {
                            queryBuilder.append(", ");
                        }
                    }
                    queryBuilder.append(") ORDER BY p.PostID DESC LIMIT ?, ?");

                    pst = con.prepareStatement(queryBuilder.toString());
                    for (int i = 0; i < categories.size(); i++) {
                        pst.setString(i + 1, categories.get(i));
                    }
                    int offset = (Integer.parseInt(CountPageTb.getText()) - 1) * 10;
                    pst.setInt(categories.size() + 1, offset);
                    pst.setInt(categories.size() + 2, 10);
                    rs = pst.executeQuery();
                    this.posts = new ArrayList<>();
                    while (rs.next()) {
                        int postId = rs.getInt("PostID");
                        int userId = rs.getInt("UserID");
                        String title = rs.getString("Title");
                        String content = rs.getString("Content");
                        java.util.Date createdAt = rs.getTimestamp("CreatedAt");
                        java.util.Date updatedAt = rs.getTimestamp("UpdatedAt");
                        Post post = new Post(postId, userId, title, content, createdAt, updatedAt);
                        this.posts.add(post);
                    }
                    displayPosts(this.posts);
                    ;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    // Đóng tài nguyên
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                        if (pst != null) {
                            pst.close();
                        }
                        if (con != null) {
                            con.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                divide = "search";
                CountPageTb.setText("1");
                update();
            }

        }
    }

    String preSearch = "";

    void SearchPost() {
        if (!divide.equals("search") || !SearchTb.getText().equals(preSearch)) {
            CountPageTb.setText("1");
            divide = "search";
        }

        String searchText = SearchTb.getText().trim();
        preSearch = searchText;
        String searchType = (String) SearchCb.getSelectedItem();

        if (searchText.isEmpty()) {
            return;
        } else {
            Connection con = null;
            PreparedStatement pst = null;
            ResultSet rs = null;

            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                StringBuilder queryBuilder = new StringBuilder();

                queryBuilder.append("SELECT COUNT(*) AS total FROM posts p");
                if (searchType.equals("Username")) {
                    queryBuilder.append(" INNER JOIN users u ON p.UserID = u.UserID WHERE u.Username LIKE ?");
                } else if (searchType.equals("Title")) {
                    queryBuilder.append(" WHERE p.Title LIKE ?");
                } else if (searchType.equals("Content")) {
                    queryBuilder.append(" WHERE p.Content LIKE ?");
                }

                boolean hasCategory = false;
                for (Component component : CateP.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.isSelected()) {
                            hasCategory = true;
                            break;
                        }
                    }
                }

                if (hasCategory) {
                    queryBuilder.append(" AND p.PostID IN (SELECT pc.PostID FROM post_cate pc INNER JOIN categories c ON pc.CateID = c.CateID WHERE c.CateName IN (");

                    boolean isFirst = true;
                    for (Component component : CateP.getComponents()) {
                        if (component instanceof JCheckBox) {
                            JCheckBox checkBox = (JCheckBox) component;
                            if (checkBox.isSelected()) {
                                if (!isFirst) {
                                    queryBuilder.append(", ");
                                }
                                queryBuilder.append("?");
                                isFirst = false;
                            }
                        }
                    }
                    queryBuilder.append("))");

                }

                pst = con.prepareStatement(queryBuilder.toString());
                pst.setString(1, "%" + searchText + "%");

                int parameterIndex = 2;
                for (Component component : CateP.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.isSelected()) {
                            pst.setString(parameterIndex++, checkBox.getText());
                        }
                    }
                }

                rs = pst.executeQuery();
                int totalCount = 0;
                if (rs.next()) {
                    totalCount = rs.getInt("total");
                }

                // Tính toán số trang
                totalPages = (totalCount / 10) + ((totalCount % 10 != 0) ? 1 : 0);

                queryBuilder = new StringBuilder();
                queryBuilder.append("SELECT p.* FROM posts p");
                if (searchType.equals("Username")) {
                    queryBuilder.append(" INNER JOIN users u ON p.UserID = u.UserID WHERE u.Username LIKE ?");
                } else if (searchType.equals("Title")) {
                    queryBuilder.append(" WHERE p.Title LIKE ?");
                } else if (searchType.equals("Content")) {
                    queryBuilder.append(" WHERE p.Content LIKE ?");
                }

                hasCategory = false;
                for (Component component : CateP.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.isSelected()) {
                            hasCategory = true;
                            break;
                        }
                    }
                }

                if (hasCategory) {
                    queryBuilder.append(" AND p.PostID IN (SELECT pc.PostID FROM post_cate pc INNER JOIN categories c ON pc.CateID = c.CateID WHERE c.CateName IN (");

                    boolean isFirst = true;
                    for (Component component : CateP.getComponents()) {
                        if (component instanceof JCheckBox) {
                            JCheckBox checkBox = (JCheckBox) component;
                            if (checkBox.isSelected()) {
                                if (!isFirst) {
                                    queryBuilder.append(", ");
                                }
                                queryBuilder.append("?");
                                isFirst = false;
                            }
                        }
                    }
                    queryBuilder.append("))");

                }

                queryBuilder.append(" ORDER BY p.PostID DESC LIMIT ?, ?");
                pst = con.prepareStatement(queryBuilder.toString());
                pst.setString(1, "%" + searchText + "%");

                parameterIndex = 2;
                for (Component component : CateP.getComponents()) {
                    if (component instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) component;
                        if (checkBox.isSelected()) {
                            pst.setString(parameterIndex++, checkBox.getText());
                        }
                    }
                }

                int offset = (Integer.parseInt(CountPageTb.getText()) - 1) * 10;
                pst.setInt(parameterIndex++, offset);
                pst.setInt(parameterIndex++, 10);

                rs = pst.executeQuery();
                this.posts = new ArrayList<>();
                while (rs.next()) {
                    int postId = rs.getInt("PostID");
                    int userId = rs.getInt("UserID");
                    String title = rs.getString("Title");
                    String content = rs.getString("Content");
                    java.util.Date createdAt = rs.getTimestamp("CreatedAt");
                    java.util.Date updatedAt = rs.getTimestamp("UpdatedAt");
                    Post post = new Post(postId, userId, title, content, createdAt, updatedAt);
                    this.posts.add(post);
                }

                displayPosts(this.posts);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                // Đóng tài nguyên
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (pst != null) {
                        pst.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    private void SearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchBtnActionPerformed
        SearchPost();
        update();

    }//GEN-LAST:event_SearchBtnActionPerformed

    private void SearchTbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchTbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchTbActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        Account acc = new Account(Role, UserID);
        acc.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void CommentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CommentBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CommentBtnActionPerformed

    private void LikeBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LikeBtnMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_LikeBtnMouseClicked

    private void LikeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LikeBtnActionPerformed

    }//GEN-LAST:event_LikeBtnActionPerformed

    private void BtnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDelActionPerformed

    private void BtnRepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnRepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnRepActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        Login lg = new Login();
        lg.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton10ActionPerformed

    private int totalPages = new PostDaoImpl().getTotalPages();

    private void CountPageTbActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_CountPageTxtActionPerformed
        try {
            int page = Integer.parseInt(CountPageTb.getText());
            if (page > 0 && page <= totalPages) {
                update();
                displayPosts(this.posts);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter an integer from 1 to " + totalPages, "Error",
                        JOptionPane.ERROR_MESSAGE);

            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter an integer!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }// GEN-LAST:event_CountPageTxtActionPerformed

//    private void LikeBtnMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_LikeBtnMouseClicked
//
//    }// GEN-LAST:event_LikeBtnMouseClicked
    private void update() {
        System.out.println(divide);
        int currentPage = Integer.parseInt(CountPageTb.getText());
        PrePageBtn.setEnabled(currentPage > 1);
        NextPageBtn.setEnabled(currentPage < totalPages);
        JScrollBar verticalScrollBar = PostScrPn.getVerticalScrollBar();
        verticalScrollBar.setValue(0);
        if (divide.equals("all")) {
            this.posts = new PostDaoImpl().getPostsByPage(Integer.parseInt(CountPageTb.getText()));
            displayPosts(this.posts);
        }
        if (divide.equals("search")) {
            SearchPost();
        }
        if (divide.equals("filter")) {
            List<String> selectedCategories = new ArrayList<>();
            for (Component component : CateP.getComponents()) {
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    if (checkBox.isSelected()) {
                        selectedCategories.add(checkBox.getText());
                    }
                }
            }
            displayPostsByCategories(selectedCategories);
        }
    }

    private void displayPosts(List<Post> posts) {

        PostsPn.removeAll();

        int yPosition = 0;

        for (Post post : posts) {
            JPanel postPanel = createPostPanel(post);
            postPanel.setBounds(0, yPosition, PostsPn.getWidth(), postPanel.getPreferredSize().height);
            PostsPn.add(postPanel);
            yPosition += postPanel.getPreferredSize().height + 20;
        }

        PostsPn.revalidate();
        PostsPn.repaint();
    }

    private JPanel createPostPanel(Post post) {
        PostDao postDao = new PostDaoImpl();
        // Giao diện của Post
        PostPn = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        CommentBtn = new javax.swing.JButton();
        UsernamePostLb = new javax.swing.JLabel();
        DateLb = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        ContentTa = new javax.swing.JTextArea();
        TimeLb = new javax.swing.JLabel();
        JLabel LikeCountLb = new javax.swing.JLabel();
        CommentCountLb = new javax.swing.JLabel();
        JToggleButton LikeBtn = new javax.swing.JToggleButton();
        TitleLb = new javax.swing.JLabel();
        EditPostBtn = new javax.swing.JButton();
        DeletePostBtn = new javax.swing.JButton();
        CatePostP = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        listComments = new javax.swing.JPanel();
        Comment_detail = new javax.swing.JPanel();
        PnLeft = new javax.swing.JPanel();
        lbNameLeft = new javax.swing.JLabel();
        PnTop = new javax.swing.JPanel();
        lbNameTop = new javax.swing.JLabel();
        scrTop = new javax.swing.JScrollPane();
        txtContentTop = new javax.swing.JTextArea();
        BtnDel = new javax.swing.JButton();
        BtnRep = new javax.swing.JButton();
        scrBot = new javax.swing.JScrollPane();
        txtContentBot = new javax.swing.JTextArea();
        BtnEdit = new javax.swing.JButton();

        PostPn.setBackground(new java.awt.Color(182, 221, 218));
        PostPn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 176, 169), 2, true));

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-48.png"))); // NOI18N
        jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        CommentBtn.setBackground(new java.awt.Color(255, 176, 169));
        CommentBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        CommentBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-comment-30.png"))); // NOI18N
        CommentBtn.setText("More Comment");
        CommentBtn.setBorder(null);
        CommentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                post.addDisplayedComment();
                displayPosts(posts);
            }
        });

        UsernamePostLb.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        UsernamePostLb.setText("Phạm Phạm");

        DateLb.setText("08/02/2024");

        ContentTa.setEditable(false);
        ContentTa.setBackground(new java.awt.Color(182, 221, 218));
        ContentTa.setColumns(20);
        ContentTa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ContentTa.setLineWrap(true);
        ContentTa.setRows(5);
        ContentTa.setText("Lorem \n");
        ContentTa.setWrapStyleWord(true);
        ContentTa.setBorder(null);
        jScrollPane5.setViewportView(ContentTa);

        TimeLb.setText("09:41");

        LikeCountLb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png"))); // NOI18N
        LikeCountLb.setText("100");

        CommentCountLb.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-comment-24.png"))); // NOI18N
        //CommentCountLb.setText(demComment(post.getPostId()));
        int commentCount = demComment(post.getPostId());
        String commentCountString = String.valueOf(commentCount);
        CommentCountLb.setText(commentCountString);

        LikeBtn.setBackground(new java.awt.Color(255, 176, 169));
        LikeBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LikeBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-like-24.png"))); // NOI18N
        LikeBtn.setText("Like");
        LikeBtn.setBorder(null);
        LikeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LikeBtnMouseClicked(evt);
            }
        });
        LikeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LikeBtnActionPerformed(evt);
            }
        });

        TitleLb.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        TitleLb.setText("Title");

        EditPostBtn.setBackground(new java.awt.Color(1, 135, 126));
        EditPostBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EditPostBtn.setForeground(new java.awt.Color(255, 255, 255));
        EditPostBtn.setText("Edit");

        DeletePostBtn.setBackground(new java.awt.Color(1, 135, 126));
        DeletePostBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        DeletePostBtn.setForeground(new java.awt.Color(255, 255, 255));
        DeletePostBtn.setText("Delete");

        CatePostP.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Cate");

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        Comment_detail.setBackground(new java.awt.Color(182, 221, 218));
        Comment_detail.setForeground(new java.awt.Color(207, 207, 207));

        PnLeft.setBackground(new java.awt.Color(182, 221, 218));

        lbNameLeft.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbNameLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-30.png"))); // NOI18N
        lbNameLeft.setText("jLabel4");

        javax.swing.GroupLayout PnLeftLayout = new javax.swing.GroupLayout(PnLeft);
        PnLeft.setLayout(PnLeftLayout);
        PnLeftLayout.setHorizontalGroup(
                PnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PnLeftLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbNameLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        PnLeftLayout.setVerticalGroup(
                PnLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PnLeftLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbNameLeft)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PnTop.setBackground(new java.awt.Color(1, 135, 126));

        lbNameTop.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbNameTop.setForeground(new java.awt.Color(255, 176, 169));
        lbNameTop.setText("jLabel5");

        txtContentTop.setBackground(new java.awt.Color(1, 135, 126));
        txtContentTop.setColumns(20);
        txtContentTop.setLineWrap(true);
        txtContentTop.setRows(1);
        txtContentTop.setWrapStyleWord(true);
        txtContentTop.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        scrTop.setViewportView(txtContentTop);

        javax.swing.GroupLayout PnTopLayout = new javax.swing.GroupLayout(PnTop);
        PnTop.setLayout(PnTopLayout);
        PnTopLayout.setHorizontalGroup(
                PnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnTopLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(PnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbNameTop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(PnTopLayout.createSequentialGroup()
                                                .addGap(0, 79, Short.MAX_VALUE)
                                                .addComponent(scrTop, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(42, 42, 42))
        );
        PnTopLayout.setVerticalGroup(
                PnTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PnTopLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lbNameTop, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrTop, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                .addContainerGap())
        );

        BtnDel.setBackground(new java.awt.Color(1, 135, 126));
        BtnDel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnDel.setForeground(new java.awt.Color(255, 255, 255));
        BtnDel.setText("Delete");
        BtnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDelActionPerformed(evt);
            }
        });

        BtnRep.setBackground(new java.awt.Color(1, 135, 126));
        BtnRep.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnRep.setForeground(new java.awt.Color(255, 255, 255));
        BtnRep.setText("Rep");
        BtnRep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnRepActionPerformed(evt);
            }
        });

        txtContentBot.setColumns(20);
        txtContentBot.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtContentBot.setLineWrap(true);
        txtContentBot.setRows(1);
        txtContentBot.setWrapStyleWord(true);
        scrBot.setViewportView(txtContentBot);

        BtnEdit.setBackground(new java.awt.Color(1, 135, 126));
        BtnEdit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BtnEdit.setForeground(new java.awt.Color(255, 255, 255));
        BtnEdit.setText("Edit");

        javax.swing.GroupLayout Comment_detailLayout = new javax.swing.GroupLayout(Comment_detail);
        Comment_detail.setLayout(Comment_detailLayout);
        Comment_detailLayout.setHorizontalGroup(
                Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Comment_detailLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BtnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(BtnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(BtnRep, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39))
                        .addGroup(Comment_detailLayout.createSequentialGroup()
                                .addComponent(PnLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(Comment_detailLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrBot)
                                .addContainerGap())
        );
        Comment_detailLayout.setVerticalGroup(
                Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Comment_detailLayout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(PnLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(PnTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrBot, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                .addGroup(Comment_detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(BtnRep)
                                        .addComponent(BtnDel)
                                        .addComponent(BtnEdit))
                                .addContainerGap())
        );

//        javax.swing.GroupLayout listCommentsLayout = new javax.swing.GroupLayout(listComments);
//        listComments.setLayout(listCommentsLayout);
//        listCommentsLayout.setHorizontalGroup(
//                listCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addComponent(Comment_detail, javax.swing.GroupLayout.PREFERRED_SIZE, 933, Short.MAX_VALUE)
//        );
//        listCommentsLayout.setVerticalGroup(
//                listCommentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(listCommentsLayout.createSequentialGroup()
//                                .addComponent(Comment_detail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addGap(0, 0, Short.MAX_VALUE))
//        );
        javax.swing.GroupLayout PostPnLayout = new javax.swing.GroupLayout(PostPn);
        PostPn.setLayout(PostPnLayout);
        PostPnLayout.setHorizontalGroup(
                PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PostPnLayout.createSequentialGroup()
                                .addContainerGap(49, Short.MAX_VALUE)
                                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PostPnLayout.createSequentialGroup()
                                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(UsernamePostLb)
                                                        .addGroup(PostPnLayout.createSequentialGroup()
                                                                .addComponent(TimeLb)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(DateLb)))
                                                .addGap(718, 718, 718))
                                        .addComponent(CatePostP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(TitleLb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PostPnLayout.createSequentialGroup()
                                                .addComponent(EditPostBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(DeletePostBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(PostPnLayout.createSequentialGroup()
                                                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(LikeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(LikeCountLb))
                                                .addGap(18, 18, 18)
                                                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(CommentCountLb)
                                                        .addComponent(CommentBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(listComments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(134, 134, 134))
        );
        PostPnLayout.setVerticalGroup(
                PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PostPnLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(PostPnLayout.createSequentialGroup()
                                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(CatePostP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(PostPnLayout.createSequentialGroup()
                                                .addComponent(UsernamePostLb)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(TimeLb)
                                                        .addComponent(DateLb)
                                                        .addComponent(DeletePostBtn)
                                                        .addComponent(EditPostBtn))))
                                .addGap(5, 5, 5)
                                .addComponent(TitleLb, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(LikeCountLb, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(CommentCountLb)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PostPnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(LikeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(CommentBtn))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(listComments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(35, Short.MAX_VALUE))
        );
        // Hết giao diện

        //Đức code
        ///Hiển thị ô thêm comment
        listComments.removeAll();
        BoxLayout boxLayout = new BoxLayout(listComments, BoxLayout.Y_AXIS);

        listComments.setLayout(boxLayout); // Sử dụng BoxLayout cho listComments

        // tạo 1 comment mới
        JPanel newPnComment1 = new JPanel();

        JButton newBtnAdd1 = new JButton();
        newBtnAdd1.setBackground(new java.awt.Color(1, 135, 126));
        newBtnAdd1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newBtnAdd1.setForeground(new java.awt.Color(255, 255, 255));
        newBtnAdd1.setText("Add");

        //thêm comment
        JButton newBtnUpdate1 = new JButton();
        newBtnUpdate1.setBackground(new java.awt.Color(1, 135, 126));
        newBtnUpdate1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        newBtnUpdate1.setForeground(new java.awt.Color(255, 255, 255));
        newBtnUpdate1.setText("Update");

        JPanel newPnleft1 = new JPanel();
        JLabel newLbName1 = new JLabel(); // Chuyển đổi int thành String

        try {
            String query5 = "SELECT * FROM users Where UserID = " + UserID;
            Statement statement5 = conn.createStatement();
            ResultSet resultSet5 = statement5.executeQuery(query5);
            while (resultSet5.next()) {
                newLbName1.setText(resultSet5.getString("Username"));
            }
        } catch (SQLException e2) {
            System.out.println("Error displaying comments: " + e2.getMessage());
        }

        newLbName1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        newLbName1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-30.png")));

        newPnleft1.setBackground(new java.awt.Color(182, 221, 218));
        javax.swing.GroupLayout PnLeftLayout1 = new javax.swing.GroupLayout(newPnleft1);
        newPnleft1.setLayout(PnLeftLayout1);
        PnLeftLayout1.setHorizontalGroup(
                PnLeftLayout1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PnLeftLayout1.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(newLbName1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        PnLeftLayout1.setVerticalGroup(
                PnLeftLayout1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PnLeftLayout1.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(newLbName1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        JPanel newPnTop1 = new JPanel();
        JScrollPane scrTop1 = new JScrollPane();
        JLabel newLbParentName1 = new JLabel();
        JTextArea newTxtParentContent1 = new JTextArea();

        newLbParentName1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        newLbParentName1.setForeground(new java.awt.Color(255, 176, 169));

        newTxtParentContent1.setEditable(false);
        newTxtParentContent1.setBackground(new java.awt.Color(1, 135, 126));
        newTxtParentContent1.setColumns(20);
        newTxtParentContent1.setLineWrap(true);
        newTxtParentContent1.setRows(1);
        newTxtParentContent1.setWrapStyleWord(true);
        newTxtParentContent1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        scrTop1.setViewportView(newTxtParentContent1);

        newPnTop1.setBackground(new java.awt.Color(1, 135, 126));
        javax.swing.GroupLayout PnTopLayout1 = new javax.swing.GroupLayout(newPnTop1);
        newPnTop1.setLayout(PnTopLayout1);
        PnTopLayout1.setHorizontalGroup(
                PnTopLayout1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PnTopLayout1.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(PnTopLayout1.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(newLbParentName1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(PnTopLayout1.createSequentialGroup()
                                                .addGap(0, 79, Short.MAX_VALUE)
                                                .addComponent(scrTop1, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(42, 42, 42))
        );
        PnTopLayout1.setVerticalGroup(
                PnTopLayout1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(PnTopLayout1.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(newLbParentName1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrTop1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                                .addContainerGap())
        );

        JScrollPane scrBot1 = new JScrollPane();
        JTextArea newTxtContent1 = new JTextArea("Please comment here");
        //placeholder
        final boolean[] isFirstClick = {true}; // Khai báo một mảng chứa boolean

        newTxtContent1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isFirstClick[0]) {
                    newTxtContent1.setText("");
                    isFirstClick[0] = false;
                }
            }
        });

        newTxtContent1.setColumns(20);
        newTxtContent1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        newTxtContent1.setLineWrap(true);
        newTxtContent1.setRows(1);
        newTxtContent1.setWrapStyleWord(true);
        scrBot1.setViewportView(newTxtContent1);

        newPnComment1.setBackground(new java.awt.Color(182, 221, 218));
        newPnComment1.setForeground(new java.awt.Color(207, 207, 207));

        javax.swing.GroupLayout Comment_detailLayout1 = new javax.swing.GroupLayout(newPnComment1);
        newPnComment1.setLayout(Comment_detailLayout1);
        Comment_detailLayout1.setHorizontalGroup(
                Comment_detailLayout1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Comment_detailLayout1.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(newBtnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(newBtnUpdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39))
                        .addGroup(Comment_detailLayout1.createSequentialGroup()
                                .addComponent(newPnleft1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newPnTop1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(Comment_detailLayout1.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrBot1)
                                .addContainerGap())
        );
        Comment_detailLayout1.setVerticalGroup(
                Comment_detailLayout1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Comment_detailLayout1.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(Comment_detailLayout1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(newPnleft1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(newPnTop1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrBot1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                                .addGroup(Comment_detailLayout1.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(newBtnUpdate1)
                                        .addComponent(newBtnAdd1))
                                .addContainerGap())
        );
        //Thêm comment
        newBtnAdd1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    content1 = newTxtContent1.getText();
                    if (content1.isEmpty() || content1.equals("Please comment here")) {
                        JOptionPane.showMessageDialog(null, "Content cannot be empty.");
                        return;
                    }

                    listComments.removeAll();
                    String sql = "INSERT INTO comments (UserID, PostID, Content, PComment) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setInt(1, UserID);
                    statement.setInt(2, post.getPostId());
                    statement.setString(3, content1);
                    if (pComment1 == -1) {
                        statement.setNull(4, java.sql.Types.INTEGER);
                    } else {
                        statement.setInt(4, pComment1);
                    }

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(null, "Comment added successfully!");
                        newTxtContent1.setText("");
                        pComment1 = -1;
                        newTxtParentContent1.setText("");
                        newLbParentName1.setText("");

                    }
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Error adding product: " + e1.getMessage());
                } catch (NumberFormatException e2) {
                    JOptionPane.showMessageDialog(null, "Invalid price format. Please enter a valid number.");
                }

                displayPosts(posts);
            }
        }
        );

        //sửa comment
        newBtnUpdate1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    content1 = newTxtContent1.getText();
                    if (content1.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Content cannot be empty.");
                        return;
                    }

                    if (pComment1 == -1) {
                        JOptionPane.showMessageDialog(null, "You must select a comment you want to edit.");
                        return;
                    }

                    listComments.removeAll();
                    String sql = "UPDATE comments SET Content = ? WHERE CommentID = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, content1);
                    statement.setInt(2, pComment1);

                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(null, "Comment Update successfully!");
                        newTxtContent1.setText("");
                        pComment1 = -1;
                        newTxtParentContent1.setText("");
                        newLbParentName1.setText("");
                    }
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Error updating comment: " + e1.getMessage());
                } catch (NumberFormatException e2) {
                    JOptionPane.showMessageDialog(null, "Invalid price format. Please enter a valid number.");
                }
                displayPosts(posts);
            }
        });

        listComments.add(newPnComment1);
        listComments.add(Box.createVerticalStrut(20));
        //Đóng Hiển thị ô thêm comment

        displayComments(post.getPostId(), newTxtParentContent1, newLbParentName1, newTxtContent1, post.getDisplayedComments(), post.getDemPost(), isFirstClick);
        //hết code của đức 

        if (post.getUserId() != this.UserID) {
            DeletePostBtn.setVisible(false);
            EditPostBtn.setVisible(false);
        }
        DateLb.setText(post.getDate());
        TimeLb.setText(post.getTime());
        UsernamePostLb.setText(new PostDaoImpl().getUsername(post.getUserId()));
        TitleLb.setText(post.getTitle());
        ContentTa.setText(post.getContent());
        LikeCountLb.setText(Integer.toString(postDao.countLikePost(post.getPostId())));
        boolean userLiked = postDao.checkIfUserLikedPost(post.getPostId(), this.UserID);
        LikeBtn.setSelected(userLiked);
        if (userLiked) {
            LikeBtn.setBackground(new java.awt.Color(255, 176, 169));
        } else {
            LikeBtn.setBackground(Color.GRAY);
        }
        LikeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PostDao postDao = new PostDaoImpl();
                postDao.likePost(post.getPostId(), UserID);
                LikeCountLb.setText(Integer.toString(postDao.countLikePost(post.getPostId())));
                if (LikeBtn.getBackground() == Color.GRAY) {
                    LikeBtn.setBackground(new java.awt.Color(255, 176, 169));
                } else {
                    LikeBtn.setBackground(Color.GRAY);
                }
            }
        });

        EditPostBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new ant.function.Edit(post).setVisible(true);
                Window window = SwingUtilities.getWindowAncestor(EditPostBtn);
                if (window instanceof JFrame) {
                    ((JFrame) window).dispose();
                }
            }
        });
        DeletePostBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Window window = SwingUtilities.getWindowAncestor(DeletePostBtn);
                int confirmDialogResult = JOptionPane.showConfirmDialog(((JFrame) window), "Are you sure you want to delete this post and related comments?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmDialogResult == JOptionPane.YES_OPTION) {
                    new PostDaoImpl().deletePost(post.getPostId());
                    JOptionPane.showMessageDialog(((JFrame) window), "Post Deleted");
                    jButton7ActionPerformed(evt);
                }
            }
        });
        displayPostCategories(post);

        return PostPn;
    }

    private void displayPostCategories(Post post) {
        List<String> postCategories = new ArrayList<>();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            pst = con.prepareStatement("SELECT pc.CateID, c.CateName FROM post_cate pc INNER JOIN categories c ON pc.CateID = c.CateID WHERE pc.PostID = ?");
            pst.setInt(1, post.getPostId());
            rs = pst.executeQuery();

            while (rs.next()) {
                String categoryName = rs.getString("CateName");
                postCategories.add(categoryName);
            }

            int size = postCategories.size();
            for (int i = 0; i < size; i++) {
                JLabel categoryLabel = new JLabel(postCategories.get(i));
                CatePostP.add(categoryLabel);

                if (i < size - 1) {
                    JLabel commaLabel = new JLabel(",");
                    CatePostP.add(commaLabel);
                }
            }

            CatePostP.revalidate();
            CatePostP.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        // </editor-fold>
        // </editor-fold>

        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDel;
    private javax.swing.JButton BtnEdit;
    private javax.swing.JButton BtnRep;
    private javax.swing.JPanel CateP;
    private javax.swing.JPanel CatePostP;
    private javax.swing.JButton CommentBtn;
    private javax.swing.JLabel CommentCountLb;
    private javax.swing.JPanel Comment_detail;
    private javax.swing.JTextArea ContentTa;
    private javax.swing.JTextField CountPageTb;
    private javax.swing.JButton CreatePostBtn;
    private javax.swing.JLabel DateLb;
    private javax.swing.JButton DeletePostBtn;
    private javax.swing.JDialog DeleteWarning;
    private javax.swing.JButton EditPostBtn;
    private javax.swing.JPanel Header;
    private javax.swing.JToggleButton LikeBtn;
    private javax.swing.JLabel LikeCountLb;
    private javax.swing.JPanel Menu;
    private javax.swing.JButton NextPageBtn;
    private javax.swing.JPanel PnLeft;
    private javax.swing.JPanel PnTop;
    private javax.swing.JPanel PostPn;
    private javax.swing.JScrollPane PostScrPn;
    private javax.swing.JPanel PostsPn;
    private javax.swing.JButton PrePageBtn;
    private javax.swing.JPanel Reply1;
    private javax.swing.JPanel Reply2;
    private javax.swing.JButton SearchBtn;
    private javax.swing.JComboBox<String> SearchCb;
    private javax.swing.JTextField SearchTb;
    private javax.swing.JLabel TimeLb;
    private javax.swing.JLabel TitleLb;
    private javax.swing.JLabel UsernamePostLb;
    private javax.swing.JDialog ViewComment;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JLabel lbNameLeft;
    private javax.swing.JLabel lbNameTop;
    private javax.swing.JPanel listComments;
    private javax.swing.JScrollPane scrBot;
    private javax.swing.JScrollPane scrTop;
    private javax.swing.JTextArea txtContentBot;
    private javax.swing.JTextArea txtContentTop;
    // End of variables declaration//GEN-END:variables
}
