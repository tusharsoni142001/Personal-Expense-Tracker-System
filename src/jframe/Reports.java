/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;  
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;  
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author SONI
 */





public class Reports extends javax.swing.JFrame {
    
    private DrawerController drawer;
    int navigationChoice;
    /**
     * Creates new form Homepage
     */
    public Reports() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        showBarChart();
        showPieChart();
         
        
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
                ViewTransactions viewtransactions=new ViewTransactions();
                viewtransactions.setVisible(true);
                this.dispose();
                break;
        //Reports
            case 5:
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
     
     
     
  void showBarChart() {
    // Get the current year and month
    YearMonth currentYearMonth = YearMonth.now();
    int currentMonthValue = currentYearMonth.getMonthValue();

    // Create an array to store the month names
    var monthNames = new String[currentMonthValue];

    // Loop through the months from January to the current month and store the names
    for (int i = 1; i <= currentMonthValue; i++) {
        Month month = Month.of(i);
        String monthName = month.getDisplayName(TextStyle.FULL, Locale.getDefault());
        monthNames[i - 1] = monthName;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    int amount[] = new int[monthNames.length];
    for (int i = 0; i < monthNames.length; i++) {

        Month month = Month.valueOf(monthNames[i].toUpperCase());

        LocalDate firstDate = LocalDate.of(LocalDate.now().getYear(), month, 1);
        LocalDateTime firstDateTime = LocalDateTime.of(firstDate, LocalDateTime.now().toLocalTime());

        LocalDate lastDate = LocalDate.of(LocalDate.now().getYear(), month, month.length(false));
        LocalDateTime lastDateTime = LocalDateTime.of(lastDate, LocalDateTime.MAX.toLocalTime());

        Date firstDateOfMonth = Date.from(firstDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String FirstDateOfMonth = dateFormat.format(firstDateOfMonth);

        Date lastDateOfMonth = Date.from(lastDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String LastDateOfMonth = dateFormat.format(lastDateOfMonth);

        try {

            Connection con = DBConnection.getConnection();

            // Monthly spend

            String sql = "SELECT SUM(amount) FROM transactions where date BETWEEN ? AND ? And user_id=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, FirstDateOfMonth);
            pst.setString(2, LastDateOfMonth);
            pst.setInt(3, HomePage.ID);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {

                amount[i] = rs.getInt(1);

            }

            dataset.setValue(amount[i], "Total expense", monthNames[i]);

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    JFreeChart chart = ChartFactory.createBarChart("Monthly Expenses Comparison", "Month", "Amount in \u20B9",
            dataset, PlotOrientation.VERTICAL, false, true, false);
    // create plot object
    CategoryPlot bar = chart.getCategoryPlot();
    // lineCategoryPlot.setRangeGridlinePaint(Color.BLUE);
    bar.setRangeGridlinePaint(Color.black);

    // create chartPanel to display chart(graph)
    ChartPanel lineChartPanel = new ChartPanel(chart);

    barChartPanel.removeAll();
    barChartPanel.add(lineChartPanel, BorderLayout.CENTER);
    barChartPanel.validate();
}


     
  
    public void showPieChart(){
        
        
        String[] mode={"CASH","CREDIT/DEBIT CARD","NET BANKING","UPI"};
        
       
       
       
  
       int amount[]=new int[mode.length];
       int i=0,j=0;
       //Fetching and storing categories into array
       try {
                Connection con= DBConnection.getConnection();
               
                
                //fetching amount per category
                String sqlamount="Select SUM(amount) from transactions where paymentmode=? and user_id=?";
                PreparedStatement pstamount=con.prepareStatement(sqlamount);
                
                
               while(i<mode.length)
                {
                    pstamount.setString(1,mode[i]);
                    pstamount.setInt(2, HomePage.ID);
                    
                    ResultSet rsamount=pstamount.executeQuery();
                    
                    if(rsamount.next())
                    {

                        amount[j]=rsamount.getInt(1);
                        j++;
                    }
                    i++;
                }
                       
                    
             }catch (Exception e) {
            System.out.println(e);
        }
       
       DefaultPieDataset barDataset = new DefaultPieDataset( );
       i=0;
       while(i<mode.length)
       {
           if(amount[i]<1)
           {
             //create dataset
               i++;
               continue;
            
           }
           barDataset.setValue( mode[i], new Double( amount[i] ) );  
     /* barDataset.setValue( "Shopping" , new Double( 20 ) );   
      barDataset.setValue( "Travel" , new Double( 40 ) );    
      barDataset.setValue( "Health" , new Double( 10 ) );  */
     i++;
       }
      //create chart
       JFreeChart piechart = ChartFactory.createPieChart("Expenses by Payment Mode",barDataset, false,true,false);//explain
      
        PiePlot piePlot =(PiePlot) piechart.getPlot();
        
       
       //changing pie chart blocks colors
        /*piePlot.setSectionPaint("IPhone 5s", new Color(255,255,102));
        piePlot.setSectionPaint("SamSung Grand", new Color(102,255,102));
        piePlot.setSectionPaint("MotoG", new Color(255,102,153));
        piePlot.setSectionPaint("Nokia Lumia", new Color(0,204,204));*/
      
       
        piePlot.setBackgroundPaint(Color.white);
        
        //create chartPanel to display chart(graph)
        ChartPanel barChartPanel = new ChartPanel(piechart);
        pieChartPanel.removeAll();
        pieChartPanel.add(barChartPanel, BorderLayout.CENTER);
        pieChartPanel.validate();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        barChartPanel = new javax.swing.JPanel();
        pieChartPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1366, 700));
        setPreferredSize(new java.awt.Dimension(1366, 700));
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

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        barChartPanel.setLayout(new java.awt.BorderLayout());

        pieChartPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(barChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100)
                .addComponent(pieChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pieChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(barChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(835, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel4);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1370, 630));

        setSize(new java.awt.Dimension(1382, 712));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void menuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuActionPerformed
        if(drawer.isShow())
        {
            drawer.hide();
        }else{
            drawer.show();}
    }//GEN-LAST:event_menuActionPerformed

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barChartPanel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton menu;
    private javax.swing.JPanel pieChartPanel;
    // End of variables declaration//GEN-END:variables
}
