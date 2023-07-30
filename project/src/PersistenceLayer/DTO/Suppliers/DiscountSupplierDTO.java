package PersistenceLayer.DTO.Suppliers;


import PersistenceLayer.DAO.Suppliers.DiscountSupplierMapper;
import PersistenceLayer.DTO.AbstractDTO;

public class DiscountSupplierDTO extends AbstractDTO {
    private double discountAmount;
    private double minimumAmount;
    private int discountId;
    private int isPercentageDiscount;
    private  int supplierId;


    public DiscountSupplierDTO( double minimumAmount, double discountAmount, int discountId ,  int supplierId ,int isPercentageDiscount) {
        super(new DiscountSupplierMapper());
        this.discountAmount = discountAmount;
        this.minimumAmount = minimumAmount;
        this.discountId = discountId;
        this.isPercentageDiscount = isPercentageDiscount;
        this.supplierId = supplierId;
    }

    public DiscountSupplierDTO( double minimumAmount, double discountAmount, int discountId ,  int supplierId ,int isPercentageDiscount, DiscountSupplierMapper discountSupplierMapper) {
        super(discountSupplierMapper);
        this.discountAmount = discountAmount;
        this.minimumAmount = minimumAmount;
        this.discountId = discountId;
        this.isPercentageDiscount = isPercentageDiscount;
        this.supplierId = supplierId;
    }

    public int getSupplierId() {
        return supplierId;
    }


    public int getDiscountId() {
        return discountId;
    }
    public int getIsPercentageDiscount(){
        return isPercentageDiscount;
    }


    @Override
    public DiscountSupplierMapper getDao() {
        return (DiscountSupplierMapper)dao;
    }


    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getMinimumAmount() {
        return minimumAmount;
    }
}
