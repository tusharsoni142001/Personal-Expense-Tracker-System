/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;  
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javaswingdev.drawer.Drawer;
import javaswingdev.drawer.DrawerController;
import javaswingdev.drawer.DrawerItem;
import javaswingdev.drawer.EventDrawer;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


/**
 *
 * @author SONI
 */
public class AddCategory extends javax.swing.JFrame {

    private DrawerController drawer;
    int navigationChoice;
    /**
     * Creates new form Homepage
     */
    public AddCategory() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
       setCategoryDetailsToTable();
       
        drawer=Drawer.newDrawer(this)
                .header(new Header())
                .space(5)
                
               
                .drawerWidth(300)   //Width
                //.duration(300)
                
                .addChild(new DrawerItem("Home").icon(new ImageIcon(getClass().getResource("/icons/home2.png "))).build())
                .addChild(new DrawerItem("Transactions").icon(new ImageIcon(getClass().getResource("/icons/transaction.png "))).build())
                .addChild(new DrawerItem("Budget").icon(new ImageIcon(getClass().getResource("/icons/budgeting.png "))).build())
                .addChild(new DrawerItem("Category").icon(new ImageIcon(getClass().getResource("/icons/category.png "))).build())
                .addChild(new DrawerItem("Transactions history").icon(new ImageIcon(getClass().getResource("/icons/history1.png "))).build())
                .addChild(new DrawerItem("Reports").icon(new ImageIcon(getClass().getResource("/icons/report.png "))).build())
                
                .addChild(new DrawerItem("Import from Email").icon(new ImageIcon(getClass().getResource("/icons/import.png "))).build())
                
                
                .addFooter(new DrawerItem("Exit").icon(new ImageIcon(getClass().getResource("/icons/exit.png "))).build())
                
