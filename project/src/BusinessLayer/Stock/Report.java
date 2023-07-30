package BusinessLayer.Stock;

import java.util.Date;
import java.util.List;

public abstract class Report {

    protected final String REPORT_ID;
    protected String name;
    protected Date reportDate;
    protected List<Category> categories;

    public Report(String reportID, String name, List<Category> categories , Date date){
        this.REPORT_ID = reportID;
        this.name = name;
        this.reportDate = date;
        this.categories = categories;
    }

    public abstract String toString();
}