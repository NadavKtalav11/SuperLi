package BusinessLayer.Suppliers;

import PersistenceLayer.DAO.Suppliers.SupplierProductDiscountsMapper;
import PersistenceLayer.DTO.Suppliers.SupplierProductDiscountsDTO;

public class DiscountSuppliersPerProduct extends DiscountSuppliers {
    private SupplierProductDiscountsDTO supplierProductDiscountsDTO;
    private boolean percentageDiscount;


    public DiscountSuppliersPerProduct(boolean percentageDiscount , double amount , double discount , int Id , int supplierId, String productId, SupplierProductDiscountsMapper supplierProductDiscountsMapper){
        super(amount , discount, Id);
        this.percentageDiscount = percentageDiscount;
        int percentageDiscountInt = 0;
        if (percentageDiscount){
            percentageDiscountInt= 1;
        }
        supplierProductDiscountsDTO = new SupplierProductDiscountsDTO(discountId, supplierId, productId,  amount, discount,percentageDiscountInt, supplierProductDiscountsMapper );
        supplierProductDiscountsMapper.addSupplierProductDiscount(supplierProductDiscountsDTO);

    }
    public DiscountSuppliersPerProduct(SupplierProductDiscountsDTO supplierProductDiscountsDTO) {
        super(supplierProductDiscountsDTO.getMinimumAmount(), supplierProductDiscountsDTO.getDiscountAmount(), supplierProductDiscountsDTO.getDiscountId());
        if (supplierProductDiscountsDTO.getIsPercentageDiscount() != 0) {
            percentageDiscount = true;
        } else {
            percentageDiscount = false;
        }
        this.supplierProductDiscountsDTO= supplierProductDiscountsDTO;

    }

    public String toPrintStyle(){
        return (super.discountId + " - amount "  + super.amount + "discount - " + super.discount + "% discount " + percentageDiscount +  "\n");
    }


    public double getPriceAfterDiscount(String supplierProduct, double productPrice ) {
        if (percentageDiscount) {
            double price = productPrice * ((1 - getDiscount() / 100));
            return price;
        }
        else {
            double price = (productPrice - getDiscount());
            return price;
        }
    }

    public boolean getIsPercentage(){
        return percentageDiscount;
    }


    public double getAmount() {
        return amount;
    }

    public boolean isPercentageDiscount() {
        return percentageDiscount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setAmount( int amount){
        this.amount = amount;
    }

    public void setDiscount(double discount){
        this.discount = discount;
    }
}
