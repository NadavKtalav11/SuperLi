package PersistenceLayer.DTO.Suppliers;



import PersistenceLayer.DAO.Suppliers.OrderConstMapper;
import PersistenceLayer.DTO.AbstractDTO;

import java.time.LocalDate;

public class OrderConstDTO extends AbstractDTO {
    private int numOfProducts;
    private String address;
    private int branchId;
    private int supplierId;
    private LocalDate orderDate;
    private int orderId;
    private double price;
    private String Days;

    public OrderConstDTO( double price, int numOfProducts, String address,int branchId,LocalDate orderDate,  int supplierId,
                          int orderId  , String days) {
        super(new OrderConstMapper());
        this.numOfProducts = numOfProducts;
        this.address = address;
        this.branchId = branchId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.price = price;
        this.orderId = orderId;
        this.Days = days;
    }

    public OrderConstDTO(int numOfProducts, String address, int branchId, int supplierId,
                         LocalDate orderDate, int orderId  , double price , String days, OrderConstMapper orderConstMapper) {
        super(orderConstMapper);
        this.numOfProducts = numOfProducts;
        this.address = address;
        this.branchId = branchId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.price = price;
        this.orderId = orderId;
        this.Days = days;
    }



    public LocalDate getOrderDate() {
        return orderDate;
    }

    public int getNumOfProducts() {
        return numOfProducts;
    }

    public String getAddress() {
        return address;
    }

    public int getBranchId() {
        return branchId;
    }

    public double getPrice() {
        return price;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getDays() {
        return Days;
    }

    @Override
    public OrderConstMapper getDao() {
        return (OrderConstMapper) dao;
    }
}



