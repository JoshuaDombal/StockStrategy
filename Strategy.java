// Josh Dombal
// Databases 330 Winter 2018

import java.util.Scanner;
import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.*;

import java.text.*;


public class Strategy {

  static Connection conn = null;
  //static Day prevDay = new Day();


  static HashMap<Industry, ArrayList<Company>> industries = new HashMap<Industry, ArrayList<Company>>();
  static ArrayList<String> listOfIndustries = new ArrayList<String>();


  static HashMap<String, Double> divMap = new HashMap<String, Double>();
  static HashMap<String, ArrayList<IntervalDay>> bigMap = new HashMap<String, ArrayList<IntervalDay>>();

  static IntervalDay prevDay = new IntervalDay();
  static int totalSplits = 0;
  static ArrayList<IntervalDay> twoRatio = new ArrayList<IntervalDay>();
  static ArrayList<IntervalDay> threeOneRatio = new ArrayList<IntervalDay>();
  static ArrayList<IntervalDay> threeTwoRatio = new ArrayList<IntervalDay>();
  static boolean tRat = false;




  static HashMap<String, ArrayList<IntervalDay>> map = new HashMap<String, ArrayList<IntervalDay>>();






//static final String dropPerformanceTable= "drop table if exists dombalj.Performance;";


//static final String createPerformanceTable= "create table dombalj.Performance (Industry char(30), Ticker char(6), StartDatechar(10), EndDatechar(10), TickerReturnchar(12), IndustryReturnchar(12));";


static final String insertPerformance= "insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);";





