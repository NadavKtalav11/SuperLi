package BusinessLayer.Suppliers;

import PersistenceLayer.DAO.Suppliers.OrderMapper;
import PersistenceLayer.DAO.Suppliers.ProductInOrderMapper;
import PersistenceLayer.DTO.Suppliers.OrderDTO;

import java.time.LocalDate;
import java.util.List;



public class Order {
    protected LocalDate orderDate;
    protected Double totalPrice;
    protected int totalNumOfProducts;
    protected List<ProductInOrder> products;
    protected int branchId ;
    protected String address;
    protected int orderId;
    protected OrderDTO orderDTO;
    protected ProductInOrderMapper productInOrderMapper;
    protected int supplierId;
    protected String supplierName;


    public Order(int supplierId, String supplierName , int branchId , String address , int orderId, OrderMapper orderMapper, List<ProductInOrder> orderProducts) {
        this.supplierId = supplierId;
        this.supplierName= supplierName;
        this.branchId = branchId;
        products = orderProducts;
        totalPrice = 0.0;
        totalNumOfProducts = 0;
        for (ProductInOrder productInOrder: products) {
            totalPrice = totalPrice + productInOrder.getTotalPrice();
            totalNumOfProducts = totalNumOfProducts+productInOrder.getAmount();
        }
        this.address = address;
        this.orderId= orderId;
        this.orderDate = LocalDate.now();
        orderDTO = new OrderDTO(totalPrice, totalNumOfProducts,address,branchId, orderDate, supplierId,orderId , orderMapper);
        orderMapper.addOrder(orderDTO);
    }

    protected Order(int supplierId, String supplierName , int branchId , String address , int orderId, List<ProductInOrder> orderProducts) {
        this.supplierId = supplierId;
        this.supplierName= supplierName;
        this.branchId = branchId;
        products = orderProducts;
        totalPrice = 0.0;
        totalNumOfProducts = 0;
        for (ProductInOrder productInOrder: products) {
            totalPrice = totalPrice + productInOrder.getTotalPrice();
            totalNumOfProducts = totalNumOfProducts+productInOrder.getAmount();
        }
        this.address = address;
        this.orderId= orderId;
        this.orderDate = LocalDate.now();

    }

    public Order(OrderDTO orderDTO , String supplierName, List<ProductInOrder> productInOrderList) {
        this.supplierId = orderDTO.getSupplierId();
        this.supplierName= supplierName;
        this.branchId = orderDTO.getBranchId();
        products = productInOrderList;
        totalPrice = 0.0;
        totalNumOfProducts = 0;
        for (ProductInOrder productInOrder: products) {
            totalPrice = totalPrice + productInOrder.getTotalPrice();
            totalNumOfProducts = totalNumOfProducts+productInOrder.getAmount();
        }
        this.address = orderDTO.getAddress();
        this.orderId= orderDTO.getOrderId();
        this.orderDate = orderDTO.getOrderDate();
        this.orderDTO =orderDTO;
    }


    public Double getTotalPrice() {
        return totalPrice;
    }

    public void printOrder() {
        System.out.println("supplier name - " + supplierName + "\n" +
                "supplier Id - " + supplierId + "\n" +
                "address - " + address + "\n" +
                "order date - " + orderDate + "\n" +
                "order Id - " + orderId + "\n" +
                "products : \n");
        for (int i = 0; i < products.size(); i++) {
            System.out.println("product- " + products.get(i).getProductName() + " | catalog number " + products.get(i).getCatalogNum() + " | amount- " + products.get(i).getAmount() +
                    " | Price before discount- " + products.get(i).getTotalPriceBeforeDiscount() + " | discount- " + products.get(i).getDiscount() + " | price after discount- " + products.get(i).getTotalPrice() + "\n");
        }
        System.out.println("num of Products - " + totalNumOfProducts + "\n" +
                "total order price - " + totalPrice);
        System.out.println("\n");
    }

    public List<ProductInOrder> getProducts(){
        return products;
    }


    public int getSupplierId(){
        return supplierId;
    }

    public String getAddress() {
        return address;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getBranchId() {
        return branchId;
    }

    public int getTotalNumOfProducts() {
        return totalNumOfProducts;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }

    public ProductInOrderMapper getProductInOrderMapper() {
        return productInOrderMapper;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public enum OrderStatus{
        not_order_yet,
        order_and_wait_for_delivery,
    }

    public String getStringOrder(){
        String toReturn = "";
        toReturn = toReturn + "supplier name - " + supplierName + "\n" +
                "supplier Id - " + supplierId + "\n" +
                "address - " + address + "\n" +
                "order date - " + orderDate + "\n" +
                "order Id - " + orderId + "\n" +
                "products : \n";
        for (int i = 0; i < products.size(); i++) {
           toReturn = toReturn + "product- " + products.get(i).getProductName() + " | catalog number " + products.get(i).getCatalogNum() + " | amount- " + products.get(i).getAmount() +
                    " | Price before discount- " + products.get(i).getTotalPriceBeforeDiscount() + " | discount- " + products.get(i).getDiscount() + " | price after discount- " + products.get(i).getTotalPrice() + "\n";
        }
        toReturn = toReturn + "num of Products - " + totalNumOfProducts + "\n" +
                "total order price - " + totalPrice + "\n";
        return toReturn;
    }
}



    /*public void addProductToOrder(Product supplierProduct, int amount, int catalogNum){
        ProductInOrder newProd = new ProductInOrder(supplierProduct,amount ,supplier, catalogNum);
        this.products.add(newProd);
        totalPrice = totalPrice+newProd.getTotalPrice();
        totalNumOfProducts = totalNumOfProducts+ amount;
    }*/

    /*public void editProductInOrderAmount(ProductInOrder productInOrder , int newAmount){
        int lastAmount = productInOrder.getAmount();
        double lastPrice = productInOrder.getTotalPrice();
        productInOrder.setAmount(newAmount);
        totalPrice = totalPrice-lastPrice + productInOrder.getTotalPrice();
        totalNumOfProducts = totalNumOfProducts - lastAmount + newAmount;
    }*/


    /*public void sendOrder(){
        this.orderDate = LocalDate.now();
        this.currOrderStatus= OrderStatus.order_and_wait_for_delivery;
    }*/


