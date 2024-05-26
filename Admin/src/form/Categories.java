package form;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

public class Categories extends javax.swing.JPanel {

    public Categories() {
        initComponents();
        setOpaque(false);
        CateTbl.addTableStyle(jScrollPane1);
        DisplayCategories();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel1 = new swing.RoundPanel();
        jLabel3 = new javax.swing.JLabel();
        CateNameTb = new javax.swing.JTextField();
        DeleteBtn = new javax.swing.JButton();
        AddBtn = new javax.swing.JButton();
        EditBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        roundPanel2 = new swing.RoundPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CateTbl = new swing.Table();
        SearchTb = new javax.swing.JTextField();
        SearchBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        SearchCb = new javax.swing.JComboBox<>();
        RefreshBtn = new javax.swing.JButton();

        roundPanel1.setBackground(new java.awt.Color(60, 60, 60));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("CateName");

        DeleteBtn.setText("Delete");
        DeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteBtnActionPerformed(evt);
            }
        });

        AddBtn.setText("Add New Cate");
        AddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddBtnActionPerformed(evt);
            }
        });

        EditBtn.setText("Edit");
        EditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditBtnActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Update Category");

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addComponent(AddBtn)
                                .addGap(18, 18, 18)
                                .addComponent(DeleteBtn)
                                .addGap(18, 18, 18)
                                .addComponent(EditBtn))
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CateNameTb, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CateNameTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DeleteBtn)
                    .addComponent(EditBtn)
                    .addComponent(AddBtn))
                .addGap(15, 15, 15))
        );

        roundPanel2.setBackground(new java.awt.Color(60, 60, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Category Management");

        CateTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "CateID", "CateName", "Action"
            }
        ));
        CateTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CateTblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(CateTbl);
        if (CateTbl.getColumnModel().getColumnCount() > 0) {
            CateTbl.getColumnModel().getColumn(0).setMinWidth(120);
            CateTbl.getColumnModel().getColumn(0).setPreferredWidth(120);
            CateTbl.getColumnModel().getColumn(0).setMaxWidth(120);
            CateTbl.getColumnModel().getColumn(1).setPreferredWidth(200);
            CateTbl.getColumnModel().getColumn(2).setMinWidth(150);
            CateTbl.getColumnModel().getColumn(2).setPreferredWidth(150);
            CateTbl.getColumnModel().getColumn(2).setMaxWidth(150);
        }

        SearchBtn.setText("Search");
        SearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchBtnActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Search");

        SearchCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CateID", "CateName", "UserID" }));
        SearchCb.setSelectedIndex(1);

        RefreshBtn.setText("Refresh");
        RefreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundPanel2Layout = new javax.swing.GroupLayout(roundPanel2);
        roundPanel2.setLayout(roundPanel2Layout);
        roundPanel2Layout.setHorizontalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(627, 627, 627))
                    .addGroup(roundPanel2Layout.createSequentialGroup()
                        .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(roundPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SearchTb, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SearchCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SearchBtn)
                                .addGap(334, 334, 334)
                                .addComponent(RefreshBtn)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        roundPanel2Layout.setVerticalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SearchTb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(SearchCb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(SearchBtn)
                        .addComponent(RefreshBtn)))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(roundPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 824, Short.MAX_VALUE)
                    .addComponent(roundPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void DeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteBtnActionPerformed
        if (id == 0) {
            JOptionPane.showMessageDialog(this, "Select the category to delete");
        } else {
            int confirmDialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirmDialogResult == JOptionPane.YES_OPTION) {
                try {
                    Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                    String Query = "DELETE FROM Categories WHERE CateID = " + id;
                    Statement Del = Con.createStatement();
                    Del.executeUpdate(Query);
                    JOptionPane.showMessageDialog(this, "Category Deleted");
                    CateNameTb.setText("");
                    Con.close();
                    DisplayCategories();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e);
                }
            }
        }
    }//GEN-LAST:event_DeleteBtnActionPerformed

    private void AddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddBtnActionPerformed
        if (CateNameTb.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter category");
        } else {
            try {

                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                PreparedStatement Add = Con.prepareStatement("insert into Categories(CateName) value(?)");
                Add.setString(1, CateNameTb.getText());
                int row = Add.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category saved and awaiting approval");
                Con.close();
                CateNameTb.setText("");
                DisplayCategories();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }//GEN-LAST:event_AddBtnActionPerformed

    private void EditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditBtnActionPerformed
        if (id == 0) {
            JOptionPane.showMessageDialog(this, "Selecte the category to edit");
        } else {
            try {
                Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
                String Query = "Update Categories set CateName = ?, UserID = null where CateID =?";
                PreparedStatement Edit = Con.prepareStatement(Query);
                Edit.setString(1, CateNameTb.getText());
                Edit.setInt(2, id);
                int row = Edit.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category Updated and awaiting approval");
                Con.close();
                CateNameTb.setText("");
                DisplayCategories();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e);
            }
        }
    }//GEN-LAST:event_EditBtnActionPerformed
    int id = 0;
    private void CateTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CateTblMouseClicked
        DefaultTableModel model = (DefaultTableModel) CateTbl.getModel();
        int MyIndex = CateTbl.getSelectedRow();
        id = Integer.valueOf(model.getValueAt(MyIndex, 0).toString());
        CateNameTb.setText(model.getValueAt(MyIndex, 1).toString());
    }//GEN-LAST:event_CateTblMouseClicked

    private void SearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchBtnActionPerformed
         if (SearchTb.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter searchbox");
        } else {
            if (SearchCb.getSelectedIndex() == 0) {
                SearchWithCateID();
            }
            if (SearchCb.getSelectedIndex() == 1) {
                SearchWithCateName();
            }
            if (SearchCb.getSelectedIndex() == 2) {
                SearchWithUserID();
            }
        }
    }//GEN-LAST:event_SearchBtnActionPerformed

    private void RefreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshBtnActionPerformed
        DisplayCategories();
    }//GEN-LAST:event_RefreshBtnActionPerformed
    Connection Con = null;
    PreparedStatement pst = null;
    ResultSet Rs = null;
    Statement St = null;

    private void DisplayCategories() {
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            St = Con.createStatement();
            Rs = St.executeQuery("select * from categories");
            CateTbl.setModel(DbUtils.resultSetToTableModel(Rs));
        } catch (Exception e) {
        }
    }

    private void SearchWithCateID() {
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            String query = "SELECT * FROM categories WHERE CateID LIKE ?";
            PreparedStatement search = Con.prepareStatement(query);
            search.setString(1, "%" + SearchTb.getText() + "%");
            Rs = search.executeQuery();
            CateTbl.setModel(DbUtils.resultSetToTableModel(Rs));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SearchWithUserID() {
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            String query = "SELECT * FROM categories WHERE UserID LIKE ?";
            PreparedStatement search = Con.prepareStatement(query);
            search.setString(1, "%" + SearchTb.getText() + "%");
            Rs = search.executeQuery();
            CateTbl.setModel(DbUtils.resultSetToTableModel(Rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SearchWithCateName() {
        try {
            Con = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            String query = "SELECT * FROM categories WHERE CateName LIKE ?";
            PreparedStatement search = Con.prepareStatement(query);
            search.setString(1, "%" + SearchTb.getText() + "%");
            Rs = search.executeQuery();
            CateTbl.setModel(DbUtils.resultSetToTableModel(Rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddBtn;
    private javax.swing.JTextField CateNameTb;
    private swing.Table CateTbl;
    private javax.swing.JButton DeleteBtn;
    private javax.swing.JButton EditBtn;
    private javax.swing.JButton RefreshBtn;
    private javax.swing.JButton SearchBtn;
    private javax.swing.JComboBox<String> SearchCb;
    private javax.swing.JTextField SearchTb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private swing.RoundPanel roundPanel1;
    private swing.RoundPanel roundPanel2;
    // End of variables declaration//GEN-END:variables
}
