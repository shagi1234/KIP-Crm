package tm.payhas.crm.dataModels;

import java.util.ArrayList;

public class DataDashboard {
    private int count;
    private int pageCount;
    private ArrayList<DataNews> news;
    private ArrayList<DataNews> holidays;
    private ArrayList<DataBirthdays> birthdays;

    public ArrayList<DataNews> getNews() {
        return news;
    }

    public void setNews(ArrayList<DataNews> news) {
        this.news = news;
    }

    public ArrayList<DataNews> getHolidays() {
        return holidays;
    }

    public void setHolidays(ArrayList<DataNews> holidays) {
        this.holidays = holidays;
    }

    public ArrayList<DataBirthdays> getBirthdays() {
        return birthdays;
    }

    public void setBirthdays(ArrayList<DataBirthdays> birthdays) {
        this.birthdays = birthdays;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

}
