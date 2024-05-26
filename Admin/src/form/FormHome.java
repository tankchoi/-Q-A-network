package form;

import swing.Model_Card;
import java.sql.*;

public class FormHome extends javax.swing.JPanel {

    public FormHome() {
        initComponents();
        setOpaque(false);
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            Statement stmt = null;

            stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total_staffs FROM users WHERE role IN (0, 1)");
            rs.next();
            int totalStaffs = rs.getInt("total_staffs");

            rs = stmt.executeQuery("SELECT COUNT(*) AS total_users FROM users WHERE role = 2");
            rs.next();
            int totalUsers = rs.getInt("total_users");

            rs = stmt.executeQuery("SELECT COUNT(*) AS total_posts FROM posts");
            rs.next();
            int totalPosts = rs.getInt("total_posts");

            rs = stmt.executeQuery("SELECT COUNT(*) AS total_comments FROM comments");
            rs.next();
            int totalComments = rs.getInt("total_comments");

            rs.close();
            stmt.close();
            conn.close();
            card1.setData(new Model_Card("Staffs", "Total Staffs", totalStaffs));
            card4.setData(new Model_Card("Users", "Total Users",totalUsers));
            card2.setData(new Model_Card("Posts", "Total Posts", totalPosts));
            card3.setData(new Model_Card("Comments", "Total Comments", totalComments));
        } catch (SQLException se) {

            se.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        roundPanel1 = new swing.RoundPanel();
        jLabel1 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        card1 = new component.Card();
        card3 = new component.Card();
        card2 = new component.Card();
        card4 = new component.Card();
        roundPanel2 = new swing.RoundPanel();

        roundPanel1.setBackground(new java.awt.Color(60, 60, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Dashboard");

        jLayeredPane1.setLayout(new java.awt.GridLayout(1, 0, 10, 0));
        jLayeredPane1.add(card1);
        jLayeredPane1.add(card3);
        jLayeredPane1.add(card2);
        jLayeredPane1.add(card4);

        javax.swing.GroupLayout roundPanel1Layout = new javax.swing.GroupLayout(roundPanel1);
        roundPanel1.setLayout(roundPanel1Layout);
        roundPanel1Layout.setHorizontalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE))
                .addContainerGap())
        );
        roundPanel1Layout.setVerticalGroup(
            roundPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roundPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        roundPanel2.setBackground(new java.awt.Color(60, 60, 60));

        javax.swing.GroupLayout roundPanel2Layout = new javax.swing.GroupLayout(roundPanel2);
        roundPanel2.setLayout(roundPanel2Layout);
        roundPanel2Layout.setHorizontalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 825, Short.MAX_VALUE)
        );
        roundPanel2Layout.setVerticalGroup(
            roundPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(roundPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(roundPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(roundPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private component.Card card1;
    private component.Card card2;
    private component.Card card3;
    private component.Card card4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private swing.RoundPanel roundPanel1;
    private swing.RoundPanel roundPanel2;
    // End of variables declaration//GEN-END:variables
}
