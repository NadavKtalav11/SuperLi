package PersistenceLayer.DAO.Suppliers;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Suppliers.SupplierProductDiscountsDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class SupplierProductDiscountsMapper extends AbstractMapper {
    private String column_1_discountId = "DiscountId";
    private String column_2_suppId = "supplierId";
    private String column_3_productId = "ProductId";
    private String column_4_minimumAmount = "amount";
    private String column_5_DiscountAmount = "discountAmount";
    private String column_6_isPercentage = "percentageDiscount";

    // discountId to DTO
    private IdentityHashMap<Integer, SupplierProductDiscountsDTO> identityMapByDiscountId;
    private IdentityHashMap<Integer, List<SupplierProductDiscountsDTO>> identityMapBySupplier;


    public SupplierProductDiscountsMapper() {
        super("suppliersProductsDiscounts");
        identityMapByDiscountId = new IdentityHashMap<>();
        identityMapBySupplier = new IdentityHashMap<>();

    }

    public  void  clearData(){
        identityMapByDiscountId.clear();
        identityMapBySupplier.clear();
        removeAll();
    }

    protected SupplierProductDiscountsDTO convertReaderToObject(ResultSet reader) {
        SupplierProductDiscountsDTO result = null;
        try {
            result = new SupplierProductDiscountsDTO(reader.getInt(column_1_discountId), reader.getInt(column_2_suppId),
                    reader.getString(column_3_productId), reader.getDouble(column_4_minimumAmount),
                    reader.getDouble(column_5_DiscountAmount), reader.getInt(column_6_isPercentage));
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return result;


    }

    public List<SupplierProductDiscountsDTO> checkIfContainInIdentityMapBySupplier(int supplierId) {
        return identityMapBySupplier.get(supplierId);
    }

    public SupplierProductDiscountsDTO checkIfContainInIdentityMapByDiscount(int discountId) {
        return identityMapByDiscountId.get(discountId);
    }

    public int getMaxDiscountId() {
        //SupplierProductDiscountsDTO object ;
        String sql = "SELECT MAX(" + column_1_discountId + ") as maxi FROM " + table_name + " ;";
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

    public boolean addSupplierProductDiscount(SupplierProductDiscountsDTO supplierProductDiscountsDTO) {
        String sql = MessageFormat.format("INSERT INTO " + table_name + " ({0}, {1}, {2} , {3}, {4}, {5}) VALUES(?,?,?,?,?,?)",
                column_1_discountId, column_2_suppId, column_3_productId, column_4_minimumAmount, column_5_DiscountAmount, column_6_isPercentage);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplierProductDiscountsDTO.getDiscountId());
            pstmt.setInt(2, supplierProductDiscountsDTO.getSupplierId());
            pstmt.setString(3, supplierProductDiscountsDTO.getProductId());
            pstmt.setDouble(4, supplierProductDiscountsDTO.getMinimumAmount());
            pstmt.setDouble(5, supplierProductDiscountsDTO.getDiscountAmount());
            pstmt.setInt(6, supplierProductDiscountsDTO.getIsPercentageDiscount());

            pstmt.executeUpdate();
            identityMapByDiscountId.put(supplierProductDiscountsDTO.getDiscountId(), supplierProductDiscountsDTO);
            if (identityMapBySupplier.get(supplierProductDiscountsDTO.getSupplierId()) != null) {
                identityMapBySupplier.get(supplierProductDiscountsDTO.getSupplierId()).add(supplierProductDiscountsDTO);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeSupplierDetails(int supplierId) {
        remove(supplierId, column_2_suppId);
        identityMapBySupplier.remove(supplierId);
        List<Integer> toRemove = new ArrayList<>();
        for (Integer discountId : identityMapByDiscountId.keySet()) {
            if (identityMapByDiscountId.get(discountId).getSupplierId() == supplierId) {
                toRemove.add(discountId);
            }
        }
        for (Integer id : toRemove) {
            identityMapByDiscountId.remove(id);
        }
    }


    public List<SupplierProductDiscountsDTO> getSupplierDiscountsDTOs(int supplierId) {
        List<SupplierProductDiscountsDTO> objects = new ArrayList<>();
        List<SupplierProductDiscountsDTO> isContain = checkIfContainInIdentityMapBySupplier(supplierId);
        if (isContain != null) {
            return isContain;
        }
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_2_suppId + "=" + supplierId;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                SupplierProductDiscountsDTO curr = convertReaderToObject(resultSet);
                objects.add(curr);
                if (!identityMapByDiscountId.containsKey(curr.getDiscountId())) {
                    identityMapByDiscountId.put(curr.getDiscountId(), curr);
                }
            }
            identityMapBySupplier.put(supplierId, objects);
            return objects;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void removeProductDiscounts(String productId, int supplierId) {

        remove(supplierId, productId, column_2_suppId, column_3_productId);
    }

    public void removeProductDiscount(String productId, int supplierId, int discountId) {
        remove(supplierId, productId, discountId, column_2_suppId, column_3_productId, column_1_discountId);
    }
}


