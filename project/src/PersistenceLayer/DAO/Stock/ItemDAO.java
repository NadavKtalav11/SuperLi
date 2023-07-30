package PersistenceLayer.DAO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Stock.ItemDTO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ItemDAO extends AbstractMapper {

    private static final String ITEM_ID_COLUMN = "ITEM_ID";
    private static final String EXPIRATION_COLUMN = "expirationDate";
    private static final String LOCATION_COLUMN = "location";
    private static final String IN_LOCATION_COLUMN = "inLocationPosition";
    private static final String IS_DAMAGED_COLUMN = "isDamaged";
    private static final String SUPPLIER_PRICE_COLUMN = "supplierPrice";
    private static final String DISCOUNT_PRICE_COLUMN = "discountPrice";
    private static final String DAMAGE_TYPE_COLUMN = "damageType";
    private static final String PRODUCT_ID_COLUMN = "productID";
    //private static final String table_ProductToDamageToItem = "ProductToDamageToItem";


    public ItemDAO() {
        super("Item");
    }

    @Override
    protected ItemDTO convertReaderToObject(ResultSet reader) {
        ItemDTO result = null;
        try {
            result = new ItemDTO(this, reader.getString(ITEM_ID_COLUMN), reader.getString(EXPIRATION_COLUMN), reader.getString(LOCATION_COLUMN), reader.getDouble(SUPPLIER_PRICE_COLUMN), reader.getDouble(DISCOUNT_PRICE_COLUMN), reader.getInt(IS_DAMAGED_COLUMN), reader.getString(DAMAGE_TYPE_COLUMN), reader.getString(IN_LOCATION_COLUMN));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void addItem(String itemID, String date, String location, String inLocation, double purchasePrice, double sellingPrice, String productID) throws SQLException {
        String sqlQuery = String.format("INSERT INTO %s Values (?, ?, ?, ?, ?, ?, ?, ?, ?)",table_name);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, itemID);
            statement.setString(2, date);
            statement.setString(3, location);
            statement.setString(4, inLocation);
            statement.setInt(5, 0);
            statement.setDouble(6, purchasePrice);
            statement.setDouble(7, sellingPrice);
            statement.setString(8, "NONE");
            statement.setString(9, productID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("fail to add item to item table: "+e.getMessage());
        }
    }

    public void updateIsDamaged(int isDamaged, String itemID) {
        update(IS_DAMAGED_COLUMN, isDamaged, ITEM_ID_COLUMN, itemID);
    }

    public void updateDiscountPrice(double newPrice, String itemID) {
        update(DISCOUNT_PRICE_COLUMN, newPrice, ITEM_ID_COLUMN, itemID);
    }

    public void updateLocation(String newLocation, String itemID) {
        update(LOCATION_COLUMN, newLocation, ITEM_ID_COLUMN, itemID);
    }

    public List<ItemDTO> getItems(String productID) throws SQLException{
        String query = String.format("Select * From %s where %s = '%s' ",table_name, PRODUCT_ID_COLUMN, productID);
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            List<ItemDTO> items = new LinkedList<>();
            while(resultSet.next()) {
                items.add(convertReaderToObject(resultSet));
            }
            return items;
        } catch(SQLException ex) {
            throw new SQLException("fail to select items" + ex.getMessage());
        }
    }

    public void updateDamageType(String damage, String itemID) {
        update(DAMAGE_TYPE_COLUMN, damage, ITEM_ID_COLUMN, itemID);
    }

    public void deleteItem(String itemID) throws Exception{
        remove(ITEM_ID_COLUMN, itemID);
    }

    /*
    public void deleteItemFromProdDamTable(String itemID) throws Exception{
        String query = String.format("Delete From %s where %s = '%s'", table_ProductToDamageToItem, "itemID", itemID);
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            int result = statement.executeUpdate(query);
        }
        catch(SQLException ex) {
            throw new SQLException("fail to delete item from ProductToDamageToItem" + ex.getMessage());
        }
    }
     */
}
