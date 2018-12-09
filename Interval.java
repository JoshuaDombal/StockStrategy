


public class Interval {

    int intervalID;

    String startDate;
    String endDate;

    double startPrice;
    double endPrice;

    int size;

    public double intervalSum;


    IntervalDay[] days;

    public Interval() {
        this.days = new IntervalDay[60];
    }
    public Interval(int intervalID, String startDate, String endDate, double startPrice, double endPrice) {
        this.intervalID = intervalID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.days = new IntervalDay[60];
        this.size = 0;
    }

    public void addDay(IntervalDay d) {
        days[size] = d;
        size++;
    }


    public void setSize(int s) {
        this.size = s;
    }

    public void setStartDate(String sd) {
        this.startDate = sd;
    }

    public void setEndDate(String ed) {
        this.endDate = ed;
    }

    public void setStartPrice(double sp) {
        this.startPrice = sp;
    }

    public void setEndPrice(double ep) {
        this.endPrice = ep;
    }

    public void setIntervalSum(double d) {
        this.intervalSum = d;
    }


    public int getSize() {
        return this.size;
    }




    public int getDayNumber() {
        return this.size + 1;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public double getStartPrice() {
        return this.startPrice;
    }

    public double getEndPrice() {
        return this.endPrice;
    }

    public IntervalDay[] getIntervalDays() {
        return this.days;
    }

    public double getIntervalSum() {
        return this.intervalSum;
    }

}
