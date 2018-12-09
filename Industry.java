
import java.util.*;


public class Industry {



    //public String tickerSymbol;
    public String startDate;
    public String endDate;
    public String industryName;

    public String maxStartDate;
    public String minEndDate;

    public double tradingDays;
    public int p;


    public ArrayList<Company> companies;

    //public ArrayList<Interval> intervals;


    public ArrayList<HashMap<String, Day>> days;


    public Industry() {

    }

    public Industry (String startDate, String endDate, String industryName) {
        //this.tickerSymbol = tickerSymbol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.industryName = industryName;
        //this.intervals = new ArrayList<Interval>();
    }

    public int calculateP() {
        double d = tradingDays/60;
        int p = (int)d;

        return p;
    }


    //     SETTERS *********************************************
        //public void setTicker(String tickerSymbol) {
        //    this.tickerSymbol = tickerSymbol;
        //}

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public void setIndustryName(String industryName) {
            this.industryName = industryName;
        }

        public void setMaxStartDay(String maxStartDate) {
            this.maxStartDate = maxStartDate;
        }

        public void setMinEndDay(String minEndDate) {
            this.minEndDate = minEndDate;
        }

        public void setCompanies(ArrayList<Company> companies) {
            this.companies = companies;
        }



        public void addCompany(Company c) {
            companies.add(c);
        }

        //public void addInterval(Interval i) {
        //    intervals.add(i);
        //}


    //     GETTERS *********************************************

        //public String getTicker() {
        //    return this.tickerSymbol;
        //}

        public String getStartDate() {
            return this.startDate;
        }

        public String getEndDate() {
            return this.endDate;
        }

        public String getIndustryName() {
            return this.industryName;
        }

        public String getMaxStartDay() {
            return this.maxStartDate;
        }

        public String getMinEndDay() {
            return this.minEndDate;
        }

        public ArrayList<Company> getCompanies() {
            return companies;
        }

        public Company getCompany(String compName) {

            Company comp = companies.get(0);

            if (companies.isEmpty()) {
                System.out.println("EMPTY");
            }
            //System.out.println(companies.size());
            for (Company c : companies) {
                if (c.getTicker() != null) {
                    //System.out.println(c.getTicker());
                    if (c.getTicker().equals(compName)) {
                        comp = c;
                    }
                } else {
                    System.out.println("WHY IS THIS HAPPENING");
                }


            }

            return comp;

        }

        //public ArrayList<Interval> getIntervals() {
        //    return intervals;
        //}



        public double getMinTradingDays() {
            double minTDays = 100000;
            for (Company c : companies) {
                //System.out.println(c.getTicker());
                //System.out.println(c.getTradingDays());
                if (c.getTradingDays() >= 150) {
                    if (c.getTradingDays() < minTDays) {
                        minTDays = c.getTradingDays();
                    }
                }
            }
            this.tradingDays = minTDays;

            return minTDays;
        }



}
