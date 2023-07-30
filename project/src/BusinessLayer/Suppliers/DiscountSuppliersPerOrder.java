package BusinessLayer.Suppliers;

import PersistenceLayer.DAO.Suppliers.DiscountSupplierMapper;
import PersistenceLayer.DTO.Suppliers.DiscountSupplierDTO;

public class DiscountSuppliersPerOrder extends DiscountSuppliers {
    DiscountSupplierDTO discountSupplierDTO;
    boolean percentageDiscount;


    public DiscountSuppliersPerOrder(boolean percentageDiscount , double amount , double discount , int Id , int supplierId, DiscountSupplierMapper discountSupplierMapper){
        super(amount , discount, Id);
        this.percentageDiscount = percentageDiscount;
        int percentageDiscountInt = 0;
        if (percentageDiscount){
            percentageDiscountInt =1;
        }
        discountSupplierDTO= new DiscountSupplierDTO(amount, discount,Id, supplierId, percentageDiscountInt, discountSupplierMapper);
        discountSupplierMapper.addSupplierDiscount(discountSupplierDTO);

    }


    public DiscountSuppliersPerOrder(DiscountSupplierDTO discountSupplierDTO){

        super( discountSupplierDTO.getMinimumAmount(),discountSupplierDTO.getDiscountAmount(), discountSupplierDTO.getDiscountId() );
        if ( discountSupplierDTO.getIsPercentageDiscount()!=0) {
            percentageDiscount = true;
        }
        else {
            percentageDiscount = false;
        }
        discountSupplierDTO= discountSupplierDTO;
    }

    public double getPriceAfterDiscount(String supplierProduct, double productPrice ) {
        double price = 0;
        if (percentageDiscount) {
            price = productPrice * ((1 - getDiscount() / 100));

        } else {
            price = (productPrice - getDiscount());

        }
        return price;

    }

    public String toPrintStyle(){
        return (super.discountId + " - amount "  + super.amount + "discount - " + super.discount + "% discount " + percentageDiscount + "\n") ;
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

