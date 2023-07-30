package PersistenceLayer.DAO.Suppliers;


import BusinessLayer.Suppliers.PaymentOptions;
import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Suppliers.SupplierCardDTO;

import java.sql.*;
import java.text.MessageFormat;

public class SupplierCardMapper extends AbstractMapper {


    public SupplierCardMapper() {
        super("supplierCard");
    }

    private static String column_1_bnNumber = "BNNumber";
    private static String column_2_BAnkNumber = "bankAccountNumber";
    private static String column_3_paymentTerm = "paymentOptions";
    public static String column_4_supplierId = "supplierId";

    //private IdentityHashMap<Integer, SupplierCardDTO> identityMap;


    protected SupplierCardDTO convertReaderToObject(ResultSet reader) {
        SupplierCardDTO result = null;
        try {
            result = new SupplierCardDTO(reader.getInt(column_1_bnNumber), reader.getInt(column_2_BAnkNumber), PaymentOptions.valueOf(reader.getString(column_3_paymentTerm)), reader.getInt(column_4_supplierId), this);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public SupplierCardDTO selectSupplierCard(int id) {
        //List<SupplierCardDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_4_supplierId + "=" + id;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                return convertReaderToObject(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public boolean addSupplierCard(int supplierId, PaymentOptions paymentOptions, int bankAccount, int bnNumber, int contactCounter) {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2} , {3}) VALUES(?,?,?,?)",
                column_1_bnNumber, column_2_BAnkNumber, column_3_paymentTerm, column_4_supplierId);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bnNumber);
            pstmt.setInt(2, bankAccount);
            pstmt.setString(3, paymentOptions.toString());
            pstmt.setInt(4, supplierId);
            //pstmt.setInt(5, contactCounter);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean addSupplierCard(SupplierCardDTO supplierCardDTO) {
        String sql = MessageFormat.format("INSERT INTO " + table_name + " ({0}, {1}, {2} , {3}) VALUES(?,?,?,?)",
                column_1_bnNumber, column_2_BAnkNumber, column_3_paymentTerm, column_4_supplierId);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplierCardDTO.getBNNumber());
            pstmt.setInt(2, supplierCardDTO.getBankAccountNumber());
            pstmt.setString(3, supplierCardDTO.getPaymentOptions().toString());
            pstmt.setInt(4, supplierCardDTO.getSupplierId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    public void updateBNNumber(int bnNumber, int supplierId) {
        update(column_1_bnNumber, bnNumber, column_4_supplierId, supplierId);

    }

    public void updatePayment(PaymentOptions paymentOptions, int supplierId) {
        update(column_3_paymentTerm, paymentOptions.toString(), column_4_supplierId, supplierId);
    }

    public void updateBankNumber(int bankNumber, int supplierId) {
        update(column_2_BAnkNumber, bankNumber, column_4_supplierId, supplierId);

    }


    public void removeSupplierCard(int supplierId) {
        try {
            remove(supplierId, column_4_supplierId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
