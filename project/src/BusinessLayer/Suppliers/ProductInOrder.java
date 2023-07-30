package BusinessLayer.Suppliers;

import PersistenceLayer.DAO.Suppliers.ProductInOrderMapper;
import PersistenceLayer.DTO.Suppliers.ProductInOrderDTO;

public class ProductInOrder {
    private String productName;
    private int catalogNum;
    private int amount;
    private double totalPrice;
    private double totalPriceBeforeDiscount;
    private  double discount;
    private ProductInOrderDTO productInOrderDTO;
    private int branchId;
    private String productId;


    public ProductInOrder(String productId, int branchId , int orderId, int catalogNum, String productName, int amount , double priceBefore, double priceAfter, ProductInOrderMapper productInOrderMapper){
        this.amount = amount;
        this.totalPrice = priceAfter;
        this.totalPriceBeforeDiscount = priceBefore;
        this.discount = priceBefore-priceAfter;
        this.catalogNum = catalogNum;
        this.productName = productName;
        this.branchId = branchId;
        this.productId =productId;
        this.productInOrderDTO = new ProductInOrderDTO( amount,  priceBefore,priceAfter,
        discount,  catalogNum, orderId, branchId ,productName, productId, productInOrderMapper);
        productInOrderMapper.addProductInOrder(productInOrderDTO);



    }

    public ProductInOrder(ProductInOrderDTO productInOrderDTO){
        this.amount = productInOrderDTO.getAmount();
        this.totalPrice = productInOrderDTO.getPriceAfterDiscount();
        this.totalPriceBeforeDiscount = productInOrderDTO.getPriceBeforeDiscount();
        this.discount = this.totalPriceBeforeDiscount-this.totalPrice;
        this.catalogNum = productInOrderDTO.getCatalogNumber();
        this.productName = productInOrderDTO.getProductName();
        this.branchId = productInOrderDTO.getBranchId();
        this.productId =productInOrderDTO.getProductId();
        this.productInOrderDTO = productInOrderDTO;


    }


    public int getAmount() {
        return amount;
    }


    public double getTotalPriceBeforeDiscount(){
        return totalPriceBeforeDiscount;
    }
    public double getDiscount(){
        return discount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public ProductInOrderDTO getProductInOrderDTO() {
        return productInOrderDTO;
    }

    public  int getCatalogNum(){
        return catalogNum;
    }

    public String getProductName() {
        return productName;
    }

    public int getBranchId() {
        return branchId;
    }

    public String getProductId() {
        return productId;
    }
}
