package PersistenceLayer.DAO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Stock.ProductDTO;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProductDAO extends AbstractMapper{

    private static final String PRODUCT_ID_COLUMN = "PRODUCT_ID";
    private static final String NAME_COLUMN = "name";
    private static final String STORE_AMOUNT_COLUMN = "storeAmount";
    private static final String WAREHOUSE_AMOUNT_COLUMN = "warehouseAmount";
    private static final String DAMAGED_AMOUNT_COLUMN = "damagedAmount";
    private static final String NOTIFICATION_AMOUNT_COLUMN = "notificationAmount";
    private static final String PURCHASE_PRICE_COLUMN = "purchasePrice";
    private static final String SELLING_PRICE_COLUMN = "sellingPrice";
    private static final String MANUFACTURER_COLUMN = "manufacture";
    private static final String DEMAND_COLUMN = "demand";
    private static final String SUPPLY_TIME_COLUMN = "supplyTime";
    private static final String IS_ORDERED_COLUMN = "isOrdered";
    private static final String CATEGORY_COLUMN = "categoryID";
    private static final String ITEM_COLUMN = "itemCounter";

    public ProductDAO(){
        super("Product");
    }

    public ProductDTO convertReaderToObject(ResultSet reader) {
        ProductDTO result = null;
        try {
            result = new ProductDTO(reader.getString(PRODUCT_ID_COLUMN), reader.getString(NAME_COLUMN), reader.getInt(NOTIFICATION_AMOUNT_COLUMN), reader.getDouble(PURCHASE_PRICE_COLUMN), reader.getDouble(SELLING_PRICE_COLUMN), reader.getString(MANUFACTURER_COLUMN), reader.getInt(DEMAND_COLUMN), reader.getInt(SUPPLY_TIME_COLUMN), reader.getString(CATEGORY_COLUMN), reader.getInt(ITEM_COLUMN), reader.getInt(STORE_AMOUNT_COLUMN), reader.getInt(WAREHOUSE_AMOUNT_COLUMN), reader.getInt(DAMAGED_AMOUNT_COLUMN), reader.getInt(IS_ORDERED_COLUMN), this);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public boolean checkIfProductExists(String productID) throws SQLException {
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'",
                table_name, PRODUCT_ID_COLUMN, productID);
        return checkIfNotEmpty(sqlQuery);
    }

    public ProductDTO getProduct(String productId) throws SQLException {
        try {
            checkIfProductExists(productId);
        } catch (Exception ex){
            throw new SQLException(ex.getMessage());
        }
        String query = String.format("Select * From %s where %s = '%s' ",table_name, PRODUCT_ID_COLUMN, productId);
        ProductDTO pdto;
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            pdto = convertReaderToObject(resultSet);
            return pdto;
        } catch(Exception ex){
            throw new SQLException("fail to select products");
        }
    }

    public void addProduct(String productID, String name, double purchasePrice, double sellingPrice, String manufacture, int demand, int supplyTime, int initialNotificationAmount, String categoryID) throws Exception {
        String sqlQuery = String.format("INSERT INTO %s Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",table_name);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, productID);
            statement.setString(2, name);
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            statement.setInt(5, 0);
            statement.setInt(6, initialNotificationAmount);
            statement.setDouble(7, purchasePrice);
            statement.setDouble(8, sellingPrice);
            statement.setString(9, manufacture);
            statement.setInt(10, demand);
            statement.setInt(11, supplyTime);
            statement.setInt(12, 0);
            statement.setString(13, categoryID);
            statement.setInt(14, 1);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("fail to add product to product table: "+e.getMessage());
        }
    }

    public List<ProductDTO> getCategoriesProducts(String categoryID) throws SQLException{
        String query = String.format("Select * From %s where %s = '%s' ",table_name, CATEGORY_COLUMN, categoryID);
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            List<ProductDTO> products = new LinkedList<>();
            while(resultSet.next()) {
                products.add(convertReaderToObject(resultSet));
            }
            return products;
        } catch(SQLException ex) {
            throw new SQLException("fail to select categories" + ex.getMessage());
        }
    }

    public Map<String, List<ProductDTO>> loadAllCategoriesProducts(List<String> categories) throws SQLException{
        Map<String, List<ProductDTO>> ans = new HashMap<>();
        for (String category: categories) {
            ans.put(category, getCategoriesProducts(category));
        }
        return ans;
    }

    public void updateStoreAmount(int storeAmount, String productID){
        update(STORE_AMOUNT_COLUMN, storeAmount, PRODUCT_ID_COLUMN, productID);
    }

    public void updateWarehouseAmount(int warehouseAmount, String productID){
        update(WAREHOUSE_AMOUNT_COLUMN, warehouseAmount, PRODUCT_ID_COLUMN, productID);
    }
    public void updateNotifyAmount(int notify, String productID){
        update(NOTIFICATION_AMOUNT_COLUMN, notify, PRODUCT_ID_COLUMN, productID);
    }

    public void updateCounter(int itemCounter, String productID) {
        update(ITEM_COLUMN, itemCounter, PRODUCT_ID_COLUMN, productID);
    }

    public void updatePurchasePrice(double price, String productID) {
        update(PURCHASE_PRICE_COLUMN, price, PRODUCT_ID_COLUMN, productID);
    }

    public void updateSellingPrice(double price, String productID) {
        update(SELLING_PRICE_COLUMN, price, PRODUCT_ID_COLUMN, productID);
    }

    public void updateIsOrdered(int newIsOrdered, String productID) {
        update(IS_ORDERED_COLUMN, newIsOrdered, PRODUCT_ID_COLUMN, productID);
    }

    public void updateSupplyTime(int time, String productID) {
        update(SUPPLY_TIME_COLUMN, time, PRODUCT_ID_COLUMN, productID);
    }

    public void updateDemand(int newDemand, String productID) {
        update(DEMAND_COLUMN, newDemand,PRODUCT_ID_COLUMN, productID);
    }

    public void updateDamagedAmount(int amountToSet, String product_id) {
        update(DAMAGED_AMOUNT_COLUMN, amountToSet,PRODUCT_ID_COLUMN, product_id);
    }
}
