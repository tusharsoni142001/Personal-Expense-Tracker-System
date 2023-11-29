/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javaswingdev.drawer.DrawerController;
import javaswingdev.drawer.Drawer;
import javaswingdev.drawer.DrawerItem;
import javaswingdev.drawer.EventDrawer;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.Date;
import java.util.TimeZone;
import javax.mail.internet.InternetAddress;

/** 
 *
 * @author SONI
 */
public class HomePage extends javax.swing.JFrame {

    private DrawerController drawer;
    int navigationChoice;
    /**
     * Creates new form Homepage
     */
    public HomePage() {
        initComponents();
        showPieChart();
        showLineChart();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        displayCardDetails();
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
//        showDetails();


    

        /*new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                
            }
        }, 
        1000 
        );*/
        
       
        
        /*ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> transactionFromEmail(), 5, TimeUnit.SECONDS); // Call your function here
        scheduler.shutdown();*/
        
    }
    
    public HomePage(String name,int ID)
    {
        userDetails(name, ID);
    }
    
    
    
    //Method to change Frame
    public void changeFrame(int navigationChoice)
    {
        switch (navigationChoice) {
        //Home
            case 0:
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
    public static int Flag=0;
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
     
    
    
    //Method to get user name and ID
    public static int ID;
    public  static String name;
    public void userDetails(String name,int ID)
    {
        HomePage.name=name;
        HomePage.ID=ID;
    }
    
    /*int budgetamt;
    String budgetmonth;
    //Method to show details 
    public void showDetails
    {
        try {
                Connection con= DBConnection.getConnection();
                
                String budgetquery=String.format("select amount,month from budget where user_id=%x",HomePage.ID);
                Statement stmt=con.createStatement();
                ResultSet rs=stmt.executeQuery(budgetquery);
                while(rs.next())
                {
                    budgetamt=rs.getInt(1);
                    budgetmonth=rs.getString(2);
                }
                
                
               
                
                                  
                    
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/
    
    
    //Pie Chart
    public void showPieChart(){
        
        int size=0;
        
       
       //Getting no. of categories;
       try {
                Connection con= DBConnection.getConnection();
                //System.out.println(HomePage.ID);
                String sql="SELECT COUNT(*) FROM category where user_id=?";
                PreparedStatement pstsize=con.prepareStatement(sql);
                pstsize.setInt(1,HomePage.ID);
                ResultSet rssize=pstsize.executeQuery();
                
                
               
                while(rssize.next())
                { 
                    size=rssize.getInt(1);
                  
                }
                
                    
             }catch (Exception e) {
            System.out.println(e);
        }
       
       String categories[]=new String[size];
       int catid[]=new int[size];
       int amount[]=new int[size];
       int i=0,j=0;
       //Fetching and storing categories into array
       try {
                Connection con= DBConnection.getConnection();
                //System.out.println(HomePage.ID);
                String sql1="SELECT catname,catID FROM category where user_id=?";
                PreparedStatement pstpie=con.prepareStatement(sql1);
                pstpie.setInt(1,HomePage.ID);
                ResultSet rspie=pstpie.executeQuery();
                
                
               //Not entering inwhile loop
                while(rspie.next())
                { 
                    System.out.println(i);
                    categories[i]=rspie.getString(1);
                    catid[i]=rspie.getInt(2);
                    i++;
                }
                
                //fetching mount per category
                String sqlamount="Select SUM(amount) from transactions where cat_id=? and user_id=?";
                PreparedStatement pstamount=con.prepareStatement(sqlamount);
                
                i=0;
               while(i<categories.length)
                {
                    pstamount.setInt(1,catid[i]);
                    pstamount.setInt(2, HomePage.ID);
                    
                    ResultSet rsamount=pstamount.executeQuery();
                    
                    if(rsamount.next())
                    {

                        amount[j]=rsamount.getInt(1);
                        j++;
                    }
                    i++;
                }
                       
                //ResultSet rspie=pstpie.executeQuery();
                
                
               
                while(rspie.next())
                { 
                    categories[i]=rspie.getString(1);
                    catid[i]=rspie.getInt(2);
                    i++;
                }
                
                    
             }catch (Exception e) {
            System.out.println(e);
        }
       
       DefaultPieDataset barDataset = new DefaultPieDataset( );
       i=0;
       while(i<categories.length)
       {
           if(amount[i]<1)
           {
             //create dataset
               i++;
               continue;
            
           }
           barDataset.setValue( categories[i], new Double( amount[i] ) );  
     /* barDataset.setValue( "Shopping" , new Double( 20 ) );   
      barDataset.setValue( "Travel" , new Double( 40 ) );    
      barDataset.setValue( "Health" , new Double( 10 ) );  */
     i++;
       }
      //create chart
       JFreeChart piechart = ChartFactory.createPieChart("Categories",barDataset, false,true,false);//explain
      
        PiePlot piePlot =(PiePlot) piechart.getPlot();
        
       
       //changing pie chart blocks colors
        /*piePlot.setSectionPaint("IPhone 5s", new Color(255,255,102));
        piePlot.setSectionPaint("SamSung Grand", new Color(102,255,102));
        piePlot.setSectionPaint("MotoG", new Color(255,102,153));
        piePlot.setSectionPaint("Nokia Lumia", new Color(0,204,204));*/
      
       
        piePlot.setBackgroundPaint(Color.white);
        
        //create chartPanel to display chart(graph)
        ChartPanel barChartPanel = new ChartPanel(piechart);
        panelPieChart.removeAll();
        panelPieChart.add(barChartPanel, BorderLayout.CENTER);
        panelPieChart.validate();
    }
    
    
    
    
    
