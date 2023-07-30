package PersistenceLayer.DAO.Suppliers;


import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Suppliers.SupplierDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class SupplierMapper extends AbstractMapper {

    private static String column_1_name = "name";
    private static String column_2_Id = "supplierId";
    private static String column_3_delivery = "canDeliver";
    private static String column_4_days = "days";
    private static String column_5_address = "address";

    private IdentityHashMap<Integer, SupplierDTO> identityMap;
    List<SupplierDTO> allSuppliers;


    public SupplierMapper() {
        super("Supplier");
        this.identityMap = new IdentityHashMap<>();
        allSuppliers = new ArrayList<>();
    }


    public SupplierDTO checkIfContainInIdentityMapBySupplier(int supplierId) {
        return identityMap.get(supplierId);
    }


    protected SupplierDTO convertReaderToObject(ResultSet reader) {
        SupplierDTO result = null;
        try {
            result = new SupplierDTO(reader.getString(column_1_name), reader.getInt(column_2_Id),
                    reader.getInt(column_3_delivery), reader.getString(column_4_days), reader.getString(column_5_address));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public SupplierDTO selectSupplier(int id) {
        if (checkIfContainInIdentityMapBySupplier(id) != null) {
            return checkIfContainInIdentityMapBySupplier(id);
        }
        SupplierDTO objects = null;
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_2_Id + " = " + id + "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                objects = convertReaderToObject(resultSet);
                identityMap.put(id, objects);

                return objects;
            }

        } catch (SQLException e) {
            ;
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public List<SupplierDTO> selectAllSuppliers() {
        if (!allSuppliers.isEmpty()) {
            return allSuppliers;
        }
        List<SupplierDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                objects.add(convertReaderToObject(resultSet));
            }
            for (SupplierDTO supplierDTO : objects) {
                allSuppliers.add(supplierDTO);
                identityMap.put(supplierDTO.getId(), supplierDTO);
            }
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void updateAddress(String address, int supplierId) {
        update(column_5_address, address, column_2_Id, supplierId);
        /*if (identityMap.containsKey(supplierId)){
            identityMap.get(supplierId).setAddress(address);
        }
        if (!allSuppliers.isEmpty()){
            allSuppliers.get(supplierId).setAddress(address);
        }*/
    }

    public void updateName(String name, int supplierId) {
        update(column_1_name, name, column_2_Id, supplierId);
        /*if (identityMap.containsKey(supplierId)){
            identityMap.get(supplierId).setAddress(name);
        }
        if (!allSuppliers.isEmpty()){
            allSuppliers.get(supplierId).setName(name);
        }*/
    }

    public void updateCanDeliver(int canDeliver, String days, int supplierId) {
        update2Values(column_3_delivery, canDeliver, column_4_days, days, column_2_Id, supplierId);
        /*if (identityMap.containsKey(supplierId)){
            identityMap.get(supplierId).setCanDeliver(canDeliver);
            identityMap.get(supplierId).setDays(days);
        }
        if (!allSuppliers.isEmpty()){
            allSuppliers.get(supplierId).setCanDeliver(canDeliver);
            allSuppliers.get(supplierId).setDays(days);
        }*/
    }
    public void clearData (){
        allSuppliers.clear();
        identityMap.clear();
        removeAll();
    }

    public void addSupplier(SupplierDTO supplierDTO) {
        String sql = MessageFormat.format("INSERT INTO " + table_name + "({0} ,{1}, {2}, {3}, {4} ) VALUES(?,?,?,?,?)",
                column_1_name, column_2_Id, column_3_delivery, column_4_days, column_5_address);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplierDTO.getName());
            pstmt.setInt(2, supplierDTO.getId());
            pstmt.setInt(3, supplierDTO.getCanDeliver());
            pstmt.setString(4, supplierDTO.getDays());
            pstmt.setString(5, supplierDTO.getAddress());

            pstmt.executeUpdate();

            if (!allSuppliers.isEmpty()) {
                allSuppliers.add(supplierDTO);
            }
            identityMap.put(supplierDTO.getId(), supplierDTO);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void removeSupplier(int supplierId) {
        try {
            remove(supplierId, column_2_Id);
            if (identityMap.containsKey(supplierId)) {
                identityMap.remove(supplierId);
            }
            boolean found = false;
            SupplierDTO supplierDTOToRemove = null;
            if (!allSuppliers.isEmpty()) {
                for (SupplierDTO supplierDTO : allSuppliers) {
                    if (supplierDTO.getId() == supplierId) {
                        supplierDTOToRemove = supplierDTO;
                        break;
                    }
                }
            }
            allSuppliers.remove(supplierDTOToRemove);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


