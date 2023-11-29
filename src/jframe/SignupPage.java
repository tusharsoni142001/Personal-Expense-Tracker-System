/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package jframe;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
/**
 *
 * @author SONI
 */
public class SignupPage extends javax.swing.JFrame {

    /**
     * Creates new form SignupPage
     */
    public SignupPage() {
        initComponents();
        setResizable(false);
    }
    
    public void SignupPage ()
    {
        
    }
    
    //method to check if any field is empty.
    public boolean insertSignupDetailsValidate()
    {
        String uname = txt_username.getText();
        String email=txt_email.getText();
        String password=txt_confirmpassword.getText();
        String confirmpassword=txt_confirmpassword.getText();
        String age=txt_age.getText();
        String gender=txt_gender.getSelectedItem().toString();
        
        
        if(uname.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "User name field is empty");
                return false;
            }
        else if(email.isEmpty() || !email.matches("^.+@.+\\..+$"))
            {
                JOptionPane.showMessageDialog(this, "Please enter valid email id.");
                return false;
            }
        else if(password.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Password field is empty");
                return false;
            }
        else if(confirmpassword.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Confirm password field is empty");
                return false;
            }
        else if(age.isEmpty())
        {
                JOptionPane.showMessageDialog(this, "Age field is empty");
                return false;
        }
        else if(gender.isEmpty())
        {
                JOptionPane.showMessageDialog(this, "Gender field is empty");
                return false;
        }
        