    //Line Chart
    public void showLineChart(){
        
         
      try {
                
                Connection con= DBConnection.getConnection();
        // Get the current date and time
        Date currentDate = new Date();
        int count=0;
        String [] dates=new String[7];
        // Set the date format
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // Get the calendar instance and set its time to the current date
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(currentDate);
        
        // Set the calendar's date to the first date of the month
        calendar1.set(Calendar.DATE, 1);
        
        // Print the dates with an increment of 5 until the last date of the month is reached
        while (calendar1.get(Calendar.MONTH) == currentDate.getMonth()) {
            System.out.println(dateFormat1.format(calendar1.getTime()));
            dates[count]=dateFormat1.format(calendar1.getTime());
            calendar1.add(Calendar.DATE, 5);
            
            count ++;
        }

        System.out.println("Printng dates from array");
        for(int i=0;i<count;i++)
        {
            System.out.println(dates[i]);
        }
        
        
        //create dataset for the graph
         DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM");

        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);

        //To format date with time as 00:00:00 and 23:59:59 to fetch details
        DateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date dateprev = null;
        Date datecurr = null;
        Date datelast = null;
        
        count=0;
        double spend=0;
        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i += 5) {
            
            System.out.println(i);
            calendar.set(Calendar.DAY_OF_MONTH, i);
            if (calendar.get(Calendar.MONTH) == currentMonth) {
                
                
                //To fetch amount of 1st day of the month
                if (calendar.get(Calendar.MONTH) == currentMonth && i==1) {
                    
                    try {
                            date1 = originalDateFormat.parse(dates[count]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    
                String newDate = newDateFormat.format(date1);
                String newStartTime = "00:00:00";
                String newStartDateTime = newDate + " " + newStartTime;

                // Set the new date and time to 23:59:59
                String newEndTime = "23:59:59";
                String newEndDateTime = newDate + " " + newEndTime;    
                    //Fetching amout from databse
                   String sql_firstdate="SELECT SUM(amount) FROM transactions where date BETWEEN ? And ? And user_id=?";
                PreparedStatement pst_firstdate=con.prepareStatement(sql_firstdate);

                pst_firstdate.setString(1,newStartDateTime);
                pst_firstdate.setString(2,newEndDateTime);
                pst_firstdate.setInt(3,HomePage.ID);
                
                ResultSet rs_firstdate=pst_firstdate.executeQuery();
                if(rs_firstdate.next())
                {
                    spend=rs_firstdate.getDouble(1);
                    
                }
                    String date = dateFormat.format(calendar.getTime());
                    dataset.setValue(spend, "Amount", date);
                    count++;
                    spend=0;
                }
                
                
                //else if date isnot first date
                else if (calendar.get(Calendar.MONTH) == currentMonth) {
                     try {
                            dateprev = originalDateFormat.parse(dates[count-1]);
                            datecurr = originalDateFormat.parse(dates[count]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    
                String newDateprev = newDateFormat.format(dateprev);
                String newStartTime = "00:00:00";
                String newStartDateTime = newDateprev + " " + newStartTime;

                String newDatecurr = newDateFormat.format(datecurr);
                // Set the new date and time to 23:59:59
                String newEndTime = "23:59:59";
                String newEndDateTime = newDatecurr + " " + newEndTime;  
                
                
                    //Fetching amout from databse
                   String sql_firstdate="SELECT SUM(amount) FROM transactions where date BETWEEN ? And ? And user_id=?";
                PreparedStatement pst_firstdate=con.prepareStatement(sql_firstdate);

                pst_firstdate.setString(1,newStartDateTime);
                pst_firstdate.setString(2,newEndDateTime);
                pst_firstdate.setInt(3,HomePage.ID);
                
                ResultSet rs_firstdate=pst_firstdate.executeQuery();
                if(rs_firstdate.next())
                {
                    spend=spend+rs_firstdate.getDouble(1);
                    
                }
                    String date = dateFormat.format(calendar.getTime());
                    dataset.setValue(spend, "Amount", date);
                    count++;
                }
                
            }
        }

        
        //create chart
        JFreeChart linechart = ChartFactory.createLineChart("Monthly Expenses","Dates","amount in \u20B9", 
                dataset, PlotOrientation.VERTICAL, false,true,false);
        
        //create plot object
         CategoryPlot lineCategoryPlot = linechart.getCategoryPlot();
       // lineCategoryPlot.setRangeGridlinePaint(Color.BLUE);
        lineCategoryPlot.setBackgroundPaint(Color.white);
        
        //create render object to change the moficy the line properties like color
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineCategoryPlot.getRenderer();
        Color lineChartColor = new Color(204,0,51);
        lineRenderer.setSeriesPaint(0, lineChartColor);
        
         //create chartPanel to display chart(graph)
        ChartPanel lineChartPanel = new ChartPanel(linechart);
        panelLineChart.removeAll();
        panelLineChart.add(lineChartPanel, BorderLayout.CENTER);
        panelLineChart.validate();
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

  
  //Method to display monthly budget, totl spend, Reamining, last month spend
  public void displayCardDetails()
  {
      double spend=0;
      int budget = 0;
      int remaining;
      int lastMonthSpend = 0;
      
      
      // Get current year and month
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        
        // Get first day of the month
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDateTime firstDateTimeOfMonth = LocalDateTime.of(firstDayOfMonth, LocalTime.MIN);
        String FirstDateOfMonth = firstDateTimeOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        
        // Get last day of the month
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        LocalDateTime lastDateTimeOfMonth = LocalDateTime.of(lastDayOfMonth, LocalTime.MAX);
        String LastDateOfMonth = lastDateTimeOfMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        
      
      try {
                
                Connection con= DBConnection.getConnection();
                
                //Monthly spend

                String sql="SELECT SUM(amount) FROM transactions where date BETWEEN ? AND ? And user_id=?";
                PreparedStatement pst=con.prepareStatement(sql);
                
                //System.out.println(FirstDateOfMonth);
                //System.out.println(LastDateOfMonth);
                //System.out.println(HomePage.ID);
                
                pst.setString(1,FirstDateOfMonth);
                pst.setString(2,LastDateOfMonth);
                pst.setInt(3,HomePage.ID);
                
                ResultSet rs=pst.executeQuery();
                if(rs.next())
                {
                    
                    spend=rs.getDouble(1);
                    
                }
                
                
                //Budget
                String monthInMMMMFormat = today.format(DateTimeFormatter.ofPattern("MMMM"));
                
                String sql2="SELECT amount FROM budget where month=? AND user_id=?";
                PreparedStatement pstbudget=con.prepareStatement(sql2);
                
                pstbudget.setString(1,monthInMMMMFormat);
                pstbudget.setInt(2,HomePage.ID);
                
                ResultSet rsbudget=pstbudget.executeQuery();
                if(rsbudget.next())
                {
                    
                    budget=rsbudget.getInt(1);

                }
                
                
                //Previous month 
                // Get first day of previous month
                LocalDate firstDayOfPreviousMonth = LocalDate.of(year, month - 1, 1);
                LocalDateTime firstDateTimeOfPreviousMonth = LocalDateTime.of(firstDayOfPreviousMonth, LocalTime.MIN);
                String FirstDateOfPreviousMonth = firstDateTimeOfPreviousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                //System.out.println("First date of previous month: " + formattedFirstDateOfPreviousMonth);

                // Get last day of previous month
                LocalDate lastDayOfPreviousMonth = today.withDayOfMonth(1).minusDays(1);
                LocalDateTime lastDateTimeOfPreviousMonth = LocalDateTime.of(lastDayOfPreviousMonth, LocalTime.MAX);
                String LastDateOfPreviousMonth = lastDateTimeOfPreviousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                //System.out.println("Last date of previous month: " + formattedLastDateOfPreviousMonth);
                
                
                String sql3="SELECT SUM(amount) FROM transactions where date BETWEEN ? AND ? And user_id=?";
                PreparedStatement pst3=con.prepareStatement(sql3);
                
                pst3.setString(1,FirstDateOfPreviousMonth);
                pst3.setString(2,LastDateOfPreviousMonth);
                pst3.setInt(3,HomePage.ID);
                
                ResultSet rs3=pst3.executeQuery();
                if(rs3.next())
                {
                    lastMonthSpend=rs3.getInt(1);

                }
        
                //Displaying values
                
                txt_spend.setText("\u20B9 "+String.valueOf(spend));
                txt_budget.setText("\u20B9 "+String.valueOf(budget));
      
                remaining=budget-(int)spend;
                if(remaining>=0)
                {
                    txt_remaining.setText("\u20B9 "+String.valueOf(remaining));
                }
                else
                {
                    jPanel22.setBackground(new Color(255,204,204));
                    remaining=Math.abs(remaining);
                    txt_remaining.setText("\u20B9 "+String.valueOf(remaining));
                   txt_remainingtext1.setText("Overspend by");
                }
                
                txt_lastMonthSpend.setText("\u20B9 "+String.valueOf(lastMonthSpend));
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        menu = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        txt_lastMonthSpend = new javax.swing.JLabel();
        txt_lastMonth = new javax.swing.JLabel();
        panelPieChart = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        txt_budget = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        txt_remaining = new javax.swing.JLabel();
        txt_remainingtext1 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        txt_spend = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        panelLineChart = new javax.swing.JPanel();

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
        jLabel2.setText("Welcome,"+HomePage.name);
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 10, -1, -1));

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

        jPanel17.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 102, 51)));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_lastMonthSpend.setFont(new java.awt.Font("Segoe UI Semibold", 1, 40)); // NOI18N
        txt_lastMonthSpend.setForeground(new java.awt.Color(102, 102, 102));
        jPanel17.add(txt_lastMonthSpend, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 220, 60));

