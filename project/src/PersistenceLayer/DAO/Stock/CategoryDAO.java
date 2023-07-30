package PersistenceLayer.DAO.Stock;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Stock.CategoryDTO;
import PersistenceLayer.DTO.Stock.ProductDTO;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CategoryDAO extends AbstractMapper{

    private static final String CATEGORY_ID_COLUMN = "CATEGORY_ID";
    private static final String NAME_COLUMN = "name";
    private static final String SUPER_COLUMN = "superCategory";
    private static final String COUNTER_COLUMN = "subCounter";

    private final ProductDAO productDAO;
    
    public CategoryDAO(ProductDAO pdao){
        super("Category");
        productDAO = pdao;
    }

    public boolean checkIfCategoryExists(String categoryID) throws SQLException {
        String sqlQuery = String.format("SELECT * FROM %s WHERE %s = '%s'",
                table_name, CATEGORY_ID_COLUMN, categoryID);
        return checkIfNotEmpty(sqlQuery);
    }

    public CategoryDTO convertReaderToObject(ResultSet reader) {
        CategoryDTO result = null;
        try {
            result = new CategoryDTO(reader.getString(CATEGORY_ID_COLUMN), reader.getString(NAME_COLUMN), reader.getInt(COUNTER_COLUMN), this);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void addCategory(String categoryID, String name, String superCategory) throws Exception {
        String sqlQuery = String.format("INSERT INTO %s Values (?, ?, ?, ?)",table_name);
        try (Connection conn = this.connect();
             PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, categoryID);
            statement.setString(2, name);
            statement.setString(3, superCategory);
            statement.setInt(4, 1);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("fail to add category to category table: "+e.getMessage());
        }
    }


    public List<CategoryDTO> getMainCategories(int branchID) throws SQLException{
        String query = "SELECT * FROM " + table_name + " WHERE " + CATEGORY_ID_COLUMN + " like '" + branchID + "#%' AND " + SUPER_COLUMN + " IS NULL";
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet res = statement.executeQuery(query);
            List<CategoryDTO> categories = new LinkedList<>();
            while(res.next()){
                categories.add(convertReaderToObject(res));
            }
            return categories;
        } catch (SQLException e) {
            throw new SQLException("fail to select categories" + e.getMessage());
        }
    }

    public CategoryDTO getCategory(String categoryID) throws Exception{
        try {
            checkIfCategoryExists(categoryID);
        } catch (Exception ex){
            throw new SQLException(ex.getMessage());
        }
        String query = String.format("Select * From %s where %s = '%s' ",table_name, CATEGORY_ID_COLUMN, categoryID);
        CategoryDTO cdto;
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            cdto = convertReaderToObject(resultSet);
            return cdto;
        } catch(Exception ex){
            throw new SQLException("fail to select category");
        }
    }

    public List<CategoryDTO> getSubCategories(String categoryID) throws Exception{
        try {
            checkIfCategoryExists(categoryID);
        } catch (Exception ex){
            throw new SQLException(ex.getMessage());
        }
        String query = String.format("Select * From %s where %s = '%s' ",table_name, SUPER_COLUMN, categoryID);
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            List<CategoryDTO> categories = new LinkedList<>();
            while(resultSet.next()) {
                categories.add(convertReaderToObject(resultSet));
            }
            return categories;
        } catch(Exception ex){
            ex.printStackTrace();
            throw new SQLException("fail to select categories");
        }
    }

    public List<ProductDTO> getCategoriesProducts(String categoryID) throws SQLException{
        try {
            checkIfCategoryExists(categoryID);
        } catch (SQLException ex){
            throw new SQLException(ex.getMessage());
        }
        return productDAO.getCategoriesProducts(categoryID);
    }

    public List<CategoryDTO> getCategories(int branchID) throws SQLException{
        String query = "SELECT * FROM " + table_name + " WHERE " + CATEGORY_ID_COLUMN + " like '" + branchID + "#%'";
        try (Connection conn = this.connect();
             Statement statement = conn.createStatement()){
            ResultSet res = statement.executeQuery(query);
            List<CategoryDTO> categories = new LinkedList<>();
            while(res.next()){
                categories.add(convertReaderToObject(res));
            }
            return categories;
        } catch (SQLException e) {
            throw new SQLException("fail to select categories");
        }
    }

    public void updateSubCategoryID(int newCategory, String categoryID){
        update(COUNTER_COLUMN, newCategory, CATEGORY_ID_COLUMN, categoryID);
    }

}
