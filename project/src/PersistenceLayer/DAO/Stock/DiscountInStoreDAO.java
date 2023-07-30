package PersistenceLayer.DAO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Stock.DiscountInStoreDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountInStoreDAO extends AbstractMapper {

    private static final String DISCOUNT_ID_COLUMN = "DISCOUNT_ID";
    private static final String START_DATE_COLUMN = "startDate";
    private static final String END_DATE_COLUMN = "endDate";
    private static final String DISCOUNT_VALUE_COLUMN = "discountValue";
    private static final String IS_PERCENT_COLUMN = "isPercent";
    private static final String DISCOUNT_TYPE_COLUMN = "discountType";
    private static final String table_CategoryToDiscount = "CategoryToDiscount";
    private static final String table_ProductToDamageToDiscount = "ProductToDamageToDiscount";


    public DiscountInStoreDAO(){
        super("DiscountInStore");
    }


    @Override
    protected DiscountInStoreDTO convertReaderToObject(ResultSet reader) {
        DiscountInStoreDTO result = null;
        try {
            result = new DiscountInStoreDTO(this, reader.getString(DISCOUNT_ID_COLUMN), reader.getString(START_DATE_COLUMN), reader.getString(END_DATE_COLUMN), reader.getDouble(DISCOUNT_VALUE_COLUMN), reader.getInt(IS_PERCENT_COLUMN), reader.getString(DISCOUNT_TYPE_COLUMN));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    protected String convertReaderToString(ResultSet reader, String columnName) {
        String result = "";
        try {
            result = reader.getString(columnName);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void addDiscount(String discountID, String sDate, String eDate, double value, int isPercent, String disType) throws SQLException {
        String sqlQuery = String.format("INSERT INTO %s Values (?, ?, ?, ?, ?, ?)",table_name);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, discountID);
            statement.setString(2, sDate);
            statement.setString(3, eDate);
            statement.setInt(4, isPercent);
            statement.setDouble(5, value);
            statement.setString(6, disType);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new SQLException("fail to add discount to discount table: "+e.getMessage());
        }
    }

    public void addDiscountToDisCatTable(String discountID, String categoryID) throws SQLException {
        String sqlQuery = String.format("INSERT INTO %s Values (?, ?)",table_CategoryToDiscount);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, categoryID);
            statement.setString(2, discountID);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new SQLException("fail to add discount to CategoryToDiscount table: "+e.getMessage());
        }
    }

    public void addDiscountToDisProdDamTable(String discountID, String productID, String damageType) throws SQLException {
        String sqlQuery = String.format("INSERT INTO %s Values (?, ?, ?)",table_ProductToDamageToDiscount);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, productID);
            statement.setString(2, damageType);
            statement.setString(3, discountID);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new SQLException("fail to add discount to ProductToDamageToDiscount table: "+e.getMessage());
        }
    }

    public List<DiscountInStoreDTO> getCategoryDiscounts(String categoryID) throws SQLException{
        List<String> discountIDs = new ArrayList<>();
        List<DiscountInStoreDTO> discountInStoreDTOS = new ArrayList<>();
        String query = String.format("Select %s From %s where %s = '%s' ", "discountID", table_CategoryToDiscount, "categoryID", categoryID);
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                discountIDs.add(convertReaderToString(resultSet, "discountID"));
            }
        }
        catch(SQLException ex) {
            throw new SQLException("fail to select discountIDs from CategoryToDiscount" + ex.getMessage());
        }
        if (discountIDs.size() != 0) {
            for (String discountID : discountIDs) {
                query = String.format("Select * From %s where %s = '%s' ",table_name, DISCOUNT_ID_COLUMN, discountID);
                try (Connection conn = this.connect();
                     Statement statement = conn.createStatement()){
                    ResultSet resultSet = statement.executeQuery(query);
                    while(resultSet.next()) {
                        discountInStoreDTOS.add(convertReaderToObject(resultSet));
                    }
                }
                catch(SQLException ex) {
                    throw new SQLException("fail to select discountIDs from ProductToDamageToDiscount" + ex.getMessage());
                }
            }
        }
        return discountInStoreDTOS;
    }


    public List<DiscountInStoreDTO> getAllProductDiscounts(String productID) throws SQLException{
        List<String> discountIDs = new ArrayList<>();
        List<DiscountInStoreDTO> discountInStoreDTOS = new ArrayList<>();
        String query = String.format("Select %s From %s where %s = '%s' ", "discountID",table_ProductToDamageToDiscount, "productID", productID);
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                discountIDs.add(convertReaderToString(resultSet, "discountID"));
            }
        }
        catch(SQLException ex) {
            throw new SQLException("fail to select discountIDs from ProductToDamageToDiscount" + ex.getMessage());
        }
        if (discountIDs.size() != 0) {
            for (String discountID : discountIDs) {
                query = String.format("Select * From %s where %s = '%s' ",table_name, DISCOUNT_ID_COLUMN, discountID);
                try (Connection conn = this.connect();
                     Statement statement = conn.createStatement()){
                    ResultSet resultSet = statement.executeQuery(query);
                    while(resultSet.next()) {
                        discountInStoreDTOS.add(convertReaderToObject(resultSet));
                    }
                }
                catch(SQLException ex) {
                    throw new SQLException("fail to select discountIDs from ProductToDamageToDiscount" + ex.getMessage());
                }
            }
        }
        return discountInStoreDTOS;
    }



    public List<DiscountInStoreDTO> getProductDamageDiscounts(String productID, String damageType) throws SQLException{
        List<String> discountIDs = new ArrayList<>();
        List<DiscountInStoreDTO> discountInStoreDTOS = new ArrayList<>();
        String query = String.format("Select %s From %s where %s = '%s' AND %s = '%s'", "discountID",table_ProductToDamageToDiscount, "productID", productID, "damageType", damageType);
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                discountIDs.add(convertReaderToString(resultSet, "discountID"));
            }
        }
        catch(SQLException ex) {
            throw new SQLException("fail to select discountIDs from ProductToDamageToDiscount" + ex.getMessage());
        }
        if (discountIDs.size() != 0) {
            for (String discountID : discountIDs) {
                query = String.format("Select * From %s where %s = '%s' ",table_name, DISCOUNT_ID_COLUMN, discountID);
                try (Connection conn = this.connect();
                     Statement statement = conn.createStatement()){
                    ResultSet resultSet = statement.executeQuery(query);
                    while(resultSet.next()) {
                        discountInStoreDTOS.add(convertReaderToObject(resultSet));
                    }
                }
                catch(SQLException ex) {
                    throw new SQLException("fail to select discountIDs from ProductToDamageToDiscount" + ex.getMessage());
                }
            }
        }
        return discountInStoreDTOS;
    }

    public void updateStartDate(String newDate, String discountID) {
        update(START_DATE_COLUMN, newDate, DISCOUNT_ID_COLUMN, discountID);
    }

    public void updateEndDate(String newDate, String discountID) {
        update(END_DATE_COLUMN, newDate, DISCOUNT_ID_COLUMN, discountID);
    }

    public void updateDiscountValue(Double value, String discountID) {
        update(DISCOUNT_VALUE_COLUMN, value, DISCOUNT_ID_COLUMN, discountID);
    }

    public void updateIsPercent(int isPercent, String discountID) {
        update(IS_PERCENT_COLUMN, isPercent, DISCOUNT_ID_COLUMN, discountID);
    }

    public void deleteDiscount(String discountID) throws Exception{
        remove(DISCOUNT_ID_COLUMN, discountID);
    }

    public void deleteDiscountFromDisCatTable(String discountID, String categoryID) throws Exception{
        String query = String.format("Delete From %s where %s = '%s' AND %s = '%s'", table_CategoryToDiscount, "discountID", discountID, "categoryID", categoryID);
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            int result = statement.executeUpdate(query);
        }
        catch(SQLException ex) {
            throw new SQLException("fail to delete discount from CategoryToDiscount" + ex.getMessage());
        }
    }

    public void deleteDiscountFromDisProdDamTable(String discountID, String productID, String damageType) throws Exception{
        String query = String.format("Delete From %s where %s = '%s' AND %s = '%s' AND %s = '%s'", table_ProductToDamageToDiscount, "discountID", discountID, "productID", productID, "damageType",  damageType);
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            int result = statement.executeUpdate(query);
        }
        catch(SQLException ex) {
            throw new SQLException("fail to delete discount from ProductToDamageToDiscount" + ex.getMessage());
        }
    }

    public void removeAllFromSubTables () throws Exception{
        String sql = "DELETE FROM " + table_CategoryToDiscount ;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        sql = "DELETE FROM " + table_ProductToDamageToDiscount ;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
