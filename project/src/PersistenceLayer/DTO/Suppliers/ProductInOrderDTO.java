package PersistenceLayer.DTO.Suppliers;


import PersistenceLayer.DAO.Suppliers.ProductInOrderMapper;
import PersistenceLayer.DTO.AbstractDTO;

public class ProductInOrderDTO extends AbstractDTO {

    private String productName;
    private int amount;
    private double priceBeforeDiscount;
    private double priceAfterDiscount;
    private double discount;
    private int catalogNumber;
    private int orderId;
    private int branchId;
    private String productId;

    public ProductInOrderDTO( int amount,  double priceBeforeDiscount,double priceAfterDiscount,
                             double discount, int catalogNumber, int orderId , int branchId ,String productName, String productId ) {
        super(new ProductInOrderMapper());
        this.priceAfterDiscount = priceAfterDiscount;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.amount = amount;
        this.discount = discount;
        this.catalogNumber = catalogNumber;
        this.orderId = orderId;
        this.branchId = branchId;
        this.productName =productName;
        this.productId =productId;
    }
    public ProductInOrderDTO(int amount,  double priceBeforeDiscount,double priceAfterDiscount,
                             double discount, int catalogNumber, int orderId,int branchId,
                             String productName,String productId,  ProductInOrderMapper productInOrderMapper ) {
        super(productInOrderMapper);
        this.priceAfterDiscount = priceAfterDiscount;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.amount = amount;
        this.discount = discount;
        this.catalogNumber = catalogNumber;
        this.orderId = orderId;
        this.branchId = branchId;
        this.productName = productName;
        this.productId= productId;
    }


    public int getBranchId() {
        return branchId;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public String getProductId() {
        return productId;
    }

    public double getDiscount() {
        return discount;
    }

    public int getAmount() {
        return amount;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public double getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    @Override
    public ProductInOrderMapper getDao() {
        return (ProductInOrderMapper) dao;
    }
}
