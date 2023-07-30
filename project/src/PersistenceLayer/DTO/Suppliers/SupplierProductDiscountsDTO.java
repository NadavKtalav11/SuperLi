package PersistenceLayer.DTO.Suppliers;

import PersistenceLayer.DTO.AbstractDTO;
import PersistenceLayer.DAO.Suppliers.SupplierProductDiscountsMapper;

public class SupplierProductDiscountsDTO extends AbstractDTO {
    private String productId;
    private double discountAmount;
    private double minimumAmount;
    private int discountId;
    private int isPercentageDiscount;
    private int supplierId;

    public SupplierProductDiscountsDTO(  int discountId, int supplierId, String productId  ,  double minimumAmount, double discountAmount,  int isPercentageDiscount) {
        super(new SupplierProductDiscountsMapper());
        this.discountAmount = discountAmount;
        this.minimumAmount = minimumAmount;
        this.discountId = discountId;
        this.isPercentageDiscount = isPercentageDiscount;
        this.productId = productId;
        this.supplierId = supplierId;

    }

    public SupplierProductDiscountsDTO(int discountId, int supplierId, String productId  , double minimumAmount, double discountAmount, int isPercentageDiscount , SupplierProductDiscountsMapper supplierMapper) {
        super(supplierMapper);
        this.discountAmount = discountAmount;
        this.minimumAmount = minimumAmount;
        this.discountId = discountId;
        this.isPercentageDiscount = isPercentageDiscount;
        this.productId = productId;
        this.supplierId = supplierId;

    }


    public int getSupplierId() {
        return supplierId;
    }

    public int getDiscountId() {
        return discountId;
    }

    public String getProductId() {
        return productId;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }


    public int getIsPercentageDiscount() {
        return isPercentageDiscount;
    }

    @Override
    public SupplierProductDiscountsMapper getDao() {
        return (SupplierProductDiscountsMapper) dao;
    }
}