        else
        {
            return true;
        }
    }
    
    // Method for email validation
    public boolean emailValidate()
    {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);
        String email=txt_email.getText();
        Matcher matcher = pattern.matcher(email);
        boolean matchFound = matcher.find();
        if(matchFound) {
            
           return true;
        } else 
        {
            JOptionPane.showMessageDialog(this, "Enter valid email id");
            return false;
        }
    }
    
    
    // Method for password validation
    public boolean passwordValidate()
    {
        Pattern pattern = Pattern.compile("^^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[A-Z]).{8,}$", Pattern.CASE_INSENSITIVE);
        String password=txt_confirmpassword.getText();
        Matcher matcher = pattern.matcher(password);
        boolean matchFound = matcher.find();
        if(matchFound) {
            
           return true;
        } else 
        {
            JOptionPane.showMessageDialog(this, "Password must contain :\nMinimum 8 characters \nAtleast 1 digit \nAtleast 1 special Character \nAtleast 1 Capital case letter");
            return false;
        }
    }
    
    //Method for age validation
    public boolean agevalidate()
    {
        Pattern pattern = Pattern.compile("^-?\\d+$", Pattern.CASE_INSENSITIVE);
        String age=txt_age.getText();
        
        Matcher matcher = pattern.matcher(age);
        boolean matchFound = matcher.find();
        if(matchFound) {
            if(Integer.parseInt(age)>=18)
            {
           return true;
            }
            else{
                JOptionPane.showMessageDialog(this, "User should be 18 years old");
            return false;
            }
            
        } else 
        {
            JOptionPane.showMessageDialog(this, "Enter valid age");
            return false;
        }
    }
    
    //method to insert values in to user table
    public void insertSignupDetails()
    {
        String uname = txt_username.getText();
        String email=txt_email.getText();
        String password=txt_password.getText();
        String confirmpassword=txt_confirmpassword.getText();
        String age=txt_age.getText();
        String gender=txt_gender.getSelectedItem().toString();
        
        
        
        //Check for confirm passowrd
        if(password.equals(confirmpassword))
        {
           
                //Database Connectivity
                try {
                    Connection con= DBConnection.getConnection();
                    String sql="insert into user(name,email,password,age,gender)values(?,?,?,?,?)";
                    PreparedStatement pst=con.prepareStatement(sql);

                    pst.setString(1,uname);
                    pst.setString(2,email);
                    pst.setString(3,password);
                    pst.setString(4,age);
                    pst.setString(5,gender);
                  


                    int updatedRowCount=pst.executeUpdate();

                    if(updatedRowCount>0)
                    {
                        JOptionPane.showMessageDialog(this,"Registered Successfully");
                        LoginPage login=new LoginPage();
                        login.setVisible(true);
                        dispose();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this,"Registeration Failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }  
           
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Confirm password does not match. Try again...");
        }
       
    }

    
    //check dupllicate user
    public boolean checkDuplicateUser()
    {
        String email=txt_email.getText();
        boolean isExist=false;
        
         try {
                    Connection con= DBConnection.getConnection();
                    String sql="select * from user where email=?";
                    PreparedStatement pst=con.prepareStatement(sql);

                    pst.setString(1,email);
                    
                    ResultSet rs=pst.executeQuery();
                    
                    if(rs.next())
                    {
                        JOptionPane.showMessageDialog(this, "User already exist");
                        isExist=false;
                    }else{
                        isExist=true;
                    }
         }catch (Exception e) {
                    e.printStackTrace();
                }  
         
         return isExist;
        
    }
    
    
    //Methodto add categorie in category table
    public void addOnlineCategory()
    {
        
        try {
                String email=txt_email.getText();
                int user_id=0;
                Connection con= DBConnection.getConnection();
                String sql="select userID from user where email = ?";
                PreparedStatement pst=con.prepareStatement(sql);
                
                pst.setString(1,email);
                
                
                ResultSet rs=pst.executeQuery();
                if(rs.next())
                {
                    user_id=rs.getInt("userID");
                    
                }
                
                
                String sql1="insert into category(catname,user_id)values(?,?)";
                        PreparedStatement pst1=con.prepareStatement(sql1);


                        //Online Category
                        pst1.setString(1,"Online");
                        pst1.setInt(2,user_id);
                        pst1.executeUpdate();
                        
                        //Groceries Category
                        pst1.setString(1,"Groceries");
                        pst1.setInt(2,user_id);
                        pst1.executeUpdate();
                        
                        //Bills Category
                        pst1.setString(1,"Bills");
                        pst1.setInt(2,user_id);
                        pst1.executeUpdate();
                        
                        //Fuel Category
                        pst1.setString(1,"Fuel");
                        pst1.setInt(2,user_id);
                        pst1.executeUpdate();
                        
                        //Health Category
                        pst1.setString(1,"Health");
                        pst1.setInt(2,user_id);
                        pst1.executeUpdate();
                        
                        //Shopping Category
                        pst1.setString(1,"Shopping");
                        pst1.setInt(2,user_id);
                        pst1.executeUpdate();
                        
                        //Travel Category
                        pst1.setString(1,"Travel");
                        pst1.setInt(2,user_id);
                        pst1.executeUpdate();
                        
                        //Other Category
                        pst1.setString(1,"Other");
                        pst1.setInt(2,user_id);
                        pst1.executeUpdate();
                        
                        //Entertainment Category
                        pst1.setString(1,"Entertainment");
                        pst1.setInt(2,user_id);
                        pst1.executeUpdate();

                        
                
                
                    
                    
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_username = new app.bolivia.swing.JCTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_email = new app.bolivia.swing.JCTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_age = new app.bolivia.swing.JCTextField();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle3 = new rojerusan.RSMaterialButtonCircle();
        txt_gender = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        txt_confirmpassword = new javax.swing.JPasswordField();
        jCheckBox2 = new javax.swing.JCheckBox();
        txt_password = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 23)); // NOI18N
        jLabel2.setText("Registration");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 16)); // NOI18N
        jLabel3.setText("Create New Account...");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, -1, -1));

        txt_username.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(59, 195, 41)));
        txt_username.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_username.setPlaceholder("Enter Username...");
        txt_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_usernameActionPerformed(evt);
            }
        });
        jPanel1.add(txt_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 330, -1));

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/name.png"))); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, -1, -1));

        jLabel5.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel5.setText("Full Name");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 80, -1, -1));

        jLabel6.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/mail.png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, -1, -1));

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel7.setText("Email ID");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 160, -1, -1));

        txt_email.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(59, 195, 41)));
        txt_email.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_email.setPlaceholder("Enter email id...");
        txt_email.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_emailFocusLost(evt);
            }
        });
        jPanel1.add(txt_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 330, -1));

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/password.png"))); // NOI18N
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, -1, -1));

        jLabel9.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel9.setText("Password");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 240, -1, -1));

        jLabel10.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/confirm.png"))); // NOI18N
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 320, -1, -1));

        jLabel11.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel11.setText("Confirm Password");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 320, -1, -1));

        jLabel12.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/age.png"))); // NOI18N
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 394, 30, 30));

        jLabel13.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel13.setText("Age");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 400, -1, -1));

        jLabel14.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/genders.png"))); // NOI18N
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 480, -1, -1));

        jLabel15.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel15.setText("Gender");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 480, -1, -1));

        txt_age.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(59, 195, 41)));
        txt_age.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_age.setPlaceholder("Enter Age...");
        jPanel1.add(txt_age, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 430, 330, -1));

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(255, 255, 255));
        rSMaterialButtonCircle2.setForeground(new java.awt.Color(0, 0, 0));
        rSMaterialButtonCircle2.setText("Back to Login");
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 740, 140, 40));

        rSMaterialButtonCircle3.setBackground(new java.awt.Color(59, 195, 41));
        rSMaterialButtonCircle3.setText("SIGNUP");
        rSMaterialButtonCircle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle3ActionPerformed(evt);
            }
        });
        jPanel1.add(rSMaterialButtonCircle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 590, 280, 60));

        txt_gender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        jPanel1.add(txt_gender, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 510, 330, 33));

        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 358, -1, -1));

        txt_confirmpassword.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(59, 195, 41)));
        txt_confirmpassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_confirmpasswordActionPerformed(evt);
            }
        });
        jPanel1.add(txt_confirmpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, 330, 33));

        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 277, -1, -1));

        txt_password.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(59, 195, 41)));
        txt_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_passwordActionPerformed(evt);
            }
        });
        jPanel1.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, 330, 33));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 490, 750));

        setSize(new java.awt.Dimension(506, 728));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_usernameActionPerformed

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed
        if(insertSignupDetailsValidate())
        {
            if(emailValidate() && passwordValidate() && agevalidate() && checkDuplicateUser())
            {
               
                insertSignupDetails();
                addOnlineCategory();
            }
        }
    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    private void txt_emailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_emailFocusLost
     
    }//GEN-LAST:event_txt_emailFocusLost

    private void txt_confirmpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_confirmpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_confirmpasswordActionPerformed

    private void txt_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_passwordActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(jCheckBox1.isSelected())
        {
            txt_confirmpassword.setEchoChar((char)0);
        }else
        {
            txt_confirmpassword.setEchoChar('*');
        }

    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
       
               if(jCheckBox2.isSelected())
        {
            txt_password.setEchoChar((char)0);
        }else
        {
            txt_password.setEchoChar('*');
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

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
            java.util.logging.Logger.getLogger(SignupPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignupPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignupPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignupPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignupPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private app.bolivia.swing.JCTextField txt_age;
    private javax.swing.JPasswordField txt_confirmpassword;
    private app.bolivia.swing.JCTextField txt_email;
    private javax.swing.JComboBox<String> txt_gender;
    private javax.swing.JPasswordField txt_password;
    private app.bolivia.swing.JCTextField txt_username;
    // End of variables declaration//GEN-END:variables
}