                .event(new EventDrawer(){
                    @Override
                    public void selected(int i, DrawerItem di){
                        System.out.println(i);
                        
                            navigationChoice=i;
                            changeFrame(navigationChoice);
                        
                    }
                })
                .build();
       }
    
    public void changeFrame(int navigationChoice)
    {
        switch (navigationChoice) {
        //Home
            case 0:
                HomePage home=new HomePage();
                home.setVisible(true);
                this.dispose();
                break;
        //Add Transaction
            case 1:
                AddTransaction transaction=new AddTransaction();
                transaction.setVisible(true);
                this.dispose();
                break;
        //Set Budget
            case 2:
                SetBudget budget=new SetBudget();
                budget.setVisible(true);
                this.dispose();
                break;
        //Add Category
            case 3:
                
               
                break;
        //View Transaction
            case 4:
                ViewTransactions viewtransactions=new ViewTransactions();
                viewtransactions.setVisible(true);
                this.dispose();
                break;
        //Reports
            case 5:
                Reports report=new Reports();
                report.setVisible(true);
                this.dispose();
                break;
        //Import from Email
            case 6:
                localflag=1;
                    transactionFromEmail();
                break;
                
        //Logout
            case 7:
                HomePage.ID=0;
                HomePage.name=null;
                System.exit(1);
                break;
            default:
                break;
        }
    }
    
    
    
     int localflag=0;
     int emailConnectionFlag=0;
     //method to ask for email id and password to add transactions from email
     public void transactionFromEmail()
    {
        try {
                Connection con= DBConnection.getConnection();
                 String sql="select transac_email,transac_pass from user where userID=?";
                 
                 PreparedStatement pst=con.prepareStatement(sql);
                 
                 String username;  
                 String password;
                 pst.setInt(1, HomePage.ID);
                 
                 ResultSet rs=pst.executeQuery();
                 
                 /*if(rs.next())
                 {
                     System.out.println("true"+rs.getString("trnsac_email"));
                 }*/
                 System.out.println(HomePage.Flag);
                if(rs.next())
                 {  
                     username=rs.getString("transac_email");
                     password=rs.getString("transac_pass");
                     
                     if((rs.getString("transac_email")==null && HomePage.Flag==0) ||(rs.getString("transac_email")==null)&&localflag==1 )
                     {
                        System.out.println(rs.getString("transac_email"));
                        transactionEmail emailChoice=new transactionEmail();
                        emailChoice.setVisible(true);
                        HomePage.Flag=1;
                        localflag=0;
                     }else
                    {
                        
                        
                        receiveMail(username, password);
                        if(emailConnectionFlag==1)
                        {
                            JOptionPane.showMessageDialog(this,"Something went wrong");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this,"Transactions imported Successfully.");
                        }
                    }
                     
                 }
                
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }
     
    //Method to validate empty fields  ||  method to check if any field is empty.
    public boolean validatecategory()
    {
        String cat=txt_category.getText();
        
        
        if(cat.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Please enter category");
            return false;
        } 
              
        
        return true;
        
    }
    
    
    //Methd to enter transaction details in database
    public void addCategory()
    {
        String cat=txt_category.getText();
        
        try {
                Connection con= DBConnection.getConnection();
                 String sql="select catname from category where catname=? and user_id=?";
                 
                 PreparedStatement pst=con.prepareStatement(sql);
                 
                 pst.setString(1, cat);
                 pst.setInt(2, HomePage.ID);
                 
                 ResultSet rs=pst.executeQuery();
                 
                 if(rs.next())
                 {
                     JOptionPane.showMessageDialog(this,"Category already exist");
                 }
                 
                else
                 {

                
                        String sql1="insert into category(catname,user_id)values(?,?)";
                        PreparedStatement pst1=con.prepareStatement(sql1);


                        pst1.setString(1,cat);
                        pst1.setInt(2,HomePage.ID);


                        int updatedRowCount=pst1.executeUpdate();

                        if(updatedRowCount>0)
                        {
                            JOptionPane.showMessageDialog(this,"Category added successful");


                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this,"Failed");
                        }
                }  
                    
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    //Method to update category details in database
    public void updateCategory()
    {
        String cat=txt_category.getText();
        
       
        try {
                 Connection con= DBConnection.getConnection();
                 String sql="select catname from category where catname=? and user_id=?";
                 
                 PreparedStatement pst=con.prepareStatement(sql);
                 
                 pst.setString(1, cat);
                 pst.setInt(2, HomePage.ID);
                 
                 ResultSet rs=pst.executeQuery();
                 
                 if(rs.next())
                 {
                     JOptionPane.showMessageDialog(this,"Category already exist");
                 }
                 
                else
                 {
                
                String sql1="update category set catname=? where catname=? and user_id=?";
                PreparedStatement pst1=con.prepareStatement(sql1);
                
                
                pst1.setString(1,cat);
                pst1.setString(2,cat_name);
                pst1.setInt(3,HomePage.ID);
                
               
                
                int updatedRowCount=pst1.executeUpdate();
                
                if(updatedRowCount>0)
                {
                    JOptionPane.showMessageDialog(this,"Category updated successful");
                    System.out.println(catid);
                    
                }
                else
                {
                    JOptionPane.showMessageDialog(this,"Category not updated");
                }
                 }  
                    
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    //Methd to enter transaction details in database
    public void deleteCategory()
    {
        String cat=txt_category.getText();
        
        try {
                Connection con= DBConnection.getConnection();
                 String sql="delete from category where catname=? and user_id=?";
                 
                 PreparedStatement pst=con.prepareStatement(sql);
                 
                 pst.setString(1, cat);
                 pst.setInt(2, HomePage.ID);
                 
                 int updatedRowCount=pst.executeUpdate();

                        if(updatedRowCount>0)
                        {
                            JOptionPane.showMessageDialog(this,"Category deleted successful");


                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this,"No category deleted");
                        }
                    
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    //Fetching data from database into tables
    int tid=1,catid;
    String cat_name;
    //String paymentmode,transactionid,desc;
    DefaultTableModel model;
    
    public void setCategoryDetailsToTable()
    {
        
        try {
                Connection con= DBConnection.getConnection();
                //System.out.println(HomePage.ID);
                String sql="select catID,catname from category where user_id = +"+HomePage.ID;
                Statement st=con.createStatement();
                ResultSet rs=st.executeQuery(sql);
                
               
                while(rs.next())
                {
                    
                    String cat_name=rs.getString("catname");
                    catid=rs.getInt("catID");
                    
                    Object[] Obj={tid,cat_name};
                    model=(DefaultTableModel)categoryTable.getModel();
                    model.addRow(Obj);
                    tid++;
                }
                    
             }catch (Exception e) {
            System.out.println(e);
        }
        
    }
    public void clearTable()
    {
        DefaultTableModel model=(DefaultTableModel)categoryTable.getModel();
        model.setRowCount(0);
        tid=1;
    }
    
    
        //Method to fetch transaction from email
  public void receiveMail(String username, String password) {
  try {
    Properties properties = new Properties();
    properties.setProperty("mail.store.protocol", "imaps");

    Session emailSession = Session.getDefaultInstance(properties);
    Store emailStore = emailSession.getStore("imaps");
    emailStore.connect("imap.gmail.com", username, password);

    // getting inbox folder
    Folder emailFolder = emailStore.getFolder("INBOX");
    emailFolder.open(Folder.READ_ONLY);

    Message messages[] = emailFolder.getMessages();
    for (int i = messages.length -25 ; i < messages.length; i++) {
      Message message = messages[i];
      System.out.println("Email Number: " + (i + 1));
      System.out.println("Subject: " + message.getSubject());
      System.out.println("From: " + message.getFrom()[0]);
      System.out.println("Sent date: " + message.getSentDate());
      
      
      //Retriving sender email 
      String sentEmail = null,sentDate;
      if (message.getFrom()[0] instanceof InternetAddress) {
        sentEmail = ((InternetAddress) message.getFrom()[0]).getAddress();
        System.out.println("Email address: " + sentEmail);
    }
      
        //Retriving sent date in dd/mm/yyyy format
        java.util.Date sentDate1 = message.getSentDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = dateFormat.format(sentDate1);
        System.out.println("Sent date: " + date1);
        
        //Retriving sent time in HH:mm:ss format
        java.util.Date sentTime = message.getSentDate();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        String time = timeFormat.format(sentTime);
        System.out.println("Time: " + time);
        
        
        //To check if current date is within one year or not
        Calendar oneYearAgo = Calendar.getInstance();
        oneYearAgo.add(Calendar.YEAR, -1);
        Calendar sentDateCal = Calendar.getInstance();
        sentDateCal.setTime(sentDate1);
        
        
        //Checking if email is of type Sent or Received. It should be Sent to confirm a payment is made
        //by user. We are triming the string to get "Sent" sub string.
        String aa=message.getSubject();
        String[] parts = aa.split("\\s+");
        String sentPart = parts[0];
        
        System.out.println("Sent part of subject:" + sentPart);
        
        
        
        if ((sentDateCal.after(oneYearAgo) || sentDateCal.equals(oneYearAgo)) && ("noreply@phonepe.com".equals(sentEmail) && "Sent".equals(sentPart))){
            // Sent date is within the last one year and email is from phonePe

      //if(sentEmail=="noreply@phonepe.com")
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
    for (int j = 0; j < multipart.getCount()-1; j++) {
        BodyPart bodyPart = multipart.getBodyPart(j);
        if (bodyPart.getContent() instanceof String) {
            String html = (String) bodyPart.getContent();
            
            //System.out.println("printing cotent");
            
            //System.out.println("Content: " + html);
            // Extract required information from HTML content using regular expressions
            String dateRegex = "([A-Z][a-z]{2} \\d{1,2}, \\d{4})";
            String amountRegex = "\\₹\\s*(\\d+)";
            String txnIdRegex = "Txn\\.\\s*ID\\s*:\\s*(\\w+)";
            //String descRegex = "^Paid to\\s+(\\S.*)$";
            
            Pattern datePattern = Pattern.compile(dateRegex);
            Pattern amountPattern = Pattern.compile(amountRegex);
            Pattern txnIdPattern = Pattern.compile(txnIdRegex);
            //Pattern descPattern = Pattern.compile(descRegex);
            
            Matcher dateMatcher = datePattern.matcher(html);
            Matcher amountMatcher = amountPattern.matcher(html);
            Matcher txnIdMatcher = txnIdPattern.matcher(html);
            //Matcher descMatcher = descPattern.matcher(html);
            
            String date = null;
            String amount = null;
            String txnId = null;
            String desc=null;
            int cat_id=0;
            
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
            /*if (descMatcher.matches()) {
                System.out.println("desc true");
                desc = descMatcher.group();
            }*/
            
            
            //System.out.println("Date: " + date1);
            //String amount1 = amount.substring(1);
            

            //System.out.println("Amount: " + amount1);
            //System.out.println("Transaction ID: " + txnId);
            
            //System.out.println("Desc " + desc);
            
            double amount1 = Double.parseDouble(amount.substring(1));
            desc =  "Sent Rs." + message.getSubject().substring(6);
            String paymentMode1="UPI";
            
            
            //checing if transaction already exist in transaction table
            try {
                 Connection con= DBConnection.getConnection();
                 String sql="select * from transactions where transaction_id=? and user_id=?";
                 
                 PreparedStatement pst=con.prepareStatement(sql);
                 
                 pst.setString(1, txnId);
                 pst.setInt(2, HomePage.ID);
                 
                 ResultSet rs=pst.executeQuery();
                 
                 if(rs.next())
                 {
                     System.out.println("transaction exist");
                 }
                 
                else
                 {
                     System.out.println("transaction don't exist");
                     //Category ID
                     
                     Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
                     String sql2="select catID from category where user_id ="+HomePage.ID+" and catname ='Online'";
                ResultSet rs4 = st.executeQuery(sql2);
                     
                //PreparedStatement pst3=con.prepareStatement(sql2);
                
                //pst3.setInt(1,HomePage.ID);
                //pst3.setString(2,"Online");
                
                //ResultSet rs4=pst3.executeQuery();
                System.out.println("cat id sql query");
               
                
                while(rs4.next())
                {
                System.out.println("category ID successfull");
                    cat_id=rs4.getInt("catID");
                    System.out.println(cat_id);
                }
                /*while(rs4.next())
                {
                    System.out.println("category ID successfull");
                    cat_id=rs4.getInt(1);
                    
                }*/

                // inserting transactions into database
                try {
                //Connection con= DBConnection.getConnection();
                
                
                String sql1="insert into transactions(amount,date,description,paymentmode,transaction_id,cat_id,user_id)values(?,?,?,?,?,?,?)";
                PreparedStatement pst_insert=con.prepareStatement(sql1);
                
                pst_insert.setDouble(1,amount1);
                pst_insert.setString(2,date1);
                pst_insert.setString(3,desc);
                pst_insert.setString(4,paymentMode1);
                pst_insert.setString(5,txnId);
                pst_insert.setInt(6,cat_id);
                pst_insert.setInt(7,HomePage.ID);
                
               
                
                int updatedRowCount=pst_insert.executeUpdate();
                
                if(updatedRowCount>0)
                {
                    System.out.println("transaction added");
                }
                else
                {
                    System.out.println("transaction addition failed");
                }

                
                    
                    
        } catch (Exception e) {
            System.out.println(e);
        }
                
                 }  
                    
        } catch (Exception e) {
            System.out.println(e);
        }
            
            
            
        }
    }
}
} else {
           
            
        }
    }

    // Close the folder and store
    emailFolder.close(false);
    emailStore.close();
  } catch (Exception e) {
    System.out.println(e);
    
        emailConnectionFlag=1;
    
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        menu = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_category = new app.bolivia.swing.JCTextField();
        rSMaterialButtonCircle3 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle4 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle5 = new rojerusan.RSMaterialButtonCircle();
        jScrollPane4 = new javax.swing.JScrollPane();
        categoryTable = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 5, 50));

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 25)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/male_user_50px.png"))); // NOI18N
        jLabel2.setText("Welcome, "+HomePage.name);
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 25)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Expense Monitoring System");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, -1, -1));

        menu.setText("|||");
        menu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuActionPerformed(evt);
            }
        });
        jPanel1.add(menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1366, 70));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 1, 25)); // NOI18N
        jLabel9.setText("Add Category");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 230, 40));

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel8.setText("Category name");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, -1));

        txt_category.setBackground(new java.awt.Color(204, 204, 255));
        txt_category.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txt_category.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txt_category.setPlaceholder("Enter category name");
        jPanel3.add(txt_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 310, -1));

        rSMaterialButtonCircle3.setBackground(new java.awt.Color(102, 102, 255));
        rSMaterialButtonCircle3.setText("DELETE");
        rSMaterialButtonCircle3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle3ActionPerformed(evt);
            }
        });
        jPanel3.add(rSMaterialButtonCircle3, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 260, 130, 90));

        rSMaterialButtonCircle4.setBackground(new java.awt.Color(102, 102, 255));
        rSMaterialButtonCircle4.setText("ADD");
        rSMaterialButtonCircle4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle4ActionPerformed(evt);
            }
        });
        jPanel3.add(rSMaterialButtonCircle4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 140, 90));

        rSMaterialButtonCircle5.setBackground(new java.awt.Color(102, 102, 255));
        rSMaterialButtonCircle5.setText("UPDATE");
        rSMaterialButtonCircle5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonCircle5ActionPerformed(evt);
            }
        });
        jPanel3.add(rSMaterialButtonCircle5, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 260, 130, 90));

        jPanel16.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 640));

        categoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Srno", "Name"
            }
        ));
        categoryTable.setColorBackgoundHead(new java.awt.Color(102, 102, 255));
        categoryTable.setColorBordeFilas(new java.awt.Color(102, 102, 255));
        categoryTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        categoryTable.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        categoryTable.setFont(new java.awt.Font("Yu Gothic UI Light", 0, 25)); // NOI18N
        categoryTable.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        categoryTable.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI", 1, 20)); // NOI18N
        categoryTable.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 20)); // NOI18N
        categoryTable.setRowHeight(35);
        categoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                categoryTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(categoryTable);

        jPanel16.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, 770, 370));

        getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 70, 1370, 630));

        setSize(new java.awt.Dimension(1382, 709));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuActionPerformed
        if(drawer.isShow())
        {
            drawer.hide();
        }else{
            drawer.show();}
    }//GEN-LAST:event_menuActionPerformed

    private void rSMaterialButtonCircle3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle3ActionPerformed
        if(validatecategory())
        {
            deleteCategory();
            clearTable();
            setCategoryDetailsToTable();
        }
    }//GEN-LAST:event_rSMaterialButtonCircle3ActionPerformed

    private void rSMaterialButtonCircle4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle4ActionPerformed
           if(validatecategory())
        {
            addCategory();
            clearTable();
            setCategoryDetailsToTable();
        }
    }//GEN-LAST:event_rSMaterialButtonCircle4ActionPerformed

    private void rSMaterialButtonCircle5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle5ActionPerformed
         if(validatecategory())
        {
            updateCategory();
            clearTable();
            setCategoryDetailsToTable();
        }
    }//GEN-LAST:event_rSMaterialButtonCircle5ActionPerformed

    private void categoryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_categoryTableMouseClicked
        int rowNo=categoryTable.getSelectedRow();
        TableModel model=categoryTable.getModel();
        
        txt_category.setText(model.getValueAt(rowNo,1).toString());
        cat_name=model.getValueAt(rowNo,1).toString();
    }//GEN-LAST:event_categoryTableMouseClicked

    
    
    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojeru_san.complementos.RSTableMetro categoryTable;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton menu;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle3;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle4;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle5;
    private app.bolivia.swing.JCTextField txt_category;
    // End of variables declaration//GEN-END:variables
}
