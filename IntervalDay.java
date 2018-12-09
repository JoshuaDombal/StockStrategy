



public class IntervalDay {


    String ticker;
    String transDate;
    double openPrice;
    double closePrice;



    double divisor = 1;

    public IntervalDay previousDay;
    public IntervalDay nextDay;

    public boolean isEmpty = true;
    public String splitType;





    public IntervalDay() {
        isEmpty = true;
    }

    public IntervalDay(String ticker, String transDate, double openPrice, double closePrice) {

        this.ticker = ticker;
        this.transDate = transDate;
        this.openPrice = openPrice;
        this.closePrice = closePrice;
    }


    public double getClose() {
      return this.closePrice;
    }

    public double getOpen() {
      return this.openPrice;
    }

    public String getTicker() {
      return this.ticker;
    }

    public String getTransDate() {
        return this.transDate;
    }

    public double getOpenPrice() {
        return this.openPrice;
    }

    public double getClosePrice() {
        return this.closePrice;
    }

    public double getDivisor() {
        return this.divisor;
    }

    public boolean getIsEmpty() {
        return this.isEmpty;
    }

    public IntervalDay getPreviousDay() {
      return this.previousDay;
    }

    public IntervalDay getNextDay() {
      return this.nextDay;
    }


// ********* SETTERS *****************

    public void setDivisor(double d) {
        this.divisor = d;
    }






}
