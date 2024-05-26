/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ant.view;

import java.sql.*;
import javax.swing.*;

public class Account extends javax.swing.JFrame {

    private int UserID;
    private int Role;
    Connection conn = null;

    public Account(int Role, int UserID) {
        this.UserID = UserID;
        this.Role = Role;
        initComponents();
        connectDB();
        displayUserInfo();
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

    private void displayUserInfo() {
        try {
            String query = "SELECT * FROM users WHERE UserID =?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, UserID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                txtEmail.setText(resultSet.getString("Email"));
                txtUserName.setText(resultSet.getString("Username"));
                txtFullName.setText(resultSet.getString("Fullname"));
                txtPhone.setText(resultSet.getString("Phone"));
                txtDes.setText(resultSet.getString("Description"));
            } else {
                JOptionPane.showMessageDialog(this, "User information not found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error getting user information:" + e.getMessage());
        }
    }

//    private void updateUserInfo() {
//        String email = txtEmail.getText();
//        String username = txtUserName.getText();
//        String fullname = txtFullName.getText();
//        String phone = txtPhone.getText();
//        String des = txtDes.getText();
//
//        if (email.isEmpty() || fullname.isEmpty() || phone.isEmpty() || des.isEmpty() || username.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please complete all information.");
//            return;
//        }
//
//        String currentUsername = "";
//        try {
//            String query = "SELECT * FROM users WHERE UserID = ?";
//            PreparedStatement pst = conn.prepareStatement(query);
//            pst.setInt(1, UserID);
//            ResultSet rs = pst.executeQuery();
//
//            if (rs.next()) {
//                currentUsername = rs.getString("Username");
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//        }
//
//        boolean usernameChanged = !username.equals(currentUsername);
//
//        try {
//            if (usernameChanged) {
//                String checkQuery = "SELECT COUNT(*) FROM users WHERE Username = ?";
//                PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
//                checkStatement.setString(1, username);
//                ResultSet resultSet = checkStatement.executeQuery();
//                resultSet.next();
//                int count = resultSet.getInt(1);
//
//                if (count > 0) {
//                    JOptionPane.showMessageDialog(this, "Username already exists in the database.");
//                    return;
//                }
//            }
//
//            String query = "UPDATE users SET Email=?, Username=?, Fullname=?, Phone=?, Description=? WHERE UserID =?";
//            PreparedStatement statement = conn.prepareStatement(query);
//            statement.setString(1, email);
//            statement.setString(2, username);
//            statement.setString(3, fullname);
//            statement.setString(4, phone);
//            statement.setString(5, des);
//            statement.setInt(6, UserID);
//
//            int rowsUpdated = statement.executeUpdate();
//            if (rowsUpdated > 0) {
//                JOptionPane.showMessageDialog(this, "User information has been updated successfully.");
//
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, "Error updating user information:" + ex.getMessage());
//        }
//    }
    private void updateUserInfo() {
        String email = txtEmail.getText();
        String username = txtUserName.getText();
        String fullname = txtFullName.getText();
        String phone = txtPhone.getText();
        String des = txtDes.getText();

        if (email.isEmpty() || fullname.isEmpty() || phone.isEmpty() || des.isEmpty() || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all information.");
            return;
        }

        // Kiểm tra định dạng số điện thoại
        if (!isValidPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number. Please enter 10 digits.");
            return;
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address.");
            return;
        }

        String currentUsername = "";
        try {
            String query = "SELECT * FROM users WHERE UserID = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, UserID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                currentUsername = rs.getString("Username");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        boolean usernameChanged = !username.equals(currentUsername);

        try {
            if (usernameChanged) {
                String checkQuery = "SELECT COUNT(*) FROM users WHERE Username = ?";
                PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
                checkStatement.setString(1, username);
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count > 0) {
                    JOptionPane.showMessageDialog(this, "Username already exists in the database.");
                    return;
                }
            }

            String query = "UPDATE users SET Email=?, Username=?, Fullname=?, Phone=?, Description=? WHERE UserID =?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, username);
            statement.setString(3, fullname);
            statement.setString(4, phone);
            statement.setString(5, des);
            statement.setInt(6, UserID);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "User information has been updated successfully.");

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating user information:" + ex.getMessage());
        }
    }
    
    // validate phone
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    // validate email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtDes = new javax.swing.JTextField();
        btnChangePass = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        txtPhone = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(1, 135, 126));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(1, 135, 126));
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 700));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/Image/logo.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Account");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-user-30.png"))); // NOI18N
        jLabel3.setText("Username");

        txtUserName.setBackground(new java.awt.Color(182, 221, 218));
        txtUserName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-password-30.png"))); // NOI18N
        jLabel4.setText("Description");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-name-30.png"))); // NOI18N
        jLabel5.setText("Full Name");

        txtFullName.setBackground(new java.awt.Color(182, 221, 218));
        txtFullName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-phone-30.png"))); // NOI18N
        jLabel6.setText("Phone Number");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-email-30.png"))); // NOI18N
        jLabel7.setText("Email");

        txtEmail.setBackground(new java.awt.Color(182, 221, 218));
        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        txtDes.setBackground(new java.awt.Color(182, 221, 218));
        txtDes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnChangePass.setBackground(new java.awt.Color(249, 214, 120));
        btnChangePass.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnChangePass.setForeground(new java.awt.Color(255, 255, 255));
        btnChangePass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-register-30.png"))); // NOI18N
        btnChangePass.setText("Change Password");
        btnChangePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePassActionPerformed(evt);
            }
        });

        btnBack.setBackground(new java.awt.Color(249, 214, 120));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-register-30.png"))); // NOI18N
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(249, 214, 120));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-register-30.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(249, 214, 120));
        btnReset.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/Icon/icons8-register-30.png"))); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        txtPhone.setBackground(new java.awt.Color(182, 221, 218));
        txtPhone.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUserName)
                            .addComponent(txtFullName)
                            .addComponent(txtEmail)
                            .addComponent(txtDes)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnChangePass)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBack)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(txtDes, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChangePass)
                    .addComponent(btnReset)
                    .addComponent(btnUpdate)
                    .addComponent(btnBack))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnChangePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePassActionPerformed
        // TODO add your handling code here:
        ChangePassword cp = new ChangePassword(Role, UserID);
        cp.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnChangePassActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        HomePage hp = new HomePage(Role, UserID);
        hp.setVisible(true);
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        updateUserInfo();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        displayUserInfo();
    }//GEN-LAST:event_btnResetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnChangePass;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtDes;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
