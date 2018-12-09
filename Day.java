public class Day {


    public String ticker;
    public String date;
    public double Open;
    public double High;
    public double Low;
    public double Close;
    public double divisor;
    public Day previousDay;
    public Day nextDay;
    public boolean isEmpty = true;
    public double movingAve = 0;
    public String splitType;

    public Day () {
      isEmpty = true;
    }

    public Day (String ticker, String date, double Open, double Close, double High, double Low) {
        this.ticker = ticker;
        this.date = date;
        this.Open = Open;
        this.High = High;
        this.Low = Low;
        this.Close = Close;
        this.divisor = 1;
        //this.previousDay = previousDay;
        this.isEmpty = false;
        this.movingAve = 0;
    }


    public String getTicker() {
      return this.ticker;
    }

    public String getDate() {
      return this.date;
    }

    public double getOpen() {
      return this.Open;
    }

    public double getHigh() {
      return this.High;
    }

    public double getLow() {
      return this.Low;
    }

    public double getClose() {
      return this.Close;
    }

    public double getDivisor() {
      return this.divisor;
    }

    public Day getPreviousDay() {
      return this.previousDay;
    }

    public Day getNextDay() {
      return this.nextDay;
    }

    public boolean getIsEmpty() {
      return this.isEmpty;
    }

    public double getMovingAverage() {
      return this.movingAve;
    }

    public String getSplitType() {
        return this.splitType;
    }




}
