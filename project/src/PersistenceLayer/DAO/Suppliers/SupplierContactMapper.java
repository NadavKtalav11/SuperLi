package PersistenceLayer.DAO.Suppliers;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Suppliers.SupplierContactDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class SupplierContactMapper extends AbstractMapper {


    private String column_1_Id = "Id";
    private String column_2_PhoneNumber = "PhoneNumber";
    private String column_3_name = "Name";
    public String column_4_supplierId = "supplierId";


    private IdentityHashMap<Integer, Map<Integer, SupplierContactDTO>> identityMap;
    List<SupplierContactDTO> allSupplierContacts;

    public SupplierContactMapper() {
        super("suplliersContacts");
        identityMap = new IdentityHashMap<>();
        allSupplierContacts = new ArrayList<>();
    }

    public  void  clearData(){
        identityMap.clear();
        allSupplierContacts.clear();
    }


    protected SupplierContactDTO convertReaderToObject(ResultSet reader) {
        SupplierContactDTO result = null;
        try {
            result = new SupplierContactDTO(reader.getInt(column_1_Id), reader.getString(column_2_PhoneNumber), reader.getString((column_3_name)), reader.getInt(column_4_supplierId));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void addSupplierContacts(SupplierContactDTO supplierContactDTO) {
        String sql = MessageFormat.format("INSERT INTO " + table_name + "({0} , {1}, {2} , {3}) VALUES(?,?,?,?)",
                column_1_Id, column_2_PhoneNumber, column_3_name, column_4_supplierId);

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, supplierContactDTO.getId());
            pstmt.setString(2, supplierContactDTO.getPhoneNumber());
            pstmt.setString(3, supplierContactDTO.getName());
            pstmt.setInt(4, supplierContactDTO.getSupplierId());
            pstmt.executeUpdate();
            if (identityMap.containsKey(supplierContactDTO.getSupplierId())) {
                identityMap.get(supplierContactDTO.getSupplierId()).put(supplierContactDTO.getId(), supplierContactDTO);
            } else {
                if (!allSupplierContacts.isEmpty()) {
                    allSupplierContacts.add(supplierContactDTO);
                }
                Map<Integer, SupplierContactDTO> map = new HashMap<Integer, SupplierContactDTO>();
                map.put(supplierContactDTO.getId(), supplierContactDTO);
                identityMap.put(supplierContactDTO.getSupplierId(), map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public List<SupplierContactDTO> getSupplierContacts(int id) {
        List<SupplierContactDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_1_Id + "=" + id;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                objects.add(convertReaderToObject(resultSet));
            }
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SupplierContactDTO getSpecificSupplierContact(int idContact, int supplierId) {
        SupplierContactDTO objects = null;
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_1_Id + "=" + idContact
                + " And " + column_4_supplierId + "=" + supplierId + "";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                objects = convertReaderToObject(resultSet);
                // identityMap.put(idContact, objects);
                return objects;
            }

        } catch (SQLException e) {
            ;
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void removeSupplierContacts(int supplierId) {
        remove(supplierId, column_4_supplierId);
        identityMap.remove(supplierId);
        List<SupplierContactDTO> founded = new ArrayList<>();
        for (SupplierContactDTO supplierContactDTO : allSupplierContacts) {
            if (supplierContactDTO.getSupplierId() == supplierId) {
                founded.add(supplierContactDTO);
            }
        }
        for (SupplierContactDTO supplierContactDTO : founded) {
            allSupplierContacts.remove(supplierContactDTO);
        }
    }

    public int getMaxContactId() {
        SupplierContactDTO object;
        String sql = "SELECT MAX(" + column_1_Id + ") as maxi FROM " + table_name;
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

    public void removeContact(int contactId, int supplierId) {
        remove(contactId, supplierId, column_1_Id, column_4_supplierId);
    }


    public List<SupplierContactDTO> selectAllSupplierContacts() {
        if (!allSupplierContacts.isEmpty()) {
            return allSupplierContacts;
        }
        List<SupplierContactDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                objects.add(convertReaderToObject(resultSet));
            }
            allSupplierContacts = objects;
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


}
