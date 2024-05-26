/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package form;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.swing.*;

public class ChangePassword extends javax.swing.JPanel {

    Connection conn = null;
    private int userID;

    public ChangePassword(int userID) {
        this.userID = userID;
        initComponents();
    }

    public int getUserID() {
        return userID;
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

    private String hashPasswordMD5(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : byteData) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel3 = new swing.RoundPanel();
        lbPass = new javax.swing.JLabel();
        lbUser = new javax.swing.JLabel();
        txtConfirm = new javax.swing.JPasswordField();
        txtPass = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        txtNewPass = new javax.swing.JPasswordField();
        btnXacNhan = new javax.swing.JButton();
        lbPass1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(51, 51, 51));

        roundPanel3.setBackground(new java.awt.Color(60, 60, 60));

        lbPass.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbPass.setForeground(new java.awt.Color(255, 255, 255));
        lbPass.setText("Confirm Password:");

        lbUser.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbUser.setForeground(new java.awt.Color(255, 255, 255));
        lbUser.setText("Confirm password:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Change Password");

        btnXacNhan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnXacNhan.setText(" Confirm");
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        lbPass1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lbPass1.setForeground(new java.awt.Color(255, 255, 255));
        lbPass1.setText("New Password:");

        javax.swing.GroupLayout roundPanel3Layout = new javax.swing.GroupLayout(roundPanel3);
        roundPanel3.setLayout(roundPanel3Layout);
        roundPanel3Layout.setHorizontalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(lbPass1)
                    .addGroup(roundPanel3Layout.createSequentialGroup()
                        .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbUser)
                            .addComponent(lbPass))
                        .addGap(18, 18, 18)
                        .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPass, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                            .addComponent(txtNewPass)
                            .addComponent(txtConfirm)
                            .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(403, Short.MAX_VALUE))
        );
        roundPanel3Layout.setVerticalGroup(
            roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbUser)
                    .addGroup(roundPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel3Layout.createSequentialGroup()
                        .addComponent(lbPass1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 21, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel3Layout.createSequentialGroup()
                        .addGap(0, 4, Short.MAX_VALUE)
                        .addComponent(txtNewPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)))
                .addGroup(roundPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbPass)
                    .addGroup(roundPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(txtConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(btnXacNhan)
                .addGap(398, 398, 398))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        String currentPassword = new String(txtPass.getPassword());
        String newPassword = new String(txtNewPass.getPassword());
        String confirmPassword = new String(txtConfirm.getPassword());

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New password and confirmation password do not match!");
            return; 
        }

        if (conn == null) {
            connectDB(); 
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Failed to connect to the database!");
                return; 
            }
        }

        try {
            String query = "SELECT Password FROM users WHERE UserID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String hashedCurrentPassword = hashPasswordMD5(currentPassword);
                String storedPassword = resultSet.getString("Password");

                if (!hashedCurrentPassword.equals(storedPassword)) {
                    JOptionPane.showMessageDialog(this, "Current password is incorrect!");
                    return; 
                }

                String hashedNewPassword = hashPasswordMD5(newPassword);
                String updateQuery = "UPDATE users SET Password = ? WHERE UserID = ?";
                PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                updateStatement.setString(1, hashedNewPassword);
                updateStatement.setInt(2, userID);
                int rowsAffected = updateStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Password changed successfully!");
                    txtPass.setText("");
                    txtNewPass.setText("");
                    txtConfirm.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot change password!");
                }
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(this, "Error! An error occurred. Please try again later: " + ex.getMessage());
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnXacNhanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lbPass;
    private javax.swing.JLabel lbPass1;
    private javax.swing.JLabel lbUser;
    private swing.RoundPanel roundPanel3;
    private javax.swing.JPasswordField txtConfirm;
    private javax.swing.JPasswordField txtNewPass;
    private javax.swing.JPasswordField txtPass;
    // End of variables declaration//GEN-END:variables
}