  public static void main(String[] args) throws Exception {


    String paramsFile = "readerparams.txt";
    if (args.length >= 1) {
      paramsFile = args[0];
    }

    Properties connectprops = new Properties();
    connectprops.load(new FileInputStream(paramsFile));

    try {
      Class.forName("com.mysql.jdbc.Driver");
      String dburl = connectprops.getProperty("dburl");
      String username = connectprops.getProperty("user");
      conn = DriverManager.getConnection(dburl, connectprops);
      System.out.printf("Database connection %s %s established.%n", dburl, username);

    } catch (SQLException e) {

      System.out.println(" ");
    }

    Statement stmt = conn.createStatement();
    stmt.executeUpdate("drop table if exists dombalj.Performance;");
    stmt.executeUpdate("create table dombalj.Performance (Industry char(30), Ticker char(6), StartDate char(10), EndDate char(10), TickerReturn char(12), IndustryReturn char(12));");


    saveCompanies();
    saveComp();

    for (String s : divMap.keySet()) {
        if (s.equals("AMT") || s.equals("CTL") || s.equals("FTR") || s.equals("S") || s.equals("T") || s.equals("VZ") || s.equals("WIN")) {
            System.out.println("TICKER:   " + s + "   Divisor:  " + divMap.get(s));

        }
    }




    for (Industry i : industries.keySet()) {

        double minTD = i.getMinTradingDays();

        ArrayList<Company> alc = i.getCompanies();
        int p = i.calculateP();


        System.out.println("INDUSTRY:  "  + i.getIndustryName()  + "                  Start Date:  " + i.getMaxStartDay() +
         "     End Date:  " + i.getMinEndDay() + " Trading Days:  " + minTD + "                          Intervals:  " + p);


         getIntervals(i, i.getMaxStartDay(), i.getMinEndDay());

         if (i.getIndustryName().equals("Consumer Discretionary")) {
             ArrayList<Company> arc = i.getCompanies();
             System.out.println("AMOUNT OF COMPANIES" + arc.size());

             for (Company c : arc) {

                 HashMap<Integer, Interval> inter = c.getIntervals();
                 HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                 for (Integer in : inter.keySet()) {

                     Interval interv = inter.get(in);

                     c.setDivisor(divMap.get(c.getTicker()));

                     double div = c.getDivisor();
                     double op = interv.getStartPrice()/ div;
                     double cl = interv.getEndPrice()/ div;

                     double tReturn = cl/op - 1;
                     double tRet = cl/op;

                     interv.setIntervalSum(tRet);

                     int day = in*60 + interv.getSize() - 59;

                     if (interv.getSize() < 60) {
                         c.removeInterval(in);
                     } else {
                         int asdg = 76;
                     }

                 }
             }
            HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

             for (Company comp : arc) {

                 HashMap<Integer, Interval> inter = comp.getIntervals();

                 for (Integer in : inter.keySet()) {
                     Interval interv = inter.get(in);

                     if (avgReturns.get(in) == null) {
                         avgReturns.put(in, interv.getIntervalSum());
                     } else if (avgReturns.get(in) != null) {
                         avgReturns.put(in, avgReturns.get(in) + interv.getIntervalSum());

                     }
                                     if (in == 0) {
                                         System.out.println(interv.getIntervalSum());
                                     }

                 }

             }
              PreparedStatement pstmt;




             for (Company co : arc) {

                 HashMap<Integer, Interval> inter = co.getIntervals();

                 for (Integer integ : inter.keySet()) {

                     Interval intervat = inter.get(integ);

                     double thing = avgReturns.get(integ) - intervat.getIntervalSum();

                     double eee = ((thing/76)-1);

                     if (integ == 0) {
                         System.out.println( " Tick Return: " + (intervat.getIntervalSum()-1) + " Industry Return:  " + ((thing/76)-1));

                     }



                     pstmt = conn.prepareStatement("insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);");

                     String aaaa = String.format("%10.7f", (intervat.getIntervalSum()-1));
                     String eeee = String.format("%10.7f", eee);

                     pstmt.setString(1, i.getIndustryName());
                     pstmt.setString(2, co.getTicker());
                     pstmt.setString(3, intervat.getStartDate());
                     pstmt.setString(4, intervat.getEndDate());
                     pstmt.setString(5, aaaa);
                     pstmt.setString(6, eeee);





                 }

             }

         }

         if (i.getIndustryName().equals("Telecommunications Services")) {
             ArrayList<Company> arc = i.getCompanies();
             System.out.println("AMOUNT OF COMPANIES" + arc.size());

             for (Company c : arc) {

                 HashMap<Integer, Interval> inter = c.getIntervals();
                 HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                 for (Integer in : inter.keySet()) {

                     Interval interv = inter.get(in);

                     c.setDivisor(divMap.get(c.getTicker()));

                     double div = c.getDivisor();
                     double op = interv.getStartPrice()/ div;
                     double cl = interv.getEndPrice()/ div;

                     double tReturn = cl/op - 1;
                     double tRet = cl/op;

                     interv.setIntervalSum(tRet);

                     int day = in*60 + interv.getSize() - 59;

                     if (interv.getSize() < 60) {
                         c.removeInterval(in);
                     } else {
                         int asdg = 6;
                     }

                 }
             }
            HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

             for (Company comp : arc) {

                 HashMap<Integer, Interval> inter = comp.getIntervals();

                 for (Integer in : inter.keySet()) {
                     Interval interv = inter.get(in);

                     if (avgReturns.get(in) == null) {
                         avgReturns.put(in, interv.getIntervalSum());
                     } else if (avgReturns.get(in) != null) {
                         avgReturns.put(in, avgReturns.get(in) + interv.getIntervalSum());

                     }
                                     if (in == 0) {
                                         System.out.println(interv.getIntervalSum());
                                     }

                 }

             }

             PreparedStatement pstmt;



             for (Company co : arc) {

                 HashMap<Integer, Interval> inter = co.getIntervals();

                 for (Integer integ : inter.keySet()) {

                     Interval intervat = inter.get(integ);

                     double thing = avgReturns.get(integ) - intervat.getIntervalSum();

                     double eee = ((thing/76)-1);

                     if (integ == 0) {
                         System.out.println( " Tick Return: " + (intervat.getIntervalSum()-1) + " Industry Return:  " + ((thing/6)-1));

                     }

                     pstmt = conn.prepareStatement("insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);");


                     String aaaa = String.format("%10.7f", (intervat.getIntervalSum()-1));
                     String eeee = String.format("%10.7f", eee);

                     pstmt.setString(1, i.getIndustryName());
                     pstmt.setString(2, co.getTicker());
                     pstmt.setString(3, intervat.getStartDate());
                     pstmt.setString(4, intervat.getEndDate());
                     pstmt.setString(5, aaaa);
                     pstmt.setString(6, eeee);

                     pstmt.executeUpdate();

                 }

             }

         }


         if (i.getIndustryName().equals("Financials")) {
             ArrayList<Company> arc = i.getCompanies();
             System.out.println("AMOUNT OF COMPANIES" + arc.size());

             for (Company c : arc) {

                 HashMap<Integer, Interval> inter = c.getIntervals();
                 HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                 for (Integer in : inter.keySet()) {

                     Interval interv = inter.get(in);

                     c.setDivisor(divMap.get(c.getTicker()));

                     double div = c.getDivisor();
                     double op = interv.getStartPrice()/ div;
                     double cl = interv.getEndPrice()/ div;

                     double tReturn = cl/op - 1;
                     double tRet = cl/op;

                     interv.setIntervalSum(tRet);

                     int day = in*60 + interv.getSize() - 59;

                     if (interv.getSize() < 60) {
                         c.removeInterval(in);
                     } else {
                         int asdg = 79;
                     }

                 }
             }
            HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

             for (Company comp : arc) {

                 HashMap<Integer, Interval> inter = comp.getIntervals();

                 for (Integer in : inter.keySet()) {
                     Interval interv = inter.get(in);

                     if (avgReturns.get(in) == null) {
                         avgReturns.put(in, interv.getIntervalSum());
                     } else if (avgReturns.get(in) != null) {
                         avgReturns.put(in, avgReturns.get(in) + interv.getIntervalSum());

                     }
                                     if (in == 0) {
                                         System.out.println(interv.getIntervalSum());
                                     }

                 }

             }
              PreparedStatement pstmt;




             for (Company co : arc) {

                 HashMap<Integer, Interval> inter = co.getIntervals();

                 for (Integer integ : inter.keySet()) {

                     Interval intervat = inter.get(integ);

                     double thing = avgReturns.get(integ) - intervat.getIntervalSum();

                     double eee = ((thing/76)-1);

                     if (integ == 0) {
                         System.out.println( " Tick Return: " + (intervat.getIntervalSum()-1) + " Industry Return:  " + ((thing/79)-1));

                     }



                     pstmt = conn.prepareStatement("insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);");

                     String aaaa = String.format("%10.7f", (intervat.getIntervalSum()-1));
                     String eeee = String.format("%10.7f", eee);

                     pstmt.setString(1, i.getIndustryName());
                     pstmt.setString(2, co.getTicker());
                     pstmt.setString(3, intervat.getStartDate());
                     pstmt.setString(4, intervat.getEndDate());
                     pstmt.setString(5, aaaa);
                     pstmt.setString(6, eeee);





                 }

             }

         }



             if (i.getIndustryName().equals("Industrials")) {
                 ArrayList<Company> arc = i.getCompanies();
                 System.out.println("AMOUNT OF COMPANIES" + arc.size());

                 for (Company c : arc) {

                     HashMap<Integer, Interval> inter = c.getIntervals();
                     HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                     for (Integer in : inter.keySet()) {

                         Interval interv = inter.get(in);

                         c.setDivisor(divMap.get(c.getTicker()));

                         double div = c.getDivisor();
                         double op = interv.getStartPrice()/ div;
                         double cl = interv.getEndPrice()/ div;

                         double tReturn = cl/op - 1;
                         double tRet = cl/op;

                         interv.setIntervalSum(tRet);

                         int day = in*60 + interv.getSize() - 59;

                         if (interv.getSize() < 60) {
                             c.removeInterval(in);
                         } else {
                             int asdg = 59;
                         }

                     }
                 }
                HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                 for (Company comp : arc) {

                     HashMap<Integer, Interval> inter = comp.getIntervals();

                     for (Integer in : inter.keySet()) {
                         Interval interv = inter.get(in);

                         if (avgReturns.get(in) == null) {
                             avgReturns.put(in, interv.getIntervalSum());
                         } else if (avgReturns.get(in) != null) {
                             avgReturns.put(in, avgReturns.get(in) + interv.getIntervalSum());

                         }
                                         if (in == 0) {
                                             System.out.println(interv.getIntervalSum());
                                         }

                     }

                 }
                  PreparedStatement pstmt;




                 for (Company co : arc) {

                     HashMap<Integer, Interval> inter = co.getIntervals();

                     for (Integer integ : inter.keySet()) {

                         Interval intervat = inter.get(integ);

                         double thing = avgReturns.get(integ) - intervat.getIntervalSum();

                         double eee = ((thing/76)-1);

                         if (integ == 0) {
                             System.out.println( " Tick Return: " + (intervat.getIntervalSum()-1) + " Industry Return:  " + ((thing/59)-1));

                         }



                         pstmt = conn.prepareStatement("insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);");

                         String aaaa = String.format("%10.7f", (intervat.getIntervalSum()-1));
                         String eeee = String.format("%10.7f", eee);

                         pstmt.setString(1, i.getIndustryName());
                         pstmt.setString(2, co.getTicker());
                         pstmt.setString(3, intervat.getStartDate());
                         pstmt.setString(4, intervat.getEndDate());
                         pstmt.setString(5, aaaa);
                         pstmt.setString(6, eeee);





                     }

                 }

             }

             if (i.getIndustryName().equals("Health Care")) {
                 ArrayList<Company> arc = i.getCompanies();
                 System.out.println("AMOUNT OF COMPANIES" + arc.size());

                 for (Company c : arc) {

                     HashMap<Integer, Interval> inter = c.getIntervals();
                     HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                     for (Integer in : inter.keySet()) {

                         Interval interv = inter.get(in);

                         c.setDivisor(divMap.get(c.getTicker()));

                         double div = c.getDivisor();
                         double op = interv.getStartPrice()/ div;
                         double cl = interv.getEndPrice()/ div;

                         double tReturn = cl/op - 1;
                         double tRet = cl/op;

                         interv.setIntervalSum(tRet);

                         int day = in*60 + interv.getSize() - 59;

                         if (interv.getSize() < 60) {
                             c.removeInterval(in);
                         } else {
                             int asdg = 47;
                         }

                     }
                 }
                HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                 for (Company comp : arc) {

                     HashMap<Integer, Interval> inter = comp.getIntervals();

                     for (Integer in : inter.keySet()) {
                         Interval interv = inter.get(in);

                         if (avgReturns.get(in) == null) {
                             avgReturns.put(in, interv.getIntervalSum());
                         } else if (avgReturns.get(in) != null) {
                             avgReturns.put(in, avgReturns.get(in) + interv.getIntervalSum());

                         }
                                         if (in == 0) {
                                             System.out.println(interv.getIntervalSum());
                                         }

                     }

                 }
                  PreparedStatement pstmt;




                 for (Company co : arc) {

                     HashMap<Integer, Interval> inter = co.getIntervals();

                     for (Integer integ : inter.keySet()) {

                         Interval intervat = inter.get(integ);

                         double thing = avgReturns.get(integ) - intervat.getIntervalSum();

                         double eee = ((thing/76)-1);

                         if (integ == 0) {
                             System.out.println( " Tick Return: " + (intervat.getIntervalSum()-1) + " Industry Return:  " + ((thing/47)-1));

                         }



                         pstmt = conn.prepareStatement("insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);");

                         String aaaa = String.format("%10.7f", (intervat.getIntervalSum()-1));
                         String eeee = String.format("%10.7f", eee);

                         pstmt.setString(1, i.getIndustryName());
                         pstmt.setString(2, co.getTicker());
                         pstmt.setString(3, intervat.getStartDate());
                         pstmt.setString(4, intervat.getEndDate());
                         pstmt.setString(5, aaaa);
                         pstmt.setString(6, eeee);





                     }

                 }

             }


             if (i.getIndustryName().equals("Energy")) {
                 ArrayList<Company> arc = i.getCompanies();
                 System.out.println("AMOUNT OF COMPANIES" + arc.size());

                 for (Company c : arc) {

                     HashMap<Integer, Interval> inter = c.getIntervals();
                     HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                     for (Integer in : inter.keySet()) {

                         Interval interv = inter.get(in);

                         c.setDivisor(divMap.get(c.getTicker()));

                         double div = c.getDivisor();
                         double op = interv.getStartPrice()/ div;
                         double cl = interv.getEndPrice()/ div;

                         double tReturn = cl/op - 1;
                         double tRet = cl/op;

                         interv.setIntervalSum(tRet);

                         int day = in*60 + interv.getSize() - 59;

                         if (interv.getSize() < 60) {
                             c.removeInterval(in);
                         } else {
                             int asdg = 38;
                         }

                     }
                 }
                HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                 for (Company comp : arc) {

                     HashMap<Integer, Interval> inter = comp.getIntervals();

                     for (Integer in : inter.keySet()) {
                         Interval interv = inter.get(in);

                         if (avgReturns.get(in) == null) {
                             avgReturns.put(in, interv.getIntervalSum());
                         } else if (avgReturns.get(in) != null) {
                             avgReturns.put(in, avgReturns.get(in) + interv.getIntervalSum());

                         }
                                         if (in == 0) {
                                             System.out.println(interv.getIntervalSum());
                                         }

                     }

                 }
                  PreparedStatement pstmt;




                 for (Company co : arc) {

                     HashMap<Integer, Interval> inter = co.getIntervals();

                     for (Integer integ : inter.keySet()) {

                         Interval intervat = inter.get(integ);

                         double thing = avgReturns.get(integ) - intervat.getIntervalSum();

                         double eee = ((thing/76)-1);

                         if (integ == 0) {
                             System.out.println( " Tick Return: " + (intervat.getIntervalSum()-1) + " Industry Return:  " + ((thing/38)-1));

                         }



                         pstmt = conn.prepareStatement("insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);");

                         String aaaa = String.format("%10.7f", (intervat.getIntervalSum()-1));
                         String eeee = String.format("%10.7f", eee);

                         pstmt.setString(1, i.getIndustryName());
                         pstmt.setString(2, co.getTicker());
                         pstmt.setString(3, intervat.getStartDate());
                         pstmt.setString(4, intervat.getEndDate());
                         pstmt.setString(5, aaaa);
                         pstmt.setString(6, eeee);





                     }

                 }

             }

             if (i.getIndustryName().equals("Consumer Staples")) {
                 ArrayList<Company> arc = i.getCompanies();
                 System.out.println("AMOUNT OF COMPANIES" + arc.size());

                 for (Company c : arc) {

                     HashMap<Integer, Interval> inter = c.getIntervals();
                     HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                     for (Integer in : inter.keySet()) {

                         Interval interv = inter.get(in);

                         c.setDivisor(divMap.get(c.getTicker()));

                         double div = c.getDivisor();
                         double op = interv.getStartPrice()/ div;
                         double cl = interv.getEndPrice()/ div;

                         double tReturn = cl/op - 1;
                         double tRet = cl/op;

                         interv.setIntervalSum(tRet);

                         int day = in*60 + interv.getSize() - 59;

                         if (interv.getSize() < 60) {
                             c.removeInterval(in);
                         } else {
                             int asdg = 37;
                         }

                     }
                 }
                HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                 for (Company comp : arc) {

                     HashMap<Integer, Interval> inter = comp.getIntervals();

                     for (Integer in : inter.keySet()) {
                         Interval interv = inter.get(in);

                         if (avgReturns.get(in) == null) {
                             avgReturns.put(in, interv.getIntervalSum());
                         } else if (avgReturns.get(in) != null) {
                             avgReturns.put(in, avgReturns.get(in) + interv.getIntervalSum());

                         }
                                         if (in == 0) {
                                             System.out.println(interv.getIntervalSum());
                                         }

                     }

                 }
                  PreparedStatement pstmt;




                 for (Company co : arc) {

                     HashMap<Integer, Interval> inter = co.getIntervals();

                     for (Integer integ : inter.keySet()) {

                         Interval intervat = inter.get(integ);

                         double thing = avgReturns.get(integ) - intervat.getIntervalSum();

                         double eee = ((thing/76)-1);

                         if (integ == 0) {
                             System.out.println( " Tick Return: " + (intervat.getIntervalSum()-1) + " Industry Return:  " + ((thing/37)-1));

                         }



                         pstmt = conn.prepareStatement("insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);");

                         String aaaa = String.format("%10.7f", (intervat.getIntervalSum()-1));
                         String eeee = String.format("%10.7f", eee);

                         pstmt.setString(1, i.getIndustryName());
                         pstmt.setString(2, co.getTicker());
                         pstmt.setString(3, intervat.getStartDate());
                         pstmt.setString(4, intervat.getEndDate());
                         pstmt.setString(5, aaaa);
                         pstmt.setString(6, eeee);





                     }

                 }

             }


             if (i.getIndustryName().equals("Utilities")) {
                 ArrayList<Company> arc = i.getCompanies();
                 System.out.println("AMOUNT OF COMPANIES" + arc.size());

                 for (Company c : arc) {

                     HashMap<Integer, Interval> inter = c.getIntervals();
                     HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                     for (Integer in : inter.keySet()) {

                         Interval interv = inter.get(in);

                         c.setDivisor(divMap.get(c.getTicker()));

                         double div = c.getDivisor();
                         double op = interv.getStartPrice()/ div;
                         double cl = interv.getEndPrice()/ div;

                         double tReturn = cl/op - 1;
                         double tRet = cl/op;

                         interv.setIntervalSum(tRet);

                         int day = in*60 + interv.getSize() - 59;

                         if (interv.getSize() < 60) {
                             c.removeInterval(in);
                         } else {
                             int asdg = 33;
                         }

                     }
                 }
                HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                 for (Company comp : arc) {

                     HashMap<Integer, Interval> inter = comp.getIntervals();

                     for (Integer in : inter.keySet()) {
                         Interval interv = inter.get(in);

                         if (avgReturns.get(in) == null) {
                             avgReturns.put(in, interv.getIntervalSum());
                         } else if (avgReturns.get(in) != null) {
                             avgReturns.put(in, avgReturns.get(in) + interv.getIntervalSum());

                         }
                                         if (in == 0) {
                                             System.out.println(interv.getIntervalSum());
                                         }

                     }

                 }
                  PreparedStatement pstmt;




                 for (Company co : arc) {

                     HashMap<Integer, Interval> inter = co.getIntervals();

                     for (Integer integ : inter.keySet()) {

                         Interval intervat = inter.get(integ);

                         double thing = avgReturns.get(integ) - intervat.getIntervalSum();

                         double eee = ((thing/76)-1);

                         if (integ == 0) {
                             System.out.println( " Tick Return: " + (intervat.getIntervalSum()-1) + " Industry Return:  " + ((thing/33)-1));

                         }



                         pstmt = conn.prepareStatement("insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);");

                         String aaaa = String.format("%10.7f", (intervat.getIntervalSum()-1));
                         String eeee = String.format("%10.7f", eee);

                         pstmt.setString(1, i.getIndustryName());
                         pstmt.setString(2, co.getTicker());
                         pstmt.setString(3, intervat.getStartDate());
                         pstmt.setString(4, intervat.getEndDate());
                         pstmt.setString(5, aaaa);
                         pstmt.setString(6, eeee);





                     }

                 }

             }


             if (i.getIndustryName().equals("Materials")) {
                 ArrayList<Company> arc = i.getCompanies();
                 System.out.println("AMOUNT OF COMPANIES" + arc.size());

                 for (Company c : arc) {

                     HashMap<Integer, Interval> inter = c.getIntervals();
                     HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                     for (Integer in : inter.keySet()) {

                         Interval interv = inter.get(in);

                         c.setDivisor(divMap.get(c.getTicker()));

                         double div = c.getDivisor();
                         double op = interv.getStartPrice()/ div;
                         double cl = interv.getEndPrice()/ div;

                         double tReturn = cl/op - 1;
                         double tRet = cl/op;

                         interv.setIntervalSum(tRet);

                         int day = in*60 + interv.getSize() - 59;

                         if (interv.getSize() < 60) {
                             c.removeInterval(in);
                         } else {
                             int asdg = 29;
                         }

                     }
                 }
                HashMap<Integer, Double> avgReturns = new HashMap<Integer, Double>();

                 for (Company comp : arc) {

                     HashMap<Integer, Interval> inter = comp.getIntervals();

                     for (Integer in : inter.keySet()) {
                         Interval interv = inter.get(in);

                         if (avgReturns.get(in) == null) {
                             avgReturns.put(in, interv.getIntervalSum());
                         } else if (avgReturns.get(in) != null) {
                             avgReturns.put(in, avgReturns.get(in) + interv.getIntervalSum());

                         }
                                         if (in == 0) {
                                             System.out.println(interv.getIntervalSum());
                                         }

                     }

                 }
                  PreparedStatement pstmt;




                 for (Company co : arc) {

                     HashMap<Integer, Interval> inter = co.getIntervals();

                     for (Integer integ : inter.keySet()) {

                         Interval intervat = inter.get(integ);

                         double thing = avgReturns.get(integ) - intervat.getIntervalSum();

                         double eee = ((thing/76)-1);

                         if (integ == 0) {
                             System.out.println( " Tick Return: " + (intervat.getIntervalSum()-1) + " Industry Return:  " + ((thing/29)-1));

                         }



                         pstmt = conn.prepareStatement("insert into dombalj.Performance(Industry, Ticker, StartDate,  EndDate,  TickerReturn, IndustryReturn)  values(?, ?, ?, ?, ?, ?);");

                         String aaaa = String.format("%10.7f", (intervat.getIntervalSum()-1));
                         String eeee = String.format("%10.7f", eee);

                         pstmt.setString(1, i.getIndustryName());
                         pstmt.setString(2, co.getTicker());
                         pstmt.setString(3, intervat.getStartDate());
                         pstmt.setString(4, intervat.getEndDate());
                         pstmt.setString(5, aaaa);
                         pstmt.setString(6, eeee);





                     }

                 }

             }


         //removeIncompleteIntervals();


    }







    //System.out.println("Start Date:  ************ : " + sqlSD + "END DATE ********  : " + sqlED);




  }

