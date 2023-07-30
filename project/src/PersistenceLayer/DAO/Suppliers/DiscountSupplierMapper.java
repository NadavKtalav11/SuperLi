package PersistenceLayer.DAO.Suppliers;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Suppliers.DiscountSupplierDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class DiscountSupplierMapper extends AbstractMapper {
    private String column_1_minimumAmount = "minimumAmount";
    private String column_2_DiscountAmount = "discountAmount";
    private String column_3_discountId = "discountId";
    private String column_4_suppId = "supplierId";
    private String column_5_isPercentage = "isPrecentageDiscount";

    private IdentityHashMap<Integer, DiscountSupplierDTO> identityMap;




    public DiscountSupplierMapper() {
        super("SuppliersDiscounts");
        identityMap = new IdentityHashMap<>();
    }

    public  void  clearData(){
        identityMap.clear();

        removeAll();
    }

    protected DiscountSupplierDTO convertReaderToObject(ResultSet reader) {
        DiscountSupplierDTO result = null;
        try {
            result = new DiscountSupplierDTO(reader.getDouble(column_1_minimumAmount), reader.getDouble(column_2_DiscountAmount),
                    reader.getInt(column_3_discountId), reader.getInt(column_4_suppId), reader.getInt(column_5_isPercentage));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public DiscountSupplierDTO getDiscountDTO(int discountId) {
        //DiscountSupplierDTO object = new
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_3_discountId + "=" + discountId;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            return convertReaderToObject(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DiscountSupplierDTO> getDiscountsListDTO(int supplierId) {
        List<DiscountSupplierDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_4_suppId + "=" + supplierId;
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

    public int getMaxDiscountId() {
        //SupplierProductDiscountsDTO object ;
        String sql = "SELECT MAX(" + column_3_discountId + ") as maxi FROM " + table_name;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt("maxi");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public boolean addSupplierDiscount(DiscountSupplierDTO discountSupplierDTO) {
        String sql = MessageFormat.format("INSERT INTO " + table_name + " ({0}, {1}, {2} , {3}, {4}) VALUES(?,?,?,?, ?)",
                column_1_minimumAmount, column_2_DiscountAmount, column_3_discountId, column_4_suppId, column_5_isPercentage);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, discountSupplierDTO.getMinimumAmount());
            pstmt.setDouble(2, discountSupplierDTO.getDiscountAmount());
            pstmt.setInt(3, discountSupplierDTO.getDiscountId());
            pstmt.setInt(4, discountSupplierDTO.getSupplierId());
            pstmt.setInt(5, discountSupplierDTO.getIsPercentageDiscount());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeDiscount(int discountId) throws Exception{
        remove(column_3_discountId, discountId);
    }


    public void removeSupplierDetails(int supplierId) {
        remove(supplierId, column_4_suppId);
        //identityMap.remove(supplierId);
        List<Integer> toRemove = new ArrayList<>();
        for (Integer discountId : identityMap.keySet()) {
            if (identityMap.get(discountId).getSupplierId() == supplierId) {
                toRemove.add(discountId);
            }
        }
        for (Integer id : toRemove) {
            identityMap.remove(id);
        }
    }


}

