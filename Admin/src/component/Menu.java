package component;

import net.miginfocom.swing.MigLayout;
import swing.MenuButton;
import javax.swing.ImageIcon;
import java.awt.Font;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import event.MenuEvent;
import java.awt.Component;
import swing.Split;
import javax.swing.JLabel;
import java.sql.*;
public class Menu extends javax.swing.JPanel {

    public int role;
    private Animator animator;
    private MenuButton selectedMenu;
    private MenuButton unSelectedMenu;
    private MenuEvent event;

    public Menu(int Role, int userID) {
        this.role = Role;
        initComponents();
        setOpaque(false);
        scroll.setViewportBorder(null);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        panelMenu.setLayout(new MigLayout("wrap, fillx, inset 0", "[fill]"));
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                selectedMenu.setAnimate(fraction);
                if (unSelectedMenu != null) {
                    unSelectedMenu.setAnimate(1f - fraction);
                }
            }
        };
        try {  
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "root", "");
            String sqlQuery = "SELECT username FROM users WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                jLabel1.setText("Hello " + username + " !");
            } else {
                
            }

            
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        animator = new Animator(300, target);
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        animator.setResolution(0);
    }

    public void initMenu(MenuEvent event) {
        this.event = event;
        addMenu("1", "Dashboard", 0);
        addMenu("3", "Accounting", 3);
        addMenu("4", "Categories", 4);
        if (role == 0) {

            addMenu("2", "Staff", 1);
            addMenu("6", "Approve Category", 2);

            addMenu("5", "Posts", 5);
        }
        if (role == 1) {
            addMenu("5", "Posts", 5);
        }

        addMenu("7", "Change Password", 6);
        space();
        addMenu("key", "Logout", 7);
    }

    public void addMenu(String icon, String text, int index) {
        MenuButton menu = new MenuButton(index);
        setFont(menu.getFont().deriveFont(Font.PLAIN, 14));
        menu.setIcon(new ImageIcon(getClass().getResource("/icon/" + icon + ".png")));
        menu.setText("  " + text);
        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!animator.isRunning()) {
                    if (menu != selectedMenu) {
                        unSelectedMenu = selectedMenu;
                        selectedMenu = menu;
                        animator.start();
                        event.menuSelected(index);
                    }
                }
            }
        });
        panelMenu.add(menu);
    }

    public void setSelected(int index) {
        for (Component com : panelMenu.getComponents()) {
            MenuButton menu = (MenuButton) com;
            if (menu.getIndex() == index) {
                if (menu != selectedMenu) {
                    unSelectedMenu = selectedMenu;
                    selectedMenu = menu;
                    animator.start();
                    event.menuSelected(index);
                }
                break;
            }
        }
    }

    private void split(String name) {
        panelMenu.add(new Split(name));
    }

    private void space() {
        panelMenu.add(new JLabel(), "push");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        image = new swing.ImageAvatar();
        scroll = new javax.swing.JScrollPane();
        panelMenu = new javax.swing.JPanel();

        setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 22)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(242, 242, 242));
        jLabel1.setText("Admin");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 30, 1, 1));
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel1.setName(""); // NOI18N

        image.setBorderSize(3);
        image.setBorderSpace(2);
        image.setGradientColor1(new java.awt.Color(255, 255, 255));
        image.setGradientColor2(new java.awt.Color(255, 255, 255));
        image.setImage(new javax.swing.ImageIcon(getClass().getResource("/icon/profile.jpg"))); // NOI18N

        panelMenu.setOpaque(false);

        javax.swing.GroupLayout panelMenuLayout = new javax.swing.GroupLayout(panelMenu);
        panelMenu.setLayout(panelMenuLayout);
        panelMenuLayout.setHorizontalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 208, Short.MAX_VALUE)
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 348, Short.MAX_VALUE)
        );

        scroll.setViewportView(panelMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(image, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private swing.ImageAvatar image;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JScrollPane scroll;
    // End of variables declaration//GEN-END:variables
}