  //public void tickerReturn() {


  // Removes the intervals that do not have 60 days
  static void removeIncompleteIntervals() {

      for (Industry i : industries.keySet()) {

          ArrayList<Company> alc = i.getCompanies();
          System.out.println(alc.size());

          for (Company c : alc) {
              if (c.getTicker() != null) {
                  HashMap<Integer, Interval> cInter = c.getIntervals();
                  System.out.println(cInter.size());
                  System.out.println("INDUSTRY:  " + i.getIndustryName());
                  System.out.println("LLLLLLLLOOOOOLLLLL " + c.getTicker());

                  if (!cInter.isEmpty()) {
                      for (int inte : cInter.keySet()) {
                          Interval interva = cInter.get(inte);

                          if (interva.getSize() < 60) {
                              c.removeInterval(inte);

                          }

                      }

                  }
              }


          }
      }
  }





  static void saveCompanies() throws SQLException {
    Statement stmt = conn.createStatement();



//THIS ONE *****************************************************************************
    ResultSet results = stmt.executeQuery("SELECT Ticker, min(TransDate) as startDay, max(TransDate) as endDay, Industry, count(distinct TransDate) as TradingDays " +
    " FROM Company NATURAL JOIN PriceVolume " +
    //" WHERE Industry = 'Information Technology' OR Industry = 'Energy' " +
    //" WHERE Industry = 'Telecommunications Services' " +
    //" WHERE Industry = 'Discretionary' " +
    //" WHERE Industry = 'Utilities' " +
    //" WHERE Industry = 'Energy' " +


    " GROUP BY Ticker, Industry HAVING TradingDays >= 150 ORDER BY Ticker");

    while (results.next()) {
        //System.out.println(results.getString(1) + "        Min: " + results.getString(2) + "      Max:  " + results.getString(3) + "        Count: " + results.getInt(4));
        //System.out.println(results.getString(1));




        // Interval Day
        String in = results.getString(4);
        String sDay = results.getString(2);
        String eDay = results.getString(3);
        String tick = results.getString(1);
        double tDays = results.getDouble(5);



        System.out.println(tick + "   Min:   " + sDay + "     Max:  " + eDay +   "     Industry:   " +  in + " COUNT  " + tDays);



        if (listOfIndustries.contains(in)) {
            System.out.println("It's IN ********");

            // WORK ON PROBLEM OF ADDING ONLY ONE INDUSTRY INSTANCE TO THE HASHMAP ************************************************************

            Industry indu = new Industry();
            Interval inter = new Interval();
            // Sets the industry to the correct industry
            for (Industry ind : industries.keySet()) {
                String n = ind.getIndustryName();
                if (n.equals(in)) {
                    indu = ind;
                }
            }

            //ArrayList<Company> tempComp = industries.get();
            Company comp = new Company(indu, sDay, eDay, tick, tDays);


            ArrayList<Company> c = industries.get(indu);
            System.out.println(indu.getIndustryName() + "   " +  tick);
            //if (comp.getMaxStartDay().compareTo(sDay) < 0) {
            //    comp.setMaxStartDay(sDay);
            //    indu.setStartDate(sDay);
            //}
            comp.setMaxStartDay(sDay);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

            try {
                java.util.Date sd1 = sdf.parse(sDay);
                java.util.Date md1 = sdf.parse(indu.getMaxStartDay());

                if (sd1.compareTo(md1) > 0) {
                    indu.setMaxStartDay(sDay);
                    System.out.println("START     " + sDay + "CHANGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                }
            } catch (ParseException e) {
                System.out.println("COULDNT PARSE");
            }

            // UPDATE MAX START DAY

            try {
                java.util.Date ed = sdf.parse(eDay);
                java.util.Date mind = sdf.parse(indu.getMinEndDay());

                if (ed.compareTo(mind) < 0) {
                    indu.setMinEndDay(eDay);
                    System.out.println("END  " + eDay + "    CHANGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

                }
            } catch (ParseException e) {
                System.out.println("COULDNT PARSE");
            }


            c.add(comp);
            //indu.addCompany(comp);
            industries.put(indu, c);
        } else {
            System.out.println("******************************");
            Industry i = new Industry(sDay, eDay, in);
            ArrayList<Company> c = new ArrayList<Company>();
            Company comp = new Company(i, sDay, eDay, tick, tDays);
            listOfIndustries.add(in);
            comp.setMaxStartDay(sDay);
            comp.setMinEndDay(eDay);
            i.setMaxStartDay(sDay);
            i.setMinEndDay(eDay);
            c.add(comp);

            i.setCompanies(c);

            industries.put(i, c);
        }


        /*
        //, results.getString(2), results.getString(3), results.getInt(4));
        //industry = ind
        //System.out.printf(results.getString(1));
        //System.out.println(results.getString(1));

        //System.out.println(results.getInt(2));
        */
    }

    /*
        " WHERE Industry = 'Telecommunications Services' GROUP BY Ticker HAVING TradingDays >= 150 ORDER BY Ticker");
        ArrayList<Day> allDays = new ArrayList<Day>();



        ResultSet results = stmt.executeQuery("SELECT Ticker, min(TransDate) as min, max(TransDate) as max, count(distinct TransDate)" + " FROM PriceVolume GROUP BY Ticker");

        ResultSet results = stmt.executeQuery("SELECT Industry, count(disinct Ticker) as Ticker" + " FROM " + "company" + " NATURAL JOIN " + "PriceVolume " + "GROUP BY Industry");
    */


    stmt.close();
  }


// ******************************************************************************************************************
  static void getIntervals(Industry i, String startDay, String endDay) throws SQLException {


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");


        PreparedStatement pstmt;

        //java.util.Date sd = sdf.parse(startDay);
        //java.util.Date md = sdf.parse(endDay);

        //java.sql.Date sqlSD = new java.sql.Date(sd.getTime());
        //java.sql.Date sqlED = new java.sql.Date(md.getTime());


        pstmt = conn.prepareStatement("SELECT Ticker, TransDate, OpenPrice, ClosePrice " +
        " FROM Company NATURAL JOIN PriceVolume " +
        " WHERE TransDate >= ? AND TransDate <= ? ");

        pstmt.setString(1, startDay);
        pstmt.setString(2, endDay);

        //System.out.println("Start Date:  ************ : " + sqlSD + "END DATE ********  : " + sqlED);


        ResultSet rs = pstmt.executeQuery();

        ArrayList<String> ticks = new ArrayList<String>();


        while (rs.next()) {

            String tick = rs.getString(1);
            String date = rs.getString(2);
            double openP = rs.getDouble(3);
            double closeP = rs.getDouble(4);

            IntervalDay intDay = new IntervalDay(tick, date, openP, closeP);
            int key;


            Company comp = i.getCompany(tick);

            // FIRST COMPANY OF INTERVAL
            if (!ticks.contains(tick)) {
                //System.out.println(tick + "DATE: ((((((((((((((((((( " + date + ")))))))))))))))))))");
                comp.setCurrentInterval(0);
                key = 0;
                ticks.add(tick);
                Interval inter = new Interval();

                comp.createIntervals();


                inter.addDay(intDay);
                inter.setStartDate(date);
                inter.setStartPrice(openP);

                comp.addInterval(key, inter);
                key++;
                //System.out.println("************************************************************************************" + key + ": ");


            // Interval Already Created For Company
            } else {
                //Interval inter = comp.getCurrInterval();
                // Add day to interval
                if (comp.getCurrInterval().getSize() < 60) {
                    Interval inter = comp.getCurrInterval();

                    //inter.setStartDate(date);
                    inter.setEndDate(date);
                    inter.setEndPrice(closeP);

                    inter.addDay(intDay);


                    //System.out.println("*************60 IN A ROWWWWWWWWWWWWWWWWWWWWWWW*********************" + inter.getStartPrice() + "END " + inter.getEndPrice() + "NAME: " + comp.getTicker());

                    //key = 0;
                    //comp.setCurrentInterval(0));

                // Create new interval because first interval is full
                } else {

                    Interval newinter = new Interval();
                    newinter.setStartDate(date);
                    newinter.setStartPrice(openP);
                    //System.out.println("**&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&***EVERY 60*****&&&&&&***&&&&**********" + comp.getCurrentInterval() );
                    //comp.incrementCurrentInterval();
                    comp.addInterval(comp.getCurrentInterval(), newinter);

                    newinter.setSize(0);


                    //key++;

                }

            }

            //System.out.printf("Ticker:  " + tick + "    Date:      " + date + "      Open Price : " + openP + "        Close Price :  " + closeP);

        }

        pstmt.close();


    //System.out.println()
}





// ******************************************************************************************************************

static void checkRatio(IntervalDay day) {


    //System.out.println("XXXXXXXX");
    if (Math.abs((day.getClose()/day.getOpen()) - 2) <  .20) {

      //twoRatio = twoRatio + "," + line + "\t" + String.valueOf(openingValue) + "\t" + "1";
      //twoRation.add(day);
      if (map.containsKey(day.getTicker())) {
         day.splitType = "2:1";
        ArrayList<IntervalDay> dayss = new ArrayList<IntervalDay>();
        dayss = map.get(day.getTicker());
        dayss.add(day);
        map.put(day.getTicker(), dayss);
        //System.out.println("Added existing:    " +  day.getTicker());
      } else {
            day.splitType = "2:1";

            ArrayList<IntervalDay> days = new ArrayList<IntervalDay>();
            days.add(day);
            map.put(day.getTicker(), days);
            //System.out.println("Added NEW    " +  day.getTicker());

      }
      tRat = true;
      totalSplits += 1;
      day.divisor = day.divisor/2.00;
      divMap.put(day.getTicker(), divMap.get(day.getTicker())*2.00);
      //System.out.println("2:1 split on " + day.getDate() + " for " + day.getTicker());
    }
    else if (Math.abs((day.getClose()/day.getOpen()) - 3) <  .30) {

      //twoRatio = twoRatio + "," + line + "\t" + String.valueOf(openingValue) + "\t" + "2";
      //twoRation.add(day);
      if (map.containsKey(day.getTicker())) {
         day.splitType = "3:1";

        ArrayList<IntervalDay> dayss = new ArrayList<IntervalDay>();
        dayss = map.get(day.getTicker());
        dayss.add(day);
        map.put(day.getTicker(), dayss);
        //System.out.println("Added existing:    " +  day.getTicker());
      } else {
        day.splitType = "3:1";

        ArrayList<IntervalDay> days = new ArrayList<IntervalDay>();
        days.add(day);
        map.put(day.getTicker(), days);
        //System.out.println("Added NEW    " +  day.getTicker());

      }
      tRat = true;
      totalSplits += 1;
      day.divisor = day.divisor/3.00;
      divMap.put(day.getTicker(), divMap.get(day.getTicker())*3.00);



      //System.out.println("3:1 split on " + day.getDate() + " for " + day.getTicker());
    }
    else if (Math.abs((day.getClose()/day.getOpen()) - 1.5) <  .15) {

      //twoRatio = twoRatio + "," + line + "\t" + String.valueOf(openingValue) + "\t" + "3";
      //twoRatio.add(day);
      if (map.containsKey(day.getTicker())) {
        day.splitType = "3:2";

        ArrayList<IntervalDay> dayss = new ArrayList<IntervalDay>();
        dayss = map.get(day.getTicker());
        dayss.add(day);
        map.put(day.getTicker(), dayss);
        //System.out.println("Added existing:    " +  day.getTicker());
      } else {
        day.splitType = "3:2";

        ArrayList<IntervalDay> days = new ArrayList<IntervalDay>();
        days.add(day);
        map.put(day.getTicker(), days);
        //System.out.println("Added NEW    " +  day.getTicker());

      }
      tRat = true;
      totalSplits += 1;
      day.divisor = day.divisor/1.5;
      divMap.put(day.getTicker(), divMap.get(day.getTicker())*1.5);


      //System.out.println("3:2 split on " + day.getDate() + " for " + day.getTicker());
    }
  }






  // ******************************************************************************************************************

  static void saveComp() throws SQLException {
   Statement stmt = conn.createStatement();
   ResultSet results = stmt.executeQuery("SELECT Ticker, TransDate, OpenPrice, ClosePrice, HighPrice, LowPrice FROM PriceVolume ORDER BY TransDate ASC");
   //ArrayList<Day> allDays = new ArrayList<Day>();


   while (results.next()) {

     String tick = results.getString(1);
     String date = results.getString(2);
     double o = results.getDouble(3);
     double c = results.getDouble(4);
     double h = results.getDouble(5);
     double l = results.getDouble(6);

     //System.out.printf("Open: %.2f, High: %.2f, Low: %.2f, Close: %.2f%n", results.getDouble(1), results.getDouble(2), results.getDouble(3), results.getDouble(4));
     IntervalDay intDay = new IntervalDay(tick, date, o, c);

     //System.out.println(tick);

     if (!(divMap.containsKey(intDay.getTicker()))) {
       divMap.put(tick, 1.00);
     }

     if (prevDay.getIsEmpty() == false) {
       intDay.previousDay = prevDay;
       prevDay.nextDay = intDay;
       prevDay = intDay;
       intDay.isEmpty = false;
     } else {
       prevDay = intDay;
       intDay.isEmpty = false;
     }



     if (bigMap.containsKey(intDay.getTicker())) {
       bigMap.get(intDay.getTicker()).add(intDay);
     } else {
       ArrayList<IntervalDay> allDays = new ArrayList<IntervalDay>();
       allDays.add(intDay);
       bigMap.put(intDay.getTicker(), allDays);
     }



     checkRatio(intDay);

   //  System.out.printf("%5s %s%n", results.getString("Ticker"), results.getString("Name"));
   }


   stmt.close();
 }


/*
  public void resetMinAndMax() {
      for (Industry ind : industries) {
          ArrayList comps = industries.get(ind);

          String min = "jjjjjjjjjjjjjjjjjjjjjjjjj";
          String max = "  ";

          for (Company comp: comps) {

          }
      }
  }

  */
/*
  public void getTradingDays() throws SQLException {



  }

  public void getData() throws SQLException {

      Statement stmt = conn.createStatement();

      ResultSet results = stmt.executeQuery(" SELECT P.TransDate, P.openPrice, P.closePrice " +
      "FROM PriceVolume P " +
      "WHERE Ticker = 'AMT' AND TransDate >= ''"



  }
*/
  //public void countIntervals() {


 // }











 /*
 if (Integer.parseInt(sd1[0]) > Integer.parseInt(md1[0])) {
     indu.setMaxStartDay(sDay);
     System.out.println("NEWWWWWWWWWWWWWWWWWWWWWW");
 } else if (Integer.parseInt(sd1[0]) == Integer.parseInt(md1[0])) {
     if (Integer.parseInt(sd1[1]) > Integer.parseInt(md1[1])) {
         indu.setMaxStartDay(sDay);
         System.out.println("NEWWWWWWWWWWWWWWWWWWWWWW");

     } else if (Integer.parseInt(sd1[2]) > Integer.parseInt(md1[2])) {
         indu.setMaxStartDay(sDay);
         System.out.println("NEWWWWWWWWWWWWWWWWWWWWWW");

     }
 }
*/

 // UPDATE MIN END DAY
 /*
 String[] ed = eDay.split("\\.");
 String[] mind = indu.getMinEndDay().split("\\.");
 if (Integer.parseInt(ed[0]) < Integer.parseInt(mind[0])) {
     indu.setMinEndDay(eDay);
     System.out.println("END      NEWWWWWWWWWWWWWWWWWWWWWW");

 } else if (Integer.parseInt(ed[0]) == Integer.parseInt(mind[0])) {
     if (Integer.parseInt(ed[1]) < Integer.parseInt(mind[1])) {
         indu.setMinEndDay(eDay);
         System.out.println("END      NEWWWWWWWWWWWWWWWWWWWWWW");

     } else if (Integer.parseInt(ed[2]) < Integer.parseInt(mind[2])) {
         indu.setMinEndDay(eDay);
         System.out.println("END      NEWWWWWWWWWWWWWWWWWWWWWW");

     }
 }
*/

     /*
 if (indu.getMaxStartDay().compareTo(sDay) < 0) {
     comp.setMaxStartDay(sDay);
     indu.setStartDate(sDay);
 }

 //if (eDay.compareTo(indu.getMinEndDay()) < 0) {
 //    comp.setMinEndDay(eDay);
 //    indu.setEndDate(eDay);
 //}


 if (eDay.compareTo(indu.getMinEndDay()) > 0) {
     comp.setMinEndDay(eDay);
     indu.setEndDate(eDay);
 }*/







}
