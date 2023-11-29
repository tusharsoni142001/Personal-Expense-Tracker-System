/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.*;

/**
 *
 * @author SONI
 */





public class ViewTransactions extends javax.swing.JFrame {
    
    private DrawerController drawer;
    int navigationChoice;
    /**
     * Creates new form Homepage
     */
    public ViewTransactions() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        setTransactionDetailsToTable();
        updateComboBox();
        
         
        
        //JScrollPane sp=new JScrollPane(descriptionTextField,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //add(sp);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
                AddCategory category=new AddCategory();
                category.setVisible(true);
                this.dispose();
               
                break;
        //View Transaction
            case 4:
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
    
           
     //method to ask for email id and password to add transactions from email
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
     
     
     
    //Variables 
    double amount;
    String paymentMode;
    String category;
    String description;
    String transactionID;
    String date;
    
    //Method to validate empty fields  ||  method to check if any field is empty.
    public boolean validatetransaction()
    {
        String checkamount=txt_amount.getText();
        description=descriptionTextField.getText();
        category=categoryComboBox.getSelectedItem().toString();
        paymentMode=paymentModeComboBox.getSelectedItem().toString();
        
        if(checkamount.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Please enter username");
            return false;
        } 
        
        if(description.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Please enter password");
            return false;
        }
        if(category.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Please enter password");
            return false;
        }
        
        if(paymentMode.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Please enter password");
            return false;
        }
        
        
        return true;
        
    }
    
    //validating amount
    public boolean validatedetails()
    {
        Pattern pattern = Pattern.compile("^-?\\d+$", Pattern.CASE_INSENSITIVE);
        String checkamount=txt_amount.getText();
        
        Matcher matcher = pattern.matcher(checkamount);
        boolean matchFound = matcher.find();
        if(matchFound) {
            
           return true;
        } else 
        {
            JOptionPane.showMessageDialog(this, "Enter valid amount");
            return false;
        }
    }
    
