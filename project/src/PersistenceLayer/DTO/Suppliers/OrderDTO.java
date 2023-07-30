package PersistenceLayer.DTO.Suppliers;


import PersistenceLayer.DAO.Suppliers.OrderMapper;
import PersistenceLayer.DTO.AbstractDTO;

import java.time.LocalDate;

public class OrderDTO extends AbstractDTO {
    private int numOfProducts;
    private String address;
    private int branchId;
    private int supplierId;
    private LocalDate orderDate;
    private int orderId;
    private double price;

    public OrderDTO(double price, int numOfProducts,  String address, int branchId,
                    LocalDate orderDate,int supplierId, int orderId ) {
        super(new OrderMapper());
        this.numOfProducts = numOfProducts;
        this.address = address;
        this.branchId = branchId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.price = price;
        this.orderId = orderId;
    }

    public OrderDTO(double price, int numOfProducts,  String address, int branchId,
                    LocalDate orderDate,int supplierId, int orderId,  OrderMapper orderMapper) {
        super(orderMapper);
        this.numOfProducts = numOfProducts;
        this.address = address;
        this.branchId = branchId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.price = price;
        this.orderId = orderId;

    }

    public int getSupplierId() {
        return supplierId;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public int getBranchId() {
        return branchId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public int getNumOfProducts() {
        return numOfProducts;
    }

    @Override
    public OrderMapper getDao() {
        return (OrderMapper) dao;
    }
}

