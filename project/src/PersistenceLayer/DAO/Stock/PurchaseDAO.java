package PersistenceLayer.DAO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Stock.PurchaseDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseDAO extends AbstractMapper {

    private static final String PURCHASE_ID_COLUMN = "PURCHASE_ID";
    private static final String DATE_COLUMN = "date";
    private static final String TOTAL_PRICE_COLUMN = "totalPrice";
    private static final String table_PurchaseToItemPrice = "PurchaseToItemPrice";

    public PurchaseDAO() {
        super("Purchase");
    }

    @Override
    protected PurchaseDTO convertReaderToObject(ResultSet reader) {
        PurchaseDTO result = null;
        try {
            result = new PurchaseDTO(this, reader.getString(PURCHASE_ID_COLUMN), reader.getString(DATE_COLUMN), reader.getDouble(TOTAL_PRICE_COLUMN));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void updateTotalPrice(double totalPrice, String purchaseID) {
        update(TOTAL_PRICE_COLUMN, totalPrice, PURCHASE_ID_COLUMN, purchaseID);
    }

    public void addNewPurchase(String purchaseID, String date) throws SQLException{
        String sqlQuery = String.format("INSERT INTO %s Values (?, ?, ?)",table_name);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, purchaseID);
            statement.setString(2, date);
            statement.setDouble(3, 0);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new SQLException("fail to add purchase to purchase table: "+e.getMessage());
        }
    }

    public void addToItemPriceTable(String purchaseID, String itemID, double supplierPrice, double sellingPrice) throws Exception{
        String sqlQuery = String.format("INSERT INTO %s Values (?, ?, ?, ?)",table_PurchaseToItemPrice);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, purchaseID);
            statement.setString(2, itemID);
            statement.setDouble(3, supplierPrice);
            statement.setDouble(4, sellingPrice);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new SQLException("fail to add purchased item to PurchaseToItemPrice table: "+e.getMessage());
        }
    }

    public void updateTotalPriceAfterFinish(String purchaseID, double totalPrice) {
        updateTotalPrice (totalPrice, purchaseID);
    }
    public void removeAllPurchaseItem() {
        String sql = "DELETE FROM " + table_PurchaseToItemPrice ;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeAllFromSubTables () throws Exception{
        removeAllPurchaseItem();
    }

}