    //Methd to enter transaction details in database
    public void addTransaction()
    {
        amount=Double.parseDouble(txt_amount.getText());
        description=descriptionTextField.getText();
        transactionID=txt_transactionID.getText();
        long millis=System.currentTimeMillis();  
        java.sql.Date date = new java.sql.Date(millis);  
        
        try {
                Connection con= DBConnection.getConnection();
                String sql="insert into transactions(amount,date,description,paymentmode,transaction_id,user_id)values(?,?,?,?,?,?)";
                PreparedStatement pst=con.prepareStatement(sql);
                
                pst.setDouble(1,amount);
                pst.setDate(2,date);
                pst.setString(3,description);
                pst.setString(4,paymentMode);
                pst.setString(5,transactionID);
                pst.setInt(6,HomePage.ID);
                
               
                
                int updatedRowCount=pst.executeUpdate();

                if(updatedRowCount>0)
                {
                    JOptionPane.showMessageDialog(this,"Transaction Successful");
                    
                    
                }
                else
                {
                    JOptionPane.showMessageDialog(this,"Failed");
                }
                    
                    
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    //Fetching data from database into table
    int tid=1;
    //String paymentmode,transactionid,desc;
    DefaultTableModel model;
    
    public void setTransactionDetailsToTable()
    {
        
        try {
                Connection con= DBConnection.getConnection();
                //System.out.println(HomePage.ID);
                String sql="select amount,paymentmode,transaction_id,description,date,cat_id from transactions where user_id = "+HomePage.ID+" ORDER BY date desc";
                Statement st=con.createStatement();
                ResultSet rs=st.executeQuery(sql);
                
               String sql_catID="select catname from category where catID=?";
               PreparedStatement pst_catID=con.prepareStatement(sql_catID);
                ResultSet rs_catID;
               
                while(rs.next())
                {
                    double amount=rs.getDouble("amount");
                    String paymentmode=rs.getString("paymentmode");
                    String transactionid=rs.getString("transaction_id");
                    String desc=rs.getString("description");
                    String date=rs.getString("date");
                    int cat_id=rs.getInt("cat_id");
                    
                    pst_catID.setInt(1,cat_id);
                    rs_catID=pst_catID.executeQuery();
                    String catName = null;
                    if(rs_catID.next())
                    {
                        catName=rs_catID.getString(1);
                    }
                    Object[] Obj={tid,amount,paymentmode,catName,transactionid,desc,date};
                    model=(DefaultTableModel)transactionTable.getModel();
                    model.addRow(Obj);
                    tid++;
                }
                 
             }catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    
    //Filter method to display content into table
    public void setTransactionDetailsToTableFilter(String paymentmode1,String cat_name, String fromDate,String toDate )
    {
        
        try {
                Connection con= DBConnection.getConnection();
                int catID = 0;
                //Category ID
                String sql_catID1="select catID from category where catname=? and user_id=?";
                PreparedStatement pst_catID1=con.prepareStatement(sql_catID1);

                pst_catID1.setString(1, cat_name);
                pst_catID1.setInt(2,HomePage.ID);

                ResultSet rs_catID1=pst_catID1.executeQuery();

                if(rs_catID1.next())
                {
                    catID=rs_catID1.getInt(1);
                    System.err.println("Category ID: "+catID);

                }


                //System.out.println(HomePage.ID);
                String sql_filter="select amount,paymentmode,transaction_id,description,date,cat_id from transactions where user_id = ? and paymentmode=? and cat_id=? and date between ? and ? ORDER BY date desc";
                PreparedStatement pst_filter=con.prepareStatement(sql_filter);
                
                pst_filter.setInt(1,HomePage.ID);
                pst_filter.setString(2,paymentmode1);
                pst_filter.setInt(3,catID);
                pst_filter.setString(4,fromDate);
                
                //pst_filter.setString(6,orderby);
                
                
                
                ResultSet rs=pst_filter.executeQuery();
                
               String sql_catID="select catname from category where catID=?";
               PreparedStatement pst_catID=con.prepareStatement(sql_catID);
                ResultSet rs_catID;
               
                while(rs.next())
                {
                    double amount=rs.getDouble("amount");
                    String paymentmode=rs.getString("paymentmode");
                    String transactionid=rs.getString("transaction_id");
                    String desc=rs.getString("description");
                    String date=rs.getString("date");
                    int cat_id=rs.getInt("cat_id");
                    
                    pst_catID.setInt(1,cat_id);
                    rs_catID=pst_catID.executeQuery();
                    String catName = null;
                    if(rs_catID.next())
                    {
                        catName=rs_catID.getString(1);
                    }
                    Object[] Obj={tid,amount,paymentmode,catName,transactionid,desc,date};
                    model=(DefaultTableModel)transactionTable.getModel();
                    model.addRow(Obj);
                    tid++;
                }
                 
             }catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    
    
    //Method to clear table
    public void clearTable()
    {
        DefaultTableModel model=(DefaultTableModel)transactionTable.getModel();
        model.setRowCount(0);
        tid=1;
    }
    
    //Method to add categories to combobox from database
    public void updateComboBox()
    {
        Connection con= DBConnection.getConnection();
        String sql="select catname from category where user_id=?";
        try{
            PreparedStatement pst=con.prepareStatement(sql);
            pst.setInt(1, HomePage.ID);
            ResultSet rs=pst.executeQuery();
            
            while(rs.next())
            {
                categoryComboBox.addItem(rs.getString("catname"));
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    
    //Method to update transaction details
    String catNameForUpdate,updatedCatName;
    public void updateTransactions()
    {
        amount=Double.parseDouble(txt_amount.getText());
        paymentMode=paymentModeComboBox.getSelectedItem().toString();
        category=categoryComboBox.getSelectedItem().toString();
        description=this.descriptionTextField.getText();
        transactionID=txt_transactionID.getText();
       int catID=0,transactionID=0;
        
        try {
            Connection con=DBConnection.getConnection();
            
            //Category ID
            String sql_catID="select catID from category where catname=? and user_id=?";
            PreparedStatement pst_catID=con.prepareStatement(sql_catID);
            
            pst_catID.setString(1, catNameForUpdate);
            pst_catID.setInt(2,HomePage.ID);
            
            ResultSet rs_catID=pst_catID.executeQuery();
            
            if(rs_catID.next())
            {
                catID=rs_catID.getInt(1);
                System.err.println("Category ID: "+catID);
            
            }
            
            //Transaction ID
            String sql_transactionID="select t_id from transactions where date=? and user_id=?";
            PreparedStatement pst_transactionID=con.prepareStatement(sql_transactionID);
            
            pst_transactionID.setString(1, date);
            pst_transactionID.setInt(2,HomePage.ID);
            
            ResultSet rs_transactionID=pst_transactionID.executeQuery();
            
            if(rs_transactionID.next())
            {
                transactionID=rs_transactionID.getInt(1);
                System.out.println("Transaction ID: "+this.transactionID);
            
            }
            System.out.println(date);
            
            
            
            //Category name to be update
            String sql_catID2="select catID from category where catname=? and user_id=?";
            PreparedStatement pst_catID2=con.prepareStatement(sql_catID2);
            
            pst_catID2.setString(1, category);
            pst_catID2.setInt(2,HomePage.ID);
            
            ResultSet rs_catID2=pst_catID2.executeQuery();
            
            if(rs_catID2.next())
            {
                catID=rs_catID2.getInt(1);
                System.err.println("Category ID: "+catID);
            
            }
            
            //Update records
            String sql_update="update transactions set amount=?,description=?,paymentmode=?,cat_id=? where user_id=? and t_id=?";
            PreparedStatement pst_update=con.prepareStatement(sql_update);
            System.out.println("check 1");
            
            pst_update.setDouble(1, amount);
            pst_update.setString(2, description);
            pst_update.setString(3,paymentMode);
            pst_update.setInt(4,catID);
            pst_update.setInt(5, HomePage.ID);
            pst_update.setInt(6, transactionID);
            System.err.println("check 2");
            
            int updateCount=0;
            updateCount=pst_update.executeUpdate();
            System.err.println("check 3");
            
            System.out.println(date);
            if(updateCount>0)
            {
                JOptionPane.showMessageDialog(this,"Transaction updated successfully");
            }
            else
            {
                JOptionPane.showMessageDialog(this,"Transaction updation failed");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        
    }
    
    
    //Method to deete transaction details
     public void deleteTransactions()
    {
        
        
        try {
                Connection con= DBConnection.getConnection();
                 String sql_delete="delete from transactions where date=? and user_id=?";
                 
                 PreparedStatement pst_delete=con.prepareStatement(sql_delete);
                 
                 pst_delete.setString(1, date);
                 pst_delete.setInt(2, HomePage.ID);
                 
                 int updatedRowCount=pst_delete.executeUpdate();

                        if(updatedRowCount>0)
                        {
                            JOptionPane.showMessageDialog(this,"Transaction deleted successful");


                        }
                        else
                        {
                            JOptionPane.showMessageDialog(this,"No transaction deleted");
                        }
                    
        } catch (Exception e) {
            System.out.println(e);
        }
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
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        transactionTable = new rojeru_san.complementos.RSTableMetro();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_transactionID = new app.bolivia.swing.JCTextField();
        txt_amount = new app.bolivia.swing.JCTextField();
        paymentModeComboBox = new javax.swing.JComboBox<>();
        categoryComboBox = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        descriptionTextField = new javax.swing.JTextField();
        update = new rojerusan.RSMaterialButtonCircle();
        Delete = new rojerusan.RSMaterialButtonCircle();
        Filter = new rojerusan.RSMaterialButtonCircle();

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
        jPanel16.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        jLabel20.setFont(new java.awt.Font("Segoe UI Semibold", 0, 30)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(102, 102, 102));
        jLabel20.setText("Transactions histroy");
        jPanel16.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 290, 50));

        transactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Srno", "Amount", "Payment Mode", "Category", "Transaction ID", "Description", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        transactionTable.setColorBackgoundHead(new java.awt.Color(102, 102, 255));
        transactionTable.setColorBordeFilas(new java.awt.Color(102, 102, 255));
        transactionTable.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        transactionTable.setColorSelBackgound(new java.awt.Color(255, 51, 51));
        transactionTable.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        transactionTable.setFuenteFilas(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        transactionTable.setFuenteFilasSelect(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        transactionTable.setFuenteHead(new java.awt.Font("Yu Gothic UI Semibold", 1, 18)); // NOI18N
        transactionTable.setRowHeight(35);
        transactionTable.getTableHeader().setReorderingAllowed(false);
        transactionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                transactionTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(transactionTable);
        if (transactionTable.getColumnModel().getColumnCount() > 0) {
            transactionTable.getColumnModel().getColumn(0).setMaxWidth(50);
            transactionTable.getColumnModel().getColumn(1).setMaxWidth(85);
            transactionTable.getColumnModel().getColumn(2).setMaxWidth(180);
            transactionTable.getColumnModel().getColumn(3).setMaxWidth(150);
            transactionTable.getColumnModel().getColumn(4).setMaxWidth(220);
            transactionTable.getColumnModel().getColumn(5).setMaxWidth(300);
            transactionTable.getColumnModel().getColumn(6).setMaxWidth(160);
        }

        jPanel16.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 1010, 540));

        getContentPane().add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 70, 1030, 640));

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 1, 25)); // NOI18N
        jLabel7.setText("Transaction details");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 230, 40));

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel8.setText("Amount:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jLabel16.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel16.setText("Payment mode:");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel17.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel17.setText("Category:");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jLabel19.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel19.setText("Transaction ID");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        jLabel18.setFont(new java.awt.Font("Verdana", 0, 17)); // NOI18N
        jLabel18.setText("Description");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, -1, -1));

        txt_transactionID.setEditable(false);
        txt_transactionID.setBackground(new java.awt.Color(204, 204, 255));
        txt_transactionID.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txt_transactionID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_transactionID.setPlaceholder("Optional");
        txt_transactionID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_transactionIDActionPerformed(evt);
            }
        });
        jPanel3.add(txt_transactionID, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 160, -1));

        txt_amount.setBackground(new java.awt.Color(204, 204, 255));
        txt_amount.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        txt_amount.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txt_amount.setPlaceholder("Enter amount");
        txt_amount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_amountActionPerformed(evt);
            }
        });
        jPanel3.add(txt_amount, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 160, -1));

        paymentModeComboBox.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        paymentModeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CASH", "CREDIT/DEBIT CARD", "NET BANKING", "UPI" }));
        paymentModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentModeComboBoxActionPerformed(evt);
            }
        });
        jPanel3.add(paymentModeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 160, 33));

        categoryComboBox.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        categoryComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryComboBoxActionPerformed(evt);
            }
        });
        jPanel3.add(categoryComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 160, 33));

        descriptionTextField.setBackground(new java.awt.Color(204, 204, 255));
        descriptionTextField.setColumns(5);
        descriptionTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        descriptionTextField.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        descriptionTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        descriptionTextField.setMaximumSize(new java.awt.Dimension(1, 18));
        descriptionTextField.setOpaque(true);
        descriptionTextField.setScrollOffset(1);
        descriptionTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descriptionTextFieldActionPerformed(evt);
            }
        });
        jScrollPane3.setViewportView(descriptionTextField);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 320, 170, 110));

        update.setBackground(new java.awt.Color(102, 102, 255));
        update.setText("Update");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });
        jPanel3.add(update, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 510, 80, 70));

        Delete.setBackground(new java.awt.Color(153, 153, 153));
        Delete.setText("Delete");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });
        jPanel3.add(Delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 510, 80, 70));

        Filter.setBackground(new java.awt.Color(102, 102, 255));
        Filter.setText("Filter");
        Filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterActionPerformed(evt);
            }
        });
        jPanel3.add(Filter, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 510, 80, 70));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 340, 640));

        setSize(new java.awt.Dimension(1382, 712));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void categoryComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryComboBoxActionPerformed
        category=categoryComboBox.getSelectedItem().toString();
    }//GEN-LAST:event_categoryComboBoxActionPerformed

    //Drop down list(combo box) mode of payment
    private void paymentModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentModeComboBoxActionPerformed
        paymentMode=paymentModeComboBox.getSelectedItem().toString();
    }//GEN-LAST:event_paymentModeComboBoxActionPerformed

    
    
    private void menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuActionPerformed
        if(drawer.isShow())
        {
            drawer.hide();
        }else{
            drawer.show();}
    }//GEN-LAST:event_menuActionPerformed

    private void txt_transactionIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_transactionIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_transactionIDActionPerformed

    private void txt_amountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_amountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_amountActionPerformed

    private void descriptionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descriptionTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descriptionTextFieldActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        updateTransactions();
        clearTable();
        setTransactionDetailsToTable();
        
    }//GEN-LAST:event_updateActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        deleteTransactions();
        clearTable();
        setTransactionDetailsToTable();
    }//GEN-LAST:event_DeleteActionPerformed

    private void FilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterActionPerformed
        transactionFilter tf=new transactionFilter(this);
        tf.setVisible(true);
    }//GEN-LAST:event_FilterActionPerformed

    private void transactionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transactionTableMouseClicked
        int rowNo=transactionTable.getSelectedRow();
        TableModel model=transactionTable.getModel();
        
        txt_amount.setText(model.getValueAt(rowNo,1).toString());
        paymentModeComboBox.setSelectedItem(model.getValueAt(rowNo,2).toString());
        categoryComboBox.setSelectedItem(model.getValueAt(rowNo,3).toString());
        catNameForUpdate=model.getValueAt(rowNo,3).toString();
        txt_transactionID.setText(model.getValueAt(rowNo,4).toString());
        //cat_name=model.getValueAt(rowNo,1).toString();
        descriptionTextField.setText(model.getValueAt(rowNo,5).toString());
        date=model.getValueAt(rowNo, 6).toString();
    }//GEN-LAST:event_transactionTableMouseClicked

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonCircle Delete;
    private rojerusan.RSMaterialButtonCircle Filter;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JTextField descriptionTextField;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton menu;
    private javax.swing.JComboBox<String> paymentModeComboBox;
    private rojeru_san.complementos.RSTableMetro transactionTable;
    private app.bolivia.swing.JCTextField txt_amount;
    private app.bolivia.swing.JCTextField txt_transactionID;
    private rojerusan.RSMaterialButtonCircle update;
    // End of variables declaration//GEN-END:variables
}
