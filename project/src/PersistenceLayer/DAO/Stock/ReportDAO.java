package PersistenceLayer.DAO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Stock.ReportDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReportDAO extends AbstractMapper{

    private static final String REPORT_ID_COLUMN = "REPORT_ID";
    private static final String NAME_COLUMN = "name";
    private static final String DATE_COLUMN = "date";

    private static final String CATEGORIES_TABLE =  "ReportToCategory";
    private static final String REPORT_ID_COLUMN2 = "REPORT_ID";
    private static final String CATEGORY_ID_COLUMN2 = "CATEGORY_ID";

    private static final String DAMAGES_TABLE =  "DamagedReportToDamageType";
    private static final String REPORT_ID_COLUMN3 = "REPORT_ID";
    private static final String DAMAGE_TYPE_COLUMN3 = "damageType";

    public ReportDAO() {
        super("Report");
    }

    @Override
    protected ReportDTO convertReaderToObject(ResultSet reader) {
        ReportDTO result = null;
        try {
            result = new ReportDTO(this , reader.getString(NAME_COLUMN), reader.getString(DATE_COLUMN), reader.getString(REPORT_ID_COLUMN));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void addReport(String reportID, String name, String date, List<String> categories) throws Exception {
        String sqlQuery = String.format("INSERT INTO %s Values (?,?,?)", table_name);
        try(Connection conn = this.connect();
            PreparedStatement statement = conn.prepareStatement(sqlQuery)){
            statement.setString(1, reportID);
            statement.setString(2, name);
            statement.setString(3, date);
            statement.executeUpdate();
        } catch (Exception e){
            throw new Exception("fail to insert new report because: "+ e.getMessage());
        }

        for (String category: categories) {
            sqlQuery = String.format("INSERT INTO %s Values (?,?)", CATEGORIES_TABLE);
            try(Connection conn = this.connect();
                PreparedStatement statement = conn.prepareStatement(sqlQuery)){
                statement.setString(1, reportID);
                statement.setString(2, category);
                statement.executeUpdate();
            } catch (Exception e){
                throw new Exception("fail to insert report to categories table because: "+ e.getMessage());
            }
        }

    }

    public void addDamages(String id, List<String> damageList) throws Exception{
        for (String damage: damageList) {
            String sqlQuery = String.format("INSERT INTO %s Values (?,?)", DAMAGES_TABLE);
            try(Connection conn = this.connect();
                PreparedStatement statement = conn.prepareStatement(sqlQuery)){
                statement.setString(1, id);
                statement.setString(2, damage);
                statement.executeUpdate();
            } catch (Exception e){
                throw new Exception("fail to insert new damage report to damages table because: "+ e.getMessage());
            }
        }
    }

    public void removeAll(){
        String sql = "DELETE FROM " + table_name + "" ;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "DELETE FROM " + CATEGORIES_TABLE + "" ;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "DELETE FROM " + DAMAGES_TABLE + "" ;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
