package BusinessLayer.Suppliers;

import PersistenceLayer.DAO.Suppliers.OrderConstMapper;
import PersistenceLayer.DTO.Suppliers.OrderConstDTO;

import java.util.ArrayList;
import java.util.List;


public class OrderConst extends Order {
    private List<Integer> supplyConstantDays;
    private OrderConstDTO orderConstDTO;


    public OrderConst(int supplierId, String supplierName , int branchId , String address , int orderId, List<ProductInOrder> orderProducts, OrderConstMapper orderConstMapper , List<Integer> supplyConstantDays) {
        super(supplierId,supplierName, branchId ,address ,orderId, orderProducts );
        this.supplyConstantDays = supplyConstantDays;
        orderConstDTO = new OrderConstDTO( totalPrice, totalNumOfProducts, address, branchId, orderDate, supplierId,  orderId, daysToString());
        orderConstMapper.addConstOrder(orderConstDTO);
    }

    public OrderConst( OrderConstDTO orderConstDTO, String supplierName, List<ProductInOrder> productInOrderList) {
        super(orderConstDTO.getSupplierId(),supplierName,  orderConstDTO.getBranchId() ,orderConstDTO.getAddress() ,orderConstDTO.getOrderId(), productInOrderList  );
        this.supplyConstantDays = daysToList(orderConstDTO.getDays());
        this.orderConstDTO = orderConstDTO;
    }

    public List<Integer> daysToList(String daysString){
        List<Integer> toReturn =new ArrayList<>(7);
        for (int i=0 ; i<daysString.length(); i++) {
            toReturn.add(Character.getNumericValue(daysString.charAt(i)));
        }
        return toReturn;
    }


    public String daysToString(){
        String toReturn = "";
        for (int day :supplyConstantDays){
            toReturn= toReturn+day;
        }
        return toReturn;
    }
}