        txt_lastMonth.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        txt_lastMonth.setForeground(new java.awt.Color(102, 102, 102));
        txt_lastMonth.setText("Last month spend");
        jPanel17.add(txt_lastMonth, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jPanel16.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1060, 50, 240, 130));

        panelPieChart.setLayout(new java.awt.BorderLayout());
        jPanel16.add(panelPieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 230, 450, 340));

        jPanel21.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 102, 51)));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_budget.setFont(new java.awt.Font("Segoe UI Semibold", 1, 40)); // NOI18N
        txt_budget.setForeground(new java.awt.Color(102, 102, 102));
        jPanel21.add(txt_budget, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 220, 60));

        jLabel17.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Budget");
        jPanel21.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jPanel16.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 50, 240, 130));

        jPanel22.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 102, 51)));
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_remaining.setFont(new java.awt.Font("Segoe UI Semibold", 1, 40)); // NOI18N
        txt_remaining.setForeground(new java.awt.Color(102, 102, 102));
        jPanel22.add(txt_remaining, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 220, 60));

        txt_remainingtext1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        txt_remainingtext1.setForeground(new java.awt.Color(102, 102, 102));
        txt_remainingtext1.setText("Safe to Spend");
        jPanel22.add(txt_remainingtext1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jPanel16.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, 240, 130));

        jPanel23.setBorder(javax.swing.BorderFactory.createMatteBorder(15, 0, 0, 0, new java.awt.Color(255, 102, 51)));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txt_spend.setFont(new java.awt.Font("Segoe UI Semibold", 1, 40)); // NOI18N
        txt_spend.setForeground(new java.awt.Color(102, 102, 102));
        jPanel23.add(txt_spend, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 220, 60));

        jLabel18.setFont(new java.awt.Font("Segoe UI Semibold", 0, 20)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Spends");
        jPanel23.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jPanel16.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 240, 130));

        panelLineChart.setLayout(new java.awt.BorderLayout());
        jPanel16.add(panelLineChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 490, 390));

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
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JButton menu;
    private javax.swing.JPanel panelLineChart;
    private javax.swing.JPanel panelPieChart;
    private javax.swing.JLabel txt_budget;
    private javax.swing.JLabel txt_lastMonth;
    private javax.swing.JLabel txt_lastMonthSpend;
    private javax.swing.JLabel txt_remaining;
    private javax.swing.JLabel txt_remainingtext1;
    private javax.swing.JLabel txt_spend;
    // End of variables declaration//GEN-END:variables
}
