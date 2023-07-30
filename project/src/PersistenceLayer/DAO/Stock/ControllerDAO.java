package PersistenceLayer.DAO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Stock.ControllerDTO;

import java.sql.*;

public class ControllerDAO extends AbstractMapper{

    private static final String REPORT_COLUMN = "reportCounter";
    private static final String CATEGORY_COLUMN = "categoryCounter";
    private static final String PURCHASE_COLUMN = "purchaseCounter";
    private static final String DISCOUNT_COLUMN = "discountCounter";
    private static final String ORDER_COLUMN = "OrderCounter";
    private static final String BRANCH_COLUMN = "BRANCH_ID";

    public ControllerDAO(){
        super("StockIDController");
    }

    @Override
    protected ControllerDTO convertReaderToObject(ResultSet reader) {
        ControllerDTO result = null;
        try {
            result = new ControllerDTO(this, reader.getInt(DISCOUNT_COLUMN), reader.getInt(CATEGORY_COLUMN), reader.getInt(PURCHASE_COLUMN), reader.getInt(REPORT_COLUMN), reader.getInt(ORDER_COLUMN), reader.getInt(BRANCH_COLUMN));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public boolean checkIfControllerExists(int branchID) throws SQLException {
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'",
                table_name, BRANCH_COLUMN, branchID);
        return checkIfNotEmpty(sqlQuery);
    }

    public void addController(int discountCounter, int mainCategoryCounter, int purchaseCounter, int reportCounter, int orderCounter, int branchID) throws SQLException {
        String sqlQuery = String.format("INSERT INTO %s Values (?, ?, ?, ?, ?, ?)",table_name);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setInt(1, reportCounter);
            statement.setInt(2, mainCategoryCounter);
            statement.setInt(3, purchaseCounter);
            statement.setInt(4, discountCounter);
            statement.setInt(5, orderCounter);
            statement.setInt(6, branchID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("fail to add controller to controller table: "+e.getMessage());
        }
    }

    public ControllerDTO getController(int branchID) throws SQLException{
        try {
            checkIfControllerExists(branchID);
        } catch (Exception ex){
            throw new SQLException(ex.getMessage());
        }
        String query = String.format("Select * From %s where %s = %d ",table_name, BRANCH_COLUMN, branchID);
        ControllerDTO cdto;
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            cdto = convertReaderToObject(resultSet);
            return cdto;
        } catch(Exception ex){
            throw new SQLException("fail to select Controller");
        }
    }

    public void updateDiscountID(int newDiscount, int branchID){
        update(DISCOUNT_COLUMN, newDiscount, BRANCH_COLUMN, branchID);
    }

    public void updateCategoryID(int newCategory, int branchID){
        update(CATEGORY_COLUMN, newCategory, BRANCH_COLUMN, branchID);
    }

    public void updateReportID(int newReport, int branchID){
        update(REPORT_COLUMN, newReport, BRANCH_COLUMN, branchID);
    }

}
