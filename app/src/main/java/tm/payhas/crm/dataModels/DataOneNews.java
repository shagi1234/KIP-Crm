package tm.payhas.crm.dataModels;

import tm.payhas.crm.dataModels.DataNews;

public class DataOneNews {
    private int pageCount;
    private int count;
    private DataNews news;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DataNews getNews() {
        return news;
    }

    public void setNews(DataNews news) {
        this.news = news;
    }
}
