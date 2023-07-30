package PersistenceLayer.DAO.Suppliers;

import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Suppliers.OrderDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class OrderMapper extends AbstractMapper {
    private String column_1_price = "price";
    private String column_2_numOfProduct = "numOfProducts";
    private String column_3_address = "address";
    private String column_4_branchId = "branchId";
    private String column_5_orderDate = "OrderDate";
    private String column_6_supplierId = "supplierId";
    private String column_7_Id = "Id";

    //private IdentityHashMap<Integer, OrderDTO> identityMap;
    private IdentityHashMap<Integer, List<OrderDTO>> allSupplierOrder;


    public OrderMapper() {
        super("orders");
        // identityMap = new IdentityHashMap<>();
        allSupplierOrder = new IdentityHashMap<>();
    }

    public  void  clearData(){

        allSupplierOrder.clear();
        removeAll();
    }

    protected OrderDTO convertReaderToObject(ResultSet reader) {
        OrderDTO result = null;
        try {
            result = new OrderDTO(reader.getInt(column_1_price), reader.getInt(column_2_numOfProduct),
                    reader.getString(column_3_address), reader.getInt(column_4_branchId), LocalDate.parse(reader.getString(column_5_orderDate)),
                    reader.getInt(column_6_supplierId), reader.getInt(column_7_Id));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public List<OrderDTO> selectAllOrders(int supplierId) {
        if (allSupplierOrder.containsKey(supplierId)) {
            if (!allSupplierOrder.get(supplierId).isEmpty()) {
                return allSupplierOrder.get(supplierId);
            }
        }
        List<OrderDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM '" + table_name + "' WHERE " + column_6_supplierId +
                " = " + supplierId;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                objects.add(convertReaderToObject(resultSet));
            }
            allSupplierOrder.put(supplierId, objects);
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> removeSupplierDetails(int supplierId) {
        remove(supplierId, column_6_supplierId);
        List<Integer> supplierOrdersList = new ArrayList<>();
        if (allSupplierOrder.containsKey(supplierId)) {
            for (OrderDTO OrderDTO : allSupplierOrder.get(supplierId))
                supplierOrdersList.add(OrderDTO.getOrderId());
        }
        allSupplierOrder.remove(supplierId);
        return supplierOrdersList;
    }


    public int getMaxDiscountId() {
        String sql = "SELECT MAX(" + column_7_Id + ") as maxi FROM '" + table_name + "' ;";
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

    public List<OrderDTO> selectAllOrders() {
        List<OrderDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name ;
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


    public boolean addOrder(OrderDTO orderDTO) {
        String sql = MessageFormat.format("INSERT INTO '" + table_name + "' ({0},{1},{2},{3},{4},{5},{6}) VALUES(?,?,?,?,?,?,?)",
                column_1_price, column_2_numOfProduct, column_3_address, column_4_branchId, column_5_orderDate,
                column_6_supplierId, column_7_Id);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, orderDTO.getPrice());
            pstmt.setInt(2, orderDTO.getNumOfProducts());
            pstmt.setString(3, orderDTO.getAddress());
            pstmt.setInt(4, orderDTO.getBranchId());
            pstmt.setString(5, orderDTO.getOrderDate().toString());
            pstmt.setInt(6, orderDTO.getSupplierId());
            pstmt.setInt(7, orderDTO.getOrderId());
            pstmt.executeUpdate();
            if (allSupplierOrder.containsKey(orderDTO.getSupplierId())) {
                allSupplierOrder.get(orderDTO.getSupplierId()).add(orderDTO);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
