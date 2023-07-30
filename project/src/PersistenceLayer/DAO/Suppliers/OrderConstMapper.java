package PersistenceLayer.DAO.Suppliers;

import BusinessLayer.Tools.Pair;
import PersistenceLayer.DAO.AbstractMapper;
import PersistenceLayer.DTO.Suppliers.OrderConstDTO;
import PersistenceLayer.DTO.Suppliers.SupplierDTO;

import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

public class OrderConstMapper extends AbstractMapper {

    private String column_2_numOfProduct = "numOfProducts";
    private String column_3_address = "address";
    private String column_4_branchId = "branchId";
    private String column_6_supplierId = "supplierId";
    private String column_5_orderDate = "orderDate";
    private String column_7_Id = "Id";
    private String column_1_price = "price";
    private String column_8_days = "days";

    private IdentityHashMap<Integer, HashMap<Integer, OrderConstDTO>> identityMap;
    private List<OrderConstDTO> allConstOrders;
    private IdentityHashMap<Integer, List<OrderConstDTO>> allSupplierOrder;



    public OrderConstMapper() {
        super("orderConst");
        identityMap = new IdentityHashMap<>();
        allConstOrders = new ArrayList<>();
        allSupplierOrder = new IdentityHashMap<>();
    }

    protected OrderConstDTO convertReaderToObject(ResultSet reader) {
        OrderConstDTO result = null;
        try {
            result = new OrderConstDTO(reader.getDouble(column_1_price), reader.getInt(column_2_numOfProduct), reader.getString(column_3_address),
                    reader.getInt(column_4_branchId), LocalDate.parse(reader.getString(column_5_orderDate)), reader.getInt(column_6_supplierId),
                    reader.getInt(column_7_Id), reader.getString(column_8_days));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public int getMaxDiscountId() {
        String sql = "SELECT MAX(" + column_7_Id + ") as maxi FROM " + table_name;
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

    public boolean addConstOrder(OrderConstDTO orderDTO) {
        String sql = MessageFormat.format("INSERT INTO " + table_name + " ({0}, {1}, {2} , {3}, {4},{5},{6}, {7}) VALUES(?,?,?,?,?,?,?,?)",
                column_1_price, column_2_numOfProduct, column_3_address, column_4_branchId, column_5_orderDate,
                column_6_supplierId, column_7_Id, column_8_days);
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, orderDTO.getPrice());
            pstmt.setInt(2, orderDTO.getNumOfProducts());
            pstmt.setString(3, orderDTO.getAddress());
            pstmt.setInt(4, orderDTO.getBranchId());
            pstmt.setString(5, orderDTO.getOrderDate().toString());
            pstmt.setInt(6, orderDTO.getSupplierId());
            pstmt.setInt(7, orderDTO.getOrderId());
            pstmt.setString(8, orderDTO.getDays());
            pstmt.executeUpdate();
            if (!allConstOrders.isEmpty()) {
                allConstOrders.add(orderDTO);
            }
            //Pair<Integer, Integer> orderIdBranchId = new Pair<Integer,Integer>(orderDTO.getOrderId(),orderDTO.getBranchId());
            if (identityMap.containsKey(orderDTO.getBranchId())) {
                identityMap.get(orderDTO.getBranchId()).put(orderDTO.getOrderId(), orderDTO);
            } else {
                HashMap<Integer, OrderConstDTO> hashMap = new HashMap();
                hashMap.put(orderDTO.getOrderId(), orderDTO);
                identityMap.put(orderDTO.getBranchId(), hashMap);
            }
            if (allSupplierOrder.containsKey(orderDTO.getSupplierId())) {
                allSupplierOrder.get(orderDTO.getSupplierId()).add(orderDTO);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<OrderConstDTO> selectAllSupplierUnOrdered(int supplierId) {
        List<OrderConstDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_6_supplierId + "=" + supplierId +
                " AND " + column_7_Id + " < 0";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                objects.add(convertReaderToObject(resultSet));
            }
            return objects;
        } catch (SQLException e) {
            ;
            e.printStackTrace();
            return null;
        }
    }




    public void updateSupplier(int supplierId, int orderId, int branchId) {
        update(column_6_supplierId, supplierId, column_7_Id, column_4_branchId, orderId, branchId);
    }

    public OrderConstDTO checkIfContainInIdentityMapByOrder(int orderId, int branchId) {
        if (identityMap.containsKey(branchId)) {
            if (identityMap.get(branchId).containsKey(branchId)) {
                return identityMap.get(branchId).get(orderId);
            }
        }
        return null;
    }

    public OrderConstDTO selectConstOrder(int orderId, int branchId) {
        if (checkIfContainInIdentityMapByOrder(orderId, branchId) != null) {
            return checkIfContainInIdentityMapByOrder(orderId, branchId);
        }
        OrderConstDTO objects = null;
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_7_Id + " = " + orderId
                + " AND " + column_4_branchId + " = " + branchId;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            if (resultSet.next()) {
                objects = convertReaderToObject(resultSet);
                if (identityMap.containsKey(branchId)) {
                    identityMap.get(branchId).put(branchId, objects);
                } else {
                    HashMap<Integer, OrderConstDTO> hashMap = new HashMap();
                    identityMap.put(branchId, hashMap);
                }
            }
            return objects;
        } catch (SQLException e) {

            e.printStackTrace();
            return null;
        }
    }

    public void updateSupplyDate(int branchId, int orderId, String days) {
        update(column_8_days, days, column_4_branchId, column_7_Id, branchId, orderId);
    }

    public List<OrderConstDTO> getAllUnOrdered() {
        if (!allConstOrders.isEmpty()) {
            return allConstOrders;
        }
        List<OrderConstDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_7_Id + " < 0";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                objects.add(convertReaderToObject(resultSet));
            }
            allConstOrders = objects;
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Integer> removeSupplierDetails(int supplierId) {
        String sql = "DELETE FROM " + table_name + " WHERE " + column_6_supplierId + "= ? " +
                " AND " + column_7_Id + " > 0";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplierId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Integer> supplierOrdersList = new ArrayList<>();
        if (allSupplierOrder.containsKey(supplierId)) {
            for (OrderConstDTO OrderDTO : allSupplierOrder.get(supplierId))
                supplierOrdersList.add(OrderDTO.getOrderId());
        }
        allSupplierOrder.remove(supplierId);
        List<Pair<Integer, Integer>> pairsToRemove = new ArrayList<>();
        for (Integer integer : identityMap.keySet()) {
            for (Integer orderID : identityMap.get(integer).keySet()) {
                if (identityMap.get(integer).get(orderID).getSupplierId() == supplierId) {
                    pairsToRemove.add(new Pair<Integer, Integer>(identityMap.get(integer).get(orderID).getBranchId(),
                            identityMap.get(integer).get(orderID).getOrderId()));
                    if (!supplierOrdersList.contains(identityMap.get(integer).get(orderID).getOrderId())) {
                        supplierOrdersList.add(identityMap.get(integer).get(orderID).getOrderId());
                    }
                }
            }
        }
        for (Pair<Integer, Integer> pair1 : pairsToRemove) {
            identityMap.get(pair1.getFirst()).remove(pair1.getSecond());
        }
        List<OrderConstDTO> orderConstDTOSList = new ArrayList<>();
        for (OrderConstDTO orderConstDTO : allConstOrders) {
            if (orderConstDTO.getSupplierId() == supplierId) {
                if (!supplierOrdersList.contains(orderConstDTO.getOrderId())) {
                    supplierOrdersList.add(orderConstDTO.getOrderId());
                }
                orderConstDTOSList.add(orderConstDTO);
            }

        }
        for (OrderConstDTO orderConstDTO : orderConstDTOSList) {
            allConstOrders.remove(orderConstDTO);
        }
        return supplierOrdersList;
    }


    public void removeUnordered(int orderId, int branchId) {
        try {
            remove(branchId, orderId, column_4_branchId, column_7_Id);
            Pair<Integer, Integer> orderIdBranchId = new Pair<Integer, Integer>(orderId, branchId);
            if (identityMap.containsKey(orderIdBranchId)) {
                identityMap.remove(orderIdBranchId);
            }
            boolean found = false;
            OrderConstDTO founded = null;
            for (OrderConstDTO orderConstDTO : allConstOrders) {
                if (found) {
                    break;
                }
                if (orderConstDTO.getOrderId() == orderId && orderConstDTO.getBranchId() == branchId) {
                    found = true;
                    founded = orderConstDTO;
                }
            }
            allConstOrders.remove(founded);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<OrderConstDTO> selectAllOrders(int supplierId) {
        if (allSupplierOrder.containsKey(supplierId)) {
            if (!allSupplierOrder.get(supplierId).isEmpty()) {
                return allSupplierOrder.get(supplierId);
            }
        }
        List<OrderConstDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_6_supplierId +
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
    public  void  clearData(){
        identityMap.clear();
        allConstOrders.clear();
        allSupplierOrder.clear();
        removeAll();
    }


    public List<OrderConstDTO> selectAllOrders() {


        List<OrderConstDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name;
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

   /* public List<OrderConstDTO> selectAllSupplierUnOrdered() {

        List<OrderConstDTO> objects = new ArrayList<>();
        String sql = "SELECT * FROM " + table_name + " WHERE " + column_6_supplierId + " = " + column_7_Id + " < 0";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                objects.add(convertReaderToObject(resultSet));
            }
            for (OrderConstDTO orderConstDTO : objects) {
                allConstOrders.add(orderConstDTO);
              //  identityMap.put(supplierDTO.getId(), supplierDTO);
            }
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }*/



}



