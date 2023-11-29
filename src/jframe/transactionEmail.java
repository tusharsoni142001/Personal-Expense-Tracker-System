/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import com.mysql.cj.callback.UsernameCallback;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SONI
 */
public class transactionEmail extends javax.swing.JFrame {

    /**
     * Creates new form tansactionEmail
     */
    public transactionEmail() {
        initComponents();
        setResizable(false);
    }

     //method to check if any field is empty.
    public boolean insertEmailDetailsValidate()
    {
        String email=txt_email.getText();
        String password=txt_password.getText();
        
        
        if(email.isEmpty() || !email.matches("^.+@.+\\..+$"))
            {
                JOptionPane.showMessageDialog(this, "Please enter valid email id.");
                return false;
            }
        else if(password.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Password field is empty");
                return false;
            }
        
        else
        {
            return true;
        }
    }
    
    
    public void addEmail()
    {
        String email=txt_email.getText();
        String password=txt_password.getText();
        
        
        try {
                Connection con= DBConnection.getConnection();
                String sql="UPDATE user SET transac_email = ?, transac_pass = ? WHERE (userID = ?);";
                PreparedStatement pst=con.prepareStatement(sql);
                
              
                pst.setString(1,email);
                pst.setString(2,password);
                pst.setInt(3,HomePage.ID);
               
                
               
                
                int updatedRowCount=pst.executeUpdate();

                if(updatedRowCount>0)
                {
                    JOptionPane.showMessageDialog(this,"Fetching transactions from your email. Stand by!");
                    
                    
                }
                else
                {
                    JOptionPane.showMessageDialog(this,"Somethnig went wrong");
                }
                    
                    
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    //Method to fetch transaction from email
    
    //String username= "tusharsoni142001@gmail.com";  
    //String password= "ctxrzwqopkyrdgvu";
    
    
  public void receiveMail() {
      String username=null;
      String password=null;
      //fetch email and password from database
      try {
          
                Connection con= DBConnection.getConnection();
                //System.out.println(HomePage.ID);
                String sql="select transac_email,transac_pass from user where userID = "+HomePage.ID;
                Statement st=con.createStatement();
                ResultSet rs=st.executeQuery(sql);
                
               
                while(rs.next())
                {
                    username=rs.getString("transac_email");
                    password=rs.getString("transac_passw");
                    
                }
                    
             }catch (Exception e) {
            System.out.println(e);
        }
      
      
  try {
      if(username ==null)
      {
          
      }
      else
      {
          
    Properties properties = new Properties();
    properties.setProperty("mail.store.protocol", "imaps");

    Session emailSession = Session.getDefaultInstance(properties);
    Store emailStore = emailSession.getStore("imaps");
    emailStore.connect("imap.gmail.com", username, password);

    // getting inbox folder
    Folder emailFolder = emailStore.getFolder("INBOX");
    emailFolder.open(Folder.READ_ONLY);

    Message messages[] = emailFolder.getMessages();
    for (int i = messages.length - 1; i < messages.length; i++) {
      Message message = messages[i];
      System.out.println("Email Number: " + (i + 1));
      System.out.println("Subject: " + message.getSubject());
      System.out.println("From: " + message.getFrom()[0]);
      System.out.println("Sent date: " + message.getSentDate());

      Object content = message.getContent();
      if (content instanceof String) {
         
        String text = (String) content;
        // Extract required information using regular expressions
        String dateRegex = "Date : ([\\d/]+)";
        String amountRegex = "Amount : ₹([\\d.]+)";
        String txnIdRegex = "Transaction ID : ([\\w\\d]+)";
        Pattern datePattern = Pattern.compile(dateRegex);
        Pattern amountPattern = Pattern.compile(amountRegex);
        Pattern txnIdPattern = Pattern.compile(txnIdRegex);
        Matcher dateMatcher = datePattern.matcher(text);
        Matcher amountMatcher = amountPattern.matcher(text);
        Matcher txnIdMatcher = txnIdPattern.matcher(text);
        String date = null;
        String amount = null;
        String txnId = null;
        if (dateMatcher.find()) {
          date = dateMatcher.group(1);
        }
        if (amountMatcher.find()) {
          amount = amountMatcher.group(1);
        }
        if (txnIdMatcher.find()) {
          txnId = txnIdMatcher.group(1);
        }
        System.out.println("Date: " + date);
        System.out.println("Amount: " + amount);
        System.out.println("Transaction ID: " + txnId);
        
      } 
      else if (content instanceof MimeMultipart) {
          
        
    // Handle HTML content
    MimeMultipart multipart = (MimeMultipart) content;
    for (int j = 0; j < multipart.getCount(); j++) {
        BodyPart bodyPart = multipart.getBodyPart(j);
        if (bodyPart.getContent() instanceof String) {
            String html = (String) bodyPart.getContent();
            
            System.out.println("printing cotent");
            
            System.out.println("Content: " + html);
            // Extract required information from HTML content using regular expressions
            String dateRegex = "([A-Z][a-z]{2} \\d{1,2}, \\d{4})";
            String amountRegex = "\\₹\\s*(\\d+)";
            String txnIdRegex = "Txn\\.\\s*ID\\s*:\\s*(\\w+)";
            String descRegex = "^Paid to\\s+(\\S.*)$";
            
            Pattern datePattern = Pattern.compile(dateRegex);
            Pattern amountPattern = Pattern.compile(amountRegex);
            Pattern txnIdPattern = Pattern.compile(txnIdRegex);
            Pattern descPattern = Pattern.compile(descRegex);
            
            Matcher dateMatcher = datePattern.matcher(html);
            Matcher amountMatcher = amountPattern.matcher(html);
            Matcher txnIdMatcher = txnIdPattern.matcher(html);
            Matcher descMatcher = descPattern.matcher(html);
            
            String date = null;
            String amount = null;
            String txnId = null;
            String desc=null;
            
            if (dateMatcher.find()) {
                System.out.println("date true");
                date = dateMatcher.group();
            }
            if (amountMatcher.find()) {
                System.out.println("amount true");
                amount = amountMatcher.group();
            }
            if (txnIdMatcher.find()) {
                System.out.println("transaction true");
                txnId = txnIdMatcher.group(1);
            }
            if (descMatcher.matches()) {
                System.out.println("desc true");
                desc = descMatcher.group();
            }
            System.out.println("Date: " + date);
            System.out.println("Amount: " + amount);
            System.out.println("Transaction ID: " + txnId);
            System.out.println("Desc " + message.getSubject());
            
        }
    }
}

    }

    // Close the folder and store
    emailFolder.close(false);
    emailStore.close();
  }} catch (Exception e) {
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

        parentPanel = new javax.swing.JPanel();
        emailTransactionSelection = new javax.swing.JPanel();
        rSMaterialButtonCircle1 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        emailDetails = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_email = new app.bolivia.swing.JCTextField();
        jLabel6 = new javax.swing.JLabel();
        rSMaterialButtonCircle3 = new rojerusan.RSMaterialButtonCircle();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();
        jLabel12 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        parentPanel.setBackground(new java.awt.Color(255, 255, 255));
        parentPanel.setLayout(new java.awt.CardLayout());

        emailTransactionSelection.setBackground(new java.awt.Color(204, 204, 255));

        rSMaterialButtonCircle1.setText("No");
        rSMaterialButtonCircle1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle1ActionPerformed(evt);
            }
        });

        rSMaterialButtonCircle2.setText("Yes");
        rSMaterialButtonCircle2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Do you want to import transactions from your email?  ");

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 1, 25)); // NOI18N
        jLabel7.setText("Import transaction from email");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Simply  provide us with access to your email and we'll fetch ");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("the transactions for you. It's quick, easy, and secure. Say yes ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("to never miss a transaction again!\"");

        javax.swing.GroupLayout emailTransactionSelectionLayout = new javax.swing.GroupLayout(emailTransactionSelection);
        emailTransactionSelection.setLayout(emailTransactionSelectionLayout);
        emailTransactionSelectionLayout.setHorizontalGroup(
            emailTransactionSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(emailTransactionSelectionLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailTransactionSelectionLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(emailTransactionSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailTransactionSelectionLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(107, 107, 107))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailTransactionSelectionLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(73, 73, 73))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailTransactionSelectionLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailTransactionSelectionLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailTransactionSelectionLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        emailTransactionSelectionLayout.setVerticalGroup(
            emailTransactionSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(emailTransactionSelectionLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(emailTransactionSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        parentPanel.add(emailTransactionSelection, "card3");

        emailDetails.setBackground(new java.awt.Color(204, 204, 204));

        jLabel8.setFont(new java.awt.Font("Segoe UI Semibold", 1, 25)); // NOI18N
        jLabel8.setText("Email details");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Email id:");

        txt_email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txt_email.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_email.setPlaceholder("Enter email id");
        txt_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Password");

        rSMaterialButtonCircle3.setText("submit");
        rSMaterialButtonCircle3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rSMaterialButtonCircle3.setFocusPainted(false);
        rSMaterialButtonCircle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle3ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 2, 17)); // NOI18N
        jLabel9.setText("Your security is our priority. To fetch your transactions,");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 2, 17)); // NOI18N
        jLabel10.setText("we use a secure server and require your email and password.");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 2, 17)); // NOI18N
        jLabel11.setText("Your information is kept safe and confidential. Trust us to ");

        txt_password.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txt_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_passwordActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 2, 17)); // NOI18N
        jLabel12.setText("protect your data.");

        jCheckBox1.setText("Show password");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout emailDetailsLayout = new javax.swing.GroupLayout(emailDetails);
        emailDetails.setLayout(emailDetailsLayout);
        emailDetailsLayout.setHorizontalGroup(
            emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(emailDetailsLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailDetailsLayout.createSequentialGroup()
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(128, 128, 128))
                        .addComponent(jLabel10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailDetailsLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailDetailsLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(15, 15, 15)))
                .addContainerGap(31, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailDetailsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailDetailsLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(174, 174, 174))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailDetailsLayout.createSequentialGroup()
                        .addComponent(rSMaterialButtonCircle3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(161, 161, 161))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, emailDetailsLayout.createSequentialGroup()
                        .addGroup(emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addGap(34, 34, 34)
                        .addGroup(emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addGroup(emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_email, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                                .addComponent(txt_password)))
                        .addGap(116, 116, 116))))
        );
        emailDetailsLayout.setVerticalGroup(
            emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(emailDetailsLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(emailDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(rSMaterialButtonCircle3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        parentPanel.add(emailDetails, "card2");

        getContentPane().add(parentPanel, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rSMaterialButtonCircle2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2ActionPerformed
        parentPanel.removeAll();
        parentPanel.add(emailDetails);
        parentPanel.repaint();
        parentPanel.revalidate();
    }//GEN-LAST:event_rSMaterialButtonCircle2ActionPerformed

    private void rSMaterialButtonCircle1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_rSMaterialButtonCircle1ActionPerformed

    private void txt_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailActionPerformed

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed
        if(insertEmailDetailsValidate())
        {
                addEmail();
                this.dispose();
        }
    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    private void txt_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_passwordActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(jCheckBox1.isSelected())
        {
            txt_password.setEchoChar((char)0);
        }else
        {
            txt_password.setEchoChar('*');
        }

    }//GEN-LAST:event_jCheckBox1ActionPerformed

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
            java.util.logging.Logger.getLogger(transactionEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(transactionEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(transactionEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(transactionEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new transactionEmail().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel emailDetails;
    private javax.swing.JPanel emailTransactionSelection;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel parentPanel;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private app.bolivia.swing.JCTextField txt_email;
    private javax.swing.JPasswordField txt_password;
    // End of variables declaration//GEN-END:variables
}
