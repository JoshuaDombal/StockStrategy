

import java.util.*;

public class Company {

    public Industry industry;
    public String startDate;
    public String endDate;
    public String ticker;

    public String maxStartDate;
    public String minEndDate;

    public double tradingDays;


    public double industryReturn;


    HashMap<Integer, Double> avgReturns;


    ArrayList<Day> days;

    public HashMap<Integer, Interval> intervals;
    public int currentInterval = 0;
    public Interval currInterval;

    public double tickerReturn;


    public double startP;
    public double endP;

    public double divisor;





    public Company (Industry industry, String startDate, String endDate, String ticker, double tradingDays) {
        this.industry = industry;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tradingDays = tradingDays;
        this.ticker = ticker;


    }



//     SETTERS *********************************************
    public void setIndustry(Industry ind) {
        this.industry = ind;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setMaxStartDay(String maxStartDate) {
        this.maxStartDate = maxStartDate;
    }

    public void setMinEndDay(String minEndDate) {
        this.minEndDate = minEndDate;
    }

    public void setTradingDays(double tradingDays) {
        this.tradingDays = tradingDays;
    }

    public void createIntervals() {
        this.intervals = new HashMap<Integer, Interval>();
    }


    public void addInterval(int key, Interval interval) {
        intervals.put(key, interval);
        currInterval = interval;
        currentInterval++;
    }

    public void setCurrentInterval(int i) {
        this.currentInterval = i;
    }

    public void incrementCurrentInterval() {
        this.currentInterval++;
    }

    public void removeInterval(int key) {
        intervals.remove(key);
    }

    public void setDivisor(double d) {
        this.divisor =  d;
    }

    public void setIndustryReturn(double ret) {
        this.industryReturn = ret;
    }






//     GETTERS *********************************************

    public Industry getIndustry() {
        return this.industry;
    }

    public String setStartDate() {
        return this.startDate;
    }

    public String setEndDate() {
        return this.endDate;
    }

    public String getTicker() {
        return this.ticker;
    }

    public String getMaxStartDay() {
        return this.maxStartDate;
    }

    public String getMinEndDay() {
        return this.minEndDate;
    }

    public double getTradingDays() {
        return this.tradingDays;
    }

    public int getCurrentInterval() {
        return this.currentInterval;
    }

    public Interval getCurrInterval() {
        return this.currInterval;
    }

    public HashMap<Integer, Interval> getIntervals() {
        return intervals;
    }

    public double getDivisor() {
        return this.divisor;
    }

    public double getIndustryReturn() {
        return this.industryReturn;
    }










}
