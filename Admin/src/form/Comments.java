package form;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Comments extends javax.swing.JPanel {

    int id = 0;

    public Comments(int id) {
        initComponents();
        setOpaque(false);
        CommentTbl.addTableStyle(jScrollPane1);
        this.id = id;
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery("select * from posts where PostID = " + id);
            if (Rs.next()) {
                ContentTa.setText(Rs.getString("Content"));
            }
        } catch (Exception e) {
        }
        DisplayComments();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel2 = new swing.RoundPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CommentTbl = new swing.Table();
        jScrollPane2 = new javax.swing.JScrollPane();
        ContentTa = new javax.swing.JTextArea();
        SearchTb = new javax.swing.JTextField();
        SearchCb = new javax.swing.JComboBox<>();
        SearchBtn = new javax.swing.JButton();
        RefreshBtn = new javax.swing.JButton();
        DeleteBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        roundPanel2.setBackground(new java.awt.Color(60, 60, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Comments Management");

        CommentTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "UserName", "Content", "RepID", "Like", "Action"
            }
        ));
        CommentTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CommentTblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(CommentTbl);
        if (CommentTbl.getColumnModel().getColumnCount() > 0) {
            CommentTbl.getColumnModel().getColumn(0).setMinWidth(150);
            CommentTbl.getColumnModel().getColumn(0).setPreferredWidth(150);
            CommentTbl.getColumnModel().getColumn(0).setMaxWidth(150);
            CommentTbl.getColumnModel().getColumn(2).setMinWidth(60);
            CommentTbl.getColumnModel().getColumn(2).setPreferredWidth(60);
            CommentTbl.getColumnModel().getColumn(2).setMaxWidth(60);
            CommentTbl.getColumnModel().getColumn(3).setMinWidth(100);
            CommentTbl.getColumnModel().getColumn(3).setPreferredWidth(100);
            CommentTbl.getColumnModel().getColumn(3).setMaxWidth(100);
            CommentTbl.getColumnModel().getColumn(4).setMinWidth(100);
            CommentTbl.getColumnModel().getColumn(4).setPreferredWidth(100);
            CommentTbl.getColumnModel().getColumn(4).setMaxWidth(100);
        }

        ContentTa.setColumns(20);
        ContentTa.setRows(5);
        ContentTa.setText("sadas");
        jScrollPane2.setViewportView(ContentTa);

        SearchCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Content", "UserName", "CommentID", "PComment" }));

        SearchBtn.setText("Search");
        SearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchBtnActionPerformed(evt);
            }
        });

        RefreshBtn.setText("Refresh");
        RefreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshBtnActionPerformed(evt);
            }
        });

        DeleteBtn.setText("Delete");
        DeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteBtnActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Search");

        javax.swing.GroupLayout roundPanel2Layout = new javax.swing.GroupLayout(roundPanel2);
        roundPanel2.setLayout(roundPanel2Layout);
        roundPanel2Layout.setHorizontalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                        .addGap(449, 449, 449))
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel2Layout.createSequentialGroup()
                        .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SearchTb, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SearchCb, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SearchBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(DeleteBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RefreshBtn)))
                        .addGap(3, 3, 3)))
                .addContainerGap())
        );
        roundPanel2Layout.setVerticalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SearchBtn)
                    .addComponent(SearchCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DeleteBtn)
                    .addComponent(RefreshBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void RefreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshBtnActionPerformed
        DisplayComments();
    }//GEN-LAST:event_RefreshBtnActionPerformed
    int key = 0;
    private void CommentTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CommentTblMouseClicked
        DefaultTableModel model = (DefaultTableModel) CommentTbl.getModel();
        int MyIndex = CommentTbl.getSelectedRow();
        key = Integer.valueOf(model.getValueAt(MyIndex, 0).toString());
    }//GEN-LAST:event_CommentTblMouseClicked
    private void DeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (key == 0) {
            JOptionPane.showMessageDialog(this, "Select the comment to delete");
        } else {
            int confirmDialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you can delete this comment and related comments?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirmDialogResult == JOptionPane.YES_OPTION) {
                try {
                    Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                    String Query = "DELETE FROM comments WHERE CommentID = ? OR PComment = ?";
                    try (PreparedStatement Del = Con.prepareStatement(Query)) {
                        Del.setInt(1, key);
                        Del.setInt(2, key);
                        int rowsAffected = Del.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Comment(s) Deleted");
                        } else {
                            JOptionPane.showMessageDialog(this, "No matching comment found");
                        }
                    }
                    Con.close();
                    key = 0;
                    DisplayComments();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);
                }
            }

        }
    }
    private void SearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchBtnActionPerformed
        if (SearchTb.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter searchbox");
        } else {
            if (SearchCb.getSelectedIndex() == 0) {
                SearchWithContent();
            }
            if (SearchCb.getSelectedIndex() == 1) {
                SearchWithUsername();
            }
            if (SearchCb.getSelectedIndex() == 2) {
                SearchWithCommentID();
            }
            if (SearchCb.getSelectedIndex() == 3) {
                SearchWithPComment();
            }
        }
    }//GEN-LAST:event_SearchBtnActionPerformed
    Connection Con = null;
    PreparedStatement pst = null;
    ResultSet Rs = null;
    Statement St = null;

    private void DisplayComments() {
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery("select * from comments where PostID = " + id);
            Connection Con1 = null;
            PreparedStatement pst1 = null;
            ResultSet Rs1 = null;
            Statement St1 = null;
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("CommentID");
            model.addColumn("UserName");
            model.addColumn("Content");
            model.addColumn("PComment");
            model.addColumn("CreatedAt");
            model.addColumn("UpdatedAt");

            while (Rs.next()) {
                int commentID = Rs.getInt("CommentID");
                int userID = Rs.getInt("UserID");
                String userName = "";
                Con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                St1 = Con1.createStatement();
                Rs1 = St1.executeQuery("select * from users where UserID = " + userID);
                if (Rs1.next()) {
                    userName = Rs1.getString("Username");
                }
                Con1.close();
                String content = Rs.getString("Content");
                int pComment = Rs.getInt("PComment");
                Timestamp createdAt = Rs.getTimestamp("CreatedAt");
                Timestamp updatedAt = Rs.getTimestamp("UpdatedAt");

                model.addRow(new Object[]{commentID, userName, content, pComment, createdAt, updatedAt});
            }

            CommentTbl.setModel(model);
            Con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void SearchWithContent() {
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            String query = "SELECT * FROM comments WHERE Content LIKE ? and PostID = " + id;
            PreparedStatement search = Con.prepareStatement(query);
            search.setString(1, "%" + SearchTb.getText() + "%");
            Rs = search.executeQuery();
            Connection Con1 = null;
            PreparedStatement pst1 = null;
            ResultSet Rs1 = null;
            Statement St1 = null;
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("CommentID");
            model.addColumn("UserName");
            model.addColumn("Content");
            model.addColumn("PComment");
            model.addColumn("CreatedAt");
            model.addColumn("UpdatedAt");

            while (Rs.next()) {
                int commentID = Rs.getInt("CommentID");
                int userID = Rs.getInt("UserID");
                String userName = "";
                Con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                St1 = Con1.createStatement();
                Rs1 = St1.executeQuery("select * from users where UserID = " + userID);
                if (Rs1.next()) {
                    userName = Rs1.getString("Username");
                }
                Con1.close();
                String content = Rs.getString("Content");
                int pComment = Rs.getInt("PComment");
                Timestamp createdAt = Rs.getTimestamp("CreatedAt");
                Timestamp updatedAt = Rs.getTimestamp("UpdatedAt");

                model.addRow(new Object[]{commentID, userName, content, pComment, createdAt, updatedAt});
            }

            CommentTbl.setModel(model);
            Con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SearchWithUsername() {
        try {
            Connection Con1 = null, Con2 = null;
            PreparedStatement pst1 = null, pst2 = null;
            ResultSet Rs1 = null, Rs2 = null;
            Statement St1 = null, St2 = null;
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            String query = "SELECT * FROM comments WHERE UserID = ? and PostID = " + id;
            PreparedStatement search = Con.prepareStatement(query);
            Con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            pst2 = Con2.prepareStatement("select * from users where Username = ?");
            pst2.setString(1, SearchTb.getText());
            Rs2 = pst2.executeQuery();
            int uID = 0;
            if (Rs2.next()) {
                uID = Rs2.getInt("UserID");
            }
            Con2.close();
            search.setInt(1, uID);
            Rs = search.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("CommentID");
            model.addColumn("UserName");
            model.addColumn("Content");
            model.addColumn("PComment");
            model.addColumn("CreatedAt");
            model.addColumn("UpdatedAt");

            while (Rs.next()) {
                int commentID = Rs.getInt("CommentID");
                int userID = Rs.getInt("UserID");
                String userName = "";
                Con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                St1 = Con1.createStatement();
                Rs1 = St1.executeQuery("select * from users where UserID = " + userID);
                if (Rs1.next()) {
                    userName = Rs1.getString("Username");
                }
                Con1.close();
                String content = Rs.getString("Content");
                int pComment = Rs.getInt("PComment");
                Timestamp createdAt = Rs.getTimestamp("CreatedAt");
                Timestamp updatedAt = Rs.getTimestamp("UpdatedAt");

                model.addRow(new Object[]{commentID, userName, content, pComment, createdAt, updatedAt});
            }

            CommentTbl.setModel(model);
            Con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SearchWithCommentID() {
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            String query = "SELECT * FROM comments WHERE CommentID LIKE ? and PostID = " + id;
            PreparedStatement search = Con.prepareStatement(query);
            search.setString(1, "%" + SearchTb.getText() + "%");
            Rs = search.executeQuery();
            Connection Con1 = null;
            PreparedStatement pst1 = null;
            ResultSet Rs1 = null;
            Statement St1 = null;
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("CommentID");
            model.addColumn("UserName");
            model.addColumn("Content");
            model.addColumn("PComment");
            model.addColumn("CreatedAt");
            model.addColumn("UpdatedAt");

            while (Rs.next()) {
                int commentID = Rs.getInt("CommentID");
                int userID = Rs.getInt("UserID");
                String userName = "";
                Con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                St1 = Con1.createStatement();
                Rs1 = St1.executeQuery("select * from users where UserID = " + userID);
                if (Rs1.next()) {
                    userName = Rs1.getString("Username");
                }
                Con1.close();
                String content = Rs.getString("Content");
                int pComment = Rs.getInt("PComment");
                Timestamp createdAt = Rs.getTimestamp("CreatedAt");
                Timestamp updatedAt = Rs.getTimestamp("UpdatedAt");

                model.addRow(new Object[]{commentID, userName, content, pComment, createdAt, updatedAt});
            }

            CommentTbl.setModel(model);
            Con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SearchWithPComment() {
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            String query = "SELECT * FROM comments WHERE PComment LIKE ? and PostID = " + id;
            PreparedStatement search = Con.prepareStatement(query);
            search.setString(1, "%" + SearchTb.getText() + "%");
            Rs = search.executeQuery();
            Connection Con1 = null;
            PreparedStatement pst1 = null;
            ResultSet Rs1 = null;
            Statement St1 = null;
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("CommentID");
            model.addColumn("UserName");
            model.addColumn("Content");
            model.addColumn("PComment");
            model.addColumn("CreatedAt");
            model.addColumn("UpdatedAt");

            while (Rs.next()) {
                int commentID = Rs.getInt("CommentID");
                int userID = Rs.getInt("UserID");
                String userName = "";
                Con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                St1 = Con1.createStatement();
                Rs1 = St1.executeQuery("select * from users where UserID = " + userID);
                if (Rs1.next()) {
                    userName = Rs1.getString("Username");
                }
                Con1.close();
                String content = Rs.getString("Content");
                int pComment = Rs.getInt("PComment");
                Timestamp createdAt = Rs.getTimestamp("CreatedAt");
                Timestamp updatedAt = Rs.getTimestamp("UpdatedAt");

                model.addRow(new Object[]{commentID, userName, content, pComment, createdAt, updatedAt});
            }

            CommentTbl.setModel(model);
            Con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private swing.Table CommentTbl;
    private javax.swing.JTextArea ContentTa;
    private javax.swing.JButton DeleteBtn;
    private javax.swing.JButton RefreshBtn;
    private javax.swing.JButton SearchBtn;
    private javax.swing.JComboBox<String> SearchCb;
    private javax.swing.JTextField SearchTb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private swing.RoundPanel roundPanel2;
    // End of variables declaration//GEN-END:variables
}
