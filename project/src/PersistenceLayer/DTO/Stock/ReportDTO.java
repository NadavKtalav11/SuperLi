package PersistenceLayer.DTO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DAO.Stock.ReportDAO;
import PersistenceLayer.DTO.AbstractDTO;

import java.util.Date;

public class ReportDTO extends AbstractDTO {

    protected final String REPORT_ID;
    protected String name;
    protected String reportDate;

    public ReportDTO(ReportDAO dao, String name, String date, String reportID) {
        super(dao);
        this.REPORT_ID = reportID;
        this.name = name;
        this.reportDate = date;
    }

    public String getREPORT_ID() {
        return REPORT_ID;
    }

    public String getName() {
        return name;
    }

    public String getReportDate() {
        return reportDate;
    }
}
