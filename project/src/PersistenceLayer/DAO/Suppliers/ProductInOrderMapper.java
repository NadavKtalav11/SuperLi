package PersistenceLayer.DAO.Suppliers;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Suppliers.ProductInOrderDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

public class ProductInOrderMapper extends AbstractMapper {

    private String column_1_amount = "amount";
    private String column_2_priceBeforeDiscount = "priceBeforeDiscount";
    private String column_3_priceAfterDiscount = "priceAfterDiscount";
    private String column_4_totalDiscount = "totalDiscount";
    private String column_5_catalogNumber = "catalogNumber";
    private String column_6_OrderId = "orderId";
    private String column_7_branchId = "branchId";
    private String column_8_productName = "productName";
    private String column_9_productId = "productId";


    private IdentityHashMap<Integer, HashMap<Integer, List<ProductInOrderDTO>>> identityMapByBranchByOrder;


    public ProductInOrderMapper() {
        super("ProductInOrder");
        identityMapByBranchByOrder = new IdentityHashMap<>();
    }

    public void removeProd(int orderId , int branchId) throws Exception{
        remove(column_6_OrderId,column_7_branchId , orderId  ,branchId );
    }

    public  void  clearData(){
        identityMapByBranchByOrder.clear();
        removeAll();
    }

    protected ProductInOrderDTO convertReaderToObject(ResultSet reader) {
        ProductInOrderDTO result = null;
        try {
            result = new ProductInOrderDTO(reader.getInt(column_1_amount),
                    reader.getDouble(column_2_priceBeforeDiscount), reader.getDouble(column_3_priceAfterDiscount),
                    reader.getDouble(column_4_totalDiscount), reader.getInt(column_5_catalogNumber),
                    reader.getInt(column_6_OrderId), reader.getInt((column_7_branchId)), reader.getString(column_8_productName), reader.getString(column_9_productId));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }


    public boolean addProductInOrder(ProductInOrderDTO productInOrderDTO) {
        String sql = MessageFormat.format("INSERT INTO " + table_name + " ({0} ,{1}, {2}, {3}, {4},{5} ,{6}, {7}, {8}) VALUES(?,?,?,?,?,?,?,?,?)",
                column_1_amount, column_2_priceBeforeDiscount, column_3_priceAfterDiscount,
                column_4_totalDiscount, column_5_catalogNumber, column_6_OrderId,
                column_7_branchId,column_8_productName,column_9_productId);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productInOrderDTO.getAmount());
            pstmt.setDouble(2, productInOrderDTO.getPriceBeforeDiscount());
            pstmt.setDouble(3, productInOrderDTO.getPriceAfterDiscount());
            pstmt.setDouble(4, productInOrderDTO.getDiscount());
            pstmt.setInt(5, productInOrderDTO.getCatalogNumber());
            pstmt.setInt(6, productInOrderDTO.getOrderId());
            pstmt.setInt(7, productInOrderDTO.getBranchId());
            pstmt.setString(8, productInOrderDTO.getProductName());
            pstmt.setString(9, productInOrderDTO.getProductId());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ProductInOrderDTO> checkIfContainInIdentityMapByOrder(int orderId, int branchId) {
        if (identityMapByBranchByOrder.containsKey(branchId)) {
            return identityMapByBranchByOrder.get(branchId).get(orderId);
        }
        return null;
    }

    public List<ProductInOrderDTO> selectProductInOrder(int orderId, int branchId) {
        if (checkIfContainInIdentityMapByOrder(orderId, branchId) != null) {
            return checkIfContainInIdentityMapByOrder(orderId, branchId);
        }
        List<ProductInOrderDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_6_OrderId + "=" + orderId
                + " AND " + column_7_branchId + "=" + branchId;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                objects.add(convertReaderToObject(resultSet));
            }
            HashMap<Integer, List<ProductInOrderDTO>> hashMap = new HashMap();
            hashMap.put(orderId, objects);
            identityMapByBranchByOrder.put(branchId, hashMap);
            return objects;
        } catch (SQLException e) {
            ;
            e.printStackTrace();
            return null;
        }
    }

}

