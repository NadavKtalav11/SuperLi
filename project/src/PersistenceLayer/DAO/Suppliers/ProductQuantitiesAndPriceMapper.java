package PersistenceLayer.DAO.Suppliers;


import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Suppliers.ProductQuantitiesAndPriceDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductQuantitiesAndPriceMapper extends AbstractMapper {

    private String column_1_supplierId = "supplierId";
    private String column_2_productId = "productId";
    private String column_3_amountCanSupply = "amountCanSupply";
    private String column_4_price = "price";
    private String column_5_catalogNumber = "catalogNumber";

    //private IdentityHashMap<Pair<Integer, String>, ProductQuantitiesAndPriceDTO> identityMap;


    public ProductQuantitiesAndPriceMapper() {
        super("ProductQuantitiesAndPrice");
    }

    protected ProductQuantitiesAndPriceDTO convertReaderToObject(ResultSet reader) {
        ProductQuantitiesAndPriceDTO result = null;
        try {
            result = new ProductQuantitiesAndPriceDTO(reader.getInt(column_1_supplierId), reader.getString(column_2_productId),
                    reader.getInt(column_3_amountCanSupply), reader.getDouble(column_4_price), reader.getInt(column_5_catalogNumber));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public List<ProductQuantitiesAndPriceDTO> getProductQuantitiesAndPriceDTO(int supplierId) {
        List<ProductQuantitiesAndPriceDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_1_supplierId + " = " + supplierId;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next())
                objects.add(convertReaderToObject(resultSet));
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public ProductQuantitiesAndPriceDTO selectProductBySupplier(int supplierId, String productId) {
        ProductQuantitiesAndPriceDTO objects;
        String sql = "SELECT * FROM " + table_name + " Where " + column_1_supplierId + " = " + supplierId
                + " And " + column_2_productId + " = " + productId + "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                objects = convertReaderToObject(resultSet);
                return objects;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void removeSupplierDetails(int supplierId) {
        remove(supplierId, column_1_supplierId);
    }

    public void addProductToSupply(ProductQuantitiesAndPriceDTO productQuantitiesAndPriceDTO) {
        String sql = MessageFormat.format("INSERT INTO " + table_name + "({0} , {1}, {2} , {3}, {4}) VALUES(?,?,?,?,?)",
                column_1_supplierId, column_2_productId, column_3_amountCanSupply, column_4_price, column_5_catalogNumber);

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productQuantitiesAndPriceDTO.getSupplierId());
            pstmt.setString(2, productQuantitiesAndPriceDTO.getProductId());
            pstmt.setInt(3, productQuantitiesAndPriceDTO.getAmountCanSupply());
            pstmt.setDouble(4, productQuantitiesAndPriceDTO.getPrice());
            pstmt.setDouble(5, productQuantitiesAndPriceDTO.getCatalogNumber());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeProduct(String productId, int supplierId) {
        remove(supplierId, productId, column_1_supplierId, column_2_productId);
    }

    public void updateProductPrice(int supplierId, String productId, Double newPrice) {
        update(column_4_price, newPrice, column_1_supplierId, column_2_productId, supplierId, productId);
    }

}


