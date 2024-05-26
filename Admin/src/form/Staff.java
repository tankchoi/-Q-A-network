package form;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class Staff extends javax.swing.JPanel {

    Connection conn = null;
    public int Role;

    public Staff(int Role) {
        this.Role = Role;
        initComponents();
        setOpaque(false);
        table1.addTableStyle(jScrollPane1);
        connectDB();
        displayData();

    }

    public int getUserRole() {
        return Role;
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

    private void displayData() {
        try {
            int userRole = getUserRole();

            String sql = null;

            if (userRole == 0) {
                sql = "SELECT * FROM users WHERE Role IN (0, 1)";
            }

            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                String roleString = rs.getInt("Role") == 0 ? "Manager" : "Staff";
                Object[] row = {
                    rs.getInt("UserID"),
                    rs.getString("Email"),
                    rs.getString("Username"),
                    rs.getString("Fullname"),
                    rs.getString("Phone"),
                    rs.getString("Description"),
                    roleString
                };
                model.addRow(row);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error displaying data: " + e.getMessage());
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

    private void addStaff() {
        try {
            String selectedRole = (String) jComboBox1.getSelectedItem();
            int role;
            if (selectedRole.equals("Manager")) {
                role = 0;
            } else {
                role = 1;
            }

            int userRole = getUserRole();
            if (userRole == 0) {
                String email = txtEmail.getText();
                String username = txtUserName.getText();
                String fullname = txtFullName.getText();
                String pass = txtPass.getText();
                String phone = txtPhone.getText();
                String des = txtDes.getText();

                if (email.isEmpty() || username.isEmpty() || fullname.isEmpty() || pass.isEmpty() || phone.isEmpty() || des.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please complete all information");
                } else {
                    String hashedPass = hashPasswordMD5(pass);

                    String sql = "INSERT INTO users (Email, Username, Fullname, Password, Phone, Description, Role) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, email);
                    statement.setString(2, username);
                    statement.setString(3, fullname);
                    statement.setString(4, hashedPass);
                    statement.setString(5, phone);
                    statement.setString(6, des);
                    statement.setInt(7, role);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Staff added successfully!");
                        txtEmail.setText("");
                        txtUserName.setText("");
                        txtFullName.setText("");
                        txtPass.setText("");
                        txtPhone.setText("");
                        txtDes.setText("");
                        displayData();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "You have no rights");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error when adding employee: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "Password encryption error: " + e.getMessage());
        }
    }

    //xóa nhân viên
    private void deleteStaff() {
        try {
            int selectedRow = table1.getSelectedRow(); // Lấy chỉ số của dòng được chọn

            if (selectedRow == -1) { // Kiểm tra xem người dùng đã chọn dòng nào chưa
                JOptionPane.showMessageDialog(this, "Please select a product to delete.");
                return;
            }

            int userID = (int) table1.getValueAt(selectedRow, 0); // Lấy ID của người dùng từ dòng được chọn

            // Cập nhật các bản ghi trong bảng categories có UserID liên kết với UserID của người dùng sắp xóa thành NULL
            String updateSql = "UPDATE categories SET UserID = NULL WHERE UserID = ?";
            PreparedStatement updateStatement = conn.prepareStatement(updateSql);
            updateStatement.setInt(1, userID);
            updateStatement.executeUpdate();
            updateStatement.close();

            // Tiếp tục xóa người dùng
            String deleteSql = "DELETE FROM users WHERE UserID = ?";
            PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
            deleteStatement.setInt(1, userID);

            // Thực thi câu lệnh
            int rowsDeleted = deleteStatement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
                displayData(); // Hiển thị lại dữ liệu sau khi xóa
            }

            deleteStatement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage());
        }
    }

    private void editStaff() {
        try {
            String selectedRole = (String) jComboBox1.getSelectedItem();
            int role;
            if (selectedRole.equals("Manager")) {
                role = 0;
            } else {
                role = 1;
            }

            int selectedRow = table1.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a staff to edit.");
                return;
            }

            int userID = (int) table1.getValueAt(selectedRow, 0);

            // Lấy dữ liệu từ các trường nhập liệu
            String email = txtEmail.getText();
            String username = txtUserName.getText();
            String fullname = txtFullName.getText();
            String phone = txtPhone.getText();
            String des = txtDes.getText();

            // Kiểm tra nếu một trong các ô input trống
            if (email.isEmpty() || username.isEmpty() || fullname.isEmpty() || phone.isEmpty() || des.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            // Thực hiện cập nhật thông tin nhân viên
            String sql = "UPDATE users SET Email = ?, Username = ?, Fullname = ?, Phone = ?, Description = ?, Role = ? WHERE UserID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, username);
            statement.setString(3, fullname);
            statement.setString(4, phone);
            statement.setString(5, des);
            statement.setInt(6, role);
            statement.setInt(7, userID);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Staff updated successfully!");
                displayData();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating Staff: " + e.getMessage());
        }
    }

    private void resetInputs() {
        txtEmail.setText("");
        txtUserName.setText("");
        txtFullName.setText("");
        txtPass.setText("");
        txtPhone.setText("");
        txtDes.setText("");
        txtPass.setEnabled(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel1 = new swing.RoundPanel();
        jLabel2 = new javax.swing.JLabel();
        lbEmail = new javax.swing.JLabel();
        lbUserName = new javax.swing.JLabel();
        lbFullName = new javax.swing.JLabel();
        lbPhone = new javax.swing.JLabel();
        lbPass = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtPass = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtFullName = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        lbDes = new javax.swing.JLabel();
        txtDes = new javax.swing.JTextField();
        btnDel = new javax.swing.JButton();
        btnadd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table1 = new swing.Table();
        jLabel3 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        btnReset = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        roundPanel1.setBackground(new java.awt.Color(60, 60, 60));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Update Staff");

        lbEmail.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbEmail.setForeground(new java.awt.Color(255, 255, 255));
        lbEmail.setText("Email:");

        lbUserName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbUserName.setForeground(new java.awt.Color(255, 255, 255));
        lbUserName.setText("Username:");

        lbFullName.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbFullName.setForeground(new java.awt.Color(255, 255, 255));
        lbFullName.setText("Fullname:");

        lbPhone.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbPhone.setForeground(new java.awt.Color(255, 255, 255));
        lbPhone.setText("Phone:");

        lbPass.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbPass.setForeground(new java.awt.Color(255, 255, 255));
        lbPass.setText("Password:");

        btnUpdate.setText("Update Staff");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        lbDes.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbDes.setForeground(new java.awt.Color(255, 255, 255));
        lbDes.setText("Description:");

        btnDel.setText("Delete Staff");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });

        btnadd.setText("Add New Staff");
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "User ID", "Email", "User Name", "Full Name", "Phone", "Description", "Role"
            }
        ));
        table1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Search");

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manager", "Staff" }));

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Staff Management");

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbFullName)
                            .addComponent(lbUserName)
                            .addComponent(lbEmail))
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(36, 36, 36)
                                        .addComponent(lbPass)))
                                .addGap(18, 18, 18)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPass)))
                            .addGroup(roundPanel1Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addComponent(btnadd)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnUpdate)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnDel)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnReset))
                                    .addGroup(roundPanel1Layout.createSequentialGroup()
                                        .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(36, 36, 36)
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lbDes)
                                            .addComponent(lbPhone))
                                        .addGap(4, 4, 4)
                                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtDes, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundPanel1Layout.createSequentialGroup()
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(roundPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPass)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbUserName)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbPhone)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbFullName)
                    .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbDes))
                .addGap(18, 18, 18)
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnadd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDel)
                    .addComponent(btnReset))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        // TODO add your handling code here:
        deleteStaff();
    }//GEN-LAST:event_btnDelActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        editStaff();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
        // TODO add your handling code here:
        addStaff();
    }//GEN-LAST:event_btnaddActionPerformed

    private void table1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table1MouseClicked

        // Lấy chỉ số của dòng được chọn
        int selectedRow = table1.getSelectedRow();

        // Kiểm tra xem có dòng nào được chọn không
        if (selectedRow >= 0) {
            // Lấy mô hình của bảng
            DefaultTableModel model = (DefaultTableModel) table1.getModel();

            txtEmail.setText(model.getValueAt(selectedRow, 1).toString());
            txtUserName.setText(model.getValueAt(selectedRow, 2).toString());
            txtFullName.setText(model.getValueAt(selectedRow, 3).toString());
            txtPhone.setText(model.getValueAt(selectedRow, 4).toString());
            txtDes.setText(model.getValueAt(selectedRow, 5).toString());
            txtPass.setEnabled(false);
            jComboBox1.setSelectedItem(model.getValueAt(selectedRow, 6).toString());
        } else {
            txtEmail.setText("");
            txtUserName.setText("");
            txtFullName.setText("");
            txtPass.setText("");
            txtPhone.setText("");
            txtDes.setText("");
        }
    }//GEN-LAST:event_table1MouseClicked

    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        try {
            int userRole = getUserRole();
            String sql = null;

            if (userRole == 0) {
                sql = "SELECT * FROM users WHERE Role IN (0, 1) AND (Email LIKE ? OR Username LIKE ? OR Fullname LIKE ?)";
            }

            PreparedStatement statement = conn.prepareStatement(sql);
            // Thiết lập tham số cho câu truy vấn tìm kiếm
            String searchTerm = "%" + txtSearch.getText() + "%";
            for (int i = 1; i <= 3; i++) {
                statement.setString(i, searchTerm);
            }

            ResultSet rs = statement.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                String roleString = rs.getInt("Role") == 0 ? "Manager" : "Staff";
                Object[] row = {
                    rs.getInt("UserID"),
                    rs.getString("Email"),
                    rs.getString("Username"),
                    rs.getString("Fullname"),
                    rs.getString("Phone"),
                    rs.getString("Description"),
                    roleString
                };
                model.addRow(row);
            }

            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error displaying data: " + e.getMessage());
        }
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        resetInputs();
    }//GEN-LAST:event_btnResetActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnadd;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbDes;
    private javax.swing.JLabel lbEmail;
    private javax.swing.JLabel lbFullName;
    private javax.swing.JLabel lbPass;
    private javax.swing.JLabel lbPhone;
    private javax.swing.JLabel lbUserName;
    private swing.RoundPanel roundPanel1;
    private swing.Table table1;
    private javax.swing.JTextField txtDes;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
